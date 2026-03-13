# Java Spring Boot — Patterns & Anti-Patterns

## Service Pattern (DO)
```java
public interface OrderService {
    OrderDTO createOrder(CreateOrderRequest request);
    Page<OrderDTO> findAll(Pageable pageable);
}

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderDTO createOrder(CreateOrderRequest request) {
        Order order = orderMapper.toEntity(request);
        return orderMapper.toDTO(orderRepository.save(order));
    }
}
```

## Controller Pattern (DO)
```java
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> create(@Valid @RequestBody CreateOrderRequest request) {
        OrderDTO created = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
```

## Exception Handling (DO)
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }
}
```

## Anti-Pattern: Field Injection (DON'T)
```java
// FALSCH
@Service
public class OrderService {
    @Autowired
    private OrderRepository repo; // ← Field Injection
}

// RICHTIG
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository repo; // ← Constructor Injection
}
```

## Anti-Pattern: Entity als API-Response (DON'T)
```java
// FALSCH — Entity direkt exposen
@GetMapping("/{id}")
public Order getOrder(@PathVariable Long id) {
    return orderRepository.findById(id).get(); // ← Entity + .get()
}

// RICHTIG — DTO + orElseThrow
@GetMapping("/{id}")
public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
    return ResponseEntity.ok(orderService.findById(id)); // Service gibt DTO zurueck
}
```

## ConfigurationProperties (DO)
```java
@ConfigurationProperties(prefix = "app.mail")
@Validated
public record MailProperties(
    @NotBlank String host,
    @Min(1) @Max(65535) int port,
    @NotBlank String from,
    boolean enabled
) {}

// application.yml:
// app:
//   mail:
//     host: smtp.example.com
//     port: 587
//     from: noreply@example.com
//     enabled: true
```

## SecurityFilterChain (DO)
```java
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable()) // Nur bei stateless REST API
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .anyRequest().authenticated())
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }
}
```

## MapStruct Mapper (DO)
```java
@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderDTO toDTO(Order entity);
    Order toEntity(CreateOrderRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Order updateEntity(@MappingTarget Order entity, UpdateOrderRequest request);
}
```

## Repository mit Custom Query (DO)
```java
public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByCustomerId(Long customerId, Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.createdAt > :since")
    List<Order> findRecentByStatus(
        @Param("status") OrderStatus status,
        @Param("since") LocalDateTime since
    );

    @Modifying
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :id")
    int updateStatus(@Param("id") Long id, @Param("status") OrderStatus status);
}
```

## Entity mit Auditing (DO)
```java
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter @NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long customerId;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal total;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private OrderStatus status = OrderStatus.PENDING;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
```

## Pagination im Controller (DO)
```java
@GetMapping
public ResponseEntity<Page<OrderDTO>> list(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "createdAt,desc") String[] sort
) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(
        Sort.Order.desc("createdAt")
    ));
    return ResponseEntity.ok(orderService.findAll(pageable));
}
```

## Scheduling (DO)
```java
@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCleanupScheduler {

    private final OrderService orderService;

    @Scheduled(cron = "0 0 2 * * ?") // Taeglich um 02:00
    public void cleanupExpiredOrders() {
        int deleted = orderService.deleteExpired();
        log.info("Expired orders cleaned up: count={}", deleted);
    }
}
```

## Async Service (DO)
```java
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final MailSender mailSender;

    @Async
    @Override
    public CompletableFuture<Void> sendOrderConfirmation(OrderDTO order) {
        mailSender.send(order.customerEmail(), "Bestellbestätigung", ...);
        return CompletableFuture.completedFuture(null);
    }
}
```

## Caching (DO)
```java
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Cacheable(value = "products", key = "#id")
    @Override
    public ProductDTO findById(Long id) {
        return productRepository.findById(id)
            .map(ProductMapper::toDTO)
            .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    @CacheEvict(value = "products", key = "#id")
    @Override
    @Transactional
    public ProductDTO update(Long id, UpdateProductRequest request) {
        // ... update logic
    }
}
```

## Integration Test mit Testcontainers (DO)
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class OrderControllerIT {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void should_createOrder_when_validRequest() {
        var request = new CreateOrderRequest(1L, new BigDecimal("99.99"));
        var response = restTemplate.postForEntity("/api/v1/orders", request, OrderDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().total()).isEqualByComparingTo("99.99");
    }
}
```

## Anti-Pattern: @SpringBootTest fuer Unit Tests (DON'T)
```java
// FALSCH — voller Context fuer einfachen Service-Test
@SpringBootTest
class OrderServiceTest {
    @Autowired OrderService orderService;
    // ... dauert 10+ Sekunden zum Starten
}

// RICHTIG — Mockito ohne Spring Context
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock OrderRepository orderRepository;
    @Mock OrderMapper orderMapper;
    @InjectMocks OrderServiceImpl orderService;

    @Test
    void should_createOrder_when_validRequest() {
        // ... laeuft in Millisekunden
    }
}
```

## application.yml Struktur (DO)
```yaml
spring:
  application:
    name: order-service
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}

  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/orders}
    username: ${DB_USER:dev}
    password: ${DB_PASSWORD:dev}

  jpa:
    hibernate:
      ddl-auto: validate  # NEVER auto/create in prod
    open-in-view: false   # NEVER true — Lazy Loading Falle

  flyway:
    enabled: true
    locations: classpath:db/migration

server:
  port: ${SERVER_PORT:8080}

logging:
  level:
    com.example: DEBUG
    org.springframework.security: WARN
```

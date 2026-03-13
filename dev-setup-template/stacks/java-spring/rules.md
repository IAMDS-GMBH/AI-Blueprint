---
description: "Java Spring Boot Regeln"
globs: ["src/**/*.java", "pom.xml", "build.gradle", "build.gradle.kts", "application*.yml", "application*.properties"]
alwaysApply: false
---

# Java Spring Boot

## Architektur
- Services via Interface abstrahieren (UserService → UserServiceImpl)
- DTOs fuer API-Layer — Entities NEVER direkt exposen
- Constructor Injection via @RequiredArgsConstructor (Lombok)
- Exception Handling nur via @RestControllerAdvice
- Schichten: Controller → Service → Repository — keine Abkuerzungen
- @Transactional auf Service-Methoden, nicht auf Repository-Ebene

## API-Konventionen
- GET /api/v1/resources → 200 (Liste) | POST → 201 Created | PUT → 200 | PATCH → 200 | DELETE → 204
- Alle Endpoints mit @PreAuthorize absichern
- Input-Validierung mit @Valid + Bean Validation
- Pagination: Spring Pageable nutzen, nie manuell
- ResponseEntity fuer alle Antworten mit korrektem Status

## Configuration & Profiles
- application.yml als Default — application-{profile}.yml fuer Umgebungen
- Profiles: dev, test, staging, prod — aktivieren via spring.profiles.active
- Secrets NEVER in application.yml — @Value("${ENV_VAR}") oder Spring Cloud Config
- @ConfigurationProperties fuer typsichere Config-Gruppen (statt viele @Value)
- Bean-Definitionen: @Configuration Klassen, NEVER XML

## Security (Spring Security)
- SecurityFilterChain als @Bean definieren (NEVER WebSecurityConfigurerAdapter — deprecated)
- JWT: Stateless Session (SessionCreationPolicy.STATELESS)
- CORS: Explizit konfigurieren via CorsConfigurationSource
- CSRF: Deaktivieren NUR bei stateless REST APIs
- Method Security: @PreAuthorize("hasRole('ADMIN')") auf Service-Methoden
- Passwoerter: BCryptPasswordEncoder — NEVER Klartext oder MD5/SHA

## Daten & Mapping
- MapStruct fuer DTO ↔ Entity Mapping (NEVER manuelles Mapping in 10+ Feldern)
- JPA Entities: @Entity, @Id, @GeneratedValue — Lazy Loading als Default
- Spring Data JPA: Repository Interfaces — Custom Queries via @Query (JPQL)
- Auditing: @CreatedDate, @LastModifiedDate via @EnableJpaAuditing
- Pagination: Pageable Parameter in Repository + Controller

## Maven / Gradle
- Maven: spring-boot-starter-parent als Parent POM
- Dependency Management via spring-boot-dependencies BOM
- Plugin: spring-boot-maven-plugin (Fat JAR)
- Versions: Properties Block (`<java.version>17</java.version>`)
- NEVER Dependency ohne Version-Management (Wildwuchs)
- Gradle: plugins { id 'org.springframework.boot' }, implementation statt compile

## Scheduling & Async
- @EnableScheduling + @Scheduled(cron = "...") fuer wiederkehrende Tasks
- @EnableAsync + @Async fuer asynchrone Ausfuehrung
- Async Methoden: Return CompletableFuture<T> — NEVER void (Fehler gehen verloren)
- Thread Pool: Eigenen TaskExecutor konfigurieren statt Default

## Caching
- @EnableCaching + @Cacheable("cacheName") auf Service-Methoden
- @CacheEvict bei Schreiboperationen
- Cache Provider: Caffeine (lokal) oder Redis (verteilt)

## Testing
- JUnit 5 + Mockito fuer Unit Tests
- @SpringBootTest + Testcontainers fuer Integration Tests
- @WebMvcTest fuer Controller Tests (kein voller Context)
- @DataJpaTest fuer Repository Tests (embedded DB)
- Test-Naming: should_expectedBehavior_when_condition()
- Test Slices: NEVER @SpringBootTest wenn ein Slice reicht (langsam)
- @MockBean fuer Service-Layer in Controller Tests
- AssertJ bevorzugen ueber JUnit Assertions

## Logging
- SLF4J + Logback (NEVER System.out.println)
- MDC fuer Request-Tracing (correlationId)
- log.error() mit Exception als letztem Parameter
- Log-Level pro Package in application.yml konfigurieren

## Verbote
- NEVER @Autowired auf Feldern — Constructor Injection via @RequiredArgsConstructor
- NEVER Logik in Entities oder DTOs
- NEVER Optional.get() ohne Check — immer .orElseThrow()
- NEVER hardcodierte Credentials — @Value oder Secrets Manager
- NEVER Magic Numbers — benannte Konstanten verwenden
- NEVER synchronized in Spring Beans — nutze @Lock oder DB-Level Locking
- NEVER WebSecurityConfigurerAdapter — SecurityFilterChain @Bean verwenden
- NEVER @SpringBootTest fuer isolierte Unit Tests (zu langsam)
- NEVER Logik in @Configuration Klassen — nur Bean-Definitionen

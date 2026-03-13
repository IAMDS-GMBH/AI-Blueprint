# .NET / C# — Patterns & Anti-Patterns

## Controller + Service (DO)
```csharp
[ApiController]
[Route("api/v1/[controller]")]
public class OrdersController : ControllerBase
{
    private readonly IOrderService _orderService;

    public OrdersController(IOrderService orderService)
    {
        _orderService = orderService;
    }

    [HttpPost]
    public async Task<ActionResult<OrderDto>> Create([FromBody] CreateOrderRequest request)
    {
        var order = await _orderService.CreateAsync(request);
        return CreatedAtAction(nameof(GetById), new { id = order.Id }, order);
    }

    [HttpGet("{id}")]
    public async Task<ActionResult<OrderDto>> GetById(int id)
    {
        var order = await _orderService.GetByIdAsync(id);
        return order is null ? NotFound() : Ok(order);
    }
}
```

## Service mit Interface (DO)
```csharp
public interface IOrderService
{
    Task<OrderDto> CreateAsync(CreateOrderRequest request);
    Task<OrderDto?> GetByIdAsync(int id);
}

public class OrderService : IOrderService
{
    private readonly IOrderRepository _repository;
    private readonly IMapper _mapper;

    public OrderService(IOrderRepository repository, IMapper mapper)
    {
        _repository = repository;
        _mapper = mapper;
    }

    public async Task<OrderDto> CreateAsync(CreateOrderRequest request)
    {
        var entity = _mapper.Map<Order>(request);
        await _repository.AddAsync(entity);
        return _mapper.Map<OrderDto>(entity);
    }
}
```

## DI Registration (DO)
```csharp
// Program.cs
builder.Services.AddScoped<IOrderService, OrderService>();
builder.Services.AddScoped<IOrderRepository, OrderRepository>();
```

## Anti-Pattern: async void (DON'T)
```csharp
// FALSCH — Exception geht verloren
public async void ProcessOrder(int id) { ... }

// RICHTIG
public async Task ProcessOrderAsync(int id) { ... }
```

## Unit Test mit Moq (DO)
```csharp
// OrderServiceTests.cs
public class OrderServiceTests
{
    private readonly Mock<IOrderRepository> _repoMock = new();
    private readonly Mock<IMapper> _mapperMock = new();
    private readonly OrderService _sut;

    public OrderServiceTests()
    {
        _sut = new OrderService(_repoMock.Object, _mapperMock.Object);
    }

    [Fact]
    public async Task Should_CreateOrder_When_ValidInput()
    {
        // Arrange
        var request = new CreateOrderRequest { CustomerId = 1, Total = 99.99m };
        var entity = new Order { Id = 1, CustomerId = 1, Total = 99.99m };
        var dto = new OrderDto { Id = 1, CustomerId = 1, Total = 99.99m };

        _mapperMock.Setup(m => m.Map<Order>(request)).Returns(entity);
        _repoMock.Setup(r => r.AddAsync(entity)).Returns(Task.CompletedTask);
        _mapperMock.Setup(m => m.Map<OrderDto>(entity)).Returns(dto);

        // Act
        var result = await _sut.CreateAsync(request);

        // Assert
        result.Should().NotBeNull();
        result.Id.Should().Be(1);
        result.Total.Should().Be(99.99m);
        _repoMock.Verify(r => r.AddAsync(entity), Times.Once);
    }

    [Fact]
    public async Task Should_ReturnNull_When_OrderNotFound()
    {
        _repoMock.Setup(r => r.GetByIdAsync(999)).ReturnsAsync((Order?)null);

        var result = await _sut.GetByIdAsync(999);

        result.Should().BeNull();
    }
}
```

## Integration Test mit WebApplicationFactory (DO)
```csharp
// OrdersControllerTests.cs
public class OrdersControllerTests : IClassFixture<WebApplicationFactory<Program>>
{
    private readonly HttpClient _client;

    public OrdersControllerTests(WebApplicationFactory<Program> factory)
    {
        _client = factory.WithWebHostBuilder(builder =>
        {
            builder.ConfigureServices(services =>
            {
                // In-Memory DB fuer Tests
                services.RemoveAll<DbContextOptions<AppDbContext>>();
                services.AddDbContext<AppDbContext>(options =>
                    options.UseInMemoryDatabase("TestDb"));
            });
        }).CreateClient();
    }

    [Fact]
    public async Task Post_ValidOrder_Returns201()
    {
        var request = new { CustomerId = 1, Total = 99.99m };
        var response = await _client.PostAsJsonAsync("/api/v1/orders", request);

        response.StatusCode.Should().Be(HttpStatusCode.Created);
        var order = await response.Content.ReadFromJsonAsync<OrderDto>();
        order.Should().NotBeNull();
        order!.Total.Should().Be(99.99m);
    }

    [Fact]
    public async Task Get_NonExistentOrder_Returns404()
    {
        var response = await _client.GetAsync("/api/v1/orders/999");
        response.StatusCode.Should().Be(HttpStatusCode.NotFound);
    }
}
```

## Parametrisierter Test (DO)
```csharp
[Theory]
[InlineData(0)]
[InlineData(-1)]
[InlineData(-99.99)]
public async Task Should_RejectOrder_When_InvalidTotal(decimal total)
{
    var request = new { CustomerId = 1, Total = total };
    var response = await _client.PostAsJsonAsync("/api/v1/orders", request);

    response.StatusCode.Should().Be(HttpStatusCode.BadRequest);
}
```

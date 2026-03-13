# Angular + TypeScript — Patterns & Anti-Patterns

## Standalone Component (DO)
```typescript
@Component({
  selector: 'app-order-card',
  standalone: true,
  imports: [CommonModule],
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `
    <div class="card" (click)="select.emit(order().id)">
      <h3>{{ order().title }}</h3>
      <p>{{ order().total | currency }}</p>
    </div>
  `,
})
export class OrderCardComponent {
  order = input.required<Order>();
  select = output<number>();
}
```

## Service mit Signals (DO — Angular 16+)
```typescript
@Injectable({ providedIn: 'root' })
export class OrderService {
  private http = inject(HttpClient);

  private _orders = signal<Order[]>([]);
  readonly orders = this._orders.asReadonly();
  readonly isLoading = signal(false);

  loadOrders() {
    this.isLoading.set(true);
    this.http.get<Order[]>('/api/v1/orders').pipe(
      finalize(() => this.isLoading.set(false)),
    ).subscribe(orders => this._orders.set(orders));
  }

  createOrder(request: CreateOrderRequest) {
    return this.http.post<Order>('/api/v1/orders', request);
  }
}
```

## Lazy Loading Route (DO)
```typescript
export const routes: Routes = [
  {
    path: 'orders',
    loadComponent: () => import('./orders/order-list.component')
      .then(m => m.OrderListComponent),
  },
];
```

## Komponenten-Test (DO)
```typescript
// order-card.component.spec.ts
import { ComponentFixture, TestBed } from "@angular/core/testing";
import { OrderCardComponent } from "./order-card.component";

describe("OrderCardComponent", () => {
  let fixture: ComponentFixture<OrderCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OrderCardComponent], // Standalone Component
    }).compileComponents();

    fixture = TestBed.createComponent(OrderCardComponent);
  });

  it("should render order title", () => {
    fixture.componentRef.setInput("order", { id: 1, title: "Test Order", total: 99.99 });
    fixture.detectChanges();

    const element = fixture.nativeElement as HTMLElement;
    expect(element.textContent).toContain("Test Order");
  });

  it("should emit select on click", () => {
    fixture.componentRef.setInput("order", { id: 1, title: "Test", total: 0 });
    fixture.detectChanges();

    let emittedId: number | undefined;
    fixture.componentInstance.select.subscribe((id: number) => emittedId = id);

    fixture.nativeElement.querySelector(".card").click();
    expect(emittedId).toBe(1);
  });
});
```

## Service-Test mit HttpTestingController (DO)
```typescript
// order.service.spec.ts
import { TestBed } from "@angular/core/testing";
import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { OrderService } from "./order.service";

describe("OrderService", () => {
  let service: OrderService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [OrderService],
    });
    service = TestBed.inject(OrderService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => httpMock.verify());

  it("should fetch orders", () => {
    const mockOrders = [{ id: 1, title: "Order 1" }];

    service.loadOrders();
    const req = httpMock.expectOne("/api/v1/orders");
    expect(req.request.method).toBe("GET");
    req.flush(mockOrders);

    expect(service.orders()).toEqual(mockOrders);
    expect(service.isLoading()).toBe(false);
  });
});
```

## Anti-Pattern: Nested Subscribe (DON'T)
```typescript
// FALSCH
this.userService.getUser().subscribe(user => {
  this.orderService.getOrders(user.id).subscribe(orders => {
    this.orders = orders; // ← Memory Leak + Nested Subscribe
  });
});

// RICHTIG
this.userService.getUser().pipe(
  switchMap(user => this.orderService.getOrders(user.id)),
  takeUntilDestroyed(),
).subscribe(orders => this.orders = orders);
```

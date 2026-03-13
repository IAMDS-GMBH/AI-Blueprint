# Python FastAPI — Patterns & Anti-Patterns

## Router + Service Pattern (DO)
```python
# routers/orders.py
from fastapi import APIRouter, Depends, HTTPException, status
from ..services.order_service import OrderService
from ..schemas.order import OrderCreate, OrderResponse
from ..dependencies import get_order_service

router = APIRouter(prefix="/api/v1/orders", tags=["orders"])

@router.post("/", response_model=OrderResponse, status_code=status.HTTP_201_CREATED)
async def create_order(
    order: OrderCreate,
    service: OrderService = Depends(get_order_service),
):
    return await service.create(order)

@router.get("/{order_id}", response_model=OrderResponse)
async def get_order(
    order_id: int,
    service: OrderService = Depends(get_order_service),
):
    result = await service.get_by_id(order_id)
    if not result:
        raise HTTPException(status_code=404, detail="Order not found")
    return result
```

## Pydantic Schema (DO)
```python
# schemas/order.py
from pydantic import BaseModel, Field
from datetime import datetime
from decimal import Decimal

class OrderCreate(BaseModel):
    customer_id: int
    total: Decimal = Field(ge=0, decimal_places=2)

class OrderResponse(BaseModel):
    id: int
    customer_id: int
    total: Decimal
    created_at: datetime

    model_config = {"from_attributes": True}
```

## Settings (DO)
```python
# config.py
from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    database_url: str
    jwt_secret: str
    debug: bool = False

    model_config = {"env_file": ".env"}

settings = Settings()
```

## Anti-Pattern: Logik im Router (DON'T)
```python
# FALSCH
@router.post("/")
async def create_order(order: OrderCreate, db: Session = Depends(get_db)):
    db_order = Order(**order.dict())
    db.add(db_order)
    db.commit()  # ← Business-Logik gehoert in Service
    return db_order
```

## API Test mit httpx (DO)
```python
# tests/test_orders.py
import pytest
from httpx import AsyncClient, ASGITransport
from app.main import app

@pytest.fixture
async def client():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        yield ac

@pytest.mark.anyio
async def test_should_create_order_when_valid_input(client: AsyncClient, auth_headers):
    response = await client.post(
        "/api/v1/orders",
        json={"customer_id": 1, "total": "99.99"},
        headers=auth_headers,
    )

    assert response.status_code == 201
    data = response.json()
    assert data["customer_id"] == 1
    assert data["total"] == "99.99"

@pytest.mark.anyio
async def test_should_return_404_when_order_not_found(client: AsyncClient, auth_headers):
    response = await client.get("/api/v1/orders/999", headers=auth_headers)
    assert response.status_code == 404
    assert response.json()["detail"] == "Order not found"
```

## Service Unit Test (DO)
```python
# tests/test_order_service.py
import pytest
from unittest.mock import AsyncMock, MagicMock
from app.services.order_service import OrderService
from app.schemas.order import OrderCreate

@pytest.fixture
def mock_repo():
    repo = MagicMock()
    repo.create = AsyncMock(return_value={"id": 1, "customer_id": 1, "total": "99.99"})
    repo.get_by_id = AsyncMock(return_value=None)
    return repo

@pytest.fixture
def service(mock_repo):
    return OrderService(repository=mock_repo)

@pytest.mark.anyio
async def test_should_create_order(service, mock_repo):
    request = OrderCreate(customer_id=1, total=99.99)
    result = await service.create(request)

    assert result["id"] == 1
    mock_repo.create.assert_called_once()

@pytest.mark.anyio
async def test_should_raise_when_not_found(service):
    with pytest.raises(Exception, match="not found"):
        await service.get_by_id(999)
```

## Shared Fixtures (DO)
```python
# tests/conftest.py
import pytest
from httpx import AsyncClient, ASGITransport
from app.main import app
from app.config import Settings

@pytest.fixture(scope="session")
def anyio_backend():
    return "asyncio"

@pytest.fixture
async def client():
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as ac:
        yield ac

@pytest.fixture
def auth_headers():
    # Test-Token generieren
    return {"Authorization": "Bearer test-jwt-token"}
```

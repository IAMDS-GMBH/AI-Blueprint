---
description: "Python FastAPI Regeln"
globs: ["**/*.py", "requirements.txt", "pyproject.toml"]
alwaysApply: false
---

# Python FastAPI

## Architektur
- Router → Service → Repository Schichtung
- Pydantic Models fuer Request/Response (NEVER dict als API-Response)
- Dependency Injection via Depends()
- Settings via pydantic-settings (BaseSettings) — NEVER hardcodierte Config
- Async wo moeglich (async def statt def fuer I/O-Operationen)

## API-Konventionen
- Router in eigenen Dateien: routers/orders.py, routers/users.py
- Prefix: /api/v1/ — via APIRouter(prefix="/api/v1/orders")
- Response Models explizit: response_model=OrderResponse
- Status Codes: POST → 201, DELETE → 204, GET/PUT → 200
- HTTPException fuer Fehler mit detail-Message

## Testing (pytest)
- pytest als Test Runner — pytest.ini oder pyproject.toml fuer Config
- httpx AsyncClient fuer API Tests (NEVER requests in async Tests)
- Test-Typen: Unit (Services/Utils) → Integration (API + DB) → E2E
- Test-Dateien: tests/ Ordner, Struktur spiegelt src/ — tests/test_order_service.py
- Test-Naming: test_should_create_order_when_valid_input()
- Fixtures: conftest.py fuer shared Fixtures (db_session, test_client, auth_headers)
- Mocking: pytest-mock (mocker fixture) oder unittest.mock — NEVER echte externe APIs
- DB Tests: Testcontainers oder SQLite in-memory als Fallback
- Coverage: pytest-cov — Minimum 80% fuer neue Features
- NEVER Tests die Reihenfolge-abhaengig sind — jeder Test muss isoliert laufen
- NEVER print() in Tests — assert verwenden, bei Bedarf -v Flag

## Typing
- Type Hints ueberall — mypy strict mode kompatibel
- Optional[X] statt X | None (Python 3.9 Kompatibilitaet)
- TypeVar und Generic fuer wiederverwendbare Patterns

## Verbote
- NEVER `from module import *`
- NEVER bare except — immer spezifische Exceptions
- NEVER print() fuer Logging — immer logging.getLogger()
- NEVER SQL Strings concatenieren — immer ORM oder parameterized queries
- NEVER sync I/O in async Funktionen (blockiert Event Loop)
- NEVER requirements.txt ohne Version-Pins

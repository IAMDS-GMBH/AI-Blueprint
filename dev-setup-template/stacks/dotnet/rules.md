---
description: ".NET / C# Regeln"
globs: ["**/*.cs", "**/*.csproj", "**/*.sln"]
alwaysApply: false
---

# .NET / C#

## Architektur
- Clean Architecture: Controllers → Services → Repositories
- Interfaces fuer alle Services (ISomethingService)
- Dependency Injection via builder.Services (Built-in DI)
- DTOs fuer API-Layer — Entities nie direkt exposen
- Async/Await konsequent fuer I/O-Operationen

## API-Konventionen
- [ApiController] + [Route("api/v1/[controller]")]
- ActionResult<T> als Return-Typ
- POST → CreatedAtAction() (201) | GET → Ok() | DELETE → NoContent()
- Input-Validierung via Data Annotations + [ApiController] auto-validation
- Global Exception Handling via Middleware

## Naming
- PascalCase: Klassen, Methoden, Properties, Namespaces
- camelCase: lokale Variablen, Parameter
- _camelCase: private Felder (mit Underscore-Prefix)
- I-Prefix fuer Interfaces: IOrderService
- Async-Suffix fuer async Methoden: GetOrderAsync()

## Testing (xUnit)
- xUnit als Test Framework — [Fact] fuer einfache, [Theory] fuer parametrisierte Tests
- Moq oder NSubstitute fuer Mocking — NEVER eigene Mock-Klassen bauen
- FluentAssertions fuer lesbare Assertions (.Should().Be(), .Should().Contain())
- Test-Typen: Unit (Services) → Integration (WebApplicationFactory) → E2E
- Test-Dateien: Separates Test-Projekt (ProjectName.Tests), Struktur spiegelt Hauptprojekt
- Test-Naming: Should_CreateOrder_When_ValidInput() oder Create_ValidInput_ReturnsCreated()
- WebApplicationFactory<Program> fuer Integration Tests mit echtem HTTP Pipeline
- DB Tests: Testcontainers oder InMemoryDatabase (EF Core)
- Mocking: Interface-basiert — nur Interfaces mocken, nie konkrete Klassen
- NEVER static oder global State in Tests — jeder Test isoliert
- NEVER Thread.Sleep() in Tests — async/await + TaskCompletionSource

## Verbote
- NEVER `public` Felder — immer Properties
- NEVER `async void` — immer `async Task`
- NEVER String-Concatenation fuer SQL — immer EF Core oder Dapper parameterized
- NEVER `catch (Exception)` ohne Logging oder Re-Throw
- NEVER hardcodierte Connection Strings — immer Configuration/Secrets

---
description: "Angular + TypeScript Regeln"
globs: ["src/**/*.ts", "src/**/*.html", "src/**/*.component.*"]
alwaysApply: false
---

# Angular + TypeScript

## Architektur
- Standalone Components (ab Angular 14+) — NEVER NgModules fuer neue Components
- Services: @Injectable({ providedIn: 'root' }) fuer Singletons
- Smart/Dumb Components: Container (mit Service-Calls) vs. Presentational (nur @Input/@Output)
- Lazy Loading fuer Feature-Routes

## Komponenten
- OnPush Change Detection als Default
- Signals (ab Angular 16+) bevorzugen ueber RxJS fuer einfachen State
- @Input() mit required: true wo noetig
- @Output() mit EventEmitter<T> — typisiert

## State Management
- Einfach: Services mit Signals oder BehaviorSubject
- Komplex: NgRx (Store/Effects) oder NGRX SignalStore
- HTTP: HttpClient mit typed Responses — NEVER untyped .get()

## RxJS
- Unsubscribe via takeUntilDestroyed() oder async Pipe
- NEVER .subscribe() ohne Cleanup
- Operators: map, filter, switchMap, catchError — NEVER nested subscribes

## Testing (Jest + Testing Library)
- Jest bevorzugen ueber Karma (schneller, besseres Mocking)
- @testing-library/angular fuer Komponenten (alternativ: ComponentFixture)
- Test-Typen: Unit (Services) → Component (Render/Input/Output) → E2E (Playwright/Cypress)
- Test-Dateien: `component-name.component.spec.ts` (Angular Convention)
- Test-Naming: it("should emit select event when row clicked")
- Mocking: jest.mock() fuer Module, jest.fn() fuer Funktionen
- Services: TestBed.configureTestingModule mit provide + useValue fuer Mocks
- HTTP: HttpClientTestingModule + HttpTestingController fuer API-Tests
- NEVER echte HTTP-Calls in Tests — immer HttpTestingController oder MSW
- NEVER TestBed fuer reine Unit Tests von Services ohne Dependencies

## Verbote
- NEVER NgModules in neuem Code (Standalone Components)
- NEVER any-Type
- NEVER subscribe() in Templates — async Pipe nutzen
- NEVER direkte DOM-Manipulation — Renderer2 oder Directives
- NEVER Business-Logik in Komponenten — immer Services

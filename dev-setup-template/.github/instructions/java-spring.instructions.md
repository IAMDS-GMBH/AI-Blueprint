---
applyTo: "**/*.java"
---

<!-- Kurzfassung — vollstaendige Rules in .claude/rules/examples/java-spring.md -->

# Java Spring

- Services via Interface abstrahieren
- DTOs fuer API — Entities NEVER direkt exposen
- Constructor Injection via @RequiredArgsConstructor
- Exception Handling via @RestControllerAdvice
- @PreAuthorize fuer alle Endpoints
- Input: @Valid + Bean Validation
- NEVER @Autowired auf Feldern, NEVER System.out.println

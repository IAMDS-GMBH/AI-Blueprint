---
description: "BEISPIEL: Java Spring Boot — Vollstaendige Version in stacks/java-spring/"
globs: ["src/**/*.java"]
alwaysApply: false
---

<!--
  HINWEIS: Dies ist ein Stub. Die vollstaendige Rule liegt in:
  stacks/java-spring/rules.md    (Rule mit Frontmatter)
  stacks/java-spring/snippets.md (Code-Patterns)

  setup.sh kopiert alle Stacks nach .claude/rules/stacks/.
  /configure aktiviert die passenden automatisch.
-->

# Java Spring Boot (Kurzversion)

- Services via Interface abstrahieren
- DTOs fuer API-Layer — Entities NEVER direkt exposen
- Constructor Injection via @RequiredArgsConstructor
- NEVER @Autowired auf Feldern
- NEVER System.out.println — nur SLF4J

→ Vollstaendige Version: `stacks/java-spring/`

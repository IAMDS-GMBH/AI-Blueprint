# /configure — Claude Code Projekt konfigurieren

Erkennt automatisch den Tech-Stack und passt die Claude Code Konfiguration an.

Aufgabe: $ARGUMENTS

## 1. Modus erkennen

Lies CLAUDE.md.

- **Placeholders vorhanden** (`[COMPANY NAME]`, `[PROJECT NAME]`, `[STACK]`) → **Ersteinrichtung**
- **Keine Placeholders** → **Feintuning**

## 2a. Ersteinrichtung — Stack erkennen oder abfragen

### Bei bestehendem Repo mit Code — automatisch erkennen:
```
pom.xml / build.gradle(.kts)          → Java Spring Boot
package.json mit "vue"                 → Vue.js
package.json mit "react"               → React
angular.json                           → Angular
requirements.txt / pyproject.toml      → Python
*.csproj / *.sln                       → .NET
package.json mit "hono"                → Node Hono
package.json mit "express"             → Node Express
docker-compose.yml / Dockerfile        → Docker
*.sql / flyway/ / db/migrations/       → Datenbank erkennen
```

Erkannten Stack dem User zeigen und bestaetigen lassen.
Zusaetzlich fragen: **Firmenname** und **Projektname** (falls nicht aus package.json/pom.xml ableitbar).

### Bei leerem Repo — interaktiv abfragen:
1. Firmenname
2. Projektname
3. Backend: java-spring / python-fastapi / dotnet / node-hono / none
4. Frontend: vue / react / angular / none
5. Datenbank: oracle / postgresql / mysql / mongodb / none

## 2b. Ersteinrichtung — Dateien anpassen

### Claude Code Dateien
- Passende Rules aus `.claude/rules/stacks/` nach `.claude/rules/` kopieren
  - z.B. `java-spring/rules.md` → `.claude/rules/java-spring.md`
  - z.B. `java-spring/snippets.md` → `.claude/rules/snippets/java-spring.md`
- Globs in kopierten Rules pruefen und an Projektstruktur anpassen
  - z.B. `src/**/*.java` → `backend/src/**/*.java` wenn backend/ Ordner existiert
- CLAUDE.md: Alle Placeholders ersetzen
  - `[COMPANY NAME]` → erkannter/abgefragter Firmenname
  - `[PROJECT NAME]` → erkannter/abgefragter Projektname
  - `[STACK]` → z.B. "Java Spring Boot 3 + Vue.js 3 + PostgreSQL"
- .mcp.json: MCP-Server vorschlagen basierend auf Stack

### Gemeinsam
- MEMORY.md: Placeholders ersetzen + initiale Eintraege:
  - Erkannter Tech-Stack
  - Projektstruktur (wichtigste Ordner)
  - Erkannte Module

## 3. Feintuning (wenn bereits konfiguriert)

- Rules-Globs pruefen — matchen sie noch existierende Dateien?
- Neue Dateien/Module erkennen die noch keine Rule haben
- MCP-Server vorschlagen basierend auf Stack
- Aenderungen als Vorschlag zeigen, User bestaetigt

## 4. Zusammenfassung

```
Konfiguration abgeschlossen:
- Stack: [erkannter Stack]
- Rules aktiviert: [Liste]
- Placeholders ersetzt in: CLAUDE.md, MEMORY.md

Naechste Schritte:
1. Geaenderte Dateien reviewen
2. /plan fuer die erste Aufgabe nutzen
```

## Wichtig
- Aenderungen als Diff-Preview zeigen bevor geschrieben wird
- User muss jede Aenderung bestaetigen
- Bei Unsicherheit (z.B. Stack-Version): nachfragen statt raten
- Nicht-passende Stacks in stacks/ lassen, NICHT loeschen
- NUR Claude Code Dateien anfassen (.claude/, CLAUDE.md, .mcp.json, MEMORY.md)

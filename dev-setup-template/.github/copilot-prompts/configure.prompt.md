# Configure — GitHub Copilot Projekt konfigurieren

Erkennt automatisch den Tech-Stack und passt die Copilot Konfiguration an.

## 1. Modus erkennen

Lies copilot-instructions.md.

- **Placeholders vorhanden** (`[COMPANY NAME]`, `[PROJECT NAME]`, `[STACK]`) → **Ersteinrichtung**
- **Keine Placeholders** → **Feintuning**

## 2a. Ersteinrichtung — Stack erkennen oder abfragen

### Bestehendes Repo mit Code — automatisch erkennen:
```
pom.xml / build.gradle(.kts)          → Java Spring Boot
package.json mit "vue"                 → Vue.js
package.json mit "react"               → React
angular.json                           → Angular
requirements.txt / pyproject.toml      → Python
*.csproj / *.sln                       → .NET
package.json mit "hono"                → Node Hono
```

Erkannten Stack zeigen und bestaetigen lassen.
Fragen: **Firmenname** und **Projektname**.

### Leeres Repo — interaktiv abfragen:
1. Firmenname, Projektname
2. Backend: java-spring / python-fastapi / dotnet / node-hono / none
3. Frontend: vue / react / angular / none
4. Datenbank: oracle / postgresql / mysql / mongodb / none

## 2b. Ersteinrichtung — Dateien anpassen

### Copilot Dateien
- Passende Instructions aus `.github/instructions/stacks/` nach `.github/instructions/` kopieren
  - z.B. `java-spring/rules.md` → `.github/instructions/java-spring.instructions.md`
- copilot-instructions.md: Alle Placeholders ersetzen
  - `[COMPANY NAME]` → Firmenname
  - `[PROJECT NAME]` → Projektname
  - `[STACK]` → z.B. "Java Spring Boot 3 + Vue.js 3 + PostgreSQL"
- .vscode/mcp.json: MCP-Server vorschlagen basierend auf Stack

### Gemeinsam
- MEMORY.md: Placeholders ersetzen + initiale Eintraege (Tech-Stack, Projektstruktur)

## 3. Feintuning (wenn bereits konfiguriert)

- Instructions Globs pruefen — matchen sie existierende Dateien?
- Neue Module erkennen die noch keine Instruction haben
- MCP-Server vorschlagen
- Aenderungen als Vorschlag zeigen, User bestaetigt

## 4. Zusammenfassung

```
Konfiguration abgeschlossen:
- Stack: [erkannter Stack]
- Instructions aktiviert: [Liste]
- Placeholders ersetzt in: copilot-instructions.md, MEMORY.md
```

## Wichtig
- Aenderungen zeigen bevor geschrieben wird
- User muss bestaetigen
- Bei Unsicherheit: nachfragen statt raten
- Nicht-passende Stacks in stacks/ lassen, NICHT loeschen
- NUR Copilot Dateien anfassen (.github/, copilot-instructions.md, .vscode/mcp.json, MEMORY.md)

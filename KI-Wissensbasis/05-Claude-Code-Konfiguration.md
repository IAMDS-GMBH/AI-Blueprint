# 05 – Claude Code / GitHub Copilot Konfiguration

> Struktur und Konfiguration für Claude Code und GitHub Copilot im Team.

---

## Die 5 Konfigurationsebenen 📸 `⭐ HOCH`

### 1. CLAUDE.md (bzw. copilot-instructions.md)
- **Wird bei jeder Session geladen**
- **Inhalt:** Tech-Stack, Projektkonventionen, Projektstruktur
- Alles was der Agent **jederzeit** wissen muss
- **Regel:** Datei immer **möglichst schlank** halten
- **Copilot-Äquivalent:** `.github/copilot-instructions.md`

### 2. Rules (Pfad-spezifische Instruktionen)
- Werden **automatisch aktiv** je nachdem welche Dateien bearbeitet werden
- Perfekt für **dateispezifische Coding-Standards**
- Liegen in `.claude/rules/` – jede `.md`-Datei ist eine Rule
- **Copilot-Äquivalent:** `.github/instructions/*.instructions.md` mit `applyTo` Patterns

**Beispiel – pfadbasierte Rule (korrektes Frontmatter):**
```markdown
---
description: Java Spring Boot Pflichtregeln – aktiv bei .java Dateien
globs: ["**/*.java"]
alwaysApply: false
---

# Java Spring Boot – Pflichtregeln
- Services immer via Interface abstrahieren
- DTOs für API-Layer – Entities nie direkt exposen
- Alle Endpoints mit @PreAuthorize absichern
```

**Wichtig – korrekter Frontmatter-Key:**
```
globs: ["**/*.java"]   ← RICHTIG
paths: ["..."]         ← FALSCH (wird ignoriert)
```

**Empfohlene Rule-Struktur (unser DEV-SETUP):**
```
.claude/rules/
  java-spring.md        # Aktiv bei **/*.java
  vue-frontend.md       # Aktiv bei **/*.vue, **/*.ts
  db-migrations.md      # Aktiv bei **/*.sql, **/migrations/**
```

**Wichtig:** MEMORY.md – nur die ersten **200 Zeilen** werden beim Session-Start geladen. Topic-spezifische Dateien werden on-demand geladen. → MEMORY.md schlank halten, Details in separate Dateien auslagern.

### 3. Skills
- Anleitungen, damit der Agent **nach deinen Vorgaben** arbeitet
- Werden **nur bei Bedarf** geladen (→ spart Kontext)
- Ideal für: Domänenspezifisches Wissen, wiederkehrende Prozesse
- **Link:** https://resources.anthropic.com/hubfs/The-Complete-Guide-to-Building-Skill-for-Claude.pdf
- **Copilot-Äquivalent:** Skills in `.github/` oder Workspace-Skills

### 4. Sub-Agents
- Laufen **komplett isoliert** in einem eigenen Kontextfenster
- Für **leistungsintensive Aufgaben**: Frontend-Entwicklung, Testing, Refactoring
- Jeder Sub-Agent hat seine eigene `CLAUDE.md`-Konfiguration
- **Copilot-Äquivalent:** Subagent-Invocation via Agent Mode

### 5. MCP-Server
- Verbinden Claude Code mit **externen Systemen**
- Beispiele: GitHub, Jira, Supabase, Slack, Google, Draw.io
- Konfiguration in `.mcp.json`
- **Copilot-Äquivalent:** MCP-Server in VS Code Settings

---

## Konfigurationsdateien

### .claude/settings.json
```json
{
  "permissions": {
    "allow": [
      "Bash(git *)", "Bash(mvn *)", "Bash(npm *)", "Bash(npx *)",
      "Bash(docker *)", "Bash(flyway *)",
      "Read(*)", "Write(*)", "Edit(*)", "Glob(*)", "Grep(*)"
    ],
    "deny": [
      "Bash(rm -rf *)", "Bash(DROP *)", "Bash(DELETE FROM *)"
    ]
  },
  "enabledPlugins": {
    "context7@claude-plugins-official": true,
    "code-review@claude-plugins-official": true,
    "ralph-loop@claude-plugins-official": true,
    "typescript-lsp@claude-plugins-official": true,
    "security-guidance@claude-plugins-official": true,
    "figma@claude-plugins-official": true
  }
}
```
- **`permissions`:** Definiert welche Bash-Befehle Claude ohne Rückfrage ausführen darf
- **`enabledPlugins`:** Aktiviert Claude Code Plugins projekt-lokal (kein globales Setup nötig)
- **Aktion:** [ ] Team-Standard-Permissions definieren

### .mcp.json
```json
{
  "servers": {
    // MCP Tool-Konfigurationen für Claude Code / Copilot
  }
}
```
- **Zweck:** Externe Tool-Anbindungen definieren
- Siehe [12-MCP-und-Integrationen.md](12-MCP-und-Integrationen.md) für Details

### .claude/commands/ (Slash Commands)
- **Äquivalent zu Copilot:** Custom Prompt-Files
- Ermöglichen eigene `/`-Befehle
- Ideal für wiederkehrende Aufgaben: PR-Erstellung, Tests, Refactoring
- **Aktion:** [ ] Standard-Commands für das Team erstellen

---

## Claude Skills – Wie funktionieren sie? `⭐ HOCH`

### Konzept:
- Skills sind **strukturierte Anleitungen** (Markdown-Dateien)
- Sie werden vom Agent **on-demand** geladen wenn der Kontext passt
- Sie enthalten: Trigger-Beschreibung, Schritt-für-Schritt-Anleitungen, Constraints

### Aufbau einer Skill-Datei:
```markdown
---
description: Wann und wofür dieser Skill verwendet wird
applyTo: "**/*.py"  # Optional: Nur für bestimmte Dateitypen
---

# Skill Name

## Wann verwenden
...

## Anleitung
...

## Constraints
...
```

### Claude Code Plugin Marketplace `⭐ HOCH`

Claude Code hat einen Plugin-Marketplace mit offiziell gepflegten Erweiterungen.
Plugins sind **project-local installierbar** – kein globales Setup nötig, Projekt kopieren = fertig.

**Installation (im Projektverzeichnis):**
```bash
# Einzeln installieren
/plugin install context7@claude-plugins-official
/plugin install ralph-loop@claude-plugins-official

# Ergebnis: .claude/settings.json wird automatisch mit "enabledPlugins" erweitert
```

**Empfohlene Plugins für Entwickler-Teams:**
| Plugin | Was es tut |
|--------|-----------|
| `context7@claude-plugins-official` | Aktuelle Library-Docs im Kontext – kein Halluzinieren bei Versionsfragen |
| `ralph-loop@claude-plugins-official` | Iterative Selbst-Verifikation – KI prüft Output vor Antwort |
| `typescript-lsp@claude-plugins-official` | TypeScript Language Server Integration |
| `jdtls-lsp@claude-plugins-official` | Java Language Server Integration |
| `code-review@claude-plugins-official` | Automatische Code-Review-Hinweise |
| `security-guidance@claude-plugins-official` | Security-Checks beim Coden |
| `figma@claude-plugins-official` | Figma Design-Zugriff direkt im Kontext |
| `commit-commands@claude-plugins-official` | Commit-Message-Generierung |
| `superpowers@claude-plugins-official` | Erweiterte Reasoning-Fähigkeiten |

**Plugin vs. MCP-Server – der Unterschied:**
```
Plugin (enabledPlugins):  Erweitert das VERHALTEN von Claude Code
                          Beispiel: ralph-loop → KI prüft sich selbst
                          Nur für Claude Code verfügbar

MCP-Server (.mcp.json):   Gibt Claude Zugang zu EXTERNEN TOOLS
                          Beispiel: postgres → Claude kann DB abfragen
                          Funktioniert auch für GitHub Copilot
```

**Für Copilot-User:** Plugins haben kein direktes Äquivalent.
Teils durch MCP-Server ersetzbar (context7), teils durch copilot-instructions.md-Regeln.

### Anthropic Official Skills (GitHub Repo)
> **Repo:** https://github.com/anthropics/skills
> Enthält: frontend-design, doc-coauthoring, docx, pptx, xlsx, webapp-testing etc.
> **Nicht zu verwechseln** mit claude-plugins-official – das sind eigene Skill-Dateien zum Selbstbauen.

**Was ist enthalten:**
| Skill | Was er tut |
|-------|-----------|
| **Frontend Design Skill** | Generiert professionelle UI-Komponenten nach Design-Vorgaben |
| **Document Skills** | Erstellt/bearbeitet DOCX, PDF, PPTX, XLSX direkt |
| **webapp-testing** | Test-Generierung für Web-Apps |

**Struktur eines Skills (zum Selbstbauen):**
```markdown
---
name: my-skill-name
description: Was dieser Skill macht und wann er genutzt wird
---

# My Skill Name

## Wann verwenden
...

## Anleitung
...

## Guidelines
...
```

**Wichtig:** Der `description`-Wert bestimmt wann Claude den Skill automatisch lädt.

### Trusted AI (Enterprise) `🔵 MITTEL`
- **Link:** https://assets.anthropic.com/m/66daaa23018ab0fd/original/Anthropic-enterprise-ebook-digital.pdf
- Enterprise-Aspekte: Sicherheit, Compliance, Governance
- **Aktion:** [ ] Relevante Policies für unser Unternehmen extrahieren

---

## ⭐ Vollständiger Vergleich: Claude Code vs. GitHub Copilot

> **Kernfrage:** Kann ich alles was Claude Code kann, auch in GitHub Copilot machen – und dort sogar andere Modelle nutzen?

> **Kurze Antwort:** ~85% der Features haben ein Äquivalent in Copilot. Copilot hat den Vorteil der Modell-Wahl (Claude, GPT-4o, Gemini, o3 etc.). Claude Code hat Vorteile bei CLI-Power, Swarms und Browser-Verifikation.

### Feature-für-Feature-Vergleich

| Feature | Claude Code | GitHub Copilot | Status | Anmerkungen |
|---------|-------------|----------------|--------|-------------|
| **Projekt-Instruktionen** | `CLAUDE.md` | `.github/copilot-instructions.md` | ✅ Gleichwertig | Copilot-Variante auch pro Workspace möglich |
| **Datei-spezifische Rules** | `.claude/rules/*.md` mit glob | `.instructions.md` mit `applyTo` YAML | ✅ Gleichwertig | Copilot nutzt Frontmatter-YAML für Glob-Patterns |
| **Skills (On-Demand)** | `.claude/skills/` | `.github/copilot-skills/` oder Workspace-Skills | ✅ Gleichwertig | Copilot Skills werden über `description` getriggert |
| **Sub-Agents** | `Task(...)` API, isolierter Kontext | `@agent` Subagent-Invocation | ✅ Gleichwertig | Copilot Agents laufen ebenfalls isoliert |
| **Custom Agents (.md)** | Nicht direkt (eher Sub-Agents) | `.github/agents/*.md` (AGENTS.md) | ✅ Copilot besser | Copilot hat ein dediziertes Agent-System mit @name |
| **MCP-Server** | `.mcp.json` im Projekt | VS Code Settings + `.vscode/mcp.json` | ✅ Gleichwertig | Gleiche MCP-Server funktionieren in beiden |
| **Slash Commands** | `.claude/commands/*.md` | `.github/copilot-prompts/*.md` (Prompt Files) | ✅ Gleichwertig | Copilot nennt sie "Reusable Prompts" |
| **Permission-Kontrolle** | `.claude/settings.json` | VS Code Trust-Settings | ✅ Gleichwertig | Copilot hat feingranulare Tool-Genehmigungen |
| **CLI-Terminal-Modus** | ✅ Nativer CLI-Agent | ❌ Nur in VS Code | ⚠️ Claude besser | Claude Code lebt im Terminal, Copilot lebt in der IDE |
| **Plan Mode** | Nativer Plan Mode | Manuell via Prompt ("plan first") | ⚠️ Claude besser | Copilot hat keinen expliziten Plan-Modus |
| **Auto-Accept** | `--auto-accept` Flag | Agent Mode Trust-Level | ✅ Gleichwertig | Copilot: "Always allow" pro Tool |
| **Swarms (Multi-Agent)** | Claude Code Swarms | ❌ Nicht nativ | ❌ Claude exklusiv | Copilot hat keine Agent-Distribution |
| **Browser-Verifikation** | Chrome Extension | ❌ Nicht verfügbar | ❌ Claude exklusiv | Wichtig für den Verifikations-Workflow |
| **Mobile Remote Control** | Claude Code Mobile App | ❌ Nicht verfügbar | ❌ Claude exklusiv | Unterwegs Agents steuern |
| **Modell-Wahl** | Nur Claude-Modelle | Claude, GPT-4o, o3, Gemini, etc. | ⭐ Copilot besser | DER große Copilot-Vorteil |
| **Kosten** | Anthropic API / Max Plan | GitHub Copilot Subscription | Unterschiedlich | Copilot: Flat Rate vs. Claude: Usage-based |
| **IDE-Integration** | Leichtgewichtig (CLI) | Tiefe VS Code Integration | ⭐ Copilot besser | Code Actions, Inline Chat, Test Generation |
| **Inline Suggestions** | ❌ Nein (CLI-only) | ✅ Autocomplete während des Tippens | ⭐ Copilot besser | Copilot's Kernstärke |

---

### Copilot-Äquivalente im Detail

#### 1. Instructions = CLAUDE.md
```
Claude Code:     CLAUDE.md (Projekt-Root)
GitHub Copilot:  .github/copilot-instructions.md
```
- Beide werden bei **jeder Session** automatisch geladen
- Copilot unterstützt zusätzlich **Workspace-level** Instructions in VS Code Settings
- **Empfehlung:** Denselben Inhalt pflegen, nur Pfad anpassen

#### 2. Rules = Instruction Files mit applyTo
```
Claude Code:     .claude/rules/python-style.md (mit glob pattern)
GitHub Copilot:  .github/instructions/python-style.instructions.md
```
Copilot verwendet YAML-Frontmatter:
```yaml
---
description: "Python coding standards"
applyTo: "**/*.py"
---
Verwende Type Hints. Keine Funktionen über 30 Zeilen. ...
```

#### 3. Skills = Copilot Skills
```
Claude Code:     .claude/skills/deployment.md
GitHub Copilot:  Skills via Workspace-Skills oder .github/ Struktur
```
- Copilot Skills haben dieselbe Mechanik: **Trigger über description**, on-demand geladen
- Beispiel: Eine Skill-Datei mit `description: "Deployment auf Azure"` wird geladen wenn der User nach Deployment fragt

#### 4. Sub-Agents = @agent in Copilot
```
Claude Code:     Subtask("Recherchiere ...", agent_config)
GitHub Copilot:  Copilot ruft intern Subagents auf (Explore, etc.)
```
- Copilot hat eingebaute Agents (`@Explore`, Custom Agents via AGENTS.md)
- Claude Code Subagents sind flexibler konfigurierbar

#### 5. Custom Agents = AGENTS.md (Copilot-Exklusiv!)
Copilot hat ein Feature das Claude Code **nicht** hat:
```markdown
<!-- .github/agents/reviewer.md -->
---
name: CodeReviewer
description: Reviews PRs for quality
tools: ["read_file", "grep_search"]
---
Du bist ein erfahrener Code-Reviewer...
```
- Aufrufbar via `@CodeReviewer` im Chat
- Eigene Tool-Beschränkungen pro Agent
- **Empfehlung:** Das ist ein Copilot-Vorteil – nutzen!

#### 6. Slash Commands = Reusable Prompts
```
Claude Code:     .claude/commands/review.md → /review
GitHub Copilot:  .github/copilot-prompts/review.prompt.md → /review
```
- Funktional identisch
- Copilot Prompt Files unterstützen zusätzlich Variablen und Tool-Referenzen

#### 7. MCP Server = Identisch
```
Claude Code:     .mcp.json
GitHub Copilot:  .vscode/mcp.json oder VS Code Settings
```
- **Exakt dieselben MCP-Server** funktionieren in beiden Systemen
- Draw.io, Playwright, Figma – alles kompatibel
- Copilot: Konfiguration kann auch **User-level** gemacht werden (global für alle Projekte)

---

### Was Claude Code EXKLUSIV kann (nicht in Copilot)

| Feature | Warum relevant | Workaround in Copilot |
|---------|---------------|----------------------|
| **Swarms** | Multi-Agent parallel | Nicht möglich – manuell aufteilen |
| **Browser-Verifikation** (Chrome Ext.) | Agent prüft eigene Arbeit im Browser | Playwright MCP als Teilersatz |
| **CLI-Native** | Terminal-Power-User Workflow | Copilot CLI (begrenzt) |
| **Mobile App Remote** | Unterwegs Agents steuern | Nicht möglich |
| **Nativer Plan Mode** | Explizit planen vor Ausführung | Prompt: "Erstelle zuerst einen Plan" |
| **Permission dontAsk** | Agent arbeitet ungestört | "Always allow" pro Tool |

---

### Was GitHub Copilot BESSER kann

| Feature | Vorteil |
|---------|---------|
| **Modell-Wahl** | Claude, GPT-4o, o3, Gemini 2.5 – bestes Modell pro Task wählen |
| **Inline Autocomplete** | Code-Vorschläge beim Tippen – Claude Code hat das nicht |
| **IDE-Tiefenintegration** | Code Actions, Quick Fixes, Test Generation, Rename mit KI |
| **Custom Agents (@name)** | Dediziertes Agent-System mit eigenem Context und Tools |
| **Kosten-Planbarkeit** | Flat Rate vs. Usage-based API-Kosten |
| **Multi-Language-IDE** | Gleiche Erfahrung in VS Code, JetBrains, Neovim etc. |

---

### Empfehlung: Wie beides kombinieren?

```
┌──────────────────────────────────────────────────┐
│              Tägliche Entwicklung                  │
│                                                    │
│   GitHub Copilot (VS Code)                        │
│   ├── Inline Autocomplete (alle Sprachen)         │
│   ├── Agent Mode für komplexe Tasks               │
│   ├── MCP-Server für Tool-Integration             │
│   ├── Custom @Agents für Team-Workflows           │
│   └── Modell-Wahl pro Aufgabe                     │
│                                                    │
├──────────────────────────────────────────────────┤
│              Spezial-Aufgaben                      │
│                                                    │
│   Claude Code (Terminal)                           │
│   ├── Swarms für große Refactorings               │
│   ├── Browser-Verifikation (Chrome Extension)     │
│   ├── Mobile Remote für unterwegs                 │
│   └── CLI-Scripting & Batch-Operationen           │
│                                                    │
├──────────────────────────────────────────────────┤
│              Geteilte Konfiguration               │
│                                                    │
│   Shared:                                         │
│   ├── .mcp.json ←→ .vscode/mcp.json (gleiche     │
│   │   Server)                                     │
│   ├── Instructions (gleicher Inhalt, anderer Pfad)│
│   ├── Skills (gleiche Mechanik)                   │
│   └── Prompt Files / Slash Commands               │
└──────────────────────────────────────────────────┘
```

### Migrations-Checkliste: Claude Code → Copilot Äquivalente

- [ ] `CLAUDE.md` → `.github/copilot-instructions.md` kopieren
- [ ] `.claude/rules/*` → `.github/instructions/*.instructions.md` mit `applyTo`
- [ ] `.claude/skills/*` → Workspace-Skills mit `description` Trigger
- [ ] `.claude/commands/*` → `.github/copilot-prompts/*.prompt.md`
- [ ] `.mcp.json` → `.vscode/mcp.json` (Format fast identisch)
- [ ] Sub-Agents → `@agent` Custom Agents in `.github/agents/`
- [ ] Permissions → VS Code Trust Settings konfigurieren

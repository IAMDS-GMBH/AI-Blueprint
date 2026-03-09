# 11 – Lokale KI / On-Premise

> Modelle und Hardware für den lokalen Betrieb von KI ohne Cloud-Abhängigkeit.

---

## Lokale Laufzeitumgebungen

### OLLAMA `⭐ HOCH`
- **Was:** Plattform zum lokalen Betrieb von LLMs (llama, mistral, gemma, etc.)
- **Warum relevant:** Volle Datenhoheit, keine API-Kosten, Offline-Betrieb
- **Integration mit:**
  - **Claude Code CLI** → Lokales Modell statt Anthropic API
  - **GitHub Copilot** → Via VS Code LM API oder Custom Provider
  - **Moltot** → Vollautomatisierter Agent lokal
- **Aktion:** ✅ Setup dokumentiert – siehe Abschnitt "Ollama mit Claude Code / Copilot verbinden"

---

## Ollama mit Claude Code und Copilot verbinden `⭐ HOCH`

### Wann sinnvoll?
- Kein Anthropic API-Key vorhanden (z.B. neue Entwickler, Schulungsumgebung)
- Datenschutz: Kein Code verlässt das Netzwerk
- Kostenreduktion: Routine-Tasks lokal, komplexe Aufgaben in der Cloud
- Offline-Entwicklung (kein Internet)

### Schritt 1: Ollama installieren + Modell laden

```bash
# macOS
brew install ollama

# Linux
curl -fsSL https://ollama.com/install.sh | sh

# Windows: https://ollama.com → Download

# Ollama starten (läuft im Hintergrund)
ollama serve

# Modell herunterladen (einmalig, ~4-8 GB je nach Modell)
ollama pull llama3.2          # Allrounder, empfohlen
ollama pull mistral           # Schneller, leichter
ollama pull qwen2.5-coder     # Speziell für Code (empfohlen für Entwickler!)
ollama pull codellama         # Meta's Code-spezialisiertes Modell

# Verfügbare Modelle anzeigen
ollama list

# Test
curl http://localhost:11434/api/generate \
  -d '{"model": "llama3.2", "prompt": "Hallo!"}'
```

### Schritt 2: Claude Code mit Ollama verbinden

Ollama bietet eine OpenAI-kompatible API – Claude Code kann diese nutzen:

```bash
# Option A: Umgebungsvariablen setzen (für aktuelle Session)
export ANTHROPIC_BASE_URL=http://localhost:11434/v1
export ANTHROPIC_API_KEY=ollama        # Beliebiger Wert, wird ignoriert
export CLAUDE_MODEL=llama3.2           # Welches Ollama-Modell verwenden

claude

# Option B: Claude Code Config dauerhaft setzen
claude config set apiBaseUrl http://localhost:11434/v1
claude config set model llama3.2

# Zurückwechseln zur Anthropic Cloud
claude config set apiBaseUrl https://api.anthropic.com
claude config set model claude-sonnet-4-6
```

**Wichtig:** Nicht alle Claude-Code-Funktionen laufen mit lokalen Modellen:
| Feature | Mit Ollama | Bemerkung |
|---------|-----------|-----------|
| Code generieren | ✅ | Gut mit qwen2.5-coder |
| /plan | ✅ | Meist gut |
| /review | ⚠️ | Qualität abhängig vom Modell |
| /swarm (Sub-Agents) | ⚠️ | Funktioniert, aber langsamer |
| KERNEL Prompts | ✅ | Wie gewohnt |
| Tool Use (MCP) | ⚠️ | Nur wenn Modell Function Calling unterstützt |

### Schritt 3: GitHub Copilot mit Ollama verbinden

Copilot ist standardmäßig an GitHub/OpenAI gebunden. Es gibt zwei Wege:

**Option A: VS Code – Ollama als lokales Language Model (empfohlen)**

VS Code unterstützt seit 2024 lokale LLMs via Extension:

```
1. VS Code Extension installieren: "Ollama" (von ollama.ai)
2. VS Code Settings → "Copilot: Enable Local Models" = true
   (falls unterstützt in eurer VS Code Version)
```

Alternativ mit **Continue** Extension:
```
1. VS Code Extension "Continue" installieren
2. config.json (~/.continue/config.json) anpassen:
{
  "models": [{
    "title": "Llama 3.2 (lokal)",
    "provider": "ollama",
    "model": "llama3.2",
    "apiBase": "http://localhost:11434"
  }]
}
```

**Option B: Copilot Chat mit lokalem Kontext anreichern**

Copilot bleibt beim Cloud-Modell, bekommt aber lokale Antworten als Kontext:
- Nicht die bevorzugte Methode
- Für reine Offline-Nutzung ist Claude Code + Ollama besser geeignet

**Empfehlung:** Für lokale Entwicklung → Claude Code + Ollama. Für Cloud → Copilot mit GitHub.

### Welches Ollama-Modell für was?

| Aufgabe | Empfehlung | Größe |
|---------|-----------|-------|
| Code generieren (Java, Vue, SQL) | `qwen2.5-coder:7b` | 4 GB |
| Allgemeine Fragen + Code | `llama3.2:3b` | 2 GB |
| Komplexere Aufgaben (8+ GB RAM) | `llama3.1:8b` | 5 GB |
| Sehr gute Code-Qualität (16+ GB RAM) | `qwen2.5-coder:32b` | 19 GB |
| Schnell + leicht | `mistral:7b` | 4 GB |

```bash
# Empfehlung für Entwickler-Laptops (8-16 GB RAM):
ollama pull qwen2.5-coder:7b
```

### Hybrid-Workflow (empfohlen für Teams)

```bash
# Morgens: Lokales Modell für normale Entwicklung
export ANTHROPIC_BASE_URL=http://localhost:11434/v1
export ANTHROPIC_API_KEY=ollama
claude

# Bei komplexen Aufgaben / Code Review: Auf Cloud wechseln
unset ANTHROPIC_BASE_URL
export ANTHROPIC_API_KEY=sk-ant-...
claude
```

Oder via Shell-Alias:
```bash
# In ~/.zshrc oder ~/.bashrc
alias claude-local='ANTHROPIC_BASE_URL=http://localhost:11434/v1 ANTHROPIC_API_KEY=ollama claude'
alias claude-cloud='ANTHROPIC_API_KEY=sk-ant-... claude'
```

### Moltot + OLLAMA `🔵 MITTEL`
- **Was:** Vollautomatisierter Agent der komplett lokal läuft
- **Kombination:** Moltot (Agent-Framework) + OLLAMA (Modell-Runtime)
- **Warum relevant:** Autonomer Dev-Agent ohne jegliche Cloud-Abhängigkeit
- **Aktion:** [ ] PoC: Einfachen Automatisierungstask lokal laufen lassen

---

## Hardware

### AMD Ryzen AI Core `🔵 MITTEL`
- **Was:** Dedizierte KI-Beschleunigung in AMD Ryzen Prozessoren (NPU)
- **Warum relevant:** 
  - Ermöglicht lokale KI-Inferenz auf Standard-Laptops
  - Kein dedizierter GPU-Server nötig für kleinere Modelle
  - Energieeffizienter als GPU-basierte Inferenz
- **Beachten:** 
  - NPU-Unterstützung hängt vom Modell und Framework ab
  - Aktuell primär Windows-Support (DirectML)
- **Aktion:** [ ] Prüfen welche Modelle auf Ryzen AI effizient laufen

---

## Lokale Modell-Optionen

| Modell | Größe | Stärke | OLLAMA-Support |
|--------|-------|--------|----------------|
| Llama 3 | 8B-70B | Allrounder | ✅ |
| Mistral | 7B | Schnell, effizient | ✅ |
| Qwen3-Coder-Next | ? | Code-spezialisiert | Prüfen |
| PersonalPlex 7B | 7B | Conversational | Prüfen |
| GLM5 | ? | Open Source Alternative | Prüfen |
| Kimi | ? | Dev-Alternative | Eigene Runtime |

---

## Entscheidungsmatrix: Cloud vs. Lokal

| Kriterium | Cloud (API) | Lokal (OLLAMA) |
|-----------|-------------|----------------|
| Datenschutz | ⚠️ Daten verlassen Netzwerk | ✅ Alles lokal |
| Performance | ✅ Beste Modelle | ⚠️ Begrenzt durch Hardware |
| Kosten (einmalig) | — | Moderate (Hardware) |
| Kosten (laufend) | API-Kosten pro Token | Strom |
| Verfügbarkeit | ⚠️ Internet nötig | ✅ Offline möglich |
| Modell-Qualität | ✅ GPT-4/Claude | ⚠️ Kleiner, aber brauchbar |
| Setup-Aufwand | ✅ Minimal | 🔵 Moderat |

**Empfehlung:** Hybrid-Ansatz
- **Sensitiv Daten:** Lokale Modelle (OLLAMA)
- **Maximale Qualität:** Cloud-APIs (Claude, GPT-4)
- **Routine-Tasks:** Lokale Modelle reichen oft aus

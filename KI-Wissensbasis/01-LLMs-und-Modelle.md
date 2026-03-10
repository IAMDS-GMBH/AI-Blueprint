# 01 – LLMs & Modelle

> Neue Sprachmodelle und Open-Source-Alternativen, die als Ergänzung oder Ersatz für kommerzielle LLMs relevant sein könnten.

---

## Open-Source-Modelle

### GLM5 von Z.AI `⭐ HOCH`
- **Was:** Open-Source-LLM aus China, entwickelt von Zhipu AI (Z.AI)
- **Warum relevant:** Potenzielle Alternative zu GPT-4/Claude für Aufgaben, bei denen Datenhoheit oder Kosten eine Rolle spielen
- **Status:** Evaluieren – Benchmarks gegen aktuelle Modelle vergleichen
- **Aktion:** [ ] Benchmark-Vergleich mit Claude 3.5 / GPT-4o durchführen

### Qwen3-Coder-Next (Alibaba) `🔵 MITTEL`
- **Was:** Coding-spezialisiertes LLM von Alibaba (Qwen-Serie)
- **Warum relevant:** Spezialisiert auf Code-Generierung – könnte als lokales Coding-Modell dienen
- **Aktion:** [ ] Testen für Code-Review und Code-Generierung vs. Claude Code

### PersonalPlex 7B `🔵 MITTEL`
- **Was:** Conversational AI Modell mit 7B Parametern
- **Warum relevant:** Kompakt genug für lokalen Betrieb, spezialisiert auf Dialog
- **Aktion:** [ ] Mögliche Use Cases für interne Chatbots evaluieren

---

## Lokale Modelle & Alternativen

### Kimi als Dev-Alternative `🔵 MITTEL`
- **Was:** KI-Assistent (Moonshot AI, China), lokal betreibbar
- **Warum relevant:** Alternative für Entwicklungsaufgaben, wenn Cloud-Abhängigkeit reduziert werden soll
- **Aktion:** [ ] Lokale Installation testen, Coding-Capabilities bewerten

### Claude Code CLI + OLLAMA `⭐ HOCH`
- **Was:** Claude Code kann über OLLAMA mit lokalen Modellen betrieben werden
- **Warum relevant:** Ermöglicht Claude-Code-Workflows ohne API-Kosten und mit voller Datenhoheit
- **Aktion:** [ ] Setup-Anleitung erstellen, Performance mit lokalen vs. Cloud-Modellen vergleichen

---

## Mistral Coding-Modelle

### Devstral 2 (123B) `⭐ HOCH`
- **Was:** Flagship Coding-Modell von Mistral AI fuer Software Engineering Agents
- **Context:** 256K Tokens, 72.2% SWE-bench Verified
- **Warum relevant:** Bis zu 7x kosteneffizienter als Claude Sonnet, aktuell GRATIS via API
- **Lizenz:** Modified MIT (Open Source)
- **Aktion:** [ ] Vergleichstest Devstral 2 vs. Claude Sonnet fuer Code-Generierung

### Devstral Small 2 (24B) `🔵 MITTEL`
- **Was:** Kompakte Version fuer Laptops und Edge-Deployment
- **Context:** 256K Tokens, 68.0% SWE-bench Verified
- **Lizenz:** Apache 2.0 – lokal via OLLAMA betreibbar
- **Aktion:** [ ] Lokalen Betrieb via OLLAMA testen

### Codestral (25.01) `🔵 MITTEL`
- **Was:** Code-Generierung und IDE-Autocomplete mit FIM (Fill-in-the-Middle)
- **Sprachen:** 80+ inkl. COBOL
- **Pricing:** 0.30/0.90 EUR pro Million Tokens (sehr guenstig)
- **Aktion:** [ ] Continue.dev mit Codestral fuer Autocomplete konfigurieren

→ Details: siehe [17-Mistral-Teams-und-Ecosystem.md](17-Mistral-Teams-und-Ecosystem.md)

---

## Hinweise
- Bei der Modell-Evaluation immer beachten: **Lizenz, Datenschutz, Performance, Kosten**
- Lokale Modelle benötigen passende Hardware → siehe [11-Lokale-KI.md](11-Lokale-KI.md)

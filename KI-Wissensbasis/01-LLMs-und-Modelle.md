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

## Hinweise
- Bei der Modell-Evaluation immer beachten: **Lizenz, Datenschutz, Performance, Kosten**
- Lokale Modelle benötigen passende Hardware → siehe [11-Lokale-KI.md](11-Lokale-KI.md)

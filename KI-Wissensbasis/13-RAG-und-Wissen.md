# 13 – RAG & Wissensmanagement

> Retrieval Augmented Generation (RAG) und Technologien für unternehmensweites Wissensmanagement.

---

## CLaRa – Neuer Ansatz zu RAG (Apple) `⭐ HOCH`

### Was ist CLaRa?
- **Name:** CLaRa = Compression for Long-context RAG
- **Entwickler:** Apple Research
- **Link:** https://pub.towardsai.net/apple-releases-clara-a-new-approach-to-rag-compression-c9d074b5c999
- **Konzept:** 
  - Klassisches RAG hat das Problem, dass bei langen Kontexten die Qualität sinkt
  - CLaRa **komprimiert** den retrievierten Kontext intelligent
  - Behält die relevanten Informationen bei, reduziert aber Token-Verbrauch
- **Vorteile:**
  - Bessere Antwortqualität bei langen Dokumenten
  - Weniger Token-Verbrauch = niedrigere Kosten
  - Schnellere Antwortzeiten
- **Warum relevant:** Wir können damit größere Wissensbasen effizienter durchsuchen lassen
- **Aktion:** [ ] Paper lesen und evaluieren ob CLaRa für unsere Dokumentations-RAG geeignet ist

---

## NotebookLM-py – Programmatischer Zugriff auf NotebookLM `🔵 MITTEL`

> **Repo:** https://github.com/teng-lin/notebooklm-py
> Inoffizielle Python-Library die NotebookLM über die API zugänglich macht – mehr Funktionen als das Web-UI.

**Was es kann:**
- Podcasts, Videos, Quizzes, Flashcards, Slide Decks, Mind Maps aus Dokumenten generieren
- Quellen hinzufügen: URLs, PDFs, YouTube, Google Drive
- Batch-Downloads: MP3, MP4, JSON, CSV, PPTX (nicht im Standard Web-UI verfügbar)
- Chat & Research: Konversationelle Abfragen, automatischer Web-Import
- **Claude Code Agent Skills Integration** – als Skill in Workflows einbinden

**Installation:**
```bash
pip install notebooklm-py
# Mit Browser/Auth Support:
pip install "notebooklm-py[browser]"
```

**Use Cases für uns:**
- Research-Automatisierung: Dokumente automatisch als Podcast aufbereiten lassen
- Onboarding-Material: Interne Docs → Zusammenfassungen + Quizzes
- Meeting-Vorbereitung: Batch-Processing von Unterlagen
- KI-Workflow-Integration: Als MCP/Skill in Claude Code einbinden

---

## Google Notebook LM `⭐ HOCH`

### Was kann Notebook LM?
- Komplexe Themen auf Basis hochgeladener Dateien erklären
- Multiple Quellen zusammenführen und verknüpfen
- Podcast-artige Audio-Zusammenfassungen erstellen
- Fragen an die eigenen Dokumente stellen

### Use Cases im Unternehmen:
- **Onboarding:** Neue Mitarbeiter laden Dokumentation hoch, Notebook LM erklärt
- **Recherche:** Technische Papers zusammenfassen lassen
- **Meeting-Vorbereitung:** Relevante Dokumente zusammenfassen
- **Aktion:** [ ] Pilotprojekt mit interner Dokumentation starten

---

## N8N + GraphRAG `🔵 MITTEL`

### Konzept
- n8n-Workflows in Python-Code konvertieren
- GraphRAG hinzufügen für intelligente Wissensverknüpfung
- Kombination: Automatisierung + strukturiertes Wissen

### Warum GraphRAG statt Standard-RAG?
| Aspekt | Vector RAG | GraphRAG |
|--------|-----------|----------|
| Suche | Semantische Ähnlichkeit | Beziehungen & Zusammenhänge |
| Stärke | "Find similar" | "How relates to" |
| Schwäche | Verliert Zusammenhänge | Aufwändigerer Aufbau |
| Ideal für | FAQ, Docs | Vernetzte Wissenssysteme |

- **Aktion:** [ ] Evaluieren welche Wissensbereiche von GraphRAG profitieren würden

---

## WEBMCP als Wissens-Schnittstelle `🔵 MITTEL`

- WEBMCP kann auch als RAG-Layer fungieren
- APIs werden semantisch angereichert → LLMs verstehen den Kontext besser
- → Details in [12-MCP-und-Integrationen.md](12-MCP-und-Integrationen.md)

---

## Wissensmanagement-Strategie

```
┌─────────────────────────────────────────┐
│          Unternehmenswissen             │
├──────────────┬──────────────────────────┤
│ Strukturiert │ Unstrukturiert           │
│ (DB, APIs)   │ (Docs, PDFs, Wikis)     │
├──────────────┼──────────────────────────┤
│ WEBMCP       │ RAG / CLaRa             │
│ GraphRAG     │ Notebook LM             │
├──────────────┴──────────────────────────┤
│         KI-Agents & Workflows           │
│    (Claude, Copilot, Custom Agents)     │
└─────────────────────────────────────────┘
```

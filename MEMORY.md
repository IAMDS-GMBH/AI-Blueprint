# MEMORY.md – AI-Blueprint Projekt

> Wird zu Beginn jeder KI-Session automatisch geladen.

## Projektstruktur
- **KI-Wissensbasis/** — 16 Artikel zu AI & Entwicklung (LLMs, Tools, Agents, KERNEL, Security, MCP, RAG, etc.)
- **Schulung/** — 3-taegiger Entwickler-Kurs mit Theorie + Hands-on
  - Tag 1: Greenfield (Chatbot mit Spring Boot + Vue.js)
  - Tag 2: Coding Assistant + MCP-Server fuer Oracle DB
  - Tag 3: COBOL-Migration nach Java
  - PowerPoint-Prompts/ fuer Folien-Generierung
- **dev-setup-template/** — Portables Template (setup.sh kopiert Config in beliebige Projekte)

## Beziehungen
- dev-setup-template setzt Wissensbasis-Konzepte praktisch um
- Schulung vermittelt die wichtigsten Themen hands-on
- Teilnehmer kopieren dev-setup-template via setup.sh in ihre Projekte

## Tech Stack der Schulung
- Backend: Java 17, Spring Boot 3.2, Maven
- Frontend: Vue.js 3 + Vite
- KI: Claude API, Claude Code CLI, GitHub Copilot
- MCP-Server: TypeScript, Node.js 20+
- DB: Oracle (Tag 2)
- Lokal: OLLAMA

## Konventionen
- Ordner hiess frueher DEV-SETUP, jetzt dev-setup-template
- KERNEL-Framework ist der zentrale Prompting-Standard
- Schulungsinhalte auf Deutsch, du-Form

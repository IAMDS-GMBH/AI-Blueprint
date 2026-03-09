# CLAUDE.md – AI-Blueprint (Schulung & Wissensbasis)

> Diese Datei wird bei jeder KI-Session automatisch geladen.

---

## Projekt

**Name:** AI-Blueprint
**Zweck:** 3-taegige KI-Schulung fuer Entwickler + umfassende KI-Wissensdatenbank + portables Dev-Setup-Template
**Sprache der Inhalte:** Deutsch

---

## Projektstruktur

```
AI-Blueprint/
  KI-Wissensbasis/        # 16 Artikel zu AI & Entwicklung (LLMs, Tools, Security, MCP, etc.)
  Schulung/               # 3-taegiger Kurs (Theorie + Hands-on)
    Tag-1-Greenfield/     # LLM-Basics, KERNEL-Framework, Chatbot (Spring Boot + Vue.js)
    Tag-2-Coding-Assistant-MCP/  # Context-Hierarchie, MCP-Server fuer Oracle DB
    Tag-3-Migration/      # Code-Modernisierung, COBOL → Java
    PowerPoint-Prompts/   # Prompts fuer PPT-Generierung pro Tag
  dev-setup-template/     # Portables Template fuer Schulungsteilnehmer (setup.sh)
  CLAUDE.md               # Diese Datei
```

---

## Konventionen fuer Inhalte

### Schulungs-Dateien (Schulung/)
- Theorie-Dateien: Klar strukturiert mit nummerierten Abschnitten
- Aufgaben-Dateien: Schrittweise Anleitungen mit Zeitangaben
- Sprache: Verstaendlich, praxisnah, du-Form fuer Teilnehmer
- Code-Beispiele: Vollstaendig und lauffaehig
- Referenzen zur KI-Wissensbasis wo sinnvoll

### Wissensbasis-Dateien (KI-Wissensbasis/)
- Jeder Artikel mit Prioritaet (HOCH/MITTEL/NIEDRIG)
- Praktische Empfehlungen am Ende jedes Themas
- Aktionsitems als Checkboxen

### Dev-Setup-Template (dev-setup-template/)
- Template fuer beliebige Projekte, kopiert via setup.sh
- CLAUDE.md dort ist ein Template mit [PLACEHOLDER]-Werten
- Nicht fuer dieses Repo gedacht, sondern fuer Zielprojekte

---

## Arbeitsweise

- Vor Aenderungen an Schulungsinhalten: Konsistenz mit KI-Wissensbasis pruefen
- KERNEL-Framework ist der zentrale Prompting-Standard der Schulung
- Aenderungen am dev-setup-template muessen auch in der Schulungs-Theorie reflektiert werden
- Bei neuen KI-Erkenntnissen: Wissensbasis aktualisieren, dann pruefen ob Schulung betroffen

---

## Session-Start

1. `MEMORY.md` lesen (in ~/.claude/projects/.../memory/)
2. Bei Schulungsaenderungen: Abhaengigkeiten zwischen den 3 Tagen beachten
3. Bei Template-Aenderungen: setup.sh und README.md synchron halten

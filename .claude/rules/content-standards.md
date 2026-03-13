---
alwaysApply: false
globs:
  - "KI-Wissensbasis/**/*.md"
  - "dev-setup-template/**/*.md"
---

# Content-Standards

## Template-Dateien (dev-setup-template/)
- [PLACEHOLDER]-Muster fuer anpassbare Werte beibehalten
- Triple-Config: Aenderungen in .claude/ → auch in .github/ und .vibe/ umsetzen
- Agents/Rules/Skills muessen standalone funktionieren (keine Repo-Abhaengigkeit)
- setup.sh muss alle neuen Dateien kopieren

## Wissensbasis-Dateien (KI-Wissensbasis/)
- Jeder Artikel mit Prioritaet (HOCH/MITTEL/NIEDRIG)
- Praktische Empfehlungen am Ende jedes Themas
- Aktionsitems als Checkboxen
- Quellen mit Links und Datum angeben
- 00-INDEX.md bei neuen/geaenderten Artikeln aktualisieren

## Allgemein
- Sprache: Deutsch, technische Begriffe auf Englisch (MCP, KERNEL, Agent)
- Begriffe einheitlich: dev-setup-template (nicht DEV-SETUP), Triple-Config
- Keine unbelegten Behauptungen in der Wissensbasis

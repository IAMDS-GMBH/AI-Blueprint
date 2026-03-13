# Skill: Stack-Design

Erstelle neue Tech-Stacks fuer das dev-setup-template.

## Stack-Struktur
Jeder Stack besteht aus zwei Dateien in `dev-setup-template/stacks/<stack-name>/`:
- **rules.md** — Coding-Konventionen, Best Practices, NEVER/ALWAYS-Listen
- **snippets.md** — Code-Templates, Boilerplate, haeufige Patterns

## Workflow: Neuen Stack hinzufuegen

1. **Bestehende Stacks als Vorlage lesen:** `dev-setup-template/stacks/` durchsuchen
2. **Stack-Verzeichnis erstellen:** `stacks/<stack-name>/rules.md` + `snippets.md`
3. **techstack.conf erweitern:** Neuen Stack mit Beschreibung hinzufuegen
4. **setup.sh pruefen:** Sicherstellen dass Stack-Kopierlogik funktioniert

## Qualitaetskriterien fuer Rules
- Spezifisch fuer den Tech-Stack (keine generischen Tipps)
- NEVER/ALWAYS-Listen fuer haeufige Fehler
- Versions-Empfehlungen wo relevant
- Referenzen auf offizielle Docs

## Qualitaetskriterien fuer Snippets
- Vollstaendig und lauffaehig
- Kommentare erklaeren das "Warum"
- Best-Practice-Patterns (nicht nur "funktioniert irgendwie")

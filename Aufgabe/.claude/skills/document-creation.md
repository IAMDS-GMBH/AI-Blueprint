---
description: Wird geladen wenn technische Dokumente erstellt werden sollen – ADRs, technische Spezifikationen, Onboarding-Guides, Projektberichte. Angepasste Version des Anthropic Document Skills fuer unser Unternehmen.
---

# Skill: Document Creation

Basiert auf dem offiziellen Anthropic Document Skills, angepasst fuer unsere Dokumentations-Standards.

## Wann verwenden

- "Erstell ein ADR fuer die Entscheidung..."
- "Schreib eine technische Spezifikation fuer..."
- "Generiere einen Onboarding-Guide fuer..."
- "Erstell einen Projektbericht ueber..."
- "Fass diese Anforderungen als Tech-Spec zusammen"

## Verfuegbare Dokument-Typen

### 1. Architecture Decision Record (ADR)
```markdown
# ADR-[NR]: [Titel]
Datum: [DATUM]
Status: PROPOSED / AKZEPTIERT / ABGELEHNT / ERSETZT durch ADR-XXX
Autoren: [Namen]

## Kontext
[Was hat diese Entscheidung notwendig gemacht?
Welche Kräfte spielen eine Rolle (technisch, organisatorisch, zeitlich)?]

## Entscheidung
[Was wurde entschieden? Aktiv formuliert: "Wir verwenden X weil Y."]

## Begruendung
[Warum diese Option und nicht eine Alternative?]

## Alternativen betrachtet
| Option | Pro | Contra |
|--------|-----|--------|
| [Option A] | ... | ... |
| [Option B] | ... | ... |

## Konsequenzen
**Positiv:**
- ...

**Negativ / Risiken:**
- ...

**Offen:**
- [ ] Was muss noch entschieden werden?
```

### 2. Technische Spezifikation
```markdown
# Tech-Spec: [Feature-Name]
Version: 1.0
Status: DRAFT / REVIEW / APPROVED
Autor: [Name]
Datum: [DATUM]

## Zusammenfassung
[2-3 Saetze: Was wird gebaut und warum?]

## Anforderungen
### Funktionale Anforderungen
- FR-01: [Requirement]
- FR-02: [Requirement]

### Nicht-funktionale Anforderungen
- NFR-01: Performance: [Metrik]
- NFR-02: Security: [Anforderung]

## Technisches Design
### Architektur
[Komponenten-Diagramm oder Beschreibung]

### API-Endpunkte (falls relevant)
[OpenAPI-Snippet]

### Datenmodell (falls relevant)
[Entity-Beschreibung oder SQL]

## Implementierungsplan
- [ ] Phase 1: [Was]
- [ ] Phase 2: [Was]

## Offene Fragen
- [ ] [Frage fuer Review]
```

### 3. Onboarding-Guide
```markdown
# Onboarding-Guide: [System/Team/Rolle]
Stand: [DATUM]
Zielgruppe: [Neue Backend-Devs / Frontend-Devs / DevOps]

## Willkommen
[Kurze Beschreibung was hier gemacht wird und warum es wichtig ist]

## Voraussetzungen
- [ ] Zugang zu [System] erhalten
- [ ] [Tool] installiert

## Schritt-fuer-Schritt Einrichtung
### 1. [Schritt-Titel]
[Anleitung]
```bash
[Kommando]
```

## Wichtigste Konzepte
### [Konzept]
[Erklaerung]

## Haeufige Probleme
| Problem | Loesung |
|---------|---------|
| [Problem] | [Loesung] |

## Ansprechpartner
| Thema | Person |
|-------|--------|
| [Thema] | [Name / Slack] |
```

## Wie verwenden

```
"Erstell ein ADR fuer unsere Entscheidung Redis als Cache einzusetzen"
→ Skill fuellt das ADR-Template mit den relevanten Informationen

"Schreib eine Tech-Spec fuer den Password-Reset-Flow"
→ Skill erstellt vollstaendige Spezifikation aus der Aufgabenbeschreibung

"Generiere einen Onboarding-Guide fuer neue Vue.js Entwickler"
→ Skill erstellt Guide basierend auf unserem Stack und unseren Konventionen
```

## Output

Dokumente werden direkt als Markdown-Datei geschrieben:
```
docs/adr/ADR-001-redis-als-cache.md
docs/specs/password-reset-spec.md
docs/onboarding/vue-developer-guide.md
```

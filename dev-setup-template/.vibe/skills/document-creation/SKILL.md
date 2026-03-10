---
name: document-creation
description: Wird geladen wenn technische Dokumente erstellt werden sollen – ADRs, Tech-Specs, Onboarding-Guides, Projektberichte.
license: MIT
compatibility: Python 3.12+
user-invocable: true
allowed-tools:
  - read_file
  - write_file
  - grep
---

# Skill: Document Creation

## Wann verwenden

- "Erstell ein ADR fuer die Entscheidung..."
- "Schreib eine technische Spezifikation fuer..."
- "Generiere einen Onboarding-Guide fuer..."
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
Welche Kraefte spielen eine Rolle (technisch, organisatorisch, zeitlich)?]

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
[Kurze Beschreibung]

## Voraussetzungen
- [ ] Zugang zu [System] erhalten
- [ ] [Tool] installiert

## Schritt-fuer-Schritt Einrichtung
### 1. [Schritt-Titel]
[Anleitung + Kommando]

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

## Output
Dokumente werden direkt als Markdown-Datei geschrieben:
```
docs/adr/ADR-001-redis-als-cache.md
docs/specs/password-reset-spec.md
docs/onboarding/vue-developer-guide.md
```

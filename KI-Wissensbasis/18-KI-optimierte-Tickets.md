# 18 — KI-optimierte Tickets schreiben

> Prioritaet: ⭐ HOCH | Kategorie: Prompt Engineering / Entwicklungsprozess

## Ueberblick

Traditionelle Tickets (Jira, Linear, GitHub Issues) setzen auf implizites Teamwissen: muendliche Absprachen, Sprint-Meeting-Kontext, Erfahrung im Projekt. KI-Coding-Assistenten (Claude Code, Copilot, Cursor) haben keinen Zugang zu diesem impliziten Wissen. Ergebnis: KI fragt nach, stoppt, oder baut am Bedarf vorbei.

KI-optimierte Tickets machen implizites Wissen explizit — und nutzen dafuer das KERNEL-Framework als Checkliste.

---

## Kernthemen

### 1. Implizites vs. explizites Wissen in Tickets

**Das Problem:**
```
Ticket: "Performance der Suche verbessern"

Mensch denkt: "Ah, die Elasticsearch-Query im ProductService ist langsam,
              Max hat letzte Woche schon Indizes angelegt, ich soll die
              Query optimieren. Soll unter 200ms bleiben."

KI sieht: "Performance der Suche verbessern" — und weiss nicht wo anfangen.
```

Was ein Mensch automatisch ergaenzt, muss fuer KI explizit im Ticket stehen:
- **Welche** Suche? (Modul, Datei, Endpoint)
- **Was** ist das Problem? (Messwerte, Logs, Error)
- **Welcher** Tech-Stack? (Elasticsearch 8.x, Spring Boot 3.2)
- **Was** ist "gut genug"? (< 200ms p95)

### 2. KERNEL-Framework auf Tickets angewendet

Das KERNEL-Framework gilt nicht nur fuer Prompts — es ist die ideale Checkliste fuer Tickets:

| KERNEL-Prinzip | Anwendung auf Tickets | Beispiel |
|---|---|---|
| **K**eep simple | Ein Ticket = eine klar abgegrenzte Aenderung | Nicht "Auth + Profil + E-Mail" in einem Ticket |
| **E**xplicit constraints | Was NICHT gemacht werden soll | "NICHT: Frontend-Aenderungen (separates Ticket)" |
| **R**eproducible | Versionen, Branch, Environment explizit | "Branch: feature/auth, Spring Boot 3.2.5, PostgreSQL 16" |
| **N**arrow scope | Kein "und ausserdem..." — separates Ticket | Jedes "ausserdem" wird ein eigenes Ticket |
| **E**asy to verify | Akzeptanzkriterien als Checkliste | "[ ] POST /api/auth/reset gibt 200 zurueck" |
| **L**ogical structure | Kontext → Anforderung → Constraints → Erfolg | Klare Abschnitte statt Freitext |

### 3. Ticket-Templates mit KI-Fokus

#### Variante A: Feature-Ticket

```markdown
## Titel: [Bereich] Aktion + Ziel
   Beispiel: "Auth: Password-Reset Token per E-Mail senden"

### Kontext
- Betroffene Dateien/Module: src/auth/...
- Tech-Stack: Spring Boot 3.2, PostgreSQL (siehe CLAUDE.md)
- Abhaengigkeiten: Braucht Feature X (Ticket #123)
- Aktueller Stand: Endpoint existiert noch nicht

### Anforderung
- WAS gebaut werden soll (nicht WIE)
- Endpunkte, Felder, Verhalten
- Business Rules

### Akzeptanzkriterien
- [ ] POST /api/v1/auth/forgot-password sendet E-Mail
- [ ] Token laeuft nach 24h ab
- [ ] Token ist einmalig verwendbar
- [ ] Test: Happy Path + Token expired + Invalid Token

### Scope-Grenzen
- NICHT: Frontend-Aenderungen (separates Ticket)
- NICHT: E-Mail-Template-Design
- NICHT: Rate-Limiting (kommt in Ticket #125)
```

#### Variante B: Bug-Ticket

```markdown
## Titel: [Bereich] Bug: Symptom
   Beispiel: "Search: Timeout bei Produktsuche mit Sonderzeichen"

### Reproduktion
- Endpoint: GET /api/v1/products?q=Bürostuhl%26Tisch
- Erwartetes Verhalten: Ergebnis in < 500ms
- Tatsaechliches Verhalten: Timeout nach 30s
- Logs: [Link oder Auszug]
- Environment: Staging, PostgreSQL 16, Spring Boot 3.2.5

### Root Cause (falls bekannt)
- Query escaped Sonderzeichen nicht korrekt in ProductRepository.java:45

### Fix-Kriterien
- [ ] Query mit Sonderzeichen gibt Ergebnis in < 500ms
- [ ] Bestehende Tests laufen weiter gruen
- [ ] Neuer Test fuer Sonderzeichen-Query

### Scope-Grenzen
- NUR Query-Fix, NICHT Performance-Optimierung generell
```

#### Variante C: Refactoring-Ticket

```markdown
## Titel: [Bereich] Refactoring: Was + Warum
   Beispiel: "UserService: God-Class aufteilen fuer bessere Testbarkeit"

### Kontext
- Betroffene Datei: src/service/UserService.java (450 Zeilen)
- Problem: Klasse hat 12 Methoden, mischt Auth + Profile + Notifications
- Ziel: Drei separate Services mit klarer Verantwortung

### Gewuenschte Struktur
- AuthService: login, logout, resetPassword
- ProfileService: getProfile, updateProfile, deleteProfile
- NotificationService: sendEmail, sendPush

### Constraints
- Bestehende API-Endpunkte duerfen sich NICHT aendern
- Alle bestehenden Tests muessen gruen bleiben
- Keine neuen Features, nur Umstrukturierung

### Akzeptanzkriterien
- [ ] Drei separate Service-Klassen erstellt
- [ ] Controller nutzt neue Services statt UserService
- [ ] Alle bestehenden Tests gruen
- [ ] Keine Aenderung an API-Responses
```

### 4. Vorher/Nachher-Vergleich

**Vorher — vages Ticket:**
```
Titel: Login verbessern
Beschreibung: Der Login soll besser werden. Bitte auch an die Security denken.
```

**KI-Ergebnis mit vagem Ticket:**
- Fragt 5x nach ("Was heisst besser?", "Welcher Auth-Mechanismus?")
- Baut Features die niemand wollte (z.B. OAuth2 statt Simple JWT)
- Ignoriert bestehende Patterns im Projekt
- Ergebnis passt nicht zum Tech-Stack

**Nachher — KI-optimiertes Ticket:**
```
Titel: Auth: JWT Refresh-Token Rotation implementieren
Kontext: Spring Boot 3.2, Spring Security 6.x, JWT via jjwt 0.12
Anforderung: Access-Token (15min) + Refresh-Token (7d) mit Rotation
Akzeptanzkriterien:
- [ ] POST /api/v1/auth/refresh rotiert Refresh-Token
- [ ] Altes Refresh-Token wird invalidiert
- [ ] Test: Happy Path, Expired Token, Reuse Detection
Scope-Grenzen: NICHT: Frontend, NICHT: OAuth2
```

**KI-Ergebnis mit optimiertem Ticket:**
- Startet sofort mit der Implementierung
- Nutzt korrekte Dependencies (jjwt 0.12)
- Haelt sich an Scope-Grenzen
- Akzeptanzkriterien werden 1:1 zu Tests

### 5. Akzeptanzkriterien → automatisierte Tests

Der groesste Vorteil von expliziten Akzeptanzkriterien: KI kann sie direkt in Tests umwandeln.

**Given/When/Then Format (ideal fuer KI):**
```
Given: Ein User mit gueltigem Refresh-Token
When:  POST /api/v1/auth/refresh mit dem Token
Then:  Neuer Access-Token (200 OK) + neuer Refresh-Token
       UND alter Refresh-Token ist invalidiert
```

**Checklisten-Format (kompakter):**
```
- [ ] POST /refresh → 200 + neue Tokens
- [ ] POST /refresh mit abgelaufenem Token → 401
- [ ] POST /refresh mit bereits genutztem Token → 401 (Reuse Detection)
```

Beide Formate funktionieren — KI generiert daraus automatisch Testmethoden.

### 6. Integration in Ticketsysteme

#### Jira
- Custom Issue Type "KI-Feature" mit Pflichtfeldern: Tech-Kontext, Akzeptanzkriterien, Scope-Grenzen
- Template als Issue Template im Projekt hinterlegen
- Automation Rule: Ticket ohne Akzeptanzkriterien → Warning-Label

#### Linear
- Issue Template mit vordefinierten Sections
- Labels: `ki-ready` fuer Tickets die alle Pflichtfelder haben
- Cycles nutzen um Scope klein zu halten

#### GitHub Issues
- Issue Template in `.github/ISSUE_TEMPLATE/ki-feature.yml`
- Formular mit Pflichtfeldern statt Freitext
- Labels: `ki-ready`, `needs-context`

### 7. Haeufige Fehler und Anti-Patterns

| Anti-Pattern | Problem | Besser |
|---|---|---|
| "Verbessere die Performance" | KI weiss nicht wo, was, wieviel | "Reduziere Query-Zeit in ProductService.search() auf < 200ms" |
| Kein Tech-Kontext | KI raet den Stack | "Spring Boot 3.2, PostgreSQL 16, jjwt 0.12" |
| Mehrere Features in einem Ticket | KI verliert Fokus | Ein Ticket = eine Aenderung |
| Implizite Annahmen | "Wie immer" — KI kennt "immer" nicht | Alles ausschreiben, auch Offensichtliches |
| Fehlende Negativkriterien | KI baut uebers Ziel hinaus | "NICHT: Frontend, NICHT: Refactoring" |
| Akzeptanzkriterien als Prosa | Schwer automatisiert testbar | Checkliste oder Given/When/Then |
| "Bitte auch an Security denken" | Zu vage fuer KI | Konkrete Security-Requirements auflisten |

---

## Praktische Empfehlungen

- **KERNEL als Checkliste:** Vor dem Ticket-Erstellen alle 6 Prinzipien durchgehen
- **Akzeptanzkriterien immer als Checkliste** oder Given/When/Then — nie als Fliesstext
- **Tech-Kontext aus CLAUDE.md/copilot-instructions referenzieren** — nicht duplizieren, sondern verweisen
- **Ticket-Templates im Team einrichten** — einmal aufsetzen, alle profitieren
- **"KI-ready" Label einfuehren** — Team-Signal dass ein Ticket genug Kontext hat
- **Review-Frage bei jedem Ticket:** "Koennte ein neuer Entwickler (oder KI) das ohne Rueckfragen umsetzen?"

---

## Links & Quellen

- [Anthropic Claude Code Dokumentation](https://docs.anthropic.com/en/docs/claude-code) — Best Practices fuer Kontext-Management
- [Atlassian: AI-assisted Development](https://www.atlassian.com/software/jira/ai) — Jira AI-Features und Ticket-Optimierung
- [Thoughtworks Technology Radar 2025](https://www.thoughtworks.com/radar) — AI-augmented Development Practices
- [Linear Method Documentation](https://linear.app/method) — Effektive Issue-Workflows
- [GitHub Issue Forms](https://docs.github.com/en/communities/using-templates-to-encourage-useful-issues-and-pull-requests/syntax-for-issue-forms) — Template-Syntax fuer strukturierte Issues

---

## Aktionsitems

- [ ] Ticket-Template (Feature/Bug/Refactoring) in eurem Jira/Linear/GitHub einrichten
- [ ] Bestehendes Ticket mit KERNEL-Checkliste ueberarbeiten und Ergebnis vergleichen
- [ ] Team-Review: 5 Tickets der letzten Woche auf KI-Tauglichkeit pruefen
- [ ] "ki-ready" Label im Ticketsystem anlegen
- [ ] CLAUDE.md/copilot-instructions.md um Ticket-Referenz ergaenzen (Tech-Stack dort pflegen statt in jedem Ticket)

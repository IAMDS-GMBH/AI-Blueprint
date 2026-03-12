# Lessons Learned – [PROJEKTNAME / TEAM]

> Dieses Dokument wird nach jeder Korrektur durch den User oder nach einem Fehler aktualisiert.
> Ziel: Denselben Fehler nie zweimal machen.
> Die KI liest diese Datei zu Beginn jeder neuen Session.

---

## Format

```
### [DATUM] – [Kurztitel des Fehlers]
**Was passierte:** [Beschreibung was schiefging]
**Root Cause:** [Eigentliche Ursache]
**Regel fuer die Zukunft:** [Konkrete Regel die diesen Fehler verhindert]
```

---

## Eintragen wenn:
- Der User eine Korrektur vornimmt
- Ein Deployment-Fehler aufgetreten ist
- Ein Missverstaendnis zu unnoetigem Aufwand gefuehrt hat
- Eine Annahme falsch war

---

## Lessons

### 2026-03-11 – Lombok-Version inkompatibel mit Java 25
**Was passierte:** `mvn compile` schlug fehl — Lombok 1.18.30 (Spring Boot 3.2.5 Default) ist nicht kompatibel mit Java 25
**Root Cause:** Spring Boot 3.2.x bringt eine aeltere Lombok-Version mit, die neuere Java-Versionen nicht unterstuetzt
**Regel:** Bei Java 21+ immer Lombok-Version explizit auf 1.18.44+ setzen und `maven-compiler-plugin` mit Lombok Annotation Processor konfigurieren

### 2026-03-11 – start.sh killt Port aber wartet nicht
**Was passierte:** Script hat Port 8080 per `kill -9` freigegeben, aber sofort das Backend gestartet. Der alte Prozess war noch nicht vollstaendig beendet → Maven Build Failure (Exit 137)
**Root Cause:** `kill -9` sendet das Signal, aber der Prozess braucht Zeit zum Beenden. Ohne Warten startet der neue Prozess auf einem noch belegten Port
**Regel:** Nach Port-Kill immer aktiv warten bis `lsof` den Port als frei meldet (Polling mit Timeout). `free_port`-Funktion mit Retry-Loop verwenden

### 2026-03-11 – set -e in Start-Scripts ist gefaehrlich
**Was passierte:** `set -e` im start.sh hat das Script bei jedem non-zero Exit abgebrochen — auch bei erwarteten Fehlern wie `kill` auf bereits beendete Prozesse
**Root Cause:** `set -e` beendet das Script bei jedem fehlgeschlagenen Befehl, auch wenn der Fehler harmlos ist (z.B. `kill` auf nicht-existente PID)
**Regel:** In Start-Scripts die Hintergrundprozesse managen kein `set -e` verwenden. Stattdessen Fehler gezielt pruefen

### 2026-03-11 – Maven nicht installiert auf macOS
**Was passierte:** `mvn` Befehl war nicht verfuegbar — Agent musste Maven erst via Homebrew installieren
**Root Cause:** Maven ist kein Standard-Tool auf macOS und war nicht vorinstalliert
**Regel:** Immer Maven Wrapper (`./mvnw`) statt globales `mvn` verwenden. Im start.sh und in der Doku immer `./mvnw` referenzieren — das macht das Projekt portabel

### 2026-03-11 – .gitignore fehlte von Anfang an
**Was passierte:** User musste manuell nach einer .gitignore fragen — sie wurde beim Projekt-Setup nicht automatisch erstellt
**Root Cause:** Beim Greenfield-Setup wurde keine .gitignore mitgeneriert
**Regel:** Bei jedem neuen Projekt immer eine .gitignore anlegen — Java (target/, .class), Node (node_modules/, dist/), IDE (.idea/, .vscode/), Secrets (.env), OS (.DS_Store)

### 2026-03-11 – Tests wurden nicht automatisch geschrieben
**Was passierte:** Swarm hatte nur Dev + Review Agents — Tests wurden uebersprungen und als "nicht im Scope" deklariert
**Root Cause:** Kein Test Agent im Swarm-Plan vorgesehen. Tests sind aber Pflicht fuer jedes neue Feature (siehe CLAUDE.md)
**Regel:** Swarm-Reihenfolge ist immer: Dev Agent(s) → Test Agent(s) → Review Agent. Test Agents schreiben Backend-Tests (JUnit 5 + Mockito) und Frontend-Tests (Vitest + Vue Test Utils) fuer alle neuen Features. Tests sind kein "nice-to-have" sondern fester Bestandteil jedes Swarms

### 2026-03-11 – lessons.md wurde nicht automatisch vom Review Agent aktualisiert
**Was passierte:** Lessons wurden erst nach expliziter Aufforderung des Users eingetragen statt automatisch durch den Verification/Review Agent
**Root Cause:** Der Review Agent hatte keinen Auftrag, lessons.md zu aktualisieren — er hat nur geprueft, nicht dokumentiert
**Regel:** Der Verification/Review Agent muss IMMER als letzten Schritt die lessons.md aktualisieren mit allen Fixes, Workarounds und Erkenntnissen die waehrend der Ausfuehrung aufgetreten sind. Das gehoert in den Swarm-Plan als fester Verification-Schritt

### 2026-03-12 – SQL-Injection in MCP-Server WHERE-Klauseln
**Was passierte:** Die Tools `count-rows` und `query-table` akzeptierten einen `whereClause`-Parameter als Freitext-String der direkt in die SQL-Query konkateniert wurde. Das ermoeglicht SQL-Injection-Angriffe (z.B. `1=1; DROP TABLE users`)
**Root Cause:** WHERE-Klauseln koennen nicht einfach parametrisiert werden ($1) da sie beliebige Ausdruecke enthalten. Die Entwicklung hat die String-Konkatenation ohne Validierung uebernommen
**Regel:** Bei dynamischen SQL-Klauseln die nicht parametrisierbar sind: Blocklist mit gefaehrlichen SQL-Keywords implementieren (DROP, DELETE, INSERT, UPDATE, ALTER, UNION, etc.) und Sonderzeichen wie `;` und `--` blocken. Besser: WHERE-Klauseln nur als strukturierte Parameter akzeptieren (column, operator, value) statt Freitext

### 2026-03-12 – Column-Injection in query-table Tool
**Was passierte:** Das `columns`-Array im `query-table`-Tool wurde direkt in die SQL-Query interpoliert ohne zu pruefen ob die Spaltennamen tatsaechlich in der Tabelle existieren. Ein Angreifer haette beliebige SQL-Ausdruecke als "Spaltennamen" einschleusen koennen
**Root Cause:** Spaltennamen wurden als vertrauenswuerdig behandelt obwohl sie User-Input sind
**Regel:** User-uebergebene Spaltennamen immer gegen das tatsaechliche Schema der Tabelle validieren. Nur Spalten zulassen die in `information_schema.columns` existieren. Spaltennamen in Anfuehrungszeichen wrappen um Identifier-Injection zu verhindern

### 2026-03-12 – isToolAllowed() in auth-middleware.ts ist Dead Code
**Was passierte:** Die Funktion `isToolAllowed()` listet Tool-Namen die nicht mit den tatsaechlich registrierten Tools uebereinstimmen (z.B. `health-check`, `user-statistics` existieren nicht; `list-tables`, `describe-table`, `count-rows`, `query-table` fehlen). Zudem wird die Funktion nirgends aufgerufen
**Root Cause:** Die Auth-Middleware wurde als Vorlage erstellt aber nicht vollstaendig in die Tool-Registrierung integriert
**Regel:** Dead Code vermeiden. Wenn Auth-Funktionen implementiert werden, muessen sie auch in der Tool-Ausfuehrung aufgerufen werden. Tool-Listen in Auth-Checks muessen synchron mit den registrierten Tools gehalten werden

### 2026-03-12 – Health-Check-URL in start.sh falsch
**Was passierte:** start.sh pollt `/health` fuer den MCP-Server Health-Check, aber der HTTP-Wrapper definiert den Endpoint unter `/api/health`. Das fuehrt dazu, dass der Health-Check 10 Sekunden lang fehlschlaegt und das Script mit einem Fehler abbricht
**Root Cause:** Agent 4 (start.sh) und Agent 1 (HTTP-Wrapper) haben unabhaengig gearbeitet und die Health-Check-URL war nicht im gemeinsamen Vertrag definiert
**Regel:** Bei Swarm-Arbeit muessen alle Endpoints explizit im gemeinsamen Vertrag definiert werden — auch Health-Check-URLs. Agents die aufeinander aufbauen muessen die tatsaechlichen Endpoints des Vorgaenger-Agents pruefen, nicht raten

### 2026-03-12 – Sensitive Columns bei SELECT * nicht gefiltert
**Was passierte:** Das `query-table`-Tool im HTTP-Wrapper filterte sensible Spalten (password, secret, api_key, token) nur wenn explizite Spalten angegeben wurden. Bei `SELECT *` (dem Default) wurden alle Spalten zurueckgegeben, inklusive sensibler Daten
**Root Cause:** Die Wildcard-Expansion (`*` → alle Spaltennamen) lief ohne den Sensitive-Column-Filter
**Regel:** Sensitive-Column-Filtering muss auf ALLEN Code-Pfaden greifen — sowohl bei expliziter Spaltenauswahl als auch bei Wildcard. Beim Review immer alle Branches pruefen, nicht nur den Hauptpfad

### 2026-03-12 – query-users und get-user-by-email setzen users-Tabelle voraus
**Was passierte:** 2 von 13 API-Tests schlugen fehl weil die Tools `query-users` und `get-user-by-email` auf eine `users`-Tabelle zugreifen die in der `abrechnung`-Datenbank nicht existiert
**Root Cause:** Diese Tools stammen aus dem Starter-Code und wurden fuer eine andere Datenbank-Struktur entworfen
**Regel:** Bei der Integration von Starter-Code immer pruefen ob die vorausgesetzten DB-Objekte (Tabellen, Views) tatsaechlich existieren. Tools die nicht funktionsfaehig sind sollten entweder entfernt oder als "nicht verfuegbar" markiert werden

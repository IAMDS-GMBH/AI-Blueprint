# 04 – Prompt Engineering

> Techniken, Frameworks und Best Practices für effektives Prompting.

---

## KERNEL-Framework 📸 `⭐ HOCH`

> Quelle: r/PromptEngineering – "After 1000 hours of prompt engineering, I found the 6 patterns that actually matter"

Das KERNEL-Framework besteht aus 6 Prinzipien:

### K – Keep it simple
- **Schlecht:** 500 Wörter Kontext
- **Gut:** Ein klares Ziel
- **Beispiel:** Statt *"I need help writing something about Redis"* → *"Write a technical tutorial on Redis caching"*
- **Ergebnis:** 70% weniger Token-Verbrauch, 3x schnellere Antworten

### E – Explicit constraints
- Dem KI-Modell sagen, was es **NICHT** tun soll
- *"Python code"* → *"Python code. No external libraries. No functions over 20 lines."*
- **Ergebnis:** Constraints reduzieren unerwünschte Outputs um 91%

### R – Reproducible results
- Temporale Referenzen vermeiden ("current trends", "latest best practices")
- Spezifische Versionen und exakte Anforderungen nutzen
- Derselbe Prompt muss nächste Woche/nächsten Monat funktionieren
- **Ergebnis:** 94% Konsistenz über 30 Tage

### N – Narrow scope
- **Ein Prompt = ein Ziel**
- Code + Docs + Tests NICHT in einer Anfrage kombinieren
- Komplexe Aufgaben aufteilen
- **Ergebnis:** Single-Goal-Prompts 89% Zufriedenheit vs. 41% bei Multi-Goal

### E – Easy to verify
- Prompts brauchen **klare Erfolgskriterien**
- *"Make it engaging"* → *"Include 3 code examples"*
- Wenn du Erfolg nicht verifizieren kannst, kann die KI es nicht liefern
- **Ergebnis:** 85% Erfolgsrate mit klaren Kriterien vs. 41% ohne

### L – Logical structure
- Jeder Prompt folgt dieser Struktur:
  1. **Context** (Input)
  2. **Task** (Function)
  3. **Constraints** (Parameters)
  4. **Format** (Output)

### KERNEL – Gemessene Ergebnisse
| Metrik | Vorher | Nachher |
|--------|--------|---------|
| First-try Success | 72% | 94% |
| Time to useful result | — | -67% |
| Token usage | — | -58% |
| Accuracy improvement | — | +340% |
| Revisions needed | 3.2 | 0.4 |

### KERNEL – Praxisbeispiel

**Vorher (vage):**  
> "Help me write a script to process some data files and make them more efficient"  
→ Ergebnis: 200 Zeilen generischer, unbrauchbarer Code

**Nachher (KERNEL):**
```
Task: Python script to merge CSVs
Input: Multiple CSVs, same columns
Constraints: Pandas only, <50 lines
Output: Single merged.csv
Verify: Run on test_data/
```
→ Ergebnis: 37 Zeilen, funktioniert beim ersten Versuch

**Advanced Tip:** Mehrere KERNEL-Prompts verketten statt einen komplexen zu schreiben. Jeder Prompt macht eine Sache gut, gibt das Ergebnis an den nächsten weiter. Modell-agnostisch (GPT-5, Claude, Gemini, Llama).

---

## Chain-of-Verification (Meta) 📸 `⭐ HOCH`

> Prompt-Technik die Selbstverifikation erzwingt – reduziert Halluzinationen und Fehler.

### Prompt-Struktur:

```
---
[Deine Frage]

Now follow these steps:
1. Provide your initial answer
2. Generate 3-5 verification questions that would expose errors in your answer
3. Answer each verification question independently
4. Provide your final revised answer based on the verification
---
```

### Warum das funktioniert:
- Das Modell prüft sich **selbst** auf Fehler
- Verifikationsfragen decken blinde Flecken auf
- Die finale Antwort ist factual korrekter
- **Besonders gut für:** Factual Claims, technische Aussagen, Analysen

---

## Anthropic Best Practices (offiziell) `⭐ HOCH`

> Quelle: platform.claude.com/docs – Prompt Engineering Best Practices

### 1. Klar und direkt sein

KI ist kein Gedankenleser. Was vage ist, wird vage beantwortet.

```
Schlecht: "Mach das besser"
Gut:      "Refactore diese Methode: extrahiere die Validierung in eine eigene
           Methode, reduziere auf max. 20 Zeilen, behalte dieselbe Signatur"
```

**Regel:** Wenn die Anfrage an einen neuen Praktikanten unklar wäre, ist sie für die KI auch unklar.

### 2. Kontext und Motivation erklären

Erkläre **warum** du etwas willst – nicht nur was. Das verbessert den Output dramatisch.

```
Ohne Kontext:  "Schreib Tests für UserService"
Mit Kontext:   "Wir haben einen Produktionsbug gehabt wo findById null zurückgab
                statt eine Exception zu werfen. Schreib Tests die das absichern –
                fokus auf null-handling und nicht-existente IDs."
```

### 3. Beispiele geben (Few-Shot Prompting)

3–5 Beispiele im Prompt erhöhen die Treffsicherheit enorm. XML-Tags zur Strukturierung nutzen:

```xml
Formatiere Methoden-Namen immer so:

<example>
Input:  getUserById
Output: get_user_by_id
</example>

<example>
Input:  createOrderItem
Output: create_order_item
</example>
```

**Warum:** KI lernt aus Beispielen schneller als aus Beschreibungen. Zeigen schlägt erklären.

### 4. XML-Tags für Struktur

Bei langen Prompts mit mehreren Teilen: XML-Tags als Container nutzen.

```xml
<context>
  Java Spring Boot 3.2, PostgreSQL, Maven
  Bestehende UserService-Klasse: [CODE]
</context>

<task>
  Füge eine findByEmail()-Methode hinzu
</task>

<constraints>
  - Service-Interface Pattern
  - Optional<User> als Rückgabetyp
  - Keine neuen Dependencies
</constraints>
```

**Vorteil:** KI verarbeitet strukturierten Input zuverlässiger als Fließtext.

### 5. Lange Dokumente: Kontext zuerst, Frage am Ende

Bei Prompts mit viel Kontext (Log-Files, Code-Dateien, Dokumentation):

```
[Dokumentation / Code / Logs hier einfügen]

[Erst hier die eigentliche Frage stellen]
```

**Warum:** KI liest den gesamten Kontext bevor sie antwortet – die Frage am Ende wird mit vollem Wissen beantwortet.

### 6. Rolle zuweisen (Role Prompting)

```
"Du bist ein erfahrener Java-Architect mit Fokus auf Clean Architecture.
 Review diesen Code und identifiziere Architektur-Probleme."
```

Rollen helfen der KI den richtigen Ton, Detaillevel und Fokus zu wählen.

### 7. Schrittweise denken lassen (Chain of Thought)

Bei komplexen Analysen: KI explizit zum Nachdenken auffordern.

```
"Analysiere zuerst Schritt für Schritt, was dieser Code macht.
 Dann identifiziere potenzielle Bugs.
 Erst danach schlage Fixes vor."
```

Ohne diese Aufforderung springt KI direkt zur Antwort – oft falsch bei komplexen Fällen.

### 8. Was NICHT funktioniert

| Anti-Pattern | Warum problematisch | Besser |
|---|---|---|
| "Mach es besser" | Keine Erfolgskriterien | "Reduziere Methoden-Länge auf max. 20 Zeilen" |
| "Aktuelle Best Practices" | Temporale Referenz, instabil | "Java 17 Best Practices, Spring Boot 3.2" |
| Alles in einem Prompt | KI verliert Fokus | Aufteilen in separate Prompts |
| Blindes Vertrauen | KI halluziniert | Immer verifizieren |
| Fehler ohne Kontext | "Das geht nicht" | Vollständige Fehlermeldung einfügen |

---

## Weitere Prompting-Methoden

### Claude Code für Marktanalyse
- Link: https://www.instagram.com/p/DVMSDpKk0Qj/
- **Konzept:** Claude Code als Tool für systematische Marktanalysen nutzen

---

## Quick-Reference: Prompt-Checkliste

```
✅ Ein klares Ziel pro Prompt (KERNEL: N)
✅ Explizite Constraints definieren (KERNEL: E)
✅ Klare Erfolgskriterien (KERNEL: E)
✅ Logische Struktur: Context → Task → Constraints → Format (KERNEL: L)
✅ Keine temporalen Referenzen (KERNEL: R)
✅ Einfach halten (KERNEL: K)
✅ Bei kritischen Antworten: Chain-of-Verification nutzen
✅ Komplexe Aufgaben in Prompt-Ketten aufteilen
✅ Kontext und Motivation erklären (Warum, nicht nur Was)
✅ Beispiele geben wenn Formatierung/Stil wichtig ist
✅ XML-Tags bei langen strukturierten Prompts
✅ Lange Docs: Kontext oben, Frage am Ende
```

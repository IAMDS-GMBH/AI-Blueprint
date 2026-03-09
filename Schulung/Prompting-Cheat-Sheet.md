# Prompting Cheat-Sheet

> Zum Ausdrucken. Eine Seite. Alles was ihr braucht um gut zu prompten.

---

## Das KERNEL-Framework – Prompt-Struktur

```
K – Keep it simple      Ein Prompt = ein Ziel
E – Explicit constraints Was soll die KI NICHT tun?
R – Reproducible        Konkrete Versionen statt "aktuelle Best Practices"
N – Narrow scope        Code DANN Tests DANN Docs – nicht alles auf einmal
E – Easy to verify      Klare Erfolgskriterien definieren
L – Logical structure   Context → Task → Constraints → Format
```

**Template:**
```
Context:     [Was gibt es schon? Welcher Stack?]
Task:        [Was genau soll gemacht werden?]
Constraints: [Was darf NICHT passieren?]
Format:      [Wie soll das Ergebnis aussehen?]
```

---

## Profi-Techniken (macht den Unterschied)

**Warum erklären, nicht nur Was:**
```
Schlecht: "Schreib Tests für UserService"
Gut:      "Wir hatten Produktionsbug: findById gab null statt Exception zurück.
           Tests sollen das absichern – Fokus: null-handling, nicht-existente IDs"
```

**Beispiele geben (Few-Shot):**
```
Schlecht: "Schreib Methoden-Kommentare"
Gut:      "Schreib Kommentare in diesem Stil:
           <example>
           // Gibt Optional.empty() wenn User nicht gefunden, wirft nie null
           </example>"
```

**XML-Tags bei langen Prompts:**
```xml
<context>Stack + relevanter Code hier</context>
<task>Was soll gemacht werden</task>
<constraints>Was nicht passieren darf</constraints>
```

**Lange Logs/Docs: Kontext zuerst, Frage am Ende:**
```
[Fehler-Log / Code einfügen]
↓
"Was ist die Ursache dieses Fehlers?"  ← Frage NACH dem Kontext
```

**Schrittweise denken lassen:**
```
"Analysiere erst was der Code macht.
 Dann identifiziere Bugs.
 Erst danach: Fixes vorschlagen."
```

---

## Fehler korrigieren – Copy-Paste Prompts

**Compile-Fehler:**
```
"Ich bekomme diesen Fehler: [FEHLERMELDUNG]
 Analysiere die Ursache und behebe sie."
```

**Logik-Fehler:**
```
"Das Ergebnis ist falsch. Erwartet: [X]. Tatsächlich: [Y].
 Analysiere Schritt für Schritt warum das passiert."
```

**KI dreht sich im Kreis:**
```
Option 1: Neuen Chat starten
Option 2: "Dieser Teil ist korrekt: [CODE]. Baue [X] dazu."
```

**KI halluziniert (erfindet APIs):**
```
"Die Methode [X] existiert nicht in [Library].
 Welche tatsächlich existierende Alternative gibt es?"
```

---

## Chain-of-Verification (für kritische Antworten)

```
[Deine Frage]

Now follow these steps:
1. Gib deine initiale Antwort
2. Erstelle 3-5 Verifikationsfragen die Fehler aufdecken könnten
3. Beantworte jede Frage separat
4. Gib deine korrigierte finale Antwort
```

---

## Anti-Patterns – Was NICHT funktioniert

| ❌ Schlecht | ✅ Besser |
|---|---|
| "Mach es besser" | "Reduziere auf max. 20 Zeilen" |
| "Aktuelle Best Practices" | "Java 17, Spring Boot 3.2" |
| Code + Tests + Docs in einem | Separate Prompts |
| "Das geht nicht" | Vollständige Fehlermeldung einfügen |
| KI-Output blind vertrauen | Tests laufen, Build checken |

---

## Merksatz

> **Präzision in → Qualität raus.**
> KI ist kein Gedankenleser – sie ist ein sehr schneller Umsetzer.
> Je klarer deine Anfrage, desto besser dein Ergebnis.

---

*Mehr Details → `KI-Wissensbasis/04-Prompt-Engineering.md`*

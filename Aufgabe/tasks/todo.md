# Plan: setup.sh Vereinfachung + /configure als Cross-Tool Intelligenz-Layer
Datum: 2026-03-13
Status: ABGESCHLOSSEN

## Ziel

Klare Trennung: setup.sh = dummer Dateikopierer, /configure = intelligente Anpassung.
/configure funktioniert mit JEDEM Tool (Claude, Copilot, Vibe) und passt nur die vorhandenen Dateien an.
techstack.conf komplett entfernt.

## Neuer Workflow

```
bash setup.sh claude /pfad/zu/mein-projekt   # Dateien kopieren
cd mein-projekt
/configure                                    # Stack erkennen + anpassen
```

## Ergebnis

### Phase 1: setup.sh vereinfacht ✅
- [x] 1.1: Zielpfad als letztes Argument (erkennt `/`, `./`, `../`, `~`)
- [x] 1.2: Entfernt: --from-conf, parse_techstack_conf(), map_config_to_stacks(), copy_stack_rules(), replace_placeholders(), sed_inplace()
- [x] 1.3: Stacks kopiert nach .claude/rules/stacks/ und .github/instructions/stacks/
- [x] 1.4: --update Modus bleibt
- [x] 1.5: setup.sh kopiert Copilot configure.prompt.md mit
- [x] 1.6: NAECHSTE SCHRITTE zeigt /configure Aufruf pro Tool

### Phase 2: techstack.conf entfernt ✅
- [x] 2.1: techstack.conf geloescht
- [x] 2.2: Alle Referenzen entfernt (README, CLAUDE.md, configure.md, example Rules)
- Verifikation: `grep techstack` → 0 Treffer im gesamten Template

### Phase 3: /configure cross-tool ✅
- [x] 3.1: Tool-Erkennung (.claude/, .github/, .vibe/)
- [x] 3.2: Stack-Erkennung (pom.xml, package.json, angular.json, etc.)
- [x] 3.3: Pro Tool passende Dateien anpassen
- [x] 3.4: Drei Modi (bestehendes Repo / leeres Repo / Feintuning)
- [x] 3.5: Zusammenfassung am Ende

### Phase 4: /configure fuer JEDES Tool ✅
- [x] 4.1: Claude: .claude/commands/configure.md (aktualisiert)
- [x] 4.2: Copilot: .github/copilot-prompts/configure.prompt.md (NEU)
- [x] 4.3: Vibe: .vibe/agents/configure.toml (NEU)
- [x] 4.4: Alle drei synchron (gleiche Logik, tool-spezifisches Format)

### Phase 5: README.md ✅
- [x] 5.1: Quickstart vereinfacht (clone + setup.sh mit Pfad)
- [x] 5.2: techstack.conf komplett raus
- [x] 5.3: Neuer Abschnitt "/configure — Was passiert?"
- [x] 5.4: Entscheidungsbaum (neues/bestehendes Projekt)
- [x] 5.5: Feature-Paritaet + Slash Commands aktualisiert

### Phase 6: Verifikation ✅
- [x] 6.1: techstack.conf geloescht
- [x] 6.2: setup.sh ohne techstack-Referenzen
- [x] 6.3: stacks/ wird nach .claude/rules/stacks/ und .github/instructions/stacks/ kopiert
- [x] 6.4: copilot-prompts/configure.prompt.md existiert
- [x] 6.5: .vibe/agents/configure.toml existiert
- [x] 6.6: Null Referenzen auf techstack.conf im gesamten Template

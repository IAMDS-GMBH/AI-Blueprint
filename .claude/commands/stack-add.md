Fuege einen neuen Tech-Stack zum dev-setup-template hinzu:

1. Frage nach dem Stack-Namen und der Technologie (z.B. "python-fastapi")
2. Lies bestehende Stacks in dev-setup-template/stacks/ als Vorlage
3. Erstelle:
   - stacks/<stack-name>/rules.md — Coding-Konventionen, Best Practices
   - stacks/<stack-name>/snippets.md — Code-Templates, Patterns
4. Erweitere techstack.conf um den neuen Stack-Eintrag
5. Pruefe ob setup.sh den neuen Stack korrekt kopieren wuerde
6. Aktualisiere README.md falls noetig

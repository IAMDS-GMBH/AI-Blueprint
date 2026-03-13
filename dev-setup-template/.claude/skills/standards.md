---
description: "Git-Workflow, Branching, PR-Conventions, Commit-Messages"
---

# Projekt-Standards

## Git-Workflow
- Feature Branches: `feature/TICKET-kurzbeschreibung`
- Bug Fixes: `fix/TICKET-kurzbeschreibung`
- Hotfixes: `hotfix/TICKET-kurzbeschreibung`
- Main/Master ist geschuetzt — nur via PR

## Commit-Messages
```
<type>(<scope>): <beschreibung>

feat(orders): Add pagination to order list
fix(auth): Handle expired JWT tokens gracefully
refactor(api): Extract validation middleware
test(orders): Add edge case tests for empty cart
docs(readme): Update setup instructions
```
- Types: feat, fix, refactor, test, docs, chore, ci
- Scope: optional, Modul oder Feature-Name
- Beschreibung: Imperativ, max 72 Zeichen

## Pull Requests
- Titel: wie Commit-Message (`feat(orders): ...`)
- Body: Was + Warum + Test-Plan
- Mindestens 1 Reviewer
- CI muss gruen sein vor Merge
- Squash Merge bevorzugt (saubere History)

## Branching
- main = produktionsbereit (immer deploybar)
- develop = Integration (optional, je nach Team)
- Feature Branches kurzlebig (<1 Woche)
- Rebase vor PR (saubere History)

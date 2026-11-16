# Engineering agent instructions — Java application repository

## Ownership and coordination
- This repository (`frankbuttigieg1993-gmail/java_application`) owns Java source, Gradle build/test configuration, and caller workflows.
- `frankbuttigieg1993-gmail/cicd` owns shared reusable workflows and composite actions.
- Read the applicable instructions in both repositories. For cross-repository CI/CD work, follow the mandatory PR lifecycle in `cicd/AGENTS.md` and the `github-actions-development` Skill when available. Do not assume instructions from another repository are automatically loaded.

## Repository-specific change boundaries
- Inspect existing code, tests, and workflows before editing. Make only task-relevant changes.
- **Never modify, rename, overwrite, or delete an existing `.github/workflows/*` file without the user's explicit approval naming the file and intended change.** If needed and not approved, pause ALL execution and request approval. A general implementation request is insufficient.
- Do not alter unrelated application behaviour, Gradle configuration, tests, security scans, secrets, repository settings, or branch protection.
- Never weaken or disable tests to make CI pass. Do not push to `main` or merge PRs.

## Application validation
- Use the repository's Gradle wrapper and established conventions.
- Validate the caller workflow, its triggers, permissions, inputs, and cross-repository `cicd` reference.
- Confirm the requested feature runs in the actual PR pipeline after the required build stage; preserve existing pipelines unless explicitly approved.
- Record tests, workflow run URLs, conclusions, latest head commit SHA, and unresolved limitations.

## PR lifecycle
- Create a task-specific branch from `main` and a **Draft PR** targeting `main` for meaningful authorised changes.
- Cross-link the corresponding `cicd` Draft PR. Keep both Draft while either repository has incomplete or unsuccessful required validation.
- Only after required checks pass on the latest relevant commits in **both** repositories may the agent mark both PRs **Ready for Review** and request review from `frankbuttigieg-93`.
- A review request is not an approval. If reviewer assignment fails, report the limitation. Never approve or merge PRs.
- If no meaningful application change is needed, do not fabricate a change to create a PR; request direction on the two-PR requirement.
- Report PR links, head SHAs, run evidence, cross-repo dependencies, and accurate status (IN DEVELOPMENT, BLOCKED, or READY FOR HUMAN REVIEW).

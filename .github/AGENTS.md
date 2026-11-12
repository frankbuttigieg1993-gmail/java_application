# Agent instructions - java_application repository

This repository (`frankbuttigieg1993-gmail/java_application`) contains the Java/Gradle application consuming reusable workflows from `frankbuttigieg1993-gmail/cicd`. `main` is the integration branch.

## Change boundaries
- Create a feature branch from `main` and a PR targeting `main`; never merge.
- For the AI-generated lab, only create or modify `.github/workflows/ai-generated.yml` and explicitly authorised supporting tests/docs.
- Treat existing `.github/workflows/pr.yml`, `.github/workflows/build.yml`, application source, Gradle files and security workflows as read-only unless explicitly authorised.
- Before editing, list intended paths. If the task requires other changes, request approval; do not silently alter shared pipelines or the application to make a test pass.

## Workflow integration
- The new caller workflow must reference `cicd/.github/workflows/ai-generated.yml` at the authorised `cicd` feature branch or immutable ref.
- Configure `workflow_dispatch` for the new caller workflow and any appropriate PR validation trigger explicitly requested by the task.
- Check `workflow_call` inputs, repository access, checkout semantics, and permissions. Do not expose secrets or introduce unnecessary write permissions.
- Record that a feature-branch workflow reference is temporary and must be updated to an approved immutable ref before production adoption.

## Validation and PRs
- Verify that the executed run actually uses the newly created workflow and composite action; record run URL, run ID, job results and test evidence.
- Investigate failures only within authorised files. Report blockers instead of modifying protected files or bypassing checks.
- Do not claim success from a YAML parse alone; do not claim a manual dispatch occurred unless it actually ran.
- Create a PR with validation results, risks, and a link to the companion `cicd` PR. Never merge.

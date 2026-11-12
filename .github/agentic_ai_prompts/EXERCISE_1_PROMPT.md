# Exercise 1 - AI-delegated GitHub Actions implementation

Implement a new, isolated cross-repository GitHub Actions lab. Work independently within the authorised scope, troubleshoot failures and create two PRs for my review. **Do not merge.**

## Repositories and branches
- `frankbuttigieg1993-gmail/cicd`: create `feature/ai-generated-lab` from `main`.
- `frankbuttigieg1993-gmail/java_application`: create `feature/ai-generated-lab` from `main`.
- Read applicable `AGENTS.md` files and the `github-actions-development` Skill before changing files.

## Strict file allowlist
In `cicd`, create ONLY:
- `.github/workflows/ai-generated.yml`
- `.github/actions/ai-generated/action.yml`

In `java_application`, create ONLY:
- `.github/workflows/ai-generated.yml`

Treat all other files as read-only. **Do not modify** existing `java-build-pr.yml`, `java-build-all.yml`, `pr.yml`, `build.yml`, any existing composite actions, source code, Gradle configuration, repository rules or security controls. If additional changes are essential, stop and request approval rather than proceeding.

## Required functionality
1. Create a reusable `cicd` workflow named `ai-generated.yml` that supports `workflow_call`.
2. It must have four sequential jobs named `Bootstrap`, `Build`, `AI_Generated`, and `Unit_Tests` (use valid GitHub Actions job IDs and readable display names). Each job must depend on the preceding job.
3. Use existing actions/workflows **as read-only references** for the bootstrap, Gradle build and unit-test patterns. Do not copy unnecessary complexity or alter those originals.
4. Create `.github/actions/ai-generated/action.yml` as a simple composite action that lists files and directories in the current workspace. Keep inputs/outputs to an absolute minimum, and use an explicit shell for any `run` step.
5. Ensure the reusable workflow correctly references the composite action from `cicd`, even though the consuming application's source is checked out. Do not assume a local relative action path resolves to `cicd`.
6. Create the `java_application` caller workflow with `workflow_dispatch`, referencing the `cicd` feature branch for this lab. Keep GitHub token permissions minimal.

## Validation
- Validate YAML, workflow-call references, job dependencies, action structure and repository permissions.
- Push both branches. Attempt a manual run of the Java application workflow and verify the **new** action actually ran and the build and unit tests succeeded.
- If GitHub prevents dispatch of a new workflow that does not yet exist on the default branch, report this accurately and use an already-authorised PR-triggered validation path if possible. Do not modify `main`, an existing workflow or repository settings to bypass this constraint.
- If a required check fails, inspect logs and correct only allowlisted files; rerun as appropriate. Do not claim success for checks that were not executed.

## Deliverables
- A PR against `main` in each repository; cross-link the PRs.
- Run URL and ID, per-job conclusions, evidence of the new composite action's execution, and any failures/limitations.
- A short final report: implementation, files changed, validation, security implications, PR URLs, outstanding issues, and status (COMPLETE / PARTIALLY COMPLETE / BLOCKED / FAILED).
- Do not report COMPLETE unless both PRs exist and the end-to-end validation actually succeeded.

Before implementation, summarise the allowed files and protected files. Then proceed without asking for approval for routine decisions within that scope.

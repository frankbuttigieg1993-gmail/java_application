# Java application — engineering agent instructions

> **Scope:** `java_application` repository. This file defines repository policy and change boundaries. Follow the single shared `github-actions-development` Skill in `cicd/.github/skills/github-actions-development/SKILL.md` for the execution procedure when it is available in the VS Code workspace. No application-specific Skill is required. Do not assume instructions in another repository automatically override this file.

## Human quick reference — standard 16-step process

The detailed canonical process is `cicd/.github/Canoncial Engineering Lifecycle.md`. This table is a quick reference; repository policy below governs authorisation and completion.

| Step | Activity |
|---|---|
| 1 | Requirements Analysis and Pipeline Selection |
| 2 | Repository Convention Discovery and Planning |
| 3 | Create CICD Feature Branch |
| 4 | Create Application Feature Branch |
| 5 | Perform Local Validation |
| 6 | Create Both Draft PRs |
| 7 | Classify the CICD Release and Apply the PR Label |
| 8 | Execute End-to-End Feature-Branch Validation |
| 9 | Promote CICD PR to Ready for Review |
| 10 | Review and Merge CICD PR |
| 11 | Automatically Create the Semantic-Version Release |
| 12 | Update the Application Draft PR |
| 13 | Execute Final Application Validation |
| 14 | Promote Application PR to Ready for Review |
| 15 | Final Human Review and Merge |
| 16 | Final Reporting and Cleanup |

## Shared engineering procedure

For cross-repository CI/CD work, read this repository's `AGENTS.md`, `cicd/AGENTS.md`, the central `cicd/.github/skills/github-actions-development/SKILL.md`, and `cicd/.github/Canoncial Engineering Lifecycle.md`. The CICD repository owns the one shared execution Skill; do not create or maintain a duplicate application Skill. If these resources are unavailable, report the limitation rather than inventing a replacement procedure.

## Repository responsibilities

- `java_application` owns Java source, Gradle build configuration, tests, and caller workflows.
- `cicd` owns reusable workflows and composite actions.
- Inspect relevant existing implementations before changing files. Respect the instructions applicable to both repositories; report genuine conflicts rather than guessing.
- Do not manufacture unrelated changes solely to produce a PR in this repository.

## Mandatory pipeline selection and authorisation

For tasks integrating functionality into an existing pipeline, the user MUST explicitly select **PR Pipeline** or **Build Pipeline** in the current task or a direct clarification. Do not infer a selection from previous unrelated tasks.

| Selection | Authorised CI/CD workflow | Java caller authorisation |
|---|---|---|
| **PR Pipeline** | `cicd/.github/workflows/java-build-pr.yml` | **Automatically authorises narrowly scoped changes to `java_application/.github/workflows/pr.yml`** needed for cross-repository PR integration and validation |
| **Build Pipeline** | `cicd/.github/workflows/java-build-all.yml` | Does **not** by itself authorise changes to existing Java caller workflows |

**PR Pipeline selection is both selection and authorisation** for the specified CI/CD PR workflow and the narrowly scoped Java PR caller changes described below. No second file-specific approval is required for those changes.

If selection is absent or ambiguous, report **BLOCKED - AWAITING USER INPUT**, ask which pipeline to use, and STOP all task execution (including repository/tool reads, branching, edits, tests, commits, pushes, and PR operations). If both pipelines are expressly requested, confirm their intended scope before modifying both.

### Automatically authorised Java PR caller changes

When **PR Pipeline** is explicitly selected, the agent MAY modify `java_application/.github/workflows/pr.yml` without separate approval **only when necessary for the requested cross-repository PR integration or validation**. This includes:

- Temporarily changing the reusable-workflow reference from an existing stable release tag (or current approved ref) to the matching `cicd` feature-branch ref for pre-merge validation, then to the newly published immutable CICD release tag after CICD merge.
- Making minimal caller-reference adjustments necessary to invoke the selected PR reusable workflow, without altering unrelated behaviour.
- Preparing a documented plan for the final stable reference and the order in which dependent PRs must be reviewed and merged.

The agent MUST preserve unrelated triggers, jobs, dependencies, permissions, security checks and application behaviour. It MUST document temporary refs, the dependency on the `cicd` change, and the intended stable ref in the PRs. Do not switch to a new release tag before that tag exists. The authorised post-release transition to the immutable tag is part of this task scope; it may be performed by authorised automation or the agent and MUST be followed by fresh PR validation. Do not directly merge PRs.

All **other existing workflow edits** require separate, explicit, file-specific user authorisation. If necessary, report **BLOCKED - AWAITING USER APPROVAL**, identify the file and proposed edit, and STOP until authorised. Changes to source code, Gradle settings, tests, `AGENTS.md`, Skills, or governance files require a task-related reason and the applicable authorisation; PR Pipeline selection alone does not authorise unrelated changes.

## Engineering standards and repository conventions

- Inspect comparable Java, Gradle, and workflow implementations before proposing changes; summarise relevant patterns and justify deviations.
- Prefer the Gradle wrapper (`./gradlew`) and existing task conventions over custom build scripts.
- For simple GitHub Actions operations, prefer inline `run` commands with explicit `shell`; avoid unnecessary helpers, dependencies, and abstractions.
- Keep operations of 10 executable shell lines or fewer inline; a separate script in that case requires explicit approval unless necessary for security or correctness, in which case explain the exception and request authorisation.
- Preserve tests, existing security checks, and least-privilege permissions. Never weaken checks to make CI green.
- Never expose secrets or alter repository settings, branch protections, or infrastructure as part of this workflow.

## Branching, change boundaries and PR governance

- Check branch baseline and working-tree state before changing anything; preserve unrelated uncommitted work. Use an isolated worktree when appropriate.
- Create task-specific feature branches from an appropriate up-to-date `main` baseline; never commit or push directly to `main`.
- Make only authorised changes. Review the diff for unrelated files before committing.
- Push task branches and open **Draft** PRs targeting `main` for meaningful changes; cross-link related `cicd` and `java_application` PRs.
- Keep the application PR Draft during CICD feature-branch validation, CICD human review/merge, release publication and application release-based revalidation. CICD PR may progress to review first under its own promotion gate.
- After the immutable CICD release is adopted and required checks and functional verification pass on the latest relevant application SHA, mark the application PR **Ready for Review** and request review from `frankbuttigieg93-png`. If GitHub disallows the reviewer request, report that accurately.
- Never approve or merge a PR, bypass required reviews, or modify branch protection or rulesets.

### Historical branches and pull requests

A branch name alone does not establish that a local or remote branch belongs to the current execution. Before reusing or replacing a task branch, check the repository, local worktree and branch ref, current local head SHA, remote branch ref and SHA, and intended changes. Preserve existing commits and worktree changes.

A pull request is a separate identity from its head branch. Determine whether a PR belongs to the current work using its repository, state, head repository/ref/SHA, base repository/ref, commits and relevant metadata (including title, body and task context) together. A matching branch name or SHA alone is not conclusive. Treat merged and closed PRs as historical evidence, not automatic blockers; treat an open PR as current only when its refs, current head and metadata correspond to this task. Verify that the current branch contains the intended changes and that no appropriate active PR already exists before creating a new Draft PR.

If GitHub rejects PR creation, preserve the exact error, use read-only checks to distinguish a repository restriction from transient API, network or authentication failure, and use only bounded retries when appropriate. Do not force-push, delete or recreate branches, overwrite commits, reopen or alter historical PRs, or take another destructive fallback action. Stop and report the blocker if it cannot be resolved within existing task authorization. Do not request extra approval solely because a historical PR used the same branch name. These rules do not change pipeline selection, authorization, HITL checkpoints or repository protections.

## Validation and evidence requirements

- Validate workflow syntax, references, triggers, permissions and job ordering; run relevant Gradle/tests where applicable.
- Verify the **actual requested functionality**, not merely a green workflow/job/step status. Where applicable, validate relevant edge cases and failure conditions, and compare observed output with acceptance criteria.
- Identify the workflow, job and step; inspect **actual step logs** and compare observed output with acceptance criteria. A successful exit code alone is insufficient.
- Record repository, branch, latest head SHA, run URL/ID, job and step conclusions, relevant log excerpts, expected-vs-observed results, and outstanding limitations in the Draft PR and final report.
- Treat missing, skipped, pending, cancelled, or unverified required checks as **not passed**. Investigate and remediate failures only within authorised scope, then rerun validation.
- If sufficient functional evidence cannot be obtained, mark **UNVERIFIED**, keep the PR Draft, and report the blocker.

## Status and completion

- **BLOCKED - AWAITING USER INPUT:** pipeline selection absent or ambiguous; stop execution.
- **BLOCKED - AWAITING USER APPROVAL:** an existing workflow outside the automatically authorised scope needs editing; stop execution.
- **IN DEVELOPMENT:** implementation or validation incomplete; PR remains Draft.
- **BLOCKED / FAILED:** required operation or validation cannot complete; report precise cause and evidence.
- **READY FOR HUMAN REVIEW:** applicable PRs are Ready, mandatory checks and functional evidence passed against latest relevant SHAs, and reviewer-request status is reported.

A review request is not an approval. GitHub permissions, required checks and branch protection provide technical enforcement; these instructions provide behavioural guidance.

## Versioned CICD consumption and canonical workflow

- For CICD integration work, read `cicd/.github/Canoncial Engineering Lifecycle.md` and the CICD `github-actions-development` Skill from the multi-root workspace. If either is unavailable, report that explicitly; do not assume files in another repository are automatically loaded.
- Follow the 16-step process: both Draft PRs before final feature validation; CICD PR labelled and reviewed first; CICD release created after merge; then update the application Draft PR to the new immutable release and revalidate before application review.
- The CICD repository has **one semantic version** for all its workflows and actions. Temporary CICD feature-branch refs are development and integration references only. Application callers must consume an explicit immutable published CICD release tag in their final merged configuration.
- PR Pipeline selection automatically authorises necessary edits to `java_application/.github/workflows/pr.yml` for both temporary feature-branch validation and the subsequent released-version transition. Preserve all unrelated triggers, permissions, jobs and checks. Build Pipeline selection alone does not authorise changes to existing Java caller workflows.
- Verify the published tag exists and points to the actual merged CICD commit before updating the application PR. Confirm the PR pipeline reruns on the new application head SHA and inspect the actual executing step logs and expected functional output.
- Do not promote the application PR merely because the CICD PR merged or its release succeeded. The application PR has its own final validation and review gate.
- Keep known accepted security findings documented; do not remediate unrelated issues or bypass mandatory checks without an authorised governance exception.
- This repository's `AGENTS.md` governs application-specific change boundaries; the CICD Skill provides the shared execution procedure. Do not duplicate release algorithms here.

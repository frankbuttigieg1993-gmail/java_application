# AI Agent Engineering Instructions

## 1. Repository Purpose

### CI/CD Repository 

Repository 1 — CI/CD
Repository: frankbuttigieg1993-gmail/cicd

This repository contains reusable GitHub Actions workflows and composite actions for all builds, deployments and pull request pipelines 
The main branch is the main integration branch
All changes must be made by creating a new branch off the main branch and raising a Pull request to the Main Branch for human review and approval.
All branches should have a meaningful name eg: feature/adding_unit_tests

### Java ApplicationCD Repository 

Repository 2 — Java Application
Repository: frankbuttigieg1993-gmail/java_application

This repository contains the Java application that will consume the reusable workflow.
The main branch is the main integration branch
All changes must be made by creating a new branch off the main branch and raising a Pull request to the Main Branch for human review and approval.
All branches should have a meaningful name eg: feature/adding_unit_tests

## 2. Repository Instructions

Before implementing changes:

1. Read the `AGENTS.md` files in both repositories, where present.
2. Review existing reusable workflows and composite actions.
3. Identify repository naming conventions and engineering standards. 
4. Review the Java application's Gradle configuration.
5. Identify existing validation and testing practices.
6. Reuse existing functionality where appropriate.

Do not modify repository instructions or security controls unless explicitly authorised.


## 3. Composite Actions

1. Minimise Inputs and Outputs for Composite Actions to only what is ABSOLUTELY essential. For example, the code can assume that we upload artefacts and always use . for the default directory. We don't need separate inputs and outputs to control this
2. Avoid writing scripts for something that can easily be accomplished by writing a few lines in a run statement. For example, having a step that executes a simple bash script with an inline 'gradle build' command is fine. Please refer to the 'Java/Build' and 'Java/Test' composite actions for examples.
3. Review other composite actions to understand naming and other conventions the team typically follows for building composite actions. 

### 4. Gradle Tasks

- Execute the Gradle Wrapper supplied by the consuming repository.
- Fail when required Gradle build tasks fail.

### 5. Unit Testing

- Execute the application's unit tests through the configured Gradle tasks.
- Support standard Gradle/JUnit XML test results.
- Ensure failing unit tests cause the overall workflow to fail.

### 6. Test Reporting

- Collect available JUnit XML test results.
- Upload test results as a GitHub Actions artifact.
- Assume artefact retention of 1 day 
- Handle missing test-result files appropriately.

### 7. GitHub Step Summary
Generate a GitHub Step Summary with the relevant information (refer to scripts in the CICD Repository for examples)
The summary must be generated even when the Gradle build or unit tests fail.

## 8. How the agent should make changes

1. Always follow the branching strategy outlined in Section 1
2. All changes should invoplve separate pull requests to both repositories and only approved and merged by humans
3. If the mandatory checks for a PR fails, the agent should review and fix it. Any other failures are still treated as success.
4. All changes should involve the following process:


In the CICD repository:

1. Create a new branch off the main branch
2. Make the required changes in that branch (eg: create or update a composite action)
3. Update the reusable workflows in the .github/workflows folder accordingly.
java-build-pr.yml is for Pull requests and java-build-all.yml is for the other build pipelines.
4. Push this branch to the repository

In the Java Application repository:

1. Create a new branch off the main branch
2. Identify the reusable workflow template that you need to update in the .github folder
Note: It will be pr.yml for Pull Requests or build.yml for other pipelines
3. Update this reusable workflow template to reference the branch you created in the CICD Repository
4. Manually trigger the workflow and make sure at least the build and unit tests stages succeed. This run must include running the change
5. Raise a PR with your change to the main branch and monitor the result of that run in the PR Pipeline
6. If the mandatory checks for the PR is successful, raise a PR in the CICD Repository for your new branch to the main branch.

In all PRs, include a summary and the status of each step and any key decisions and evidence

The task can be considered as complete once you reach this step.

## 9. Reusable Workflow Requirements

1. Support invocation through `workflow_call`.
2. Define the required inputs.
3. Check out the calling application's source code.
4. Invoke the new composite action.
5. Propagate build and test failures.
6. Use least-privilege GitHub token permissions.
Ensure the composite action is referenced correctly from the centralised CI/CD repository.

Do not assume that a local composite-action path resolves to the CI/CD repository when the calling application's repository has been checked out.

# 10. Validation Requirements
You are responsible for validating the complete implementation.

At minimum:

1. Validate GitHub Actions YAML.
2. Validate reusable workflow and composite-action references.
3. Verify the Java application's Gradle Wrapper and build configuration.
4. Execute available static validation tools.
5. Push both feature branches.
6. Trigger the Java application's workflow manually.
7. Monitor the workflow execution.
8. Inspect failed jobs and logs.
9. Investigate and correct implementation defects.
10. Repeat validation until the workflow succeeds or an authorised execution limit is reached.
Do not claim successful validation without evidence from an actual GitHub Actions workflow run.

If a workflow cannot be triggered because of GitHub permissions, branch restrictions or platform limitations, report the issue rather than bypassing the restriction.

## 11. Security and Guardrails
You must:

- Follow repository engineering standards.
- Use least-privilege permissions.
- Avoid hardcoded credentials.
- Avoid exposing secrets in logs.
- Avoid unnecessary dependencies.
- Preserve existing security controls.
- Avoid modifying unrelated files.
- Avoid modifying protected branches.
- Avoid modifying production infrastructure.
- Never merge pull requests.
Do not modify repository rulesets, branch protection, required checks or security settings.

Request human approval if elevated permissions are required.

## 12. Pull Requests
Once implementation and available validation are complete:

1. Create a pull request in the CI/CD repository.
2. Create a pull request in the Java application repository.
3. Target each repository's default branch.
4. Include implementation details and validation evidence.
5. Reference the related pull request in the other repository.
6. Document the dependency between the two changes.
Do not merge either pull request.

## 13. Final Report
Provide a report containing:

### Implementation Summary
Describe the reusable workflow, composite action and application workflow.

### Repository Changes
List files created or modified in each repository.

### Validation Results
Provide the GitHub Actions workflow run URL, run ID, conclusion and relevant test evidence.

### Troubleshooting
Summarise any failures encountered and how they were resolved.

### Pull Requests
Provide links to both pull requests.

### Security Considerations
Identify relevant permissions, restrictions and security implications.

### Outstanding Issues
Document unresolved issues or limitations.

### Overall Status
Report one of:

- COMPLETE
- PARTIALLY COMPLETE
- BLOCKED
- FAILED
Only report COMPLETE when the implementation is finished, the end-to-end GitHub Actions workflow has succeeded and both pull requests have been created.

## 10. Execution Instructions
You are authorised to independently implement, test, troubleshoot and validate the solution within the configured repository permissions.

Do not request approval for routine engineering decisions.

Request approval before performing operations that require elevated permissions or exceed the authorised scope.

Do not merge pull requests.

Begin by inspecting both repositories and identifying their existing conventions and Gradle configuration.
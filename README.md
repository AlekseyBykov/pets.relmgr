# RelMgr — Release Management Tool

**RelMgr** is a lightweight desktop release management tool designed to automate repetitive tasks around patch preparation, 
release communication, and internal delivery workflows.

## Features
**1. Patch Release Analysis**
* Analyze Git tags to determine the next patch version
* Automatically detect previous fixes
* Generate consistent patch version names

**2. Patch ZIP Generation**
* Create patch archives based on prototype ZIPs
* Update version metadata inside archives (version.txt, *.version)
* Support both release patches and task-based patches

**3. Patch-by-Task Workflow**
* Generate patch versions based on task identifiers
* Produce task-oriented patch ZIPs
* Generate tester-facing messages automatically

**4. Release Messages & Letters**
* Generate plain-text patch messages
* Generate styled HTML release letters using FreeMarker templates
* Live preview of rendered HTML inside the application
* Optional email delivery via SMTP

**5. Nexus Upload (optional)**
* Upload generated patch archives to a binary repository
* Detailed response logging

## User Interface

RelMgr is a **JavaFX desktop application** built with an MVVM approach.

Main sections:
- **Final Patches** — release letters and email delivery
- **Release Patches** — analysis, ZIP generation, messages, repository upload
- **Patch by Task** — task-driven patch generation and messaging

## Design Principles
* Clear separation between UI and core logic
* No UI dependencies inside the core module
* Infrastructure isolated behind simple adapters
* Configuration via environment variables or user home directory

## Configuration
Credentials and configuration values are not stored in the repository.

User-specific configuration lives in:
```bash
~/.relmgr/config.properties
```
Example:
```bash
jira.user=...
jira.token=...
jira.baseUrl=...
```

## Build & Run
Build:
```shell
./gradlew build
```
Run:
```shell
./gradlew run
```

## Project Status

This repository represents a prototype / reference tool.

It is:
- not a full CI/CD replacement
- not a pipeline orchestrator
- intentionally lightweight and pragmatic

The goal is to demonstrate how release workflows can be automated with minimal complexity.

## **Tech stack**

- Java 21
- JavaFX 21 (controls, fxml, web)
- Gradle (Kotlin DSL)
- FreeMarker
- JGit
- Jakarta Mail
- MVVM architecture

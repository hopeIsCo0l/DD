# Dube (DD)

Dube is an Android customer-credit notebook for shopkeepers. The planned product records customers, credit, and repayments locally, then synchronizes shared shop records with a backend.

**This is a development foundation, not a working financial ledger.** Do not use it to record real debts. The Android Studio project and current launcher label are still `DD`; the intended product name is Dube.

## Current status

Implemented in the working source:

- Kotlin, Jetpack Compose, and standard Material 3 components with light/dark themes.
- Home, Customers, and Settings starter destinations with toolbar and system Back navigation.
- Shared text fields, primary actions, errors, empty states, loading, and save-status presentation.
- Android string resources for user-facing text.
- A Customers ViewModel demonstration with Refresh, loading/error state, and injectable repository contracts. Starter repositories return empty data; they do not persist records.

The architecture work is being developed separately from the initial UI foundation. Check the branch and PR you are using; a checkout of `main` may not contain unmerged work.

Not implemented yet: customer registration/editing, credit and repayment workflows, exact-money business rules, Room persistence, a durable outbox, authentication, shop permissions, backend synchronization, backups, and release deployment. A displayed pending count or save-status component does not establish that financial records were saved or accepted by a server.

Track scope and acceptance criteria in the [GitHub issues](https://github.com/hopeIsCo0l/DD/issues).

## Development prerequisites

Use Android Studio with support for the Android Gradle Plugin configured in this repository. Install Git and the Android SDK through Android Studio's SDK Manager.

| Setting | Current configuration | Source |
|---|---|---|
| Android Gradle Plugin | 9.5.1 | [Version catalog](gradle/libs.versions.toml) |
| Gradle | 9.6.0, via committed wrapper | [Wrapper properties](gradle/wrapper/gradle-wrapper.properties) |
| Gradle daemon JDK | 25 | [Daemon JVM criteria](gradle/gradle-daemon-jvm.properties) |
| Java source/target compatibility | 11 | [App build configuration](app/build.gradle.kts) |
| Compile / target SDK | API 37 / API 37 | [App build configuration](app/build.gradle.kts) |
| Minimum device SDK | API 26 (Android 8.0) | [App build configuration](app/build.gradle.kts) |
| Namespace / application ID | `com.sook.dd` | [App build configuration](app/build.gradle.kts) |

Java source compatibility 11 is separate from the JDK used to run Gradle. For terminal builds, install JDK 25 and set `JAVA_HOME` to its installation directory, with its `bin` directory on `PATH`. The committed daemon criteria select JDK 25 and include provisioning URLs; the wrapper still needs a Java installation to start.

Install Android SDK Platform 37, Platform-Tools, and the SDK Build-Tools requested by Gradle. For emulator testing, also install the Android Emulator and a suitable system image. The first sync/build needs internet access to download Gradle, dependencies, and any missing toolchain components.

API 26 is the current configured minimum, not completed device-compatibility certification. Confirm the application ID before configuring cloud services.

## Open and run

1. Clone the repository:

   ```sh
   git clone https://github.com/hopeIsCo0l/DD.git
   cd DD
   ```

2. Open the repository root in Android Studio, not just the `app` directory.
3. Configure the local Android SDK location and allow Gradle sync to finish. Android Studio normally creates `local.properties` containing your machine's `sdk.dir`. Keep that file local.
4. Use the project's Gradle JVM criteria (JDK 25); resolve any missing SDK components reported by sync.
5. Select the `app` run configuration and an emulator or USB-debugging-enabled phone running API 26 or newer.
6. Run the app. It should open Home without requiring authentication or a network connection.

Open Customers and Settings, then return with Back. On the architecture branch, Customers also offers Refresh and an empty summary. Firebase credentials, billing, and cloud setup are not needed for the starter app.

## Build and checks

Run from the repository root using the committed wrapper; do not substitute a globally installed Gradle.

```sh
./gradlew --version
./gradlew :app:assembleDebug
./gradlew :app:testDebugUnitTest
./gradlew :app:lintDebug
```

On Windows PowerShell, use `./gradlew.bat` in place of `./gradlew`.

- Debug APK: `app/build/outputs/apk/debug/app-debug.apk`
- Unit-test report: `app/build/reports/tests/testDebugUnitTest/index.html`
- Lint report: `app/build/reports/lint-results-debug.html`

With a test device or emulator connected, install the debug app or run instrumentation tests:

```sh
./gradlew :app:installDebug
./gradlew :app:connectedDebugAndroidTest
```

Open the installed app from its launcher icon, or use Android Studio Run to install and launch it.

### Troubleshooting

- **JAVA_HOME is not set / java not found:** install JDK 25, configure `JAVA_HOME` and `PATH`, then reopen the terminal. A working Android Studio build does not automatically configure your shell.
- **SDK location not found:** configure the SDK in Android Studio or create a local `local.properties` with `sdk.dir` set to your actual SDK path.
- **Missing platform or build tools:** install the versions requested by the build through SDK Manager.
- **Unsupported AGP in Android Studio:** use an IDE version supporting the pinned plugin; do not silently downgrade build versions.
- **Dependency/toolchain download failure:** check network access and proxy settings. A first build cannot rely on an empty offline cache.

## Source organization

One application module, `:app`, is used. In the architecture implementation:

- `ui/`: navigation, screens, themes, shared controls, ViewModels, and presentation state.
- `domain/`: plain Kotlin models and customer/pending-submission repository interfaces.
- `data/repositories/`: implementations of those interfaces; currently empty-data placeholders.
- `AppContainer` and `DDApplication`: application-scoped dependency construction.
- `MainActivity`: obtains the ViewModel through its factory and hosts Compose.
- `app/src/test/`: JVM tests; `app/src/androidTest/`: device/emulator tests.

Screens render state and emit events. ViewModels call domain contracts. Data implementations depend on those contracts and keep storage-specific types inside the data layer. Add `data/local`, `synchronization`, and `core` only when real components require them.

## Branch and pull-request workflow

Start a focused branch from the appropriate base after preserving any local work:

```sh
git switch main
git pull --ff-only
git switch -c feat/m1-xx-short-description
```

If the issue depends on an unmerged branch, explicitly identify that dependency and PR base. Do not reset another contributor's work.

1. Read the issue's current description, dependencies, and acceptance criteria.
2. Make the smallest coherent change and run relevant checks.
3. Review `git status` and the diff; stage only intended files.
4. Commit, push the feature branch, and open a PR describing behavior, test results, and remaining human steps.
5. Link the issue. Use `Closes #N` only when merging can legitimately complete all acceptance criteria. If human verification remains, use `Refs #N` and leave the issue open.

Ownership labels describe completion responsibility:
- `owner:agent`: implementable and verifiable with configured development tools and automated/emulator tests.
- `owner:human`: requires a human decision, setup, external verification, or physical-device sign-off.
- `needs-human-step`: names the remaining human step on mixed tickets.

Coding completion alone does not close a human-owned issue. Cloud setup, deployment, billing, signing, and distribution are separate tasks requiring explicit authorization.

## Local files and credentials

Commit source, build configuration, and the Gradle wrapper scripts, JAR, and properties. Keep SDK paths, build outputs, signing keystores, signing passwords, service-account keys, and other credentials out of commits. Review staged files rather than assuming ignore rules cover every secret format. Current ignore rules still require the audit tracked in [M1-02](https://github.com/hopeIsCo0l/DD/issues/3).

## Validation status and fresh-checkout procedure

The current working-tree architecture implementation has passed debug assembly and four JVM tests. It has launched on an emulator, where Customers, Refresh, and system Back were checked. These results do not establish a clean-clone build, a passing lint run, or physical-phone acceptance.

For reproducibility, clone into a **separate directory**, check out the exact commit under review, configure only the local JDK/SDK, and run:

```sh
git rev-parse HEAD
./gradlew --version
./gradlew :app:assembleDebug :app:testDebugUnitTest :app:lintDebug
```

Record the commit, operating system, JDK/Gradle versions, SDK configuration, and outcomes in the issue or PR. Do not delete or reset the working project for this check. The fresh-checkout verification remains outstanding in [M1-02](https://github.com/hopeIsCo0l/DD/issues/3).

Physical-phone acceptance is tracked in [M1-01](https://github.com/hopeIsCo0l/DD/issues/2) and [M1-04](https://github.com/hopeIsCo0l/DD/issues/5). Record the phone model, Android version, tested commit/build, and results for navigation, large fonts, keyboard visibility, and readable error/loading states.

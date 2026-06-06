# CLAUDE.md

Guidance for Claude Code (and other AI coding assistants) when working in this
repo. Codex reads `AGENTS.md`, which mirrors this file. Keep them in sync.

## Project at a glance

WeatherApp is a Kotlin Multiplatform / Compose Multiplatform app showing current
weather, hourly forecast, weekly forecast, favorite cities, settings.

Targets: Android, iOS, Desktop (Windows/macOS/Linux), Web (WasmJs). All UI and
business logic is shared. There is one Gradle module — `composeApp`.

Stack: Kotlin Multiplatform, Compose Multiplatform, Ktor, kotlinx.serialization,
kotlinx.coroutines, kotlinx.datetime, Koin (DI), Voyager (navigation), Coil
(images), Compass (geolocation/permissions), DataStore (preferences), Detekt
(lint).

JVM target: 11. Package root: `com.dellapp.weatherapp`.

## Architecture

Clean architecture, three layers. Respect the layering — UI never touches data.

```
core/
  data/        repository implementations, DTOs, mappers, API service, DataStore
  domain/      models, repository interfaces, use cases, DI
  ui/          shared UI components, theme, core view model, DI
  common/      utilities, enums, dimensions, colors, shapes, typography
feature/
  home/        domain (use cases) + ui (screen, view model, state, DI)
  search/      domain + ui (includes favorites)
  settings/    domain + ui
  splash/      ui only
```

Each feature owns its own Koin module: `<feature>DomainModule.kt` and
`<feature>PresentationModule.kt`. The same is true for `core` —
`CoreDataModule`, `CoreDomainModule`, `CorePresentationModule`.

## Conventions

- **Repository methods return `Result<T>`.** Errors are wrapped in
  `Result.failure(e)`, never thrown to the caller. See
  `WeatherRepositoryImpl.kt`.
- **Use cases are single-responsibility classes**, named `<Verb><Noun>UseCase`,
  with a single `invoke` operator returning `Result<T>` or a domain model. See
  `GetWeatherByLocationUseCase.kt`.
- **View models extend `androidx.lifecycle.ViewModel`** and expose
  `StateFlow<…UiState>`. State is updated via `_uiState.update { it.copy(...) }`.
  See `HomeViewModel.kt`.
- **UI state is a single data class per screen**, named `<Screen>UiState`, with
  defaults and an `isLoading` flag. See `HomeUiState.kt`.
- **Composables are functions named `<Thing>Card`, `<Thing>Row`, `<Screen>Screen`**.
  Card components live in `core/ui/components/`.
- **Spacing, colors, shapes, typography** are centralized in
  `core/common/` — never hardcode `dp`, raw colors, or font sizes in
  composables. Spacing is defined as top-level `val`s in
  `core/common/Dimensions.kt`: `TinySpacing` (4.dp), `SmallSpacing` (8.dp),
  `MediumSpacing` (12.dp), `LargeSpacing` (16.dp), `XLargeSpacing` (20.dp),
  `XXLargeSpacing` (30.dp), `XXXLargeSpacing` (48.dp). Import them by name.
  Use `MaterialTheme.colorScheme.*` for colors and `Shapes.extraLarge` etc.
  for shapes.
- **Strings are resources.** Even one-off strings. See "Localization" below.

## Localization

Strings live in `composeApp/src/commonMain/composeResources/`:

- `values/strings.xml` — English (default)
- `values-it/strings.xml` — Italian
- `values-es/strings.xml` — Spanish

Format is Android-style XML. Access strings via
`stringResource(Res.string.<name>)` from Compose, or `Res.string.<name>` for
`StringResource` references.

**When adding a string, add it to all three files.** Keep the same key name and
the same parameter ordering across translations.

## Adding a new feature — checklist

When adding a use case:
1. Create the use case class in `feature/<name>/domain/`.
2. **Register it in `feature/<name>/domain/di/<Feature>DomainModule.kt`.**
   The build will not catch a missing registration — Koin throws
   `NoBeanDefFoundException` at runtime.

When adding a screen:
1. Create `<Screen>Screen.kt`, `<Screen>ViewModel.kt`, `<Screen>UiState.kt` in
   `feature/<name>/ui/`.
2. Register the view model in
   `feature/<name>/ui/di/<Feature>PresentationModule.kt`.
3. Wire navigation through Voyager in the appropriate parent screen.

When adding a card to the weather detail view:
1. Create the composable in `core/ui/components/`.
2. Match the existing pattern (e.g. `AirQualityCard.kt`, `WindCard.kt`,
   `UVCard.kt`) for `Card`, padding, theming.
3. Add the card to `WeatherDetail.kt` as a `LazyColumn` `item { ... }`,
   surrounded by `item { Spacer(Modifier.height(MediumSpacing)) }` between
   adjacent cards. Cards do **not** live in `HomeScreen.kt` directly — they
   live inside the `WeatherDetail` composable that the home screen renders.

## Build, test, run

```bash
# Build (Android debug)
./gradlew :composeApp:assembleDebug

# Run commonTest sources on the JVM target (fastest path for any commonMain change).
# Note: `commonTest` is a Kotlin source set, NOT a Gradle task — there is no
# `:composeApp:commonTest` task. KMP exposes one test task per target:
#   desktopTest                 — JVM, fastest, use for iteration
#   testDebugUnitTest           — Android JVM unit tests
#   wasmJsTest                  — WASM
#   iosX64Test / iosSimulatorArm64Test / iosArm64Test
#   allTests                    — aggregate of all of the above
./gradlew :composeApp:desktopTest

# Desktop run
./gradlew :composeApp:run

# Web (WasmJs) dev server
./gradlew :composeApp:wasmJsBrowserDevelopmentRun

# Lint
./gradlew detekt
```

iOS: open `iosApp/iosApp.xcworkspace` in Xcode and run.

After any non-trivial change, run `:composeApp:desktopTest` before declaring
the work done. (For changes that touch platform actuals, also run
`:composeApp:allTests`.)

## Tests

- Live in `composeApp/src/commonTest/kotlin/com/dellapp/weatherapp/`.
- Use `kotlin.test` (not JUnit assertions directly), `kotlinx.coroutines.test`
  for `runTest`/`StandardTestDispatcher`, and `app.cash.turbine` for `StateFlow`
  testing.
- **Use fakes, not mocks.** See `HomeViewModelTest.kt` for the established
  pattern: `FakeWeatherRepository`, `FakeAppDataStoreRepository`, etc., as
  plain Kotlin classes implementing the real interfaces.
- View model tests set `Dispatchers.setMain(testDispatcher)` in `@BeforeTest`
  and `Dispatchers.resetMain()` in `@AfterTest`.
- For boundary logic, test the boundaries (e.g. `49`, `50`, `51`) — not just
  the happy path.

## Gotchas (read before editing)

- **API key lives in `secrets.properties`**, NOT `local.properties`. The README
  is out of date on this. The Gradle task `generateApiConfig` reads
  `API_KEY` from `secrets.properties` and writes it into a generated
  `ApiConfig.kt`. `secrets.properties` is gitignored. Do not commit it. Do not
  hardcode the key.
- **Android signing secrets** (`ANDROID_PASSWORD`, `ANDROID_ALIAS`) also live in
  `secrets.properties` and are read via `getSecret(...)` in
  `composeApp/build.gradle.kts`.
- **`commonMain` cannot import platform-specific APIs.** Things like
  `android.content.Context`, `UIKit`, `java.awt.*` belong in `androidMain`,
  `iosMain`, `desktopMain`, `wasmJsMain` respectively, behind
  `expect`/`actual`. The build often only fails on the *specific target* that
  uses the offending code, so a green Android build does not mean iOS compiles.
- **Drawables are SVGs in `composeResources/drawable/`.** A custom Gradle task
  `generateDrawableMap` produces a typed map. Reference them via
  `Res.drawable.<name>` or via the `drawableMap` lookup. Do not add raster
  PNGs to that directory unless you also know what you are doing.
- **iOS plist version** is patched at build time by `updatePlistVersion`. Do
  not manually edit `CFBundleShortVersionString` / `CFBundleVersion` in
  `iosApp/iosApp/Info.plist` — your edits will be overwritten.
- **Voyager screens use `screenModel { ... }` for view models.** Do not
  instantiate view models directly in composables.
- **Koin `viewModel` from `libs.koin.viewmodel`** — note this is the
  multiplatform binding, not `koin-androidx-viewmodel`.

## What NOT to do

- Do not introduce new dependencies without asking. The dependency surface is
  intentional.
- Do not add `Mockito`, `MockK`, or other JVM-only mocking libraries.
  `commonTest` must compile on all targets — write fakes.
- Do not refactor unrelated code while implementing a feature. Scoped diffs
  only.
- Do not commit `secrets.properties` or `local.properties`.
- Do not bump versions in `gradle/libs.versions.toml` as part of a feature PR.
  Dependency upgrades are their own PR.
- Do not edit `build/generated/...` files. They are regenerated.

## When asked to add a feature

Default workflow:
1. Read the most similar existing feature/component first. Follow its pattern.
2. List the files you intend to create or change before writing code.
3. Implement the smallest version that works.
4. Add tests in `commonTest` for any non-trivial logic, including boundaries.
5. Run `:composeApp:desktopTest` and `detekt`.
6. Surface any assumption you are not confident about *before* declaring done.

## Useful entry points

- `App.kt` — composition root.
- `HomeScreen.kt` / `HomeViewModel.kt` — canonical screen + view model pattern.
- `WeatherRepositoryImpl.kt` — canonical `Result<T>` repository pattern.
- `AirQualityCard.kt` — canonical card composable pattern.
- `HomeViewModelTest.kt` — canonical view model test with fakes + Turbine.
- `composeApp/build.gradle.kts` — custom tasks: `generateApiConfig`,
  `generateDrawableMap`, `updatePlistVersion`.

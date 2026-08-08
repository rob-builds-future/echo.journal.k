# Flutter Package Structure

Source basis:
- [target-architecture.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/engineering/target-architecture.md)
- [target-product.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/product/target-product.md)
- [target-design.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/design/target-design.md)

Purpose:
- Define the v1 Flutter project structure.
- Keep implementation feature-first, local-first, and simple enough to start building.
- Avoid package/module splits that are not needed for the first product loop.

## 1. Repository Shape

Target layout:

```text
/
  lib/
    main.dart
    app/
    core/
    shared_ui/
    features/
  test/
  integration_test/
  firebase/
  functions/
  docs/
```

Responsibilities:
- `lib/`
  Flutter app code.
- `test/`
  unit and widget tests mirroring `lib/`.
- `integration_test/`
  end-to-end app flows.
- `firebase/`
  Firebase config, Firestore rules, indexes, and local emulator configuration when needed.
- `functions/`
  Firebase Cloud Functions for protected service calls.
- `docs/`
  product, design, and engineering decisions.

Do not split into multiple Dart packages for v1 unless tooling forces it. Keep one app package and use folders as boundaries.

## 2. Flutter App Structure

Target `lib/` layout:

```text
lib/
  main.dart
  app/
    app.dart
    bootstrap/
    router/
    providers/
  core/
    auth/
    billing/
    config/
    database/
    errors/
    logging/
    network/
    security/
    services/
    storage/
    sync/
    tts/
    utils/
  shared_ui/
    components/
    echo/
    layouts/
    theme/
  features/
    auth/
    onboarding/
    journal/
    translation/
    review/
    premium/
    settings/
    account/
    capture/
    progress/
```

Rules:
- `app` wires the application together.
- `core` contains app-wide infrastructure only.
- `shared_ui` contains reusable visual building blocks and design-system code.
- `features` contains product behavior.
- Feature code should not depend on sibling feature internals.

## 3. App Module

`lib/app/` owns:
- app bootstrap
- root widget
- Riverpod provider scope setup
- router setup and route guards
- auth/setup/premium navigation gates
- environment configuration loading

Suggested structure:

```text
app/
  app.dart
  bootstrap/
    bootstrap.dart
  router/
    app_router.dart
    route_guards.dart
  providers/
    app_providers.dart
```

Keep `app` thin. It should compose dependencies and route flows, not implement feature business logic.

## 4. Core Modules

`lib/core/` owns shared infrastructure used by multiple features.

Recommended modules:
- `auth`
  Firebase Auth session adapters, auth state providers, current-user access.
- `billing`
  purchase integration and entitlement refresh entry points.
- `config`
  environment flags, Firebase options, backend endpoint configuration.
- `database`
  Drift database, tables, DAOs, migrations, local schema versioning.
- `errors`
  typed failures and result types exposed by repositories.
- `logging`
  redacted app logging helpers.
- `network`
  HTTP client setup for Cloud Functions or callable APIs.
- `security`
  secure storage wrappers for tokens and sensitive local values.
- `services`
  truly cross-cutting technical adapters when they are not owned by one feature.
- `storage`
  lightweight key-value preferences.
- `sync`
  sync orchestration, sync metadata, retry hooks, last-write-wins helpers.
- `tts`
  platform text-to-speech integration shared by review and journal listening surfaces.
- `utils`
  date formatting, language normalization, text normalization.

Core rules:
- Keep feature-specific orchestration out of `core`.
- Do not use `core/services` as a generic catch-all.
- Feature-specific service logic stays inside the owning feature or its data module.
- Keep provider credentials out of Flutter entirely.
- Keep raw Firebase/provider exceptions behind typed failures.
- Prefer small focused services over generic platform wrappers.

## 5. Shared UI And Design System

`lib/shared_ui/` owns reusable presentation primitives.

Suggested structure:

```text
shared_ui/
  components/
    primary_button.dart
    app_text_field.dart
    loading_state.dart
    error_state.dart
  echo/
    echo_mark.dart
    echo_translation_surface.dart
    echo_guidance_copy.dart
  layouts/
    app_scaffold.dart
    content_width.dart
  theme/
    app_theme.dart
    app_colors.dart
    app_spacing.dart
    app_typography.dart
```

Design placement:
- Theme tokens, spacing, typography, and color handling live in `shared_ui/theme`.
- Echo-specific visual identity lives in `shared_ui/echo`.
- Feature-specific widgets stay inside the owning feature.

Design rules:
- Keep one primary action per screen.
- Keep original and translated text visually paired.
- Keep Echo visible but subtle.
- Do not add global components until at least two features need them.

## 6. Feature Structure

Default feature layout:

```text
features/<feature>/
  presentation/
  application/
  data/
  domain/
```

Layer responsibilities:
- `presentation`
  screens, widgets, controllers, view models, route entry points.
- `application`
  use cases, feature coordinators, flow orchestration.
- `data`
  repository implementations, DTOs, local/remote data sources, mappers.
- `domain`
  entities, value objects, business rules, repository contracts when they are consumed by application code.

V1 simplification:
- Small features may omit empty folders.
- Do not add abstract interfaces unless they reduce coupling or make tests clearer.
- Prefer one repository per feature area.
- Repository contracts/interfaces should live in the domain or application-facing layer, not in `data`.
- `data` implements repository contracts; UI and application code should not depend on data internals.

## 7. V1 Feature Boundaries

### `auth`

Owns:
- sign in and sign up UI
- auth entry flow state
- auth application logic for login, logout, and session refresh triggers
- Firebase Auth integration through its data layer and `core/auth`

Does not own:
- account settings surfaces
- profile preference editing
- route guard implementation

Account and settings remain separate from auth entry/login flows.

### `onboarding`

Owns:
- quick setup flow
- target language selection
- personal color/theme setup
- onboarding completion state

Does not own:
- auth implementation
- settings persistence details

### `journal`

Owns:
- Home as the main anchor screen for v1
- home entry points into journaling
- entry editor
- entry create/update/delete/list flows
- saving journal entries to the local source of truth

Does not own:
- provider selection for translation
- premium purchase flow
- text-to-speech playback internals

### `translation`

Owns:
- live translation request flow
- translation result model
- supported language options
- non-blocking retry UX for live translation failures

Does not own:
- journal entry persistence
- final premium entitlement decisions
- provider credentials or provider routing logic

### `review`

Owns:
- saved-entry reading experience
- original/translated review presentation
- text-to-speech controls
- AI coach feedback presentation when available

Does not own:
- journal editing
- platform text-to-speech integration
- raw AI/LLM calls

### `premium`

Owns:
- paywall UI
- upgrade/restore flow
- entitlement display and refresh triggers
- feature gating state consumed by UI

Does not own:
- canonical entitlement authority
- translation provider selection

Canonical entitlement state is backend-managed for v1.

### `settings`

Owns:
- shallow settings surface
- preference edits
- language/theme/profile preference changes

Does not own:
- onboarding flow
- deep account-management flows beyond v1 essentials

### `account`

Owns:
- account settings entry points
- sign out/delete account surfaces if included in v1
- user identity presentation

Does not own:
- Firebase Auth adapter implementation

### `capture`

Owns:
- photo-to-entry capture flow
- OCR confirmation and cleanup UI
- handoff into the journal editor

V1 note:
- This is later or stubbed for the first MVP slice; it must not block the first translated-journaling loop.

### `progress`

Owns:
- lightweight streaks
- basic usage summaries
- progress surfaces on Home or Review

V1 note:
- Keep progress derived, lightweight, and secondary. It must not block the first translated-journaling loop.

## 8. Data And Service Modules

Local-first rule:
- Drift is the local source of truth for durable product data.
- Firestore is the remote sync target for user-owned product data.
- Firebase Cloud Functions is the protected service layer.

Suggested ownership:
- `core/database`
  Drift database, tables, DAOs, migrations.
- `core/sync`
  sync triggers, sync queue, last-write-wins metadata, Firestore sync helpers.
- `core/auth`
  Firebase Auth session state and authenticated user access.
- `core/security`
  secure storage for sensitive local values.
- `core/tts`
  platform text-to-speech adapter and playback primitives.
- `features/auth/data`
  auth repository implementation backed by Firebase Auth through `core/auth`.
- `features/journal/data`
  journal repository and Drift-backed journal data sources.
- `features/translation/data`
  translation repository that calls Cloud Functions, not provider APIs directly.
- `features/premium/data`
  entitlement repository that reads backend-managed entitlement state.
- `features/review/data`
  review repository for saved entries and AI coach result access; review uses `core/tts` for playback.

Service boundaries:
- Flutter may sync product data directly with Firestore through Firebase client SDKs.
- Flutter must not call DeepL, LibreTranslate, or LLM providers directly.
- Translation provider selection happens server-side.
- Provider credentials live only in backend/server config.
- Repositories expose typed failures upward.
- Repository contracts live in the domain/application-facing layer; `data` provides implementations.

V1 sync policy:
- trigger sync on app startup, authenticated session change, local write, network reconnect, and foreground resume
- use `createdAt`, `updatedAt`, and minimal metadata needed for last-write-wins
- keep complex conflict handling and collaboration out of scope

## 9. Backend Structure

Use Firebase Cloud Functions for v1.

Suggested `functions/` responsibilities:
- verify Firebase Auth for protected endpoints
- route translation requests to LibreTranslate or DeepL
- enforce backend-managed entitlement state
- handle AI coach/LLM calls
- apply rate limiting
- emit redacted telemetry/logging

Do not use Cloud Functions as the default journal storage path. Journal/product data syncs through Drift and Firestore.

## 10. Dependency Directions

Allowed:
- `app` -> `features`, `core`, `shared_ui`
- `features/*/presentation` -> same feature `application`, same feature `domain`, `shared_ui`, selected `core` providers
- `features/*/application` -> same feature `domain`, repository contracts from the domain/application-facing layer, selected `core` services
- `features/*/data` -> same feature `domain`, `core/database`, `core/sync`, `core/network`, `core/auth`, `core/errors`
- `features/*/domain` -> no Flutter, no Firebase, no Drift
- `shared_ui` -> Flutter and design tokens only
- `core` -> platform libraries and external SDK adapters

Avoid:
- sibling feature imports for implementation details
- UI importing Drift, Firestore, or Cloud Function clients directly
- UI or application code importing feature `data` internals directly
- domain models depending on DTOs
- shared UI depending on feature code
- Cloud Functions becoming the owner of journal domain logic

Cross-feature flows should use application-level coordinators or shared domain values, not direct calls into another feature's presentation or data internals.

## 11. Testing Placement

Mirror the production structure:

```text
test/
  core/
  shared_ui/
  features/
    auth/
    journal/
    translation/
    review/
    premium/
integration_test/
  first_use_flow_test.dart
  daily_practice_flow_test.dart
  review_and_premium_flow_test.dart
```

Testing priorities:
- repository logic and typed failures
- Drift migrations and DAOs
- sync last-write-wins behavior
- translation orchestration and retry UI state
- premium gating as a business rule
- journal writing and review widget flows
- first-use and daily-practice integration flows

Keep mocks at repository/service boundaries. Avoid mocking widget internals.

## 12. Scalability Notes

Add complexity only when a feature proves it needs it.

Later expansion paths:
- `capture` can grow OCR provider adapters without changing the journal handoff.
- `progress` can add richer metrics while keeping derived state separate from journal persistence.
- AI coaching can add prompt variants and personalization behind the Cloud Functions boundary.
- Embeddings or personalization should live behind backend/service boundaries, not in the Flutter client.
- If features become large, split internally first before creating new Dart packages.

V1 rule:
- If a module does not make translated journaling faster, clearer, or easier to maintain, keep it out of the first implementation.

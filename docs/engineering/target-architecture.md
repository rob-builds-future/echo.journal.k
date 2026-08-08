# Target Architecture

Source basis:
- [current-engineering-foundation.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/engineering/current-engineering-foundation.md)
- [target-product.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/product/target-product.md)

Purpose:
- Define the Flutter architecture for the rebuilt app.
- Keep the system simple, modular, and centered on translated journaling.

## 1. Overall Architecture

### Architectural style

Target style:
- feature-based
- modular inside one Flutter app
- clear domain boundaries
- minimal shared infrastructure

Recommended approach:
- Use a feature-first structure with a small shared core layer.
- Keep each feature responsible for its own presentation, application logic, and data coordination.
- Avoid deep clean-architecture nesting unless it clearly reduces coupling.

High-level shape:
- `app`
- `core`
- `features`
- `shared_ui`

Flow:
- UI -> feature controller/state -> repositories/services -> remote/local data sources

### Why this architecture

- It matches the product shape:
  one strong translated-journaling loop with a handful of supporting features.
- It scales better than a purely screen-based structure.
- It stays simpler than a heavily abstracted enterprise architecture.

## 2. Proposed Package Structure

Recommended repository layout:

```text
lib/
  app/
    app.dart
    router/
    bootstrap/
  core/
    config/
    errors/
    logging/
    network/
    storage/
    billing/
    tts/
    ocr/
    ai/
    auth/
    design/
    utils/
  shared_ui/
    components/
    layouts/
    theme/
    echo/
  features/
    onboarding/
      presentation/
      application/
      data/
      domain/
    journal/
      presentation/
      application/
      data/
      domain/
    translation/
      presentation/
      application/
      data/
      domain/
    review/
      presentation/
      application/
      data/
      domain/
    premium/
      presentation/
      application/
      data/
      domain/
    capture/
      presentation/
      application/
      data/
      domain/
    settings/
      presentation/
      application/
      data/
      domain/
    progress/
      presentation/
      application/
      data/
      domain/
    account/
      presentation/
      application/
      data/
      domain/
```

### Structure rules

- `presentation`
  screens, widgets, controllers, view models, navigation entry points
- `application`
  use cases, feature coordinators, orchestration logic
- `data`
  repositories, DTOs, remote and local data sources
- `domain`
  entities, value objects, pure business rules

Do not over-split:
- If a feature is small, combine `application` and `presentation` or `data` and `domain`.
- The folder structure is a boundary tool, not a ceremony requirement.

## 3. State Management Approach

### Proposed choice

Use `Riverpod` as the single state management approach.

### Why Riverpod

- Clear dependency wiring without a separate DI framework.
- Works well for feature-scoped state and app-scoped services.
- Supports async state, derived state, and testability cleanly.
- Easier to keep consistent than mixing multiple patterns.

### State model

Use three state types only:

- app state
  auth session, onboarding completion, premium entitlement
- feature state
  journal entry editor, review state, capture state, settings edits
- derived state
  translation output, progress summaries, streaks, premium availability

### State rules

- One primary controller per feature flow.
- Keep widget-local state only for small visual concerns.
- Put network calls and persistence behind repositories, not directly in UI controllers.
- Keep derived state derived. Do not persist what can be recomputed cheaply.

## 4. Data Layer Structure

### Data layer shape

Each feature should depend on repository interfaces, with concrete data sources hidden behind them.

Pattern:
- feature controller/use case
- repository interface
- remote/local data source
- DTO mapping

### Recommended data domains

#### Auth

- session state
- current user
- sign in / sign up / logout / delete account

#### Journal

- journal entries
- create / update / delete / list / review

#### Translation

- live translation
- supported languages
- premium translation path when enabled

#### Review

- translated-entry text-to-speech
- premium AI feedback / explanatory content

#### Settings

- language
- personal color / theme
- onboarding completion
- lightweight profile settings

#### Premium

- entitlements
- paywall state
- restore purchases

#### Capture

- image intake
- OCR extraction
- confirmation and cleanup

### Models

Keep a small domain model set:

- `User`
- `JournalEntry`
- `TranslationResult`
- `LanguageOption`
- `PremiumEntitlement`
- `AiFeedback`
- `CapturedEntryDraft`
- `AppPreferences`

Prefer domain models over leaking backend DTOs upward.

### Storage

Remote:
- Firebase Auth
- Firestore remote sync target
- Firebase Cloud Functions for protected service calls

Local:
- preferences
- cached session metadata
- optional cached recent entries
- temporary capture artifacts if needed

Use Drift as the local source of truth for durable product data. Use Firestore as the remote sync target, not a separate journal backend. Use the backend as a protected service layer, not the default journal storage path.

## 5. Data & Service Stack

Goal:
- Define the local and remote data architecture, authentication, translation services, and AI support layer for the Flutter app.

### Local Data Layer

- Use Drift on SQLite as the primary local database.
- Store durable product data locally:
  journals, translations, drafts, sync queue entries, and metadata.
- Treat the local database as the app's source of truth for journal-facing data.
- Use secure storage for tokens, secrets, and other sensitive credentials.
- Use lightweight key-value storage only for simple settings and preferences when a relational table is unnecessary.
- Keep Drift schema versioning and migrations owned by the local data layer.

### Remote Data Layer

- Use Firebase Auth for authentication.
- Use Firestore as the remote sync target for user-owned product data.
- Scope Firestore product data per authenticated user.
- Keep responsibilities separate:
  local DB is the source of truth; Firestore is the sync layer.
- The Flutter app may sync journal and product data directly with Firestore through Firebase client SDKs.
- Do not imply that all remote writes must pass through the backend.
- Add repository boundaries for feature access to data.
- Add a sync orchestration layer to coordinate local writes, remote updates, conflict handling, and retry behavior.
- Use a simple v1 conflict policy:
  last-write-wins based on local timestamps and sync metadata.
- Defer richer merge, collaboration, and multi-device conflict workflows; they are out of scope for v1.
- Trigger v1 sync on app startup, authenticated session changes, local writes, network reconnect, and foreground resume.
- Keep Firestore DTOs and sync metadata out of presentation and domain code.
- Use minimal v1 sync metadata:
  `createdAt`, `updatedAt`, and only the sync fields required for last-write-wins.
- Avoid over-complicating remote sync metadata for v1.
- Treat Firestore security rules as part of the remote data layer responsibilities; rules must restrict access to each user's own data only.
- Repositories should expose typed failures upward and avoid leaking raw Firebase exceptions into UI layers.

### Translation Layer

- Use a dual-provider translation strategy:
  LibreTranslate for free-tier traffic and fallback; DeepL API for premium users and high-quality translations.
- Treat LibreTranslate self-hosting as optional, not mandatory on day one.
- If self-hosting is not justified by cost or privacy, use LibreTranslate as a deployable backend dependency or external service.
- Validate self-hosting pragmatically because it adds operational complexity.
- Route by entitlement:
  free users use LibreTranslate; premium users use DeepL.
- Use backend-managed entitlement state as the canonical source of truth for provider routing.
- Entitlement may be derived from app store validation and mirrored into backend-managed state as needed.
- Flutter should not be the final authority for provider routing; translation provider selection is decided server-side.
- Keep provider selection behind a translation service interface.
- Keep DeepL, LibreTranslate, and other provider credentials only in backend/server config, never in the Flutter client.
- Account for latency differences in the UI and sync flow, especially during live translation.
- Make quality trade-offs explicit:
  LibreTranslate optimizes cost/control; DeepL optimizes quality for paid usage.
- Live journaling must never be blocked by translation failure.
- For live translation failure, show a recoverable non-blocking error state and allow retry from the UI.
- Do not introduce complex queueing for live translation in v1.
- Saved-entry or background translation may use retry behavior later.

### AI / Language Coach Layer

- Use LLMs only for explanations, corrections, suggestions, and tutoring.
- Do not use LLMs for raw translation.
- Run LLM calls server-side only; the Flutter client must not call model providers directly.
- Add a prompt layer for reusable coaching prompts and response shaping.
- Add guardrails for scope, safety, language constraints, and premium feature gating.
- Keep AI logging minimal:
  request metadata, latency, provider errors, and redacted coaching quality signals only.

### Backend / API Layer

- Use Firebase Cloud Functions as the minimal backend service layer for v1.
- Responsibilities:
  routing translation providers, handling LLM calls, verifying auth for protected endpoints, rate limiting, and redacted telemetry/logging.
- Use the backend for protected service calls, not as the default path for all product data sync.
- Keep backend behavior minimal and product-specific.
- Avoid moving core journal logic to the backend unless sync, billing, or provider security requires it.
- Keep live journaling independent from complex background sync behavior; stage advanced reliability features later.

### Privacy and Data Handling

- Treat journal text as sensitive user data.
- Avoid storing raw journal text in logs, telemetry, or traces unless explicitly justified and consented.
- Prefer redacted metadata for observability.
- Minimize retention for translation and AI requests.
- Avoid unnecessary content persistence in backend and service logs.

### Open Questions / Future Considerations

- Benchmark translation providers, including DeepL versus other high-quality alternatives.
- Model cost scaling for realtime translation and coaching usage.
- Define the offline mode strategy and conflict behavior.
- Evaluate whether embeddings or personalization are needed later for coaching or review experiences.

## 6. Feature Boundaries

### Core feature boundaries

#### `journal`

Owns:
- entry creation
- entry editing
- entry list
- entry persistence

Does not own:
- translation provider selection
- billing

#### `translation`

Owns:
- live translation flow
- language options
- translation result model

Does not own:
- entry persistence
- premium purchase flow

#### `review`

Owns:
- original + translated entry review
- text-to-speech playback
- premium explanatory feedback presentation

#### `premium`

Owns:
- entitlement state
- paywall logic
- upgrade / restore flow
- feature gating

#### `capture`

Owns:
- photo intake
- OCR processing
- draft cleanup before journal creation

#### `settings`

Owns:
- preferences editing
- language and theme selection
- lightweight account-related settings

#### `progress`

Owns:
- streaks
- lightweight usage summaries
- review-oriented progress surfaces

### Boundary rule

The translated journaling flow crosses multiple features, but the ownership stays clear:
- `journal` owns the entry
- `translation` owns translation generation
- `review` owns listening and AI-enhanced review
- `premium` decides which enhanced paths are available

## 7. Shared Core Logic

Keep shared logic small and intentional.

### Core modules worth sharing

- auth session handling
- routing guards
- network client configuration
- storage helpers
- language normalization
- date formatting
- text normalization for progress metrics
- Echo identity rules:
  symbol usage, personal-color translation treatment, shared presentation tokens

### What should not go into core

- feature-specific UI widgets
- feature-specific orchestration
- business logic that only one feature uses

Rule:
- if only one feature needs it, keep it inside the feature

## 8. Navigation Structure

Recommended top-level routes:

- auth
- setup
- home
- journal
- review
- premium
- capture
- settings

### Navigation principles

- Home is the primary anchor.
- Journal is the primary action screen.
- Review is entered from history or after save.
- Premium is contextual, not a tab.
- Capture flows back into Journal.
- Settings stays shallow.

Use declarative routing with route guards for:
- authenticated vs unauthenticated
- setup complete vs incomplete
- premium access where needed

## 9. Testing Strategy

### Primary testing pyramid

#### Unit tests

Test:
- domain rules
- repository logic
- translation orchestration
- premium gating
- progress derivation
- OCR result normalization

#### Widget tests

Test:
- core screens
- journal writing flow
- translation display behavior
- TTS controls
- upgrade prompts
- setup flow

#### Integration tests

Test:
- sign in -> setup -> first entry -> translation -> save
- reopen past entry -> listen -> premium feedback
- purchase / restore premium flow
- photo capture -> OCR review -> create entry

### Testing rules

- Test the translated journaling loop first.
- Test premium gating as a business rule, not just a UI state.
- Keep mocks at repository/service boundaries.
- Avoid over-mocking widget internals.

## 10. Migration Notes

### Reuse conceptually

- repository boundaries
- startup routing logic
- entry domain model semantics
- translation as part of the main entry flow
- preference ownership
- derived statistics approach
- language normalization behavior

### Do not port directly

- Koin
- StateFlow patterns
- Compose component hierarchy
- Android navigation structure
- DataStore implementation details
- Retrofit / OkHttp / Moshi stack as architecture decisions

### Migration strategy

Build in this order:

1. app bootstrap, auth session, routing guards
2. preferences and lightweight setup
3. journal entry creation and persistence
4. real-time translation in the journal flow
5. review screen with text-to-speech
6. premium gating and upgrade flow
7. AI feedback
8. photo capture
9. lightweight progress

## 11. Simplicity Rules

- Prefer one repository per feature area, not multiple layers of wrappers.
- Prefer one state management system for the whole app.
- Prefer one main screen per job.
- Prefer explicit models over generic maps.
- Prefer contextual premium gating over a large monetization subsystem.
- Prefer feature boundaries over abstract base classes.

## Summary

The target Flutter architecture should be:
- feature-first
- Riverpod-based
- repository-driven
- light on abstraction
- centered on translated journaling as the main product loop

If a technical choice does not make translated journaling faster, clearer, or easier to maintain, it should probably not be in the architecture.

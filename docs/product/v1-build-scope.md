# V1 Build Scope

Source basis:
- [target-product.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/product/target-product.md)
- [target-design.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/design/target-design.md)
- [target-architecture.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/engineering/target-architecture.md)
- [flutter-package-structure.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/engineering/flutter-package-structure.md)

Purpose:
- Define the concrete scope for the first usable Flutter build slice.
- Keep v1 centered on the smallest valuable translated-journaling loop.
- Make implementation planning possible without pulling in later product breadth.
- This is not the full target-product MVP; premium and other MVP-adjacent capabilities can follow after the core loop is validated.

## 1. V1 Product Goal

V1 must let a signed-in user set basic preferences, write a journal entry, see a live translation, listen to it, save it locally, sync it remotely, and review it later.

This first build slice should validate the core loop before implementing the broader MVP surface.

## 2. In-Scope Features

### Auth

Purpose:
- Let users access a private journal account.

Why in v1:
- Journal entries are user-owned data and need authenticated Firestore sync.

Required screens / flows:
- unified sign in / sign up screen
- signed-in session restoration
- sign out entry point if needed for testing and basic account control

Required technical support:
- `features/auth`
- Firebase Auth through `core/auth`
- route guards in `app/router`
- typed auth failures

### Quick Setup

Purpose:
- Capture only the defaults needed for a useful first writing session.

Why in v1:
- Translation requires a target language, and the Echo experience needs a minimal personal color/theme choice.

Required screens / flows:
- one short setup screen after first auth
- target language selection
- personal color/theme selection
- setup completion gate before Home

Required technical support:
- `features/onboarding`
- lightweight preferences storage
- Drift or key-value persistence for app preferences as appropriate
- setup route guard

### Journal Home And Editor

Purpose:
- Provide the main anchor screen and the primary writing surface.

Why in v1:
- This is the center of the product loop.

Required screens / flows:
- Home screen owned by `features/journal`
- recent entries list
- start new entry action
- journal editor with original text and translated text paired in one flow
- lightweight Echo-guided translation presentation, not raw machine output alone
- save entry
- non-blocking live translation error state with retry

Required technical support:
- `features/journal`
- Drift-backed journal repository
- `JournalEntry` domain model
- local write path before remote sync
- repository contracts in domain/application-facing layer

### Live Translation

Purpose:
- Translate journal text during the writing flow.

Why in v1:
- Real-time translated journaling is the core product value.

Required screens / flows:
- live translation output in the Journal screen
- loading / pending state
- recoverable non-blocking error state
- retry from the UI

Required technical support:
- `features/translation`
- Cloud Functions translation endpoint
- server-side provider routing
- default free-tier translation behavior
- backend structure prepared for future entitlement without requiring premium logic in v1
- typed translation failures
- no direct DeepL, LibreTranslate, or LLM provider calls from Flutter

### Translation Listening

Purpose:
- Let users hear translated text aloud.

Why in v1:
- Listening is part of the core learning loop.

Required screens / flows:
- one-tap play control from Journal and Review where translated text exists
- basic play/stop state
- graceful failure state if TTS is unavailable

Required technical support:
- `core/tts`
- listening controls in `features/review` and journal presentation as needed
- typed TTS failure handling

### Entry Review

Purpose:
- Let users return to saved entries and review original and translated text together.

Why in v1:
- The product promise includes saving and learning from real entries, not only transient translation.

Required screens / flows:
- open a saved entry from Home
- read original and translated text together
- listen to translated text
- return to Home

Required technical support:
- `features/review`
- read access through journal/review repositories
- Drift-backed saved entry lookup
- sync-safe entry identifiers

### Minimal Settings

Purpose:
- Let users adjust only preferences required for the first loop.

Why in v1:
- Users may need a place to change language/theme basics without deep account management.
- Settings must not block the first journaling loop.

Required screens / flows:
- shallow settings screen
- target language edit
- theme/personal color edit

Required technical support:
- `features/settings`
- lightweight preference persistence
- no deep settings sub-flows unless required for data safety
- only essential preferences required for translation and journaling

## 3. Out-of-Scope / Later

- Photo-to-entry capture and OCR.
- AI coach explanations, corrections, suggestions, and tutoring.
- Premium purchase flow and paywall polish.
- DeepL premium routing beyond a backend-ready interface if not needed for the first launch slice.
- Advanced account management beyond basic auth/session needs.
- Rich reminders.
- Prompt libraries and templates.
- Heavy progress, streaks, or analytics surfaces.
- Social, sharing, or feedback-adjacent product areas.
- Complex offline conflict resolution or collaboration flows.
- Background reliability systems that are not needed for the first translated-journaling loop.

## 4. Core User Journeys

### First Use

1. User signs up or signs in.
2. User selects target language and personal color/theme.
3. User lands on Home.
4. User starts a new journal entry.
5. User writes text and sees live translation.
6. User listens to the translated text.
7. User saves the entry.

### Daily Practice

1. User opens the app.
2. Existing session restores.
3. User starts a new entry from Home.
4. User writes naturally.
5. Translation appears in the same screen.
6. User saves and returns to Home.

### Review

1. User opens a saved entry from Home.
2. User reviews original and translated text together.
3. User listens to the translated text.
4. User returns to Home.

### Translation Failure

1. User writes in the Journal screen.
2. Live translation request fails.
3. App shows a recoverable non-blocking error state.
4. User can keep writing.
5. User can retry translation from the UI.

## 5. Must-Have Technical Capabilities

### Auth

- Firebase Auth sign in / sign up.
- Session restoration.
- Auth route guards.
- Typed auth failures.

### Local Storage

- Drift as the local source of truth for journals, translations, drafts, sync queue entries, and metadata.
- Secure storage for sensitive local values.
- Lightweight key-value storage for simple preferences when a Drift table is unnecessary.
- Drift schema versioning and migrations.

### Sync

- Firestore as remote sync target for user-owned product data.
- Per-user Firestore data scope and security rules.
- Sync triggers:
  app startup, authenticated session change, local write, network reconnect, foreground resume.
- Minimal sync metadata:
  `createdAt`, `updatedAt`, and fields needed for last-write-wins.
- Keep sync minimal and reliable.
- Keep the journaling loop usable if remote sync is delayed.
- Avoid over-engineering queues and retry systems for the first version.
- No advanced merge or collaboration logic in v1.

### Translation

- Cloud Functions endpoint for translation requests.
- Server-side translation provider routing using default free-tier behavior.
- Backend shape should be prepared for future entitlement, but entitlement is not required for v1.
- Live translation failure mapped to recoverable UI state.
- No complex live translation queueing.

### Backend Service Routing

- Firebase Cloud Functions as the v1 backend.
- Auth verification for protected endpoints.
- Translation provider routing.
- Rate limiting.
- Redacted telemetry/logging.
- Provider credentials stored only in backend/server config.

### Privacy And Data Handling

- Treat journal entries as sensitive user data.
- Avoid storing raw journal text in logs or telemetry.
- Minimize data retention for translation and backend calls.
- Store user data only where necessary for product functionality.

### Text-To-Speech

- `core/tts` platform adapter.
- Play/stop support for translated text.
- Basic unavailable/error state.

### Basic Error Handling

- Repository-level typed failures.
- UI does not receive raw Firebase, Drift, provider, or Cloud Functions exceptions.
- Journal writing remains usable when translation fails.

### Coach Support

- No AI coach support is required for the first v1 loop.
- Keep backend and package boundaries compatible with adding AI coach later.

## 6. MVP Constraints

- Keep progress lightweight or omit it from the first slice.
- Do not build advanced conflict handling.
- Do not overbuild background sync reliability.
- Do not add photo capture to the first slice.
- Do not build a complex AI tutoring surface.
- Do not build broad settings or account-management depth.
- Do not add prompt/template systems.
- Do not let premium polish block the free translated-journaling loop.
- Do not require entitlement logic for the first build slice.
- Keep Home, Journal, Translation, Listening, Save, and Review as the priority path.

## 7. Risks And Assumptions

### Risks

- Live translation latency may weaken the writing experience.
- Translation quality may affect perceived product value.
- Sync behavior may become complex if offline edits are expanded too early.
- Premium value may remain unclear if DeepL and AI coach are postponed.
- Echo identity may feel decorative if the Journal screen does not make the translation treatment meaningful.

### Assumptions

- A local-first journal with live translation is valuable before advanced AI coaching.
- Basic Firebase Auth is sufficient for the first private journal build.
- Firestore sync with last-write-wins is enough for v1.
- LibreTranslate or the default free translation path is sufficient to validate the loop.
- Premium entitlement can be added after the default free-tier translation path works.
- Text-to-speech materially improves the first learning loop.
- Progress, capture, and premium depth can be added after the core loop is working.

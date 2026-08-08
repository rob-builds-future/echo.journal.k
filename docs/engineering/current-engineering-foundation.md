# Current Engineering Foundation

Source basis:
- [file-manifest.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/file-manifest.md)
- [logic-map.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/logic-map.md)
- [feature-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/feature-inventory.md)
- [audit-coverage.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/audit-coverage.md)

Purpose:
- Document the current technical system in a rebuild-oriented way.
- Highlight what should carry into a Flutter implementation versus what is Kotlin/Android-specific.
- Separate durable business logic from legacy implementation details.
- Mark uncertainty explicitly.
- Additional product inputs provided after the legacy audit are included below and marked as stakeholder-provided where they are not directly represented in the legacy code audit.

## 1. Confirmed Architectural Structure

### High-level architecture

Confirmed:
- The current app is organized around a standard layered structure:
  UI screens/components -> view models -> repositories/services -> remote or local storage
- Dependency injection is centralized through Koin.
- State management is centered on `StateFlow` and `MutableStateFlow`.
- Remote persistence is split across two backends:
  Firebase Auth / Firestore
  a separate translation API at `https://libre4echo.de/`
- Local persistence is lightweight and preference-based through `DataStore`.

Interpretation:
- The most important technical loop appears to be real-time translation of journal entries.
- Journaling and translation are not best understood as separate stacks. The translation path appears to be central to the primary product behavior.
- For rebuild planning, the system should be treated as a translated-journaling product with supporting account, preference, and progress layers.

Stakeholder-provided additions:
- Text-to-speech for translated entries is an important retained feature.
- Native in-app purchase support is planned via StoreKit 2 and Google Play Billing for a premium plan.
- Premium is expected to unlock an enhanced translation model and AI feedback or extra explanatory information around translated text.
- Echo as guiding character, symbol, and personal-color-marked translation layer is important product identity and should be reflected in rebuild planning.
- Photo-to-entry capture from handwritten journal pages is an important feature that should remain in scope.

### Rebuild relevance

For a Flutter rebuild, the durable architectural ideas are:
- clear repository boundaries
- state derived from authenticated user context
- local preference persistence
- analytics/statistics derived from entry data rather than stored separately

The Kotlin/Android-specific parts are:
- Compose UI structure
- Koin wiring
- `StateFlow` mechanics
- Android `DataStore`
- Android navigation implementation

## 2. Critical Business Logic vs. Legacy Implementation Detail

### Critical business logic

These appear product-relevant and likely should be preserved independent of framework:

- User identity and account state:
  current user, sign-in/sign-up, profile deletion, logout, password reset
- Entry domain model:
  content, translated content, source language, target language, duration, favorite, created/updated timestamps
- Entry operations:
  observe entries, create, update, delete, toggle favorite, add duration
- Translation behavior:
  translate journal text using current source and target language preferences as part of the main entry flow
- Translation review behavior:
  support listening to translated content through text-to-speech
- Preferences:
  onboarding completion, theme, language, source language, username, template, last-congrats date, reminder configuration
- Derived statistics:
  days with entries, total words, total duration, streak logic, top-word support
- Startup routing rules:
  unauthenticated -> auth
  authenticated but not onboarded -> onboarding
  authenticated and onboarded -> main flow

### Legacy implementation detail

These appear implementation-specific and should not be treated as fixed requirements for Flutter:

- Compose screen/component split
- Koin dependency registration
- `NavDestination` and `NavExtensions`
- Retrofit, OkHttp, and Moshi choices as concrete libraries
- IPv4-only DNS override implementation
- Android `DataStore` APIs
- `StateFlow`-specific reactive wiring
- Android manifest permission declarations as the exact configuration surface

## 3. Key Models, Services, Storage, and Networking

### Core models

| model | role | rebuild relevance | certainty |
| --- | --- | --- | --- |
| `JournalEntry` | Primary journal domain object with original and translated content plus metadata. | High. This should likely survive with minimal schema changes. | confirmed |
| `User` | Authenticated user profile with preferred language and created date. | High. Core account model. | confirmed |
| `TranslateRequest` / `TranslateResponse` | Translation transport models. | Medium. Preserve behavior, not necessarily exact DTO structure. | confirmed |
| `LanguageDto` | Supported language data from translation service. | High for translation UX. | confirmed |
| `ReminderItem` / `ReminderConfig` | Reminder-related local models. | Medium, but reminder behavior remains only partially verified. | inferred |
| Premium plan / entitlement model | Subscription or purchase state controlling premium translation and AI features. | High if monetization is in active scope, but not represented in the legacy code audit. | stakeholder-provided |
| Photo-to-entry capture model | Image input plus OCR or conversion state for handwritten journal intake. | High if handwritten capture remains in scope, but not represented in the legacy code audit. | stakeholder-provided |

### Services and repositories

| service/repository | responsibility | rebuild relevance | certainty |
| --- | --- | --- | --- |
| `UserAuthRepo` / `UserAuthRepoImpl` | Auth lifecycle, profile management, Google sign-in, profile deletion, current user lookup. | High. Preserve the service boundary even if backend integration changes. | confirmed |
| `JournalRepo` / `JournalRepoImpl` | Entry CRUD and live observation. | High. Core data access boundary. | confirmed |
| `TranslationApiRepo` / `TranslationApiRepoImpl` | Translation requests and supported language fetch. | High. Product differentiator boundary. | confirmed |
| `LanguageRepo` / `LanguageRepoImpl` | Language list retrieval. | Medium. May be merged into translation data access in a rebuild if desired. | confirmed |
| `PrefsRepo` / `PrefsRepoImpl` | Local preference persistence. | High. Preserve the preference contract; storage technology can change. | confirmed |
| Text-to-speech service layer | Playback of translated entries for listening practice. | High for rebuild scope, but not explicitly captured in the legacy logic documents. | stakeholder-provided |
| Native billing / entitlement layer | StoreKit 2 and Google Play Billing integration for premium purchase state. | High if monetization is in active scope. | stakeholder-provided |
| OCR or photo-import service layer | Convert handwritten photo input into editable journal text. | High if handwritten entry capture remains in scope. | stakeholder-provided |
| AI feedback/enrichment service layer | Provide premium explanatory feedback or extra information around translated text. | High if premium learning value is part of the plan. | stakeholder-provided |

### Storage

| storage | current role | rebuild relevance | certainty |
| --- | --- | --- | --- |
| Firestore user document | Stores user profile data. | High if Firebase remains; otherwise map to equivalent backend profile model. | confirmed |
| Firestore journal entry subcollection | Stores user entries. | High if Firebase remains; otherwise preserve schema semantics. | confirmed |
| DataStore preferences | Stores local app settings and onboarding state. | High conceptually, low technology lock-in. Replace with Flutter-appropriate local persistence. | confirmed |
| Reminder JSON in preferences | Stores serialized reminder config. | Medium. Preserve only if reminder feature survives validation. | inferred |
| Premium entitlement state | Needed to gate enhanced translation, AI feedback, and premium review features. | High if premium is part of rebuild scope, but current storage shape is unknown. | stakeholder-provided |
| Photo import artifacts | Likely requires temporary image storage and OCR-processing state if handwritten capture is retained. | High if feature is in scope, but storage design is unknown. | stakeholder-provided |

### Networking

| network element | role | rebuild relevance | certainty |
| --- | --- | --- | --- |
| Libre translation API | Text translation and language list retrieval. | High if the product keeps integrated translation. | confirmed |
| Firebase Auth | Authentication provider. | High if backend continuity is desired; otherwise migrate behavior rather than SDK shape. | confirmed |
| Firestore | Cloud storage for users and entries. | High if backend continuity is desired. | confirmed |
| IPv4-only DNS override | Network workaround for API calls. | Important migration consideration. Preserve only if the underlying network issue still exists. | confirmed |
| Premium AI or enhanced translation backend | Needed if premium promises better translation quality or explanatory AI features. | High if premium scope is real, but provider and contract are currently unknown. | stakeholder-provided |

## 4. Shared Logic and Dependencies

### Shared logic likely worth carrying forward

- `LanguageUtil`:
  language-code normalization and locale/display mapping
- `DateUtil`:
  localized formatting behavior
- `PorterStemmerEn`:
  text normalization for top-word statistics
- streak calculation logic:
  derived from entry dates, not stored separately
- startup routing logic:
  based on auth state and onboarding state
- translation identity layer:
  Echo symbol plus personal-color-marked translated outputs should be treated as product-significant presentation logic, even though the current implementation details are not fully represented in the logic audit

### Shared dependencies in the legacy app

Confirmed:
- Firebase Auth
- Firebase Firestore
- Retrofit
- OkHttp
- Moshi
- Koin
- AndroidX DataStore

Flutter rebuild relevance:
- These dependencies define the current integration points, but they do not all need direct equivalents.
- The more important carry-over is the service boundary and data contract, not the library choice.

## 5. Feature-to-Technical Mapping

| feature | primary technical owners | migration-relevant logic | certainty |
| --- | --- | --- | --- |
| Authentication and account access | `AuthViewModel`; `UserAuthRepo`; Firebase Auth | auth state lifecycle, credential flows, password reset, profile bootstrap | confirmed |
| Onboarding and initial personalization | `PrefsViewModel`; `PrefsRepo`; `AuthViewModel.updateUsername` | onboarding completion rules, initial defaults, username persistence | confirmed |
| Journal home and entry browsing | `EntryViewModel`; `JournalRepo` | live entry observation, favorite toggle, delete behavior, entry selection handoff | confirmed |
| Journal writing and editing | `EntryViewModel`; `JournalRepo`; `TranslationApiRepo` | create/update entry rules, translated-content persistence, duration updates, entry-as-practice-container behavior | confirmed |
| Translation-assisted journaling | `TranslationViewModel`; `TranslationApiRepo`; `LanguageViewModel` | debounced translation, source/target preference usage, language loading and normalization; this appears to be the core language-practice loop rather than a peripheral helper | confirmed |
| Translation listening and review | text-to-speech layer; translated-entry review flow | spoken playback of translated entries | stakeholder-provided |
| Premium language support | native billing layer; entitlement checks; premium translation/AI service | plan purchase, entitlement state, feature gating, premium translation and AI enrichment | stakeholder-provided |
| Photo-to-entry capture | OCR/photo-import layer; entry creation flow | handwritten capture, OCR conversion, edit-before-save flow | stakeholder-provided |
| Statistics and streak tracking | `StatisticsViewModel`; `EntryViewModel`; `PorterStemmerEn` | derived streaks, totals, entry-day sets, word aggregation | confirmed |
| Settings and preference management | `PrefsViewModel`; `PrefsRepo`; `LanguageViewModel` | theme/language/template/username settings and persistence | confirmed |
| Profile and account maintenance | `AuthViewModel`; `UserAuthRepo` | logout, delete account, profile info lookup | confirmed |
| Reminder preferences | `PrefsRepoImpl`; reminder-related setting detail logic | serialized reminder settings only; execution path unclear | inferred |
| Visual theming and personalization | `PrefsViewModel`; `ColorManager`; theme definitions | theme selection and persisted theme state | confirmed |
| Writing prompts and inspiration | `PrefsViewModel`; template-related flows | template persistence and prompt-related configuration | inferred |
| App startup and session routing | `AppStart`; `AppNavGraph`; auth and prefs state | state-based initial routing rules | confirmed |

## 6. Technical Risks and Legacy Constraints

### Confirmed risks

- Translation dependency risk:
  translation is the core cross-feature dependency and appears central to the main product loop, but it currently depends on a separate external API.
- Premium scope risk:
  premium value depends on native billing, entitlement handling, and at least one premium-capability backend, none of which are represented in the legacy audit.
- OCR/photo import risk:
  handwritten capture introduces image handling, OCR accuracy, correction UX, and failure-state complexity not covered in the legacy code model.
- TTS platform parity risk:
  translated-entry playback may behave differently across Flutter targets unless explicitly normalized.
- Backend coupling risk:
  user and journal behavior are tightly coupled to Firebase Auth and Firestore semantics.
- Coverage gap risk:
  audit coverage shows strong documentation for Kotlin logic, but weaker coverage for Android resources, config, and some helper files.
- Reminder completeness risk:
  settings and stored config exist, but no fully verified execution/scheduling path was confirmed.
- Network workaround risk:
  the IPv4-only DNS override suggests prior network instability or backend compatibility issues.

### Legacy constraints relevant to Flutter

- Current startup behavior assumes synchronous routing decisions from combined auth + onboarding state.
- Statistics are derived live from entry streams, which may affect performance or data-fetch design in Flutter.
- Translation currently depends on user preference state for source and target languages, so translation is not a pure stateless utility.
- The current app uses optimistic updates for favorites, which implies UI state should not depend solely on remote write completion.

## 7. Likely Migration-Relevant Logic

These are the areas most likely to matter during a Flutter rebuild:

- Domain model migration:
  `JournalEntry`, `User`, language data, reminder config
- Repository contract migration:
  auth, journal, translation, preferences
- Startup and routing rules:
  auth state + onboarding state -> initial flow
- Translation behavior:
  debounced translation and language preference resolution as part of the main entry-writing flow
- Text-to-speech behavior:
  translated-entry playback rules, voice/language selection, and review-state UX
- Premium entitlement behavior:
  native purchase state, restore flow, gating logic, premium backend selection
- OCR/photo-entry behavior:
  image import, text extraction, review/correction, then entry creation
- Statistics derivation:
  streaks, totals, date grouping, top-word support
- Preference ownership:
  theme, language, template, username, onboarding flag
- Data ownership boundaries:
  what lives remotely versus locally

If rebuild scope is limited, the highest-value migration set is:
- auth
- journal CRUD
- translation
- preferences
- startup routing
- statistics

If rebuild scope is prioritized by core product value, the first engineering slice should be:
- authenticated user context
- entry creation and editing
- real-time translation in the entry flow
- translated-entry text-to-speech
- persisted source/target language preferences
- entry review with translated content

## 8. Areas That Appear Accidental, Duplicated, or Obsolete

### Likely accidental or over-specific

- `LanguageRepo` alongside `TranslationApiRepo`:
  both touch translation-service language retrieval. This may be a legitimate separation, but it may also be unnecessary duplication in a rebuild.
- IPv4-only DNS override:
  likely an environmental workaround rather than durable product logic.
- Settings drill-down fragmentation:
  technically represented as many detail surfaces; the rebuild may not need the same structural granularity.

### Likely partial or obsolete

- `ProfileSettingNotifications.kt`:
  present in the code surface but not wired into the documented screen flow.
- Reminder execution path:
  preference storage exists, but full runtime reminder behavior was not verified.
- Social/feedback-related settings affordances:
  present at the UI level but not validated as core product behavior.
- Placeholder tests:
  current test files appear to be boilerplate rather than meaningful system coverage.
- Legacy audit incompleteness for new scope:
  premium billing, AI enrichment, Echo identity behavior, and photo-to-entry are important current requirements but are not represented in the original legacy code audit.

## 9. Open Engineering Questions

- Should the Flutter rebuild preserve Firebase Auth and Firestore, or keep only the domain contracts and replace the backend?
- Is the translation API stable and reliable enough to remain a core dependency?
- What exact text-to-speech behavior should be cross-platform consistent:
  playback controls, language selection, caching, offline behavior?
- What is the entitlement model for premium:
  subscription, one-time upgrade, tiers, or region-specific plans?
- Which premium capabilities are truly distinct:
  stronger translation model, AI feedback, extra explanatory metadata, or combinations of these?
- What backend or model provider should power premium AI feedback and enhanced translation?
- How should photo-to-entry be implemented:
  on-device OCR, platform OCR, server OCR, or hybrid?
- What part of Echo identity is purely design-layer versus requiring explicit data or state in the app model?
- Does the IPv4-only DNS workaround still need to exist?
- What exact reminder behavior exists beyond stored preferences?
- Should language retrieval remain a separate abstraction from translation, or be consolidated?
- Are prompts/templates first-class data with their own model, or only lightweight preference values?
- What persistence guarantees are required for favorites, duration updates, and translated content edits?
- How large can the entry set grow before live-derived statistics become a performance concern?
- Which uncovered Android resources/config files contain rebuild-relevant behavior versus purely legacy presentation assets?

## 10. Summary

Confirmed:
- The current system is a repository-driven Kotlin/Compose app with Firebase-backed auth and journal storage, a separate translation service, `DataStore` preferences, and `StateFlow`-based state management.
- The most durable engineering assets are the domain models, repository boundaries, startup routing rules, real-time translation behavior in the entry flow, and derived statistics logic.

Uncertain:
- Reminder execution behavior, prompt/template depth, text-to-speech implementation detail, premium scope and backend design, photo-to-entry pipeline, Echo identity-state needs, and parts of the Android resource/config surface remain incompletely verified.

Flutter rebuild guidance:
- Preserve business logic and data contracts first, especially the translated-journaling loop.
- Recreate state, storage, and navigation patterns in Flutter-native forms rather than porting Kotlin/Android mechanics directly.

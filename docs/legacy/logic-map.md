# Logic Map

Source basis: [file-manifest.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/file-manifest.md), [screen-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/screen-inventory.md), and [feature-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/feature-inventory.md).

Purpose: map the non-UI logic surface of the app and connect it to product features where possible.

## 1. Models

| item | type | role | linked features | certainty |
| --- | --- | --- | --- | --- |
| `JournalEntry` | domain/data model | Core journal record containing content, translated content, source/target languages, duration, favorite flag, and Firestore timestamps. | Journal home and entry browsing; Journal writing and editing; Translation-assisted journaling; Statistics and streak tracking | confirmed |
| `User` | domain/data model | Authenticated user profile with email, username, preferred language, and created-at date. | Authentication and account access; Onboarding and initial personalization; Profile and account maintenance | confirmed |
| `TranslateRequest` | API request DTO | Request payload sent to the translation backend. | Translation-assisted journaling | confirmed |
| `TranslateResponse` | API response DTO | Response payload containing translated text from the translation backend. | Translation-assisted journaling | confirmed |
| `LanguageDto` | API/domain DTO | Language descriptor returned by the translation backend and localized for UI selection. | Translation-assisted journaling; Settings and preference management; Onboarding and initial personalization | confirmed |
| `ReminderItem` | local/support model | Reminder-related local model referenced by reminder settings. Exact runtime usage is not fully verified. | Reminder preferences | inferred |
| `ReminderConfig` | storage serialization model | Preference-layer model serialized to JSON for reminder settings storage. | Reminder preferences | confirmed |

## 2. Services and Repository Layer

| item | type | role | linked features | certainty |
| --- | --- | --- | --- | --- |
| `UserAuthRepo` / `UserAuthRepoImpl` | service/repository | Wrap Firebase Authentication plus Firestore user profile management. Handles sign-up, sign-in, Google One Tap, current user lookup, profile updates, account deletion, and loading user journal entries. | Authentication and account access; Onboarding and initial personalization; Profile and account maintenance; App startup and session routing | confirmed |
| `JournalRepo` / `JournalRepoImpl` | service/repository | Wrap Firestore journal entry CRUD and live observation under `users/{userId}/journalEntries`. | Journal home and entry browsing; Journal writing and editing; Statistics and streak tracking | confirmed |
| `TranslationApiRepo` / `TranslationApiRepoImpl` | service/repository | Wrap translation backend access for text translation and supported-language retrieval. | Translation-assisted journaling | confirmed |
| `LanguageRepo` / `LanguageRepoImpl` | service/repository | Thin language-data wrapper around the translation backend. Provides language lists as a Flow. | Translation-assisted journaling; Settings and preference management; Onboarding and initial personalization | confirmed |
| `PrefsRepo` / `PrefsRepoImpl` | service/repository | Wrap local preference persistence for onboarding, theme, target/source language, username, template, reminder JSON, and last-congrats date. | Onboarding and initial personalization; Settings and preference management; Reminder preferences; Visual theming and personalization; Writing prompts and inspiration; App startup and session routing | confirmed |

## 3. API Layer

| item | type | role | linked features | certainty |
| --- | --- | --- | --- | --- |
| `LibreTranslateService` | Retrofit API interface | Declares `POST /translate` and `GET /languages` against the translation service. | Translation-assisted journaling | confirmed |
| `BASE_URL = https://libre4echo.de/` | API configuration | Translation backend base URL used by Retrofit. | Translation-assisted journaling | confirmed |
| Retrofit + Moshi stack in `AppModule` | API infrastructure | Creates the translation client, JSON serialization, and DTO mapping. | Translation-assisted journaling | confirmed |
| IPv4-only DNS override in `AppModule` | network infrastructure | Custom OkHttp DNS filtering to IPv4 addresses only before API calls. | Translation-assisted journaling | confirmed |
| `FirebaseAuth` | platform/backend service | Authentication backend used by auth repository and password reset flow. | Authentication and account access; Profile and account maintenance; App startup and session routing | confirmed |
| `FirebaseFirestore` | platform/backend service | Persistent cloud store for user profiles and journal entries. | Authentication and account access; Journal home and entry browsing; Journal writing and editing; Statistics and streak tracking; Profile and account maintenance | confirmed |

## 4. Storage

| item | storage type | stored data | linked features | certainty |
| --- | --- | --- | --- | --- |
| Firestore `users/{uid}` documents | remote/cloud storage | User email, username, preferred language, created-at metadata. | Authentication and account access; Onboarding and initial personalization; Profile and account maintenance | confirmed |
| Firestore `users/{uid}/journalEntries` subcollection | remote/cloud storage | Journal entries with translated text, language metadata, favorite flag, duration, and timestamps. | Journal home and entry browsing; Journal writing and editing; Translation-assisted journaling; Statistics and streak tracking | confirmed |
| `Context.dataStore` via `preferencesDataStore` | local storage | Preference-backed key/value state for onboarding, theme, language, source language, username, template, reminder JSON, and last-congrats date. | Onboarding and initial personalization; Settings and preference management; Reminder preferences; Visual theming and personalization; Writing prompts and inspiration; App startup and session routing | confirmed |
| Moshi-serialized reminder JSON | local storage encoding | `Map<String, ReminderConfig>` stored under `key_reminders_json`. Read/write helper methods exist, but full feature usage was not fully traced. | Reminder preferences | inferred |
| In-memory `StateFlow` and `MutableStateFlow` state | transient runtime state | Screen/form fields, auth/session state, entry lists, translation output, onboarding status, loading/error flags. | Most features | confirmed |

## 5. State Management

The app uses view-model-centered state with Koin injection and `StateFlow`-based observation.

| item | state responsibility | primary dependencies | linked features | certainty |
| --- | --- | --- | --- | --- |
| `AuthViewModel` | Holds auth form inputs, current user, loading/error state, password reset flow, Google sign-in flow, and account deletion/logout actions. Also syncs username into preferences. | `UserAuthRepo`; `Context`; `PrefsViewModel` | Authentication and account access; Onboarding and initial personalization; Profile and account maintenance; App startup and session routing | confirmed |
| `EntryViewModel` | Observes current-user entries, creates translated entries, updates/deletes entries, toggles favorites optimistically, tracks create/update/delete results, and exposes streak-extension flags. | `AuthViewModel`; `JournalRepo`; `TranslationApiRepo` | Journal home and entry browsing; Journal writing and editing; Translation-assisted journaling; Statistics and streak tracking | confirmed |
| `TranslationViewModel` | Debounces text input, translates against current source/target language preferences, exposes live translated text, and supports one-off translation calls. | `TranslationApiRepo`; `PrefsViewModel` | Translation-assisted journaling | confirmed |
| `LanguageViewModel` | Loads supported languages and localizes display names based on device locale and language-code mapping rules. | `TranslationApiRepo` | Translation-assisted journaling; Settings and preference management; Onboarding and initial personalization | confirmed |
| `PrefsViewModel` | Exposes current theme, language, source language, username, template, onboarding state, and loading state; writes preference updates to local storage. | `PrefsRepo`; `Context` | Onboarding and initial personalization; Settings and preference management; Reminder preferences; Visual theming and personalization; Writing prompts and inspiration; App startup and session routing | confirmed |
| `StatisticsViewModel` | Derives statistics from the entry stream: unique entry days, total words, total duration, current visible streak, and streak message resource. | `EntryViewModel` | Statistics and streak tracking | confirmed |

### State flow shape

- Startup/session state:
  `AuthViewModel.user/loading` + `PrefsViewModel.onboarded/loading` drive `AppStart` and `AppNavGraph` routing.
- Entry state:
  `EntryViewModel.entries` is derived from the authenticated user ID and Firestore observation.
- Preference state:
  `PrefsViewModel` mirrors `PrefsRepo` flows into eagerly shared screen-consumable state.
- Derived analytics state:
  `StatisticsViewModel` is pure derivation over entry state rather than its own persistence layer.

## 6. Shared Logic and Cross-Cutting Utilities

| item | role | linked features | certainty |
| --- | --- | --- | --- |
| `LanguageUtil` | Maps LibreTranslate language codes to Android-compatible BCP-47 tags and locales for display/localization. | Translation-assisted journaling; Settings and preference management; Onboarding and initial personalization | confirmed |
| `DateUtil.formatDate` | Formats Firestore timestamps for locale-aware display. | Journal home and entry browsing; Journal writing and editing; Profile and account maintenance | confirmed |
| `PorterStemmerEn` | Performs English stemming for text normalization used by top-word statistics. | Statistics and streak tracking | confirmed |
| `ColorManager` | Resolves active accent/color choices from theme names. | Visual theming and personalization; App startup and session routing | confirmed |
| `Theme.kt`, `Color.kt`, `Type.kt` | Shared theming definitions consumed across screens. These are UI-adjacent but central shared logic/configuration for runtime appearance. | Visual theming and personalization | confirmed |
| `NavDestination`, `NavExtensions`, `AppNavGraph` | Central route definitions and navigation helpers tying feature flows together. | App startup and session routing; Authentication and account access; Onboarding and initial personalization; Journal home and entry browsing; Settings and preference management; Statistics and streak tracking | confirmed |
| `AppModule` + `App` | Dependency injection bootstrap and service graph assembly. | All features | confirmed |

## 7. Feature-to-Logic Crosswalk

| feature | primary logic owners | supporting logic |
| --- | --- | --- |
| Authentication and account access | `AuthViewModel`; `UserAuthRepo` / `UserAuthRepoImpl`; `FirebaseAuth` | `User`; `AppStart`; `AppNavGraph`; `PrefsViewModel` |
| Onboarding and initial personalization | `PrefsViewModel`; `PrefsRepo` / `PrefsRepoImpl`; `AuthViewModel.updateUsername` | `LanguageViewModel`; `LanguageUtil`; theme infrastructure |
| Journal home and entry browsing | `EntryViewModel`; `JournalRepo` / `JournalRepoImpl` | `JournalEntry`; `DateUtil`; navigation saved-state handoff |
| Journal writing and editing | `EntryViewModel`; `JournalRepo` / `JournalRepoImpl` | `JournalEntry`; `TranslationViewModel`; `TranslationApiRepo` |
| Translation-assisted journaling | `TranslationViewModel`; `TranslationApiRepo` / `TranslationApiRepoImpl`; `LibreTranslateService` | `LanguageViewModel`; `LanguageRepo`; `LanguageDto`; `LanguageUtil`; preference-backed language settings |
| Statistics and streak tracking | `StatisticsViewModel`; `EntryViewModel` | `JournalEntry`; `PorterStemmerEn`; last-congrats preference key |
| Settings and preference management | `PrefsViewModel`; `PrefsRepo` / `PrefsRepoImpl` | `AuthViewModel`; `LanguageViewModel`; theme infrastructure |
| Profile and account maintenance | `AuthViewModel`; `UserAuthRepo` / `UserAuthRepoImpl` | `User`; Firestore user document |
| Reminder preferences | `PrefsRepoImpl`; reminder-related settings components | `ReminderItem`; `ReminderConfig`; manifest permissions; scheduler path not yet verified |
| Visual theming and personalization | `PrefsViewModel`; `PrefsRepoImpl`; `ColorManager`; theme definitions | `AppStart`; onboarding and settings flows |
| Writing prompts and inspiration | `PrefsViewModel`; `PrefsRepoImpl` | template-related onboarding/add-entry flows; feature depth partially inferred |
| App startup and session routing | `AppStart`; `AppNavGraph`; `AuthViewModel`; `PrefsViewModel` | `NavDestination`; DI bootstrap in `App` / `AppModule` |

## 8. Notes

- The app’s main architectural center is the combination of `ViewModel` + repository abstractions + `StateFlow`.
- Remote persistence is split between Firebase Auth/Firestore and a separate translation API backend.
- Local persistence is lightweight and preference-based; no Room database or file-based domain storage was identified.
- Reminder logic appears only partially implemented from the inspected files. Storage and UI affordances exist, but a fully verified scheduling/execution layer was not found in this pass.

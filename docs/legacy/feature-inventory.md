# Feature Inventory

Source basis: [file-manifest.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/file-manifest.md) and [screen-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/screen-inventory.md).

Purpose: legacy product analysis of the current feature surface. This document groups behavior by product capability, not by package or file layout.

## Status Legend

- `confirmed`: directly supported by the current screen and logic inventory.
- `inferred`: strongly suggested by the available files and behavior, but not fully verified end to end.
- `open question`: a gap, ambiguity, or unverified implementation detail that should be checked before reuse or rebuild.
- `rebuild recommendation`: a legacy-analysis note about what should be explicitly re-specified if the feature is rebuilt.

## Features

| feature | user purpose | related screens | key actions | involved logic | status |
| --- | --- | --- | --- | --- | --- |
| Authentication and account access | Let users create an account, sign in, recover access, and enter the app securely. | `Splash screen`; `SignInScreen`; `SignUpScreen` | Sign in with email/password; sign in with Google One Tap; sign up; switch between sign-in and sign-up; send password reset email; route authenticated users into the app. | `AuthViewModel`; `UserAuthRepo` and `UserAuthRepoImpl`; auth state observed in `AppStart` and `AppNavGraph`; `SignInWithGoogle`; Firebase-backed auth and user loading. | confirmed |
| Onboarding and initial personalization | Help first-time users configure identity and app defaults before using the journal. | `OnboardingFlow` | Progress through welcome, username, language, theme, and template steps; save onboarding completion state. | `OnboardingFlow` local step state; `AuthViewModel.updateUsername`; `PrefsViewModel.setLanguage`; `PrefsViewModel.setTheme`; `PrefsViewModel.setTemplate`; `PrefsViewModel.setOnboarded`; startup routing in `AppNavGraph` based on `onboarded`. | confirmed |
| Journal home and entry browsing | Give users a home screen for browsing, filtering, opening, favoriting, and deleting journal entries. | `EntryListScreen`; `EntryDetailScreen` | View the entry list; open entry details; toggle favorites; delete entries; open inspiration popover; launch add-entry, settings, and statistics flows. | `EntryViewModel`; `JournalRepo` and `JournalRepoImpl`; `EntryRow`; `EntryListTopBar`; `EntryListBottomBar`; `InspirationPopover`; saved-state handoff of `JournalEntry` into the detail screen; entry deletion and list refresh. | confirmed |
| Journal writing and editing | Let users create new entries and update existing entries. | `AddEntryScreen`; `EntryDetailScreen` | Write journal content; save a new entry; edit an existing entry; discard unsaved changes; persist translated content with the entry; add duration/time-spent metadata. | `EntryViewModel.create/update/delete` flow; `JournalEntry` model; `JournalRepo` and `JournalRepoImpl`; `CustomTextEditor`; `EntrySection`; save/dismiss handling in `AddEntryScreen` and `EntryDetailScreen`. | confirmed |
| Translation-assisted journaling | Help users translate journal text and work with bilingual entry content while writing or reviewing an entry. | `AddEntryScreen`; `EntryDetailScreen`; `OnboardingFlow`; `SettingDetailScreen` | Enter text and receive translation; edit translated content; choose source/target language; swap writing/translation sections; reuse current language preferences. | `TranslationViewModel`; `TranslationApiRepo` and `TranslationApiRepoImpl`; `Libre4EchoApi`; `TranslateRequest`; `TranslateResponse`; `LanguageRepo`; `LanguageViewModel`; `LanguageDto`; `TranslationSection`; `SwapDivider`; preference-backed language state in `PrefsRepoImpl`. | confirmed |
| Statistics and streak tracking | Show users progress and patterns in their journaling history. | `EntryListScreen`; `StatisticsScreen` | View total entries; inspect streaks; see calendar activity; review top words; surface streak celebration UI from the home screen. | `StatisticsViewModel`; `TotalEntriesStatistic`; `StreakStatistic`; `CalendarView`; `TopWordsStatistic`; `PorterStemmerEn`; `StatisticsHeader`; `CongratsDialog`; `AnimatedFlame`; streak and text-analysis logic derived from journal entries. | confirmed |
| Settings and preference management | Let users manage ongoing app preferences after onboarding. | `SettingsScreen`; `SettingDetailScreen` | Open settings overview; change username; change target language; change theme; change writing template; navigate through settings subsections. | `SettingType`; `ProfileSettingsCard`; `AppSettingsCard`; `SettingItem`; `ProfileSettingUsername`; `ProfileSettingLanguage`; `ProfileSettingTheme`; `ProfileSettingTemplate`; `PrefsViewModel`; `PrefsRepo` and `PrefsRepoImpl`; `LanguageViewModel`. | confirmed |
| Profile and account maintenance | Let users inspect account information and perform destructive account actions from settings. | `SettingsScreen`; `SettingDetailScreen` | View profile info; log out; delete profile/account. | `ProfileSettingInfo`; `AuthViewModel.signOut`; `AuthViewModel.deleteProfile`; `UserAuthRepo.deleteUser`; settings navigation callbacks in `AppNavGraph` back to `AuthRootRoute`. | confirmed |
| Reminder preferences | Likely let users configure reminder timing and scheduling for journaling. | `SettingDetailScreen` | Open reminder settings; choose time; choose weekday; persist reminder configuration. | `ProfileSettingReminder`; `TimeSelector`; `WeekdayDropdown`; `ReminderItem`; `PrefsRepoImpl` stores reminder JSON via `ReminderConfig`; Android manifest includes notification, boot completed, and exact alarm permissions. No full reminder execution path was verified in this pass. | inferred |
| Visual theming and personalization | Let users personalize the app look and apply that choice across startup and onboarding surfaces. | `Splash screen`; `OnboardingFlow`; `SettingsScreen`; `SettingDetailScreen` | Pick a theme during onboarding; change it later in settings; apply the active color/theme to splash and other UI. | `ColorManager`; `Theme.kt`; `Color.kt`; `Type.kt`; `PrefsViewModel.theme`; `PrefsRepoImpl.setTheme`; theme usage in `AppStart`, `OnboardingFlow`, and settings detail surfaces. | confirmed |
| Writing prompts and inspiration | Provide lightweight inspiration for starting or continuing a journal entry. | `EntryListScreen`; `AddEntryScreen` | Open inspiration popover; view template/instruction content; select a template for writing. | `InspirationPopover`; `TemplateStep`; `TemplatePickerOnboarding`; template state in `PrefsViewModel` and `PrefsRepoImpl`; instruction dialog/template selection in `AddEntryScreen`. | inferred |
| App startup and session routing | Ensure users land in the correct flow based on auth and onboarding state. | `Splash screen`; `SignInScreen`; `OnboardingFlow`; `EntryListScreen` | Show splash during loading; route unauthenticated users to auth; route incomplete users to onboarding; route ready users to the main flow. | `AppStart`; `AppNavGraph`; `NavDestination`; `AuthViewModel.user/loading`; `PrefsViewModel.onboarded/loading`; initial `navigate(...)` decisions with `popUpTo`. | confirmed |

## Open Questions

- `Reminder preferences`: the settings UI and persisted reminder config are present, but this pass did not confirm a scheduler, worker, or broadcast receiver implementation.
- `Writing prompts and inspiration`: the UI surfaces and template state are present, but the full product behavior behind prompts and templates is only partially explicit in the inspected files.
- Social and feedback affordances exist in settings-related files, but their product depth was not validated enough here to elevate them to a standalone feature.

## Rebuild Recommendations

- Preserve the current feature boundaries during rebuild discovery: auth, onboarding, journal CRUD, translation, statistics, settings, profile maintenance, reminders, theming, prompts, and startup routing.
- Re-specify inferred features explicitly before implementation. In particular:
  reminder scheduling and delivery behavior
  prompt/template behavior and content model
  the depth of social and feedback flows
- Keep onboarding defaults and settings ownership aligned. Current evidence suggests language, theme, template, and username are shared between first-run setup and ongoing settings.
- Treat translation as a first-class product capability rather than a cosmetic add-on. The current app links translation to writing, review, onboarding, and settings.
- Rebuild statistics as derived behavior over entry data, not as a separate persistence model. The current implementation derives streaks, totals, calendar activity, and top words from journal entries.

## Notes

- This is a legacy analysis document, not target-state product documentation.
- `confirmed` does not imply polished or complete UX. It only means the feature is directly supported by the currently inspected screens and logic.
- `inferred` means the feature probably exists in the legacy product surface, but the current evidence does not fully verify end-to-end behavior.

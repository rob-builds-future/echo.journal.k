# File Manifest

Repository: `echo.journal.k`  
Generated: `2026-04-01`  
Method: filesystem inventory plus filename/class-name inspection.  
Uncertain entries are marked `yes` when the filename alone does not fully establish intent.

## 1. Relevant Production Source Files

### App Entry / DI / App Shell

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/MainActivity.kt` | Android activity entrypoint hosting the Compose app. | app shell | no |
| `app/src/main/java/com/rbf/echojournal/AppStart.kt` | Top-level Compose startup flow with splash and initial routing decisions. | app shell / startup | no |
| `app/src/main/java/com/rbf/echojournal/di/App.kt` | Custom `Application` class for app-wide initialization. | dependency injection / app lifecycle | no |
| `app/src/main/java/com/rbf/echojournal/di/AppModule.kt` | Koin or similar dependency wiring for repositories and view models. | dependency injection | no |

### Navigation

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/navigation/AppNavGraph.kt` | Compose navigation graph connecting auth, onboarding, and main flows. | navigation | no |
| `app/src/main/java/com/rbf/echojournal/navigation/NavDestination.kt` | Typed route definitions for app destinations. | navigation | no |
| `app/src/main/java/com/rbf/echojournal/navigation/NavExtensions.kt` | Navigation helper extensions around `NavController`. | navigation | no |

### Data Models / DTOs

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/data/local/ReminderItem.kt` | Local reminder model used by settings/reminder features. | reminders / local state | no |
| `app/src/main/java/com/rbf/echojournal/data/remote/model/JournalEntry.kt` | Journal entry data model exchanged across layers. | journal entries | no |
| `app/src/main/java/com/rbf/echojournal/data/remote/model/TranslateRequest.kt` | Request DTO for translation API calls. | translation / networking | no |
| `app/src/main/java/com/rbf/echojournal/data/remote/model/TranslateResponse.kt` | Response DTO for translation API calls. | translation / networking | no |
| `app/src/main/java/com/rbf/echojournal/data/remote/model/User.kt` | User/account data model. | auth / user profile | no |
| `app/src/main/java/com/rbf/echojournal/data/remote/model/util/LanguageDto.kt` | Language descriptor DTO. | language selection / translation | no |

### Networking / Remote Services

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/data/remote/Libre4EchoApi.kt` | Retrofit-style remote service definition for LibreTranslate integration. | translation / networking | no |

### Repository Interfaces and Implementations

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/data/repository/JournalRepo.kt` | Repository contract for journal entry operations. | journal storage / data access | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/JournalRepoImpl.kt` | Repository implementation for journal entry persistence/retrieval. | journal storage / data access | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/LanguageRepo.kt` | Repository contract for language list or language lookup. | languages / translation | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/LanguageRepoImpl.kt` | Repository implementation for language data. | languages / translation | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/PrefsRepo.kt` | Repository contract for persisted user preferences. | settings / local storage | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/PrefsRepoImpl.kt` | DataStore-backed preference repository for onboarding, theme, language, reminders, and template state. | settings / local storage | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/TranslationApiRepo.kt` | Repository contract wrapping translation API usage. | translation / networking | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/TranslationApiRepoImpl.kt` | Translation repository implementation delegating to remote API. | translation / networking | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/UserAuthRepo.kt` | Repository contract for sign-in/sign-up/auth state. | authentication | no |
| `app/src/main/java/com/rbf/echojournal/data/repository/UserAuthRepoImpl.kt` | Authentication repository implementation. | authentication | no |

### View Models

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/viewModel/AuthViewModel.kt` | Auth state and auth flow orchestration. | authentication | no |
| `app/src/main/java/com/rbf/echojournal/ui/viewModel/EntryViewModel.kt` | Entry CRUD and editor/list state. | journal entries | no |
| `app/src/main/java/com/rbf/echojournal/ui/viewModel/LanguageViewModel.kt` | Language selection and language data state. | languages / translation | no |
| `app/src/main/java/com/rbf/echojournal/ui/viewModel/PrefsViewModel.kt` | User preference and onboarding settings state. | settings / onboarding | no |
| `app/src/main/java/com/rbf/echojournal/ui/viewModel/StatisticsViewModel.kt` | Aggregated journal statistics state. | statistics | no |
| `app/src/main/java/com/rbf/echojournal/ui/viewModel/TranslationViewModel.kt` | Translation request/response state management. | translation | no |

### UI Screens

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/screens/authflow/SignInScreen.kt` | Sign-in screen UI. | authentication | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/authflow/SignUpScreen.kt` | Sign-up screen UI. | authentication | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/onboardingflow/OnboardingFlow.kt` | Multi-step onboarding flow container and step routing. | onboarding | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/mainflow/AddEntryScreen.kt` | Screen for creating/editing a journal entry and translation content. | journal editor / translation | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/mainflow/EntryDetailScreen.kt` | Detailed view of a single journal entry. | journal entries | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/mainflow/EntryListScreen.kt` | Main journal entry list/home screen. | journal entries | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/mainflow/SettingDetailScreen.kt` | Detail host for a selected settings item. | settings | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/mainflow/SettingsScreen.kt` | Top-level settings screen. | settings | no |
| `app/src/main/java/com/rbf/echojournal/ui/screens/mainflow/StatisticsScreen.kt` | Statistics dashboard screen. | statistics | no |

### UI Components: Auth Flow

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/components/authflow/AnimatedEchoSymbol.kt` | Animated logo/symbol composables for auth/splash surfaces. | branding / auth UI | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/authflow/EchoSplashScreen.kt` | Splash screen composable. | startup / auth UI | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/authflow/EchoSymbol.kt` | Static logo/symbol composables. | branding / auth UI | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/authflow/SignInWithGoogle.kt` | Google sign-in button/composable integration. | authentication | no |

### UI Components: Onboarding Flow

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/BottomBarButton.kt` | Reusable onboarding bottom action button. | onboarding UI | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/LanguagePickerListOnboarding.kt` | Language picker list tailored to onboarding flow. | onboarding / language | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/LanguageStep.kt` | Onboarding step for language selection. | onboarding / language | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/StepIndicator.kt` | Visual indicator for onboarding progress. | onboarding UI | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/StepLabels.kt` | Labels/headings for onboarding steps. | onboarding UI | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/TemplatePickerOnboarding.kt` | Template selection widget used during onboarding. | onboarding / templates | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/TemplateStep.kt` | Onboarding step for prompt/template choice. | onboarding / templates | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/ThemePickerOnboarding.kt` | Theme selection widget used during onboarding. | onboarding / theming | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/ThemeStep.kt` | Onboarding step for theme choice. | onboarding / theming | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/UsernameStep.kt` | Onboarding step for username entry. | onboarding / profile | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/onboardingflow/WelcomeStep.kt` | Introductory onboarding step. | onboarding | no |

### UI Components: Main Flow / Add Entry

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/addEntryScreen/CombinedRowUnderEntry.kt` | Composite action/info row below the editor area. | journal editor | yes |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/addEntryScreen/CustomTextEditor.kt` | Custom text editor composable for entry content. | journal editor | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/addEntryScreen/EntrySection.kt` | Entry text section in add-entry UI. | journal editor | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/addEntryScreen/SwapDivider.kt` | Divider/button for swapping sections or languages. | journal editor / translation | yes |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/addEntryScreen/TranslationSection.kt` | Translation result/input section in add-entry UI. | translation / journal editor | no |

### UI Components: Main Flow / Entry List

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/AnimatedFlame.kt` | Animated streak/flame visual. | journal streaks / gamification | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/CongratsDialog.kt` | Congratulatory dialog, likely for streak or milestone events. | journal streaks / gamification | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/EntryListBottomBar.kt` | Bottom navigation/actions for entry list screen. | journal entries / navigation | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/EntryListTopBar.kt` | Top app bar for entry list screen. | journal entries | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/EntryRow.kt` | Row item representing a journal entry in a list. | journal entries | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/InspirationPopover.kt` | Popover showing writing inspiration or prompts. | journal prompts | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/ShadowCard.kt` | Reusable stylized card wrapper. | shared UI / journal list | yes |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/entryListScreen/StatisticsHeader.kt` | Summary header for entry/stats information on the list screen. | journal entries / statistics | no |

### UI Components: Main Flow / Settings

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/AppSettingsCard.kt` | Card grouping application-level settings. | settings | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/FeedbackCard.kt` | Card for feedback/contact actions. | settings / feedback | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/InstaButton.kt` | Instagram link/action button. | settings / social | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/ProfileSettingsCard.kt` | Card grouping profile-related settings. | settings / profile | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/SettingItem.kt` | Reusable settings row item. | settings UI | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/SettingType.kt` | Enum for supported settings detail types. | settings navigation | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/LanguagePickerList.kt` | Settings-specific language picker list. | settings / language | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/ProfileSettingInfo.kt` | Detail content for profile info settings. | settings / profile | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/ProfileSettingLanguage.kt` | Detail content for language settings. | settings / language | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/ProfileSettingNotifications.kt` | Notification settings detail surface or placeholder class. | settings / notifications | yes |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/ProfileSettingReminder.kt` | Detail content for reminder settings. | settings / reminders | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/ProfileSettingTemplate.kt` | Detail content for template settings. | settings / templates | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/ProfileSettingTheme.kt` | Detail content for theme settings. | settings / theming | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/ProfileSettingUsername.kt` | Detail content for username settings. | settings / profile | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/TimeSelector.kt` | Time picker/select UI for reminders or notifications. | settings / reminders | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/settingsScreen/settingDetailScreens/WeekdayDropdown.kt` | Weekday picker/dropdown for reminder scheduling. | settings / reminders | no |

### UI Components: Main Flow / Statistics

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/statisticsScreen/CalendarView.kt` | Calendar visualization for journal activity. | statistics | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/statisticsScreen/StreakStatistic.kt` | Streak metric widget. | statistics / streaks | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/statisticsScreen/TopWordsStatistic.kt` | Top-words statistic visualization. | statistics / text analysis | no |
| `app/src/main/java/com/rbf/echojournal/ui/components/mainflow/statisticsScreen/TotalEntriesStatistic.kt` | Total-entries statistic widget. | statistics | no |

### Theme / Styling Utilities

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/ui/theme/Color.kt` | Compose color definitions. | theming | no |
| `app/src/main/java/com/rbf/echojournal/ui/theme/ColorManager.kt` | Runtime color/theme selection helper. | theming | no |
| `app/src/main/java/com/rbf/echojournal/ui/theme/Theme.kt` | Main Compose theme wrapper and font style options. | theming | no |
| `app/src/main/java/com/rbf/echojournal/ui/theme/Type.kt` | Typography definitions. | theming | no |

### General Utilities

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/java/com/rbf/echojournal/util/CustomCornerShape.kt` | Helper for custom shape creation in Compose UI. | shared UI utility | no |
| `app/src/main/java/com/rbf/echojournal/util/DateUtil.kt` | Date formatting/manipulation helpers. | shared utility / journal dates | no |
| `app/src/main/java/com/rbf/echojournal/util/LanguageUtil.kt` | Language-related helper functions or mappings. | language / translation | no |
| `app/src/main/java/com/rbf/echojournal/util/PorterStemmerEn.kt` | English Porter stemmer implementation for word analysis. | statistics / text analysis | no |

## 2. Supporting App Resources and Config

These are production-facing resources/configuration files, but not part of the requested UI/screen/model/service/navigation Kotlin inventory.

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/main/AndroidManifest.xml` | Android app manifest, permissions, launcher activity, app class. | Android config | no |
| `app/src/main/res/values/strings.xml` | Default localized strings. | localization | no |
| `app/src/main/res/values-de/strings.xml` | German localized strings. | localization | no |
| `app/src/main/res/values-pt-rBR/strings.xml` | Brazilian Portuguese localized strings. | localization | no |
| `app/src/main/res/values/colors.xml` | Default color resources. | theming / Android resources | no |
| `app/src/main/res/values-night/colors.xml` | Night-mode color resources. | theming / Android resources | no |
| `app/src/main/res/values/themes.xml` | Default Android themes. | theming / Android resources | no |
| `app/src/main/res/values-night/themes.xml` | Night-mode Android themes. | theming / Android resources | no |
| `app/src/main/res/values/styles.xml` | Additional style declarations. | Android styles | yes |
| `app/src/main/res/values/ic_launcher_background.xml` | Launcher icon background resource. | app icon resources | no |
| `app/src/main/res/xml/backup_rules.xml` | Backup behavior configuration. | Android platform config | no |
| `app/src/main/res/xml/data_extraction_rules.xml` | Data extraction/restore configuration. | Android platform config | no |
| `app/src/main/res/font/manrope_bold.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/font/manrope_extrabold.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/font/manrope_extralight.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/font/manrope_light.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/font/manrope_medium.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/font/manrope_regular.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/font/manrope_semibold.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/font/varelaround_regular.ttf` | Bundled font asset. | typography asset | no |
| `app/src/main/res/drawable/background.jpeg` | Background image asset. | UI art asset | no |
| `app/src/main/res/drawable/background2.png` | Secondary background image asset. | UI art asset | yes |
| `app/src/main/res/drawable/ic_launcher_foreground.png` | Launcher foreground asset. | app icon resources | no |
| `app/src/main/res/drawable-night/ic_launcher_foreground.png` | Night-mode launcher foreground asset. | app icon resources | no |
| `app/src/main/res/drawable-xxhdpi/android_dark_sq_ctn_3x.png` | Density-specific graphic asset. | branding / store asset | yes |
| `app/src/main/res/drawable-xxhdpi/android_light_sq_ctn_3x.png` | Density-specific graphic asset. | branding / store asset | yes |
| `app/src/main/res/drawable-xxhdpi/android_neutral_sq_ctn_3x.png` | Density-specific graphic asset. | branding / store asset | yes |
| `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` | Adaptive launcher icon definition. | app icon resources | no |
| `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml` | Adaptive round launcher icon definition. | app icon resources | no |
| `app/src/main/res/mipmap-mdpi/ic_launcher.webp` | Launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-mdpi/ic_launcher_foreground.webp` | Launcher foreground asset. | app icon resources | no |
| `app/src/main/res/mipmap-mdpi/ic_launcher_round.webp` | Round launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-hdpi/ic_launcher.webp` | Launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-hdpi/ic_launcher_foreground.webp` | Launcher foreground asset. | app icon resources | no |
| `app/src/main/res/mipmap-hdpi/ic_launcher_round.webp` | Round launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-xhdpi/ic_launcher.webp` | Launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-xhdpi/ic_launcher_foreground.webp` | Launcher foreground asset. | app icon resources | no |
| `app/src/main/res/mipmap-xhdpi/ic_launcher_round.webp` | Round launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-xxhdpi/ic_launcher.webp` | Launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-xxhdpi/ic_launcher_foreground.webp` | Launcher foreground asset. | app icon resources | no |
| `app/src/main/res/mipmap-xxhdpi/ic_launcher_round.webp` | Round launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-xxxhdpi/ic_launcher.webp` | Launcher icon asset. | app icon resources | no |
| `app/src/main/res/mipmap-xxxhdpi/ic_launcher_foreground.webp` | Launcher foreground asset. | app icon resources | no |
| `app/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp` | Round launcher icon asset. | app icon resources | no |
| `app/src/main/ic_launcher-playstore.png` | Play Store icon asset. | release branding | no |
| `app/google-services.json` | Firebase/Google services configuration. | backend config | no |

## 3. Non-Production but Relevant Development Files

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `app/src/test/java/com/rbf/echojournal/ExampleUnitTest.kt` | Placeholder local unit test. | tests | no |
| `app/src/androidTest/java/com/rbf/echojournal/ExampleInstrumentedTest.kt` | Placeholder instrumented Android test. | tests | no |
| `build.gradle.kts` | Root Gradle build definition. | build tooling | no |
| `settings.gradle.kts` | Gradle project/module settings. | build tooling | no |
| `gradle.properties` | Gradle properties and build flags. | build tooling | no |
| `gradle/libs.versions.toml` | Centralized dependency/version catalog. | build tooling | no |
| `gradle/wrapper/gradle-wrapper.properties` | Gradle wrapper configuration. | build tooling | no |
| `gradle/wrapper/gradle-wrapper.jar` | Gradle wrapper binary. | build tooling | no |
| `gradlew` | Unix Gradle wrapper script. | build tooling | no |
| `gradlew.bat` | Windows Gradle wrapper script. | build tooling | no |
| `app/build.gradle.kts` | App module build definition. | Android build tooling | no |
| `app/proguard-rules.pro` | Release shrinking/obfuscation rules. | Android build tooling | no |

## 4. Generated / Build / IDE Artifacts

These were present in the repository tree but are not production source and should be excluded from legacy code interpretation.

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `.gradle/` | Local Gradle cache/state directory, including versioned execution metadata. | generated build artifacts | no |
| `.kotlin/` | Local Kotlin tooling/session state. | generated IDE/build artifacts | no |
| `.idea/` | IDE project metadata and caches. | IDE artifacts | no |
| `app/build/` | Generated module build outputs, intermediates, Crashlytics metadata, and compiled artifacts. | generated build artifacts | no |
| `app/release/` | Release output directory or export staging area. | generated/release artifacts | yes |

## 5. Repository Collateral / Irrelevant to Production Code Inventory

These files are in-repo but outside the requested production source surface.

| path | short description | probable feature/domain | uncertain |
| --- | --- | --- | --- |
| `readme.md` | Project readme/documentation. | repository docs | no |
| `docs/legacy/file-manifest.md` | This inventory document. | repository docs | no |
| `firebase.json` | Firebase hosting/deployment config, likely for web collateral rather than Android runtime. | deployment config | yes |
| `index.html` | Standalone HTML page. | web collateral | yes |
| `datenschutz.html` | Privacy-policy HTML page. | legal/web collateral | no |
| `impressum.html` | Imprint/legal HTML page. | legal/web collateral | no |
| `img/app-icon.png` | Documentation/marketing image asset. | collateral imagery | no |
| `img/light_AddEntryScreen_withText.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_EntryDetailScreen.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_EntryListScreen.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_InspirationPopover.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_LanguagePicker.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_Onboarding1.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_Onboarding2.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_Onboarding3.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_Onboarding4.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_Onboarding5.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_SettingsScreen.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_SignInScreen.png` | Screenshot/mockup asset. | design collateral | no |
| `img/light_StatisticsScreen.png` | Screenshot/mockup asset. | design collateral | no |

## 6. Notes

- The production codebase is concentrated almost entirely under `app/src/main/java/com/rbf/echojournal`.
- Several entries were marked uncertain where names imply purpose but the file name alone does not fully establish behavior.
- Generated/build directories were inventoried at directory level because their contents are machine-generated and not stable source.

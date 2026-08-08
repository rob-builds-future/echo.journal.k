# Current Design Foundation

Source basis:
- [screen-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/screen-inventory.md)
- [feature-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/feature-inventory.md)

Purpose:
- Document the current UI and UX state of the app from a design perspective.
- Translate the legacy audit into design language.
- Separate confirmed observations from inferred conclusions.
- Additional product inputs provided after the legacy audit are included below and treated as stakeholder-provided design requirements.

## 1. Confirmed Design Observations

### Product-Level UI Structure

Confirmed:
- The app has three major user flows:
  authentication
  onboarding
  main journal usage
- The main journal usage flow branches into:
  journal home/list
  add entry
  entry detail
  settings
  statistics
- Startup includes a splash/loading surface before the app routes the user into the correct flow.

Interpretation:
- The main UX loop appears to center on real-time translation of journal entries.
- Writing and translation appear to be designed as a single language-practice interaction rather than two separate features.
- Other flows such as onboarding, settings, statistics, prompts, and reminders appear to support that core practice loop.

Stakeholder-provided additions:
- Translated entries should support text-to-speech playback.
- Echo should act as the guiding character of the experience, represented through a distinct symbol and through the user’s personal color on translation outputs.
- Photo-to-entry capture from handwritten journal pages should remain in scope.
- A premium plan is planned, with native in-app purchase flows and premium-only enhancements such as a stronger translation model and AI feedback or explanatory content around translations.

### Major Screens and Flows

| flow | screens | UX purpose |
| --- | --- | --- |
| Startup and access | `Splash screen` -> `SignInScreen` / `SignUpScreen` | Get the user into the app and resolve account state. |
| First-run setup | `OnboardingFlow` | Establish initial language, theme, template, and identity preferences. |
| Core translated journaling | `EntryListScreen` -> `AddEntryScreen` / `EntryDetailScreen` | Browse entries, write journal content, and work with real-time translated output as the core language-practice method. |
| Translation listening and review | `EntryDetailScreen` and translated entry surfaces | Reinforce learning by listening to translated entries and reviewing their meaning. |
| Handwritten capture | Photo-to-entry intake flow, not represented in the legacy screen inventory | Bring handwritten journal content into the same translated journaling workflow. |
| Premium upgrade | Native paywall or upgrade flow, not represented in the legacy screen inventory | Upgrade into enhanced translation and AI-supported feedback. |
| Progress and motivation | `EntryListScreen` -> `StatisticsScreen` | Surface streaks, totals, and activity patterns. |
| Personalization and account control | `EntryListScreen` -> `SettingsScreen` -> `SettingDetailScreen` | Adjust preferences, inspect account information, and perform account actions. |

### Recurring UI Patterns

Confirmed:

- Top app bar pattern:
  Most major screens use a top bar with a back action or screen-level navigation control.
- Hub-and-detail pattern:
  `EntryListScreen` acts as the main hub; `SettingsScreen` also acts as a hub with drill-down detail screens.
- Modal/overlay pattern:
  The current UI includes popovers, dialogs, and celebratory overlays for secondary actions and feedback.
- Step-based flow pattern:
  Onboarding is a single routed surface with local step progression rather than separate screens.
- Split-content pattern:
  Entry writing and review appear to use paired content areas for original text and translated text.
- Translation-first writing pattern:
  the original text and translated text appear to be the main paired interaction model, not a secondary utility view.
- Guided-response pattern:
  Echo appears intended to make translated output feel like a guided companion interaction rather than anonymous system output.
- Personal-color pattern:
  translation outputs are expected to be marked with the user’s personal color, reinforcing ownership and identity.
- Preference-detail pattern:
  Settings are grouped at the overview level and then broken into focused detail surfaces.

### Important User Interactions

Confirmed:

- Authentication:
  sign in, sign up, password reset, Google sign-in, switch between auth modes
- Onboarding:
  move step by step through welcome, username, language, theme, and template choices
- Journal home:
  open an entry, add an entry, open settings, open statistics, toggle favorites, delete entries, open inspiration
- Entry creation and editing:
  write content, receive translated output, edit content, save, discard changes, work with translated content
- Translation listening:
  play translated entries aloud as part of review and reinforcement
- Photo capture:
  import handwritten journal content into the digital translation flow
- Premium upgrade:
  enter a native in-app purchase flow for enhanced translation and AI-supported guidance
- Settings:
  open detail areas for username, language, profile info, theme, templates, and reminders
- Profile maintenance:
  log out and delete profile from settings-related flows
- Statistics:
  review streaks, totals, calendar activity, and top-word summaries

### Notable States and Variants

Confirmed:

- Auth state variants:
  unauthenticated, authenticated, authenticated but not onboarded
- Startup state variants:
  splash visible while loading, then route to auth, onboarding, or main flow
- Onboarding state variants:
  welcome, username, language, theme, template steps
- Journal interaction variants:
  entry list browsing, add mode, detail view, edit mode, discard confirmation
- Settings variants:
  overview state plus detail states per setting type
- Statistics variants:
  streak, total entries, top words, calendar activity
- Preference variants:
  language, theme, template, reminders
- Feedback variants:
  inspiration popover, congratulations dialog, splash animation, likely informational dialogs around entry actions
- Translation guidance variants:
  standard translated output, Echo-marked output, spoken playback, and planned premium AI-enhanced explanatory output

## 2. Inferred Design Conclusions

These points are design interpretations based on the confirmed screen and feature surface.

### Current UX Character

Inferred:
- The app appears to use reflective journaling as the structure through which language practice happens.
- The central UX proposition appears to be real-time translated journaling rather than generic journaling with multilingual support.
- Echo likely provides the emotional and symbolic wrapper around the translation experience, even if it is not explicitly labeled as a "future self" in the interface.
- The experience is likely designed to feel personalized early, with onboarding choices feeding into later writing and settings behavior.
- The design seems to favor guided flows and contained decision points rather than wide open navigation.

### Core UX Strengths

Inferred:

- Clear primary structure:
  The product has a recognizable journey from access -> setup -> journal home -> task-specific screens.
- Strong journaling focus:
  Writing, reviewing, and browsing entries appear to remain central.
- Translation is the core interaction layer:
  it does not appear as a detached utility screen and instead seems embedded in the main practice flow.
- Text-to-speech likely strengthens that same interaction loop by turning translated text into a listening exercise.
- Personalization is visible:
  Theme, language, and template choices appear to matter across multiple flows.
- Progress feedback is present:
  Streaks and statistics likely support continued engagement.

## 3. UX Inconsistencies and Design Debt

### Confirmed Issues

- Settings depth is fragmented:
  The settings experience uses an overview plus many detail subviews, which can increase navigation overhead for simple preference changes.
- Onboarding is structurally different from the rest of the app:
  It uses local step progression inside one routed screen, unlike the rest of the app’s screen-to-screen navigation.
- Notifications/reminder-related UI is incomplete from the current audited surface:
  reminder settings are present, but notification-specific UI is not fully wired into the documented screen structure.
- Social and feedback affordances exist, but their product role is not clearly established in the current documented flow.

### Inferred Issues

- The product may mix too many support mechanisms around the journaling core:
  templates, inspiration, streaks, reminders, and settings drill-downs may compete with the translated journaling core if not prioritized carefully.
- The split between `AddEntryScreen` and `EntryDetailScreen` may create overlapping interaction models:
  users likely write in one screen and edit/review in another, which may or may not feel cohesive.
- Echo guidance, personal color, translated text, spoken playback, and premium AI feedback all need a coherent visual hierarchy:
  otherwise the translation surface could become too dense.
- Settings may feel heavier than needed:
  the current drill-down model suggests a system that may be more complex than the value of the preferences warrants.
- The product likely contains multiple micro-patterns for feedback:
  popovers, dialogs, splash states, milestone surfaces, and informational overlays may not yet form a fully unified UX language.

## 4. Design Debt or Unclear UI Behavior

### Confirmed Unknowns

- Reminder UX is not fully validated end to end:
  the existence of reminder preferences does not confirm how reminders are explained, scheduled, surfaced, or recovered after failure.
- Prompt and template UX is only partially explicit:
  inspiration, templates, and writing instructions are present, but the overall content model and decision logic are not fully clear from the audit.
- Premium translation and AI-feedback UX is not represented in the legacy screen inventory:
  its paywall, upgrade prompts, and premium-state behavior need explicit design definition.
- Photo-to-entry capture UX is not represented in the legacy screen inventory:
  intake, OCR confirmation, editing, and failure handling all need explicit design definition.
- Social and feedback actions are underdefined:
  they exist in settings-related surfaces, but their user value and expected interaction depth remain unclear.

### Inferred Risk Areas

- Empty, error, and no-content states are not clearly represented in the current legacy analysis.
- Accessibility behavior is not visible from the current product-level audit.
- It is unclear whether the current design intentionally differentiates first-time guidance from repeat-use efficiency, especially in onboarding and settings.
- It is unclear how much users are expected to edit translated content versus simply view it.
- It is unclear how immediate and persistent the translation experience should feel in the rebuilt product:
  always-on, actively reactive, or more explicitly triggered.
- It is unclear how Echo should behave across the experience:
  character, symbol-only guide, tone-of-voice layer, or all three.

## 5. Open Design Questions for the Rebuild

- What is the primary emotional tone of the product:
  calm reflection, productivity, motivation, or multilingual assistance?
- How explicitly should the interface present real-time translation as the main language-practice method?
- How should Echo appear:
  as a visible guide, a symbolic marker, a tonal presence, or some combination?
- How central should translation feel in the interface:
  always visible, actively reactive, context-triggered, or optional?
- How should translated-entry text-to-speech be presented:
  inline control, dedicated review mode, or persistent listening action?
- How should the premium experience appear:
  gated actions, upgrade moments, or clearly separated premium review surfaces?
- How should photo-to-entry capture work from a user-experience perspective:
  quick camera capture, scan-and-confirm, or multi-step import and cleanup?
- Should entry creation and entry review remain separate surfaces, or be unified more tightly?
- How much onboarding is necessary before first value is delivered?
- Which settings deserve dedicated detail screens, and which should become inline controls?
- What is the right role for prompts and templates:
  optional inspiration, structured guidance, or a core part of the writing experience?
- How should streaks and statistics appear:
  motivational layer, reflective summary, or both?
- Are reminders a core retention tool worth prominent UX treatment, or a secondary preference?
- Should social and feedback actions remain inside the product surface at all?

## 6. Assumptions That Need Design Validation

- Users benefit from a dedicated onboarding sequence before they begin journaling.
- Real-time translation improves the language-practice experience rather than adding friction.
- Text-to-speech improves the same practice loop rather than feeling like an extra tool.
- Echo should remain a meaningful part of the experience identity.
- Photo-to-entry belongs inside the main journaling and translation flow.
- Separate browse, write, and detail screens are easier to understand than a more consolidated journal flow.
- A settings hub plus detail screens is better for clarity than a flatter preferences surface.
- Progress mechanics such as streaks and statistics help retention without overwhelming the core writing experience.
- Prompts, templates, and inspiration create meaningful value rather than feature clutter.

## 7. Summary

Confirmed:
- The current app design is organized around auth, onboarding, translated journaling, settings, and statistics, with translation woven into core entry flows.
- The interface uses hubs, drill-down details, dialogs, overlays, and step-based onboarding as recurring patterns.

Inferred:
- The strongest design foundation for a rebuild is a focused translated journaling experience, where real-time translation is the main practice loop and personalization and progress feedback remain supporting layers.
- The least stable design areas are reminders, prompts/templates depth, premium upgrade behavior, photo capture UX, settings complexity, and the exact role of secondary social or feedback actions.

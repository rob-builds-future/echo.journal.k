# Current Product Foundation

Source basis:
- [feature-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/feature-inventory.md)
- [screen-inventory.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/legacy/screen-inventory.md)

Scope note:
- `docs/legacy/rebuild-recommendations.md` was not available during this pass.
- This document translates the legacy audit into product language.
- Confirmed facts and interpretation are separated explicitly.
- Additional product inputs provided after the legacy audit are included below and marked as stakeholder-provided.

## 1. Confirmed Product Foundation

### Product Purpose Overview

Confirmed:
- The product is a journaling app centered on writing, reviewing, and managing personal journal entries.
- It combines journaling with translation support, onboarding-based personalization, statistics, and account-based persistence.
- The current product flow is account-first: users authenticate, complete onboarding if needed, then land in the journal home.

Interpretation:
- The clearest reading of the current product is not "general journaling with optional translation."
- The essential feature appears to be real-time translation of journal entries.
- That real-time translation flow appears to be the core language-practice method of the app.
- Other capabilities such as onboarding, theming, prompts, reminders, statistics, and account management appear to support that core method rather than define the app on their own.

Stakeholder-provided additions:
- Text-to-speech for translated entries is an important part of the language-practice loop.
- A premium plan with native in-app purchase support is planned, including an enhanced translation model and AI feedback or extra explanatory information attached to translated texts.
- "Echo" is an important guiding character represented by a distinct symbol and by the user’s personal color on translation outputs.
- Photo-to-entry capture from handwritten journal pages is an important feature that should remain in scope.

### Major Feature Areas

| feature area | user goal | current importance |
| --- | --- | --- |
| Authentication and access | Create an account, sign in, recover access, and enter the app securely. | Critical |
| Echo-guided translated journaling | Practice language through journal writing, translated responses, and the guided Echo interaction model. | Critical |
| Onboarding and initial personalization | Set language, theme, template, and profile basics before first use. | High |
| Journal home and browsing | See entries, open details, and move into writing, settings, or statistics. | Critical |
| Real-time translation journaling | Practice language by writing journal entries and receiving translated output as part of the same flow. | Critical |
| Translation listening and review | Listen to translated entries and use them for reinforcement and review. | High |
| Photo-to-entry capture | Convert handwritten journal pages into digital entries for the same translated journaling workflow. | High |
| Premium language support | Unlock higher-value translation and AI-supported learning features through paid access. | High |
| Journal writing and editing | Create and update journal entries as the container for translation-based language practice. | Critical |
| Statistics and streaks | See progress, activity, and writing patterns over time. | Medium |
| Settings and preferences | Manage language, theme, template, and profile-related preferences. | High |
| Profile and account maintenance | View account information, log out, and delete the profile. | Medium |
| Startup and session routing | Land in the correct flow based on auth and onboarding state. | Critical |

### User Goals Per Feature

- Authentication and access:
  Users need a reliable way to enter the product and recover access if blocked.
- Echo-guided translated journaling:
  Users need the product to feel like a guided exchange, with Echo acting as the recognizable companion voice of the translation experience.
- Onboarding and initial personalization:
  Users need the product to feel configured for them from the first session.
- Journal home and browsing:
  Users need a clear starting point for finding entries and moving to the next task.
- Real-time translation journaling:
  Users need to practice language by writing naturally and seeing journal content translated as part of the same interaction.
- Translation listening and review:
  Users need to hear translated entries spoken aloud as part of language reinforcement.
- Photo-to-entry capture:
  Users need a simple way to bring handwritten journal content into the translated journaling flow.
- Premium language support:
  Users need a clear upgrade path for more capable translation and deeper AI-supported feedback.
- Journal writing and editing:
  Users need to capture thoughts quickly and have the possibility to revise and review them later. If wanted some inspiration or structure to fill in might be offered as an orientation.
- Statistics and streaks:
  Users need lightweight progress feedback and motivation.
- Settings and preferences:
  Users need control over language, appearance, and writing defaults.
- Profile and account maintenance:
  Users need confidence that they can manage identity and leave the product if needed.
- Startup and session routing:
  Users need the app to resume in the right place without manual recovery.

## 2. Product Interpretation

These points are interpretations based on the confirmed feature surface. They should guide discussion, not be treated as final product truth.

### Product Positioning

Interpretation:
- The product appears to aim at language practice through reflective journaling, not just journaling with a multilingual add-on.
- Real-time translation is not peripheral. It is tied to writing, review, onboarding, and settings, which suggests it is the primary value proposition.
- The journal entry is best understood as the practice container through which translation-based learning happens.
- Text-to-speech, Echo guidance, photo capture, and premium AI support all strengthen the same core loop rather than creating separate product pillars.
- The product also uses progress mechanics, especially streaks and summary statistics, to support retention and habit formation.

### Feature Importance and Priority

Interpretation:

| feature area | likely product priority | rationale |
| --- | --- | --- |
| Echo-guided translated journaling | P0 | This appears to define the experience layer around the core translated journaling loop. |
| Real-time translation journaling | P0 | This appears to be the core user-value loop and the main language-practice method. |
| Journal writing and editing | P0 | Core product action. Without it, the product has no primary value. |
| Journal home and browsing | P0 | Primary navigation hub and repeat-use surface. |
| Authentication and access | P0 | Required for entry into the current product model. |
| Startup and session routing | P0 | Necessary glue for a usable account-based experience. |
| Translation listening and review | P1 | Strong extension of the translation-based learning loop. |
| Photo-to-entry capture | P1 | Important expansion of input into the same core workflow. |
| Premium language support | P1 | Important monetization and capability layer if product strategy depends on enhanced translation and AI help. |
| Onboarding and initial personalization | P1 | Important to first-session setup and downstream defaults. |
| Settings and preferences | P1 | Supports ongoing use and personalization continuity. |
| Statistics and streaks | P2 | Valuable for motivation and engagement, but secondary to journaling itself. |
| Profile and account maintenance | P2 | Important trust feature, but not a daily-use driver. |
| Reminder preferences | P2-P3 | Present in the legacy surface, but not fully validated end to end. |
| Writing prompts and inspiration | P2-P3 | Present, but currently looks lighter-weight and partially inferred. |

### Keep / Simplify / Rethink / Drop

Interpretation:

| area | direction | reasoning |
| --- | --- | --- |
| Echo-guided translated journaling | Keep | This appears to be part of the product identity, not just a presentation detail. |
| Real-time translation journaling | Keep | This is the clearest candidate for the app's essential feature and primary language-practice method. |
| Translation listening and review | Keep | This strengthens the learning loop around translated entries. |
| Photo-to-entry capture | Keep | This extends the core workflow to handwritten input without changing the product center. |
| Premium language support | Keep | This appears to be the planned monetization layer for deeper translation and AI value. |
| Journal writing and editing | Keep | This is the product core. |
| Journal home and browsing | Keep | It anchors repeat usage and feature discovery. |
| Authentication and access | Keep | The current product is built around account-based continuity. |
| Startup and session routing | Keep | Required for a coherent multi-state product flow. |
| Onboarding and initial personalization | Simplify | Keep the function, but the setup should likely stay focused on only the choices that materially improve first use. |
| Settings and preferences | Simplify | Keep key controls, but avoid over-fragmenting them into too many detail surfaces unless clearly needed. |
| Statistics and streaks | Keep | Useful support layer for retention and progress visibility. |
| Profile and account maintenance | Keep | Necessary for trust and account ownership. |
| Reminder preferences | Rethink | User value is plausible, but the actual delivery model and importance need validation. |
| Writing prompts and inspiration | Rethink | Potentially useful, but the product role and depth are not yet clearly established. |
| Social and feedback affordances | Drop or defer | They were not validated as a major product capability in the legacy analysis. |

## 3. Open Product Questions

- How explicitly should the product position real-time translation as the main language-practice method?
- How explicitly should Echo be framed:
  visible guide, subtle companion, or mostly symbolic presence?
- How should text-to-speech fit into the experience:
  default interaction, optional review tool, or premium-only enhancement?
- What belongs in premium:
  better translation quality, AI feedback, explanatory context, or all of the above?
- How should photo-to-entry behave:
  quick capture, OCR review workflow, or fully structured import?
- Which onboarding choices actually improve activation: username, language, theme, template, or only a subset?
- Are reminders part of the core habit loop, or only a secondary preference feature?
- How important are prompts and templates to successful writing sessions?
- Should statistics primarily motivate habit retention, support reflection, or both?
- Do users need a separate entry detail experience, or could browse and edit be more tightly merged?
- Are social and feedback actions meaningful product areas, or just utility links?

## 4. Assumptions That Need Validation

- The app’s core user value is language practice through translated journaling rather than journaling alone.
- Real-time translation materially improves user outcomes and is not merely decorative feature breadth.
- Text-to-speech materially improves the same learning loop rather than acting as a minor accessory.
- Echo as symbol, tone, and color-marked translation guide is important enough to preserve as part of product identity.
- Photo-to-entry capture belongs inside the core journaling and translation workflow rather than as an unrelated utility.
- Premium AI feedback and enhanced translation should deepen the core experience rather than create a disconnected upsell surface.
- A personalized first-run setup improves retention enough to justify the onboarding complexity.
- Streaks and statistics support motivation rather than distracting from the journaling experience.
- Reminder behavior is important enough to warrant explicit rebuild scope.
- Prompt and template support should remain part of the product rather than being treated as optional enrichment.

## 5. Summary

Confirmed:
- The current product foundation is a journaling app with account-based persistence, guided setup, translation support, settings, and progress tracking.

Interpretation:
- The strongest rebuild foundation is likely:
  real-time translated journaling as the core language-practice loop, strengthened by text-to-speech, Echo-guided identity, optional photo capture, lightweight personalization, and progress feedback.
- The least stable parts of the legacy surface are:
  reminders, prompts/templates depth, premium scope boundaries, and any social/feedback-adjacent behavior.

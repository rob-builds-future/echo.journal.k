# Target Product

Source basis:
- [current-product-foundation.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/product/current-product-foundation.md)

Purpose:
- Define the future product for the Flutter rebuild.
- Reduce the legacy surface to a smaller, clearer, stronger product.

## Product Vision

Echo is a language-practice journal where users write naturally, receive real-time translation of their entries, and improve through guidance and one focused daily practice loop.

## Target User

Primary user:
- A language learner who wants a calm, repeatable daily practice habit.
- They want to practice through self-expression, not drills alone.
- They value immediate help, low friction, and emotional continuity.

Secondary user:
- A reflective writer who also wants translation support and listening reinforcement.

Non-target for MVP:
- Users looking for a full social network.
- Users looking for a complex AI tutoring platform.
- Users looking for a broad note-taking or document-scanning app.

## Core Value Proposition

Write a personal journal entry in your own words, see the translation immediately, hear it aloud, and learn through a guided daily interaction with Echo.

This product should feel:
- personal
- calm
- guided
- useful within minutes

## Core Features

Only essential features are included here.

### 1. Real-Time Translated Journaling

- Users write a journal entry.
- The app shows the translated version as part of the same interaction.
- This is the core language-practice loop and the main reason the product exists.

### 2. Echo-Guided Translation Experience

- Translations are presented as part of the Echo experience, not as anonymous machine output.
- Echo remains a lightweight guide through symbol, tone, and personal-color-marked translation surfaces.
- This is part of product identity and should stay visible.

### 3. Translation Listening

- Users can play translated entries with text-to-speech.
- Listening is part of the same learning loop, not a separate mode.

### 4. Entry History and Review

- Users can return to past entries.
- Users can review original and translated content together.
- Review should remain simple and centered on learning progress through real entries.

### 5. Lightweight Personalization

- Users choose core defaults that materially improve the experience:
  target language
  theme / personal color
  account identity
- Personalization should support the practice loop, not become its own destination.

### 6. Premium Upgrade

- Premium unlocks stronger language support, specifically:
  enhanced translation quality
  AI feedback or extra explanatory information attached to translated text
- Premium must deepen the core loop, not create a second product.

### 7. Photo-to-Entry Capture

- Users can capture a handwritten journal entry and bring it into the same translated journaling flow.
- This matters because it expands input into the core loop without changing the product identity.

## Simplified Feature Set Compared to Legacy

The rebuild should simplify aggressively.

- Keep one clear center:
  translated journaling as the main practice loop
- Reduce setup:
  only keep onboarding choices that improve first-session usefulness
- Reduce settings depth:
  flatter settings, fewer drill-downs
- Reduce secondary surfaces:
  fewer dialogs, fewer side interactions, fewer competing pathways
- Keep statistics lightweight:
  enough to motivate, not enough to distract
- Treat prompts as optional support, not as the center of the experience

## Removed or Postponed Features

### Remove from target direction

- Social or feedback-adjacent product areas as first-class features
- Broad app complexity not tied to the translated journaling loop

### Postpone until after MVP

- Streak-heavy motivation expansion beyond a simple progress layer
- Rich reminder systems until their value is proven
- Complex template systems
- Deep prompt libraries
- Advanced account-management depth beyond essentials
- Any premium features that do not clearly strengthen translated journaling

## Feature Priorities

### MVP

- Account access
- Real-time translated journaling
- Echo-guided translation presentation
- Translation listening via text-to-speech
- Entry history and review
- Lightweight personalization:
  language, theme/personal color, basic profile
- Simple premium upgrade structure

### Later

- Photo-to-entry capture
- AI feedback and extra explanatory information, once the premium model is validated
- Lightweight progress tracking and simple streaks
- Reminders, if habit value is validated
- Optional prompts/templates, if they improve activation or retention

## Key User Flows

### Flow 1: First Use

1. Sign up or sign in.
2. Set essential preferences:
   language, theme/personal color
3. Land in the journal home.
4. Write the first entry.
5. See the translation immediately.
6. Play the translated version aloud.

### Flow 2: Daily Practice

1. Open the app.
2. Start a new journal entry quickly.
3. Write naturally.
4. Review the translated output.
5. Listen to the translation.
6. Save and return later.

### Flow 3: Review and Learn

1. Open a past entry.
2. Read original and translated versions together.
3. Listen to the translation.
4. If premium:
   view AI feedback or extra explanatory information.

### Flow 4: Premium Upgrade

1. Reach a premium boundary in a meaningful moment.
2. Understand the extra value:
   stronger translation, richer feedback
3. Upgrade through native in-app purchase flow.
4. Return directly to the core practice flow.

### Flow 5: Handwritten Capture

1. Capture a handwritten journal page.
2. Review extracted text.
3. Edit if needed.
4. Continue into the same translated journaling flow.

## Open Product Risks or Assumptions

### Risks

- The product may become too broad if prompts, reminders, statistics, premium AI, and capture workflows all compete with the core loop.
- Premium may feel weak if the upgrade value is not obviously better inside the translation experience.
- Echo may feel decorative instead of meaningful if the guide role is not clearly expressed in the interaction design.
- Photo capture may add disproportionate complexity if OCR quality is not strong enough.

### Assumptions

- Users want language practice through self-expression, not just correction.
- Real-time translation is the strongest daily habit driver.
- Text-to-speech materially improves the learning loop.
- Echo improves emotional continuity and differentiation.
- A simpler product will perform better than a feature-rich legacy rebuild.

## Product Principle

When in doubt, prefer the choice that makes translated journaling faster, clearer, and more personal.

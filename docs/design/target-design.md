# Target Design

Source basis:
- [current-design-foundation.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/design/current-design-foundation.md)
- [target-product.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/product/target-product.md)

Purpose:
- Define the UX and UI direction for the Flutter rebuild.
- Turn the target product into a simpler, cleaner interface model.

## Design Principles

- Clarity first:
  users should immediately understand what to do next.
- One strong loop:
  the interface should keep bringing users back to translated journaling.
- Fast to first value:
  users should reach writing and translation within minutes.
- Calm guidance:
  Echo should guide without overwhelming or interrupting.
- Fewer screens, fewer decisions:
  remove steps, branches, and drill-downs that do not strengthen the core flow.
- Supportive, not noisy:
  progress, premium, prompts, and settings should support the main task rather than compete with it.

## Main User Flows

### 1. First Use

Goal:
- get the user into the translated journaling loop as quickly as possible

Target flow:
1. Sign up or sign in.
2. Choose essential defaults:
   target language
   personal color / theme
3. Land directly in the journal home.
4. Start the first entry.
5. See translation immediately.
6. Optionally listen to the translated text.

### 2. Daily Practice

Goal:
- make writing and translation feel fast, repeatable, and rewarding

Target flow:
1. Open the app.
2. Tap to start a new entry from the home screen.
3. Write naturally.
4. See translated output in the same screen.
5. Listen if needed.
6. Save and return to home.

### 3. Review and Learn

Goal:
- let users revisit past entries without adding a second complex mode

Target flow:
1. Open a past entry from home.
2. Review original and translated versions together.
3. Listen to the translation.
4. If premium:
   view AI feedback or extra explanatory context.

### 4. Upgrade

Goal:
- keep premium simple, contextual, and tied to visible value

Target flow:
1. User reaches a premium boundary inside a meaningful learning moment.
2. Upgrade prompt explains the extra value clearly.
3. Native purchase flow opens.
4. User returns to the same task with premium access enabled.

### 5. Handwritten Capture

Goal:
- let handwritten journaling feed the same core experience

Target flow:
1. User captures a journal page.
2. Extracted text is shown for quick cleanup.
3. User confirms and enters the same translated journaling screen.

## Key Screens

The rebuild should use fewer, stronger screens.

### 1. Auth Screen

Purpose:
- handle sign in and sign up with minimal branching

Design direction:
- one unified auth surface instead of two heavy sibling flows
- secondary actions remain available but visually quiet

### 2. Quick Setup Screen

Purpose:
- capture only the minimum preferences needed for a good first session

Design direction:
- one short setup screen instead of a long multi-step onboarding flow
- only ask for what directly improves first use

### 3. Home Screen

Purpose:
- act as the main hub for journaling, review, and lightweight progress

Design direction:
- strongest visual priority goes to starting a new entry
- recent entries are easy to scan and reopen
- progress stays lightweight and secondary

### 4. Journal Screen

Purpose:
- combine writing, translation, and listening into one main interaction surface

Design direction:
- replace the old split between add and detail as much as possible
- keep original text and translated text visually related and easy to compare
- Echo presence should be visible here through symbol, tone, and personal-color translation treatment

### 5. Review Screen

Purpose:
- support calm rereading, listening, and premium feedback on saved entries

Design direction:
- lighter than an editor
- strong emphasis on reading, listening, and understanding

### 6. Settings Screen

Purpose:
- manage core preferences without excessive drill-down

Design direction:
- one flatter settings surface
- only complex items deserve a sub-screen

### 7. Premium Screen

Purpose:
- explain and unlock premium value

Design direction:
- concise
- tied directly to stronger translation and richer feedback
- no generic subscription marketing surface

### 8. Capture Flow

Purpose:
- bring handwritten input into the same journal flow

Design direction:
- keep this as a short utility flow, not a separate product area

## Navigation Structure

### Primary structure

- Auth
- Quick Setup
- Home
- Journal
- Review
- Settings
- Premium
- Capture

### Navigation rules

- Home is the main anchor screen.
- Journal is the main action screen.
- Review is entered from Home.
- Premium is entered contextually, not as a primary destination.
- Capture feeds into Journal, not into a separate archive or scanner area.
- Settings should stay shallow.

### Simplification from legacy

- Replace multi-step onboarding with one short setup flow.
- Reduce the number of settings detail screens.
- Reduce screen separation between creating and reviewing entries where possible.
- Remove unnecessary modal branches and secondary destinations.

## Core UI Patterns

### 1. One Primary Action Per Screen

- Each screen should make the next best action obvious.
- On Home, it is "start a new entry."
- On Journal, it is "write and review translation."
- On Review, it is "understand and listen."

### 2. Paired Text Pattern

- Original text and translated text should be visually paired.
- The relationship should feel immediate and readable.
- Translation should not feel buried or visually secondary.

### 3. Echo Pattern

- Echo appears through symbol, tone, and color-marked translation surfaces.
- Echo should guide, not narrate constantly.
- The interface should feel accompanied, not crowded.

### 4. Lightweight Progress Pattern

- Progress indicators should motivate without dominating.
- They belong on Home and review moments, not in every interaction.

### 5. Contextual Upgrade Pattern

- Premium prompts should appear only when the added value is clear.
- Upgrade moments should stay close to the premium feature being requested.

### 6. Minimal Settings Pattern

- Preferences should be edited inline when possible.
- Use sub-screens only for genuinely complex choices.

## Interaction Principles

- Show the translation fast.
- Keep writing uninterrupted.
- Make listening a one-tap action.
- Keep Echo present but subtle.
- Avoid forcing users through extra confirmation unless data loss is likely.
- Keep review separate from editing only when that makes the experience calmer and clearer.
- Keep premium prompts respectful and relevant.
- Treat photo capture as a shortcut into the same journaling flow, not as a new mode.

## Simplifications Compared to Legacy UI

- Short onboarding instead of a longer multi-step flow.
- Fewer settings drill-downs.
- Fewer overlays, popovers, and celebratory interruptions.
- Less fragmentation between create, view, and review.
- Simpler progress display.
- No broad expansion into secondary social or feedback surfaces.
- No heavy prompt/template system in MVP.

## Design Direction for Complexity Control

- If a feature does not strengthen translated journaling, it should not get prominent UI.
- If a flow needs more than a few steps, it should be reconsidered.
- If a screen has multiple competing actions, reduce it to one primary action.
- If Echo, translation, TTS, premium, and feedback all appear together, the hierarchy must still make the writing and translated output dominant.

## Summary

The target design should feel like one focused product:
- write
- translate
- listen
- learn

Everything else should support that loop with as little UI complexity as possible.

# Implementation Sequence

Source basis:
- [v1-build-scope.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/product/v1-build-scope.md)
- [target-architecture.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/engineering/target-architecture.md)
- [flutter-package-structure.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/engineering/flutter-package-structure.md)
- [target-design.md](/Users/robinbe/Developer/Apps/Kotlin/echo.journal.k/docs/design/target-design.md)

Purpose:
- Define the practical implementation order for the first Flutter build slice.
- Reduce integration risk by building small, testable vertical slices.
- Keep Home, Journal, Translation, Listening, Save, and Review on the critical path.

## 1. Delivery Goal

Deliver a first usable build where a signed-in user can complete the core loop: setup -> write -> translate -> listen -> save -> sync -> review.

## 2. Phase 0 — Project Bootstrap

Goal:
- Create the project and external service baseline before feature work starts.

Tasks:
- Create the new Flutter app.
- Set up the folder structure from `flutter-package-structure.md`.
- Add `app`, `core`, `shared_ui`, and v1 feature folders.
- Configure linting and formatting.
- Define environment/config strategy:
  dev values first, production placeholders later.
- Add placeholder flavor/env handling only if needed for Firebase or backend separation.
- Create or connect the Firebase project.
- Configure Firebase Auth.
- Configure Firestore.
- Configure Firebase Cloud Functions.
- Add initial Firestore rules and indexes placeholder.
- Add Cloud Functions skeleton for protected service calls.
- Add CI baseline if lightweight:
  format, analyze, and test.

Exit criteria:
- App runs from a clean checkout.
- Lint/analyze can run.
- Firebase config is represented without hardcoding secrets.
- Folder structure exists, but feature logic is not implemented yet.

## 3. Phase 1 — Foundations

Goal:
- Establish the app shell and shared technical foundation without adding feature complexity.

Tasks:
- Implement root app shell.
- Add Riverpod provider scope.
- Add routing/navigation skeleton.
- Add route placeholders:
  auth, setup, home, journal, review, settings.
- Add route guard shape:
  unauthenticated, setup incomplete, setup complete.
- Add theme/design tokens:
  color, typography, spacing, basic app theme.
- Add shared UI foundations:
  app scaffold, primary button, text field, loading state, error state.
- Add Echo UI primitives:
  mark/symbol placeholder, translation surface placeholder.
- Add typed error/failure model.
- Add basic redacted logging approach.
- Set up Drift database shell:
  database file, migration strategy, empty or first minimal tables.
- Set up secure storage wrapper.
- Set up lightweight preferences wrapper.
- Add minimal sync primitives:
  sync metadata type, trigger interface, no complex queue/retry behavior yet.

Exit criteria:
- App can navigate through placeholder routes.
- Theme and shared components are usable.
- Drift initializes.
- Errors and logging have a consistent shape.

## 4. Phase 2 — Auth And Setup

Goal:
- Let a user reach Home/Journal with the minimum setup needed for translation.

Tasks:
- Implement `features/auth`.
- Add unified sign in / sign up screen.
- Wire Firebase Auth through `core/auth`.
- Add session restoration.
- Add typed auth failures.
- Connect auth route guard.
- Implement `features/onboarding`.
- Add one short setup screen.
- Capture only essential preferences:
  target language and personal color/theme.
- Persist setup state and preferences.
- Connect setup completion route guard.

Exit criteria:
- User can sign in or sign up.
- Existing session restores.
- First-run setup captures target language and personal color/theme.
- User reaches Home with minimal friction.

## 5. Phase 3 — Core Journaling Loop

Goal:
- Make the journaling loop usable as early as possible, before full sync hardening.

Recommended order:
1. Implement Home under `features/journal`.
2. Add recent entries list backed by local data.
3. Add start-new-entry action.
4. Implement journal editor.
5. Add local draft state.
6. Add local save flow with Drift.
7. Add `JournalEntry` model and repository contract in the domain/application-facing layer.
8. Implement Drift-backed journal repository in `features/journal/data`.
9. Add lightweight Echo-guided translation presentation:
   paired original/translated text and Echo visual treatment, not raw output alone.
10. Implement `features/translation` request flow.
11. Add Cloud Functions translation call through repository/data layer.
12. Wire default free-tier translation behavior through backend routing.
13. Add translation loading, success, retry, and recoverable error states.
14. Add Review screen for saved entries.
15. Add open-saved-entry flow from Home.
16. Add `core/tts` platform adapter.
17. Add one-tap listening where translated text exists.

Keep out:
- premium purchase flow
- entitlement logic
- AI coach
- photo capture
- rich progress

Exit criteria:
- User can write an entry.
- Translation appears in the journal screen.
- Translation failure does not block writing.
- User can save locally.
- User can reopen a saved entry.
- User can listen to translated text where available.

## 6. Phase 4 — Sync And Remote Integration

Goal:
- Add reliable minimal remote sync without making journaling dependent on network success.

Tasks:
- Add Firestore collection shape for user-owned product data.
- Add per-user security rules.
- Add minimal sync metadata:
  `createdAt`, `updatedAt`, and fields needed for last-write-wins.
- Implement saved-entry sync from Drift to Firestore.
- Implement remote-to-local hydration for saved entries.
- Add sync trigger points:
  app startup, authenticated session change, local write, network reconnect, foreground resume.
- Use simple last-write-wins conflict behavior.
- Add delayed sync state where needed.
- Keep journal save local-first:
  local save succeeds even if remote sync is delayed.
- Avoid complex queues, background reliability systems, and merge workflows.

Exit criteria:
- Saved entries sync to Firestore for the authenticated user.
- Entries can hydrate locally after reinstall/session restore if needed.
- Delayed or unavailable sync does not block writing, saving, or review of local data.

## 7. Phase 5 — Hardening

Goal:
- Make the first build slice safe and reliable enough for internal or early external use.

Tasks:
- Review privacy handling:
  journal text is sensitive user data.
- Verify logs and telemetry avoid raw journal text.
- Verify translation/backend calls minimize retention.
- Review backend failure handling.
- Validate translation failure UX:
  non-blocking error state and retry.
- Validate poor-network behavior:
  auth/session, translation failure, local save, delayed sync.
- Validate Firestore rules restrict data to the authenticated user.
- Add minimal analytics only if truly needed for first-slice readiness.
- Polish only the critical path:
  auth, setup, home, journal, translation, listen, save, review.
- Keep settings shallow and essential.

Exit criteria:
- First end-to-end user loop works under normal conditions.
- Core loop remains usable when translation or sync fails.
- No raw journal text appears in logs/telemetry by default.

## 8. Integration Order

Recommended sequence:
1. Firebase Auth:
   integrate in Phase 2 because routing and user-owned sync depend on it.
2. Firestore:
   configure in Phase 0, implement real saved-entry sync in Phase 4.
3. Cloud Functions:
   scaffold in Phase 0, connect translation endpoint in Phase 3.
4. LibreTranslate routing:
   implement as the default free-tier route in Phase 3.
5. DeepL readiness:
   keep backend structure compatible, but do not implement premium routing in the first build slice.
6. TTS:
   add `core/tts` after saved translated text exists in Phase 3.
7. AI coach hooks:
   no implementation required; add only placeholders if needed to avoid blocking later architecture.

Rule:
- Do not integrate premium, advanced AI, or capture before the translated-journaling loop works locally.

## 9. Testing Checkpoints

Use lightweight checkpoints after each phase.

Phase 0:
- project boots
- analyze/format runs
- Firebase config is present

Phase 1:
- app shell renders
- routes navigate to placeholders
- theme/shared UI render
- Drift initializes

Phase 2:
- sign in/sign up works
- session restores
- setup gate works
- user reaches Home

Phase 3:
- local journaling works
- draft/save flow works
- translation request works
- translation failure is non-blocking
- saved entries reopen correctly
- TTS play/stop works where translated text exists

Phase 4:
- saved entries sync to Firestore
- remote data is scoped to the authenticated user
- delayed sync does not block local journaling
- last-write-wins behavior is covered by a small test

Phase 5:
- backend failures are handled
- poor-network journaling path works locally
- logs do not include raw journal text
- first end-to-end user loop passes

## 10. Milestones

### Milestone 1 — Runnable Shell

Outcome:
- Flutter app boots with routing, theme, shared UI, and empty feature routes.

Suggested focus:
- one developer/agent on app shell and routing
- one developer/agent on theme/shared UI foundations

### Milestone 2 — Auth To Home

Outcome:
- User can sign in, complete setup, and reach journal-owned Home.

Suggested focus:
- auth/session/route guards
- setup preference persistence

### Milestone 3 — Local Journal Loop

Outcome:
- User can create, save, reopen, and review a local entry without remote dependencies.

Suggested focus:
- Drift schema/repository
- Home/editor/review vertical slice

### Milestone 4 — Translation And Listening

Outcome:
- User can see live translation, recover from translation failure, and listen to translated text.

Suggested focus:
- Cloud Functions translation endpoint
- default LibreTranslate/free-tier routing
- Journal translation UI states
- `core/tts`

### Milestone 5 — Minimal Sync

Outcome:
- Saved entries sync to Firestore without blocking local journaling.

Suggested focus:
- Firestore rules
- sync metadata
- sync triggers
- delayed sync handling

### Milestone 6 — First-Slice Hardening

Outcome:
- The complete first user loop is safe, testable, and ready for early validation.

Suggested focus:
- privacy/logging review
- failure-state validation
- poor-network checks
- minimal polish on the critical path

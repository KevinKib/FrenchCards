# Deck Distribution Refactor Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Funnel all of `Deck`'s distribution methods through one shared private core, add an option to deal cards already hidden, and document every public `distribute*` method with javadoc.

**Architecture:** Card→player assignment is always `card i → player i % nbPlayers`, so `distribute`, `distributeAll`, and `distributeAllEvenly` differ only in how many cards they deal. They collapse into one private `distributeCards(...)`. The even-distribution concern (previously the `isIdenticalCardsNumberForced` flag) is promoted to a public `distributeAllEvenly` method, leaving `DistributionOptions` to carry only universal concerns (today: `Visibility`). Each distribute method gets a no-options convenience overload plus a `DistributionOptions` overload.

**Tech Stack:** Java 17, Maven, JUnit 5 (Jupiter), Hamcrest.

**Spec:** `docs/superpowers/specs/2026-06-21-deck-distribute-refactor-design.md`

**Build/test commands:**
- Full suite: `mvn test`
- Just the deck tests: `mvn test -Dtest=DeckTest`
- A single method: `mvn test -Dtest=DeckTest#methodName`

---

## File Structure

- **Modify** `src/main/java/org/kevinkib/cards/domain/deck/Deck.java` — shared core, method overloads, `distributeAllEvenly` made public, javadoc.
- **Modify** `src/main/java/org/kevinkib/cards/domain/deck/DistributionOptions.java` — record now holds `Visibility`; `DEFAULT` kept, `IDENTICAL_CARDS_NUMBER` removed, `defaults()`/`hidden()`/`shown()` added.
- **Modify** `src/test/java/org/kevinkib/cards/domain/DeckTest.java` — migrate the two `IDENTICAL_CARDS_NUMBER` call sites to `distributeAllEvenly`; add hidden-dealing tests.
- **Modify** `README.md` — update the "Deal to players" section for `distributeAllEvenly` and the `hidden()` option.

No other files change. `Card`, `Hand`, `Visibility`, and `DeckFactory` are untouched.

---

## Task 1: Shared distribution core + public `distributeAllEvenly` (request #1)

Pure structural refactor. `DistributionOptions` is left unchanged in this task, so the whole module keeps compiling and every existing test stays green. The existing suite is the regression guard for this refactor.

**Files:**
- Modify: `src/main/java/org/kevinkib/cards/domain/deck/Deck.java`

- [ ] **Step 1: Replace the four distribution methods with versions that funnel through a shared core, and make `distributeAllEvenly` public**

Replace everything from the `distribute(...)` method through the `distributeAllEvenly(...)` method (lines 21–80 of the current file) with the following. Leave the constructors, `draw`, `shuffle`, `getSize`, `createHands`, `remove`, and the `SHOULD_ALWAYS_DISTRIBUTE` constant exactly as they are.

```java
    public List<Hand> distribute(Integer nbPlayers, Integer nbCardsPerPlayer) throws CannotDistributeDeckException {
        int expectedNumberOfDistributedCards = nbPlayers * nbCardsPerPlayer;
        if (expectedNumberOfDistributedCards > this.getSize()) {
            throw new CannotDistributeDeckException();
        }
        return distributeCards(nbPlayers, expectedNumberOfDistributedCards);
    }

    public List<Hand> distributeAll(Integer nbPlayers) {
        return distributeCards(nbPlayers, getSize());
    }

    public List<Hand> distributeAll(Integer nbPlayers, DistributionOptions options) throws UnevenNumberOfCardsPerPlayerException {
        if (options.isIdenticalCardsNumberForced()) {
            return distributeAllEvenly(nbPlayers);
        }
        return distributeCards(nbPlayers, getSize());
    }

    public List<Hand> distributeAllEvenly(Integer nbPlayers) throws UnevenNumberOfCardsPerPlayerException {
        double cardsPerPlayer = (double) getSize() / nbPlayers;
        boolean isCardDistributionUneven = cardsPerPlayer != Math.floor(cardsPerPlayer);

        if (isCardDistributionUneven) {
            throw new UnevenNumberOfCardsPerPlayerException(nbPlayers, getSize());
        }

        Integer nbCardsPerPlayer = (int) cardsPerPlayer;

        try {
            return distribute(nbPlayers, nbCardsPerPlayer);
        } catch (CannotDistributeDeckException e) {
            throw new IllegalStateException(SHOULD_ALWAYS_DISTRIBUTE);
        }
    }

    private List<Hand> distributeCards(Integer nbPlayers, int totalCards) {
        List<Hand> hands = createHands(nbPlayers);
        for (int cardIndex = 0; cardIndex < totalCards; ++cardIndex) {
            hands.get(cardIndex % nbPlayers).add(cards.pop());
        }
        return hands;
    }
```

Notes:
- `distributeAllEvenly` changes from `private` to `public` (it keeps its `throws UnevenNumberOfCardsPerPlayerException`).
- `distributeAll(Integer)` no longer wraps a checked-exception call, so its old try/catch is gone — `DistributionOptions.DEFAULT` had `forced = false`, so it always took the deal-everything path; calling `distributeCards` directly is behavior-identical.

- [ ] **Step 2: Run the full suite to confirm the refactor preserved behavior**

Run: `mvn test -Dtest=DeckTest`
Expected: BUILD SUCCESS. All 8 existing `DeckTest` methods pass (the `IDENTICAL_CARDS_NUMBER` tests still go through `distributeAll(n, options)` → `distributeAllEvenly`).

- [ ] **Step 3: Commit**

```bash
git add src/main/java/org/kevinkib/cards/domain/deck/Deck.java
git commit -m "refactor: funnel Deck distribution through a shared core, expose distributeAllEvenly"
```

---

## Task 2: Deal cards already hidden via `DistributionOptions` (request #2)

Reshape `DistributionOptions` to carry `Visibility`, add the per-method `DistributionOptions` overloads, migrate the two existing `IDENTICAL_CARDS_NUMBER` call sites, then TDD the hidden behavior (red → green).

**Files:**
- Modify: `src/main/java/org/kevinkib/cards/domain/deck/DistributionOptions.java`
- Modify: `src/main/java/org/kevinkib/cards/domain/deck/Deck.java`
- Test: `src/test/java/org/kevinkib/cards/domain/DeckTest.java`

- [ ] **Step 1: Reshape `DistributionOptions`**

Replace the entire contents of `DistributionOptions.java` with:

```java
package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.Visibility;

public record DistributionOptions(Visibility visibility) {

    public static final DistributionOptions DEFAULT = new DistributionOptions(Visibility.SHOWN);

    public static DistributionOptions defaults() {
        return DEFAULT;
    }

    public DistributionOptions hidden() {
        return new DistributionOptions(Visibility.HIDDEN);
    }

    public DistributionOptions shown() {
        return new DistributionOptions(Visibility.SHOWN);
    }
}
```

`isIdenticalCardsNumberForced` and `IDENTICAL_CARDS_NUMBER` are gone; `DEFAULT` stays. The module will not compile again until Step 2 and Step 3 are done — that is expected.

- [ ] **Step 2: Rewrite the distribution methods in `Deck.java` to take options and thread `Visibility` (without applying `hide()` yet)**

Replace the distribution block from Task 1 (the `distribute(...)` method through `distributeCards(...)`) with the following. The core now accepts a `Visibility` but deliberately does **not** call `hide()` yet — that line is added in the green step below, so the test in Step 5 can fail first.

```java
    public List<Hand> distribute(Integer nbPlayers, Integer nbCardsPerPlayer) throws CannotDistributeDeckException {
        return distribute(nbPlayers, nbCardsPerPlayer, DistributionOptions.DEFAULT);
    }

    public List<Hand> distribute(Integer nbPlayers, Integer nbCardsPerPlayer, DistributionOptions options) throws CannotDistributeDeckException {
        int expectedNumberOfDistributedCards = nbPlayers * nbCardsPerPlayer;
        if (expectedNumberOfDistributedCards > this.getSize()) {
            throw new CannotDistributeDeckException();
        }
        return distributeCards(nbPlayers, expectedNumberOfDistributedCards, options.visibility());
    }

    public List<Hand> distributeAll(Integer nbPlayers) {
        return distributeAll(nbPlayers, DistributionOptions.DEFAULT);
    }

    public List<Hand> distributeAll(Integer nbPlayers, DistributionOptions options) {
        return distributeCards(nbPlayers, getSize(), options.visibility());
    }

    public List<Hand> distributeAllEvenly(Integer nbPlayers) throws UnevenNumberOfCardsPerPlayerException {
        return distributeAllEvenly(nbPlayers, DistributionOptions.DEFAULT);
    }

    public List<Hand> distributeAllEvenly(Integer nbPlayers, DistributionOptions options) throws UnevenNumberOfCardsPerPlayerException {
        double cardsPerPlayer = (double) getSize() / nbPlayers;
        boolean isCardDistributionUneven = cardsPerPlayer != Math.floor(cardsPerPlayer);

        if (isCardDistributionUneven) {
            throw new UnevenNumberOfCardsPerPlayerException(nbPlayers, getSize());
        }

        Integer nbCardsPerPlayer = (int) cardsPerPlayer;

        try {
            return distribute(nbPlayers, nbCardsPerPlayer, options);
        } catch (CannotDistributeDeckException e) {
            throw new IllegalStateException(SHOULD_ALWAYS_DISTRIBUTE);
        }
    }

    private List<Hand> distributeCards(Integer nbPlayers, int totalCards, Visibility visibility) {
        List<Hand> hands = createHands(nbPlayers);
        for (int cardIndex = 0; cardIndex < totalCards; ++cardIndex) {
            Card card = cards.pop();
            hands.get(cardIndex % nbPlayers).add(card);
        }
        return hands;
    }
```

Note: `Deck` already imports `org.kevinkib.cards.domain.*`, so `Visibility` and `Card` need no new import. `distributeAll(Integer, DistributionOptions)` no longer declares `throws UnevenNumberOfCardsPerPlayerException` — it no longer branches into the even path.

- [ ] **Step 3: Migrate the two existing `IDENTICAL_CARDS_NUMBER` test call sites**

In `src/test/java/org/kevinkib/cards/domain/DeckTest.java`:

Replace (in `givenPlayers_whenDistributeAllEvenly_thenDistributeAllCards`):
```java
            List<Hand> hands = deck.distributeAll(nbPlayers, DistributionOptions.IDENTICAL_CARDS_NUMBER);
```
with:
```java
            List<Hand> hands = deck.distributeAllEvenly(nbPlayers);
```

Replace (in `givenIncompatibleNbPlayersAndCards_whendistributeAllEvenly_thenThrowUnevenNumberOfCardsPerPlayerException`):
```java
            deck.distributeAll(nbPlayers, DistributionOptions.IDENTICAL_CARDS_NUMBER);
```
with:
```java
            deck.distributeAllEvenly(nbPlayers);
```

- [ ] **Step 4: Run the suite to confirm the refactor + migration compile and pass (still no hidden behavior)**

Run: `mvn test -Dtest=DeckTest`
Expected: BUILD SUCCESS. All existing tests pass. Hidden dealing is not yet implemented.

- [ ] **Step 5: Write the failing hidden-dealing tests**

Add these three tests to `DeckTest` (before the closing `}` of the class). `Card` is in the same package as the test, so no import is needed.

```java
    @Test
    public void givenHiddenOption_whenDistribute_thenDealtCardsAreHidden() {
        deck = DeckBuilder.aDeck().withNumberOfCards(30).build();

        assertDoesNotThrow(() -> {
            List<Hand> hands = deck.distribute(3, 10, DistributionOptions.DEFAULT.hidden());

            for (Hand hand : hands) {
                for (Card card : hand.getCards()) {
                    assertThat(card.isHidden(), is(true));
                }
            }
        });
    }

    @Test
    public void givenHiddenOption_whenDistributeAll_thenDealtCardsAreHidden() {
        deck = DeckBuilder.aDeck().withNumberOfCards(30).build();

        List<Hand> hands = deck.distributeAll(3, DistributionOptions.DEFAULT.hidden());

        for (Hand hand : hands) {
            for (Card card : hand.getCards()) {
                assertThat(card.isHidden(), is(true));
            }
        }
    }

    @Test
    public void givenDefaultOption_whenDistribute_thenDealtCardsAreShown() {
        deck = DeckBuilder.aDeck().withNumberOfCards(30).build();

        assertDoesNotThrow(() -> {
            List<Hand> hands = deck.distribute(3, 10, DistributionOptions.DEFAULT);

            for (Hand hand : hands) {
                for (Card card : hand.getCards()) {
                    assertThat(card.isShown(), is(true));
                }
            }
        });
    }
```

- [ ] **Step 6: Run the new tests to verify they fail (red)**

Run: `mvn test -Dtest=DeckTest`
Expected: BUILD FAILURE. `givenHiddenOption_whenDistribute_thenDealtCardsAreHidden` and `givenHiddenOption_whenDistributeAll_thenDealtCardsAreHidden` fail with `Expected: is <true> but: was <false>` (cards default to `SHOWN`). `givenDefaultOption_whenDistribute_thenDealtCardsAreShown` passes.

- [ ] **Step 7: Implement hidden dealing in the core**

In `Deck.distributeCards`, apply `hide()` when the visibility is hidden. Replace the loop body:

```java
        for (int cardIndex = 0; cardIndex < totalCards; ++cardIndex) {
            Card card = cards.pop();
            hands.get(cardIndex % nbPlayers).add(card);
        }
```
with:
```java
        for (int cardIndex = 0; cardIndex < totalCards; ++cardIndex) {
            Card card = cards.pop();
            if (visibility.isHidden()) {
                card.hide();
            }
            hands.get(cardIndex % nbPlayers).add(card);
        }
```

- [ ] **Step 8: Run the suite to verify everything passes (green)**

Run: `mvn test -Dtest=DeckTest`
Expected: BUILD SUCCESS. All tests pass, including the two hidden tests.

- [ ] **Step 9: Commit**

```bash
git add src/main/java/org/kevinkib/cards/domain/deck/DistributionOptions.java src/main/java/org/kevinkib/cards/domain/deck/Deck.java src/test/java/org/kevinkib/cards/domain/DeckTest.java
git commit -m "feat: allow dealing cards already hidden via DistributionOptions"
```

---

## Task 3: Javadoc on all distribute methods + README (request #3)

Documentation only — no behavior change.

**Files:**
- Modify: `src/main/java/org/kevinkib/cards/domain/deck/Deck.java`
- Modify: `README.md`

- [ ] **Step 1: Add javadoc to each public distribute method**

In `Deck.java`, add the javadoc comment immediately above each corresponding method (do not change method bodies).

Above `distribute(Integer nbPlayers, Integer nbCardsPerPlayer)`:
```java
    /**
     * Deals exactly {@code nbCardsPerPlayer} cards to each of {@code nbPlayers} players,
     * dealt face up. Equivalent to {@link #distribute(Integer, Integer, DistributionOptions)}
     * with {@link DistributionOptions#DEFAULT}.
     *
     * @throws CannotDistributeDeckException if the deck holds fewer than
     *         {@code nbPlayers * nbCardsPerPlayer} cards.
     */
```

Above `distribute(Integer nbPlayers, Integer nbCardsPerPlayer, DistributionOptions options)`:
```java
    /**
     * Deals exactly {@code nbCardsPerPlayer} cards to each of {@code nbPlayers} players.
     * Card {@code i} dealt goes to player {@code i % nbPlayers}. When
     * {@code options.visibility()} is {@code HIDDEN} every dealt card is turned face down.
     *
     * @param options distribution options; use {@link DistributionOptions#hidden()} to deal face down.
     * @throws CannotDistributeDeckException if the deck holds fewer than
     *         {@code nbPlayers * nbCardsPerPlayer} cards.
     */
```

Above `distributeAll(Integer nbPlayers)`:
```java
    /**
     * Deals the entire deck round-robin across {@code nbPlayers} players, dealt face up.
     * The split may be uneven (the first players receive the extra cards). Equivalent to
     * {@link #distributeAll(Integer, DistributionOptions)} with {@link DistributionOptions#DEFAULT}.
     */
```

Above `distributeAll(Integer nbPlayers, DistributionOptions options)`:
```java
    /**
     * Deals the entire deck round-robin across {@code nbPlayers} players. The split may be
     * uneven (the first players receive the extra cards). When {@code options.visibility()}
     * is {@code HIDDEN} every dealt card is turned face down.
     *
     * @param options distribution options; use {@link DistributionOptions#hidden()} to deal face down.
     */
```

Above `distributeAllEvenly(Integer nbPlayers)`:
```java
    /**
     * Deals the entire deck evenly across {@code nbPlayers} players, dealt face up. Equivalent to
     * {@link #distributeAllEvenly(Integer, DistributionOptions)} with {@link DistributionOptions#DEFAULT}.
     *
     * @throws UnevenNumberOfCardsPerPlayerException if the deck does not divide evenly
     *         across {@code nbPlayers}.
     */
```

Above `distributeAllEvenly(Integer nbPlayers, DistributionOptions options)`:
```java
    /**
     * Deals the entire deck evenly across {@code nbPlayers} players, giving every player the
     * same number of cards. When {@code options.visibility()} is {@code HIDDEN} every dealt
     * card is turned face down.
     *
     * @param options distribution options; use {@link DistributionOptions#hidden()} to deal face down.
     * @throws UnevenNumberOfCardsPerPlayerException if the deck does not divide evenly
     *         across {@code nbPlayers}.
     */
```

- [ ] **Step 2: Update the README "Deal to players" section**

In `README.md`, replace the code block and explanation (lines 68–82 of the current file):

```java
// Deal all cards evenly across 4 players (13 cards each)
List<Hand> hands = deck.distributeAll(4);

// Deal all cards, allowing uneven distribution
List<Hand> hands = deck.distributeAll(4, DistributionOptions.DEFAULT);

// Deal exactly 5 cards to each of 4 players
List<Hand> hands = deck.distribute(4, 5);

// Draw a single card from the top
Card card = deck.draw();
```

```
`distributeAll` with the default options distributes cards round-robin without requiring an equal split. Pass `DistributionOptions.IDENTICAL_CARDS_NUMBER` to throw `UnevenNumberOfCardsPerPlayerException` if the deck doesn't divide evenly.
```

with:

```java
// Deal all cards round-robin across 4 players (uneven split allowed)
List<Hand> hands = deck.distributeAll(4);

// Deal all cards evenly; throws UnevenNumberOfCardsPerPlayerException if it can't divide evenly
List<Hand> hands = deck.distributeAllEvenly(4);

// Deal exactly 5 cards to each of 4 players
List<Hand> hands = deck.distribute(4, 5);

// Deal face down: pass hidden options to any distribute method
List<Hand> hands = deck.distribute(4, 5, DistributionOptions.DEFAULT.hidden());

// Draw a single card from the top
Card card = deck.draw();
```

```
`distributeAll` deals round-robin and allows an uneven split. `distributeAllEvenly` throws `UnevenNumberOfCardsPerPlayerException` if the deck doesn't divide evenly. Any distribute method accepts a `DistributionOptions`; pass `DistributionOptions.DEFAULT.hidden()` to deal the cards face down.
```

- [ ] **Step 3: Verify the project still builds and tests pass**

Run: `mvn test`
Expected: BUILD SUCCESS. (Javadoc and README are non-functional; this confirms nothing was broken by editing the source file.)

- [ ] **Step 4: Commit**

```bash
git add src/main/java/org/kevinkib/cards/domain/deck/Deck.java README.md
git commit -m "docs: javadoc all Deck distribute methods and update README"
```

---

## Self-Review

**Spec coverage:**
- Shared core (request #1) → Task 1 (`distributeCards`).
- Deal already hidden (request #2) → Task 2 (`DistributionOptions.hidden()` + core `hide()`).
- Javadoc on all distribute methods (request #3) → Task 3 Step 1.
- `forced` → `distributeAllEvenly` method → Task 1 (made public) + Task 2 (options overload, flag removed).
- `DistributionOptions` reshape (keep `DEFAULT`, remove `IDENTICAL_CARDS_NUMBER`, add `defaults`/`hidden`/`shown`) → Task 2 Step 1.
- Test migration of the two `IDENTICAL_CARDS_NUMBER` sites → Task 2 Step 3.
- README fixups → Task 3 Step 2.
- Behavior preserved (default SHOWN, exceptions intact, `SHOULD_ALWAYS_DISTRIBUTE` guard) → Task 1 + Task 2 method bodies.

**Type consistency:** `distributeCards(Integer, int)` in Task 1 becomes `distributeCards(Integer, int, Visibility)` in Task 2 — the only signature evolution, and every caller is updated in the same task. `DistributionOptions.visibility()` is the record accessor used by `Deck`. `hidden()`/`shown()`/`defaults()`/`DEFAULT` names match between `DistributionOptions.java` and their uses in `Deck` and tests.

**Placeholder scan:** No TBDs; every code step shows complete code and every run step states the expected outcome.

**Scope:** Single subsystem (`Deck` distribution); one cohesive plan.

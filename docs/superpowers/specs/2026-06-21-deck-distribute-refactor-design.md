# Deck distribution: shared core, hidden dealing, and javadoc

**Date:** 2026-06-21
**Status:** Approved design

## Goals

Three changes to `Deck`:

1. **Single shared core.** All distribution methods funnel into one private method instead of each looping over the deck independently.
2. **Deal cards already hidden.** Callers can deal a hand whose cards start face-down (`Visibility.HIDDEN`).
3. **Javadoc** on every public `distribute*` method.

A design decision falls out of (2): how to carry the "hidden" option without it colliding with the existing even-distribution flag. The chosen model also fixes the API so it scales to future options without an overload explosion.

## Background

`Card` already carries a `Visibility` (`SHOWN` / `HIDDEN`) and exposes `hide()` / `show()`. Cards default to `SHOWN`. "Deal already hidden" therefore means: call `hide()` on each card as it is placed into a hand.

Today `Deck` has:

- `distribute(nbPlayers, nbCardsPerPlayer)` — deals an exact count per player (evenness guaranteed by construction).
- `distributeAll(nbPlayers)` and `distributeAll(nbPlayers, DistributionOptions)` — deals the whole deck; the `isIdenticalCardsNumberForced` flag makes it throw when the deck does not divide evenly.
- `distributeAllEvenly(nbPlayers)` — private helper.

Both public loops produce the **same** card→player assignment: card `i` goes to player `i % nbPlayers`. They differ only in how many cards are dealt (`nbPlayers * nbCardsPerPlayer` vs. the whole deck). This is why a single shared core is possible.

## Key design decision: `forced` becomes a method, not a flag

`isIdenticalCardsNumberForced` is meaningful only when dealing the whole deck — the count-based `distribute` is always even by construction. Keeping it as a flag in a shared options object would force `distribute` to carry a field it must ignore. Modeling "hidden" as a second flag in the same object would then create a `2^n` overload problem as more options are added.

Resolution: **promote `forced` to its own method** (`distributeAllEvenly`) and let `DistributionOptions` carry only universal concerns. Every distribute method then takes the same options object, and no method ignores any field. New options are added as a field plus a `with…`-style method — never as new overloads.

This is a minor breaking change at version 0.2.0 (`DistributionOptions.IDENTICAL_CARDS_NUMBER` is removed). Accepted.

## Design

### Shared private core

```java
private List<Hand> distributeCards(Integer nbPlayers, int totalCards, Visibility visibility) {
    List<Hand> hands = createHands(nbPlayers);
    for (int cardIndex = 0; cardIndex < totalCards; ++cardIndex) {
        Card card = cards.pop();
        if (visibility.isHidden()) {
            card.hide();
        }
        hands.get(cardIndex % nbPlayers).add(card);
    }
    return hands;
}
```

- `distribute` calls it with `totalCards = nbPlayers * nbCardsPerPlayer`.
- `distributeAll` / `distributeAllEvenly` call it with `totalCards = getSize()` (the deck size at call time, captured before popping).

### `DistributionOptions`

```java
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

- `DEFAULT` is **kept** (used in README and as the delegation target for no-options overloads).
- `IDENTICAL_CARDS_NUMBER` is **removed**.
- Future options: add a record field + a `with…` method; no new distribute overloads.

### Public method surface

Three pairs. Each pair is a convenience overload (defaults) plus an options overload. All bodies funnel through `distributeCards`.

| Method | Deals | Throws | Delegates to |
|---|---|---|---|
| `distribute(players, count)` | exact count each | `CannotDistributeDeckException` | `distribute(players, count, DistributionOptions.DEFAULT)` |
| `distribute(players, count, opts)` | exact count each | `CannotDistributeDeckException` | `distributeCards(...)` after size check |
| `distributeAll(players)` | whole deck, uneven OK | — | `distributeAll(players, DistributionOptions.DEFAULT)` |
| `distributeAll(players, opts)` | whole deck, uneven OK | — | `distributeCards(...)` |
| `distributeAllEvenly(players)` | whole deck, even required | `UnevenNumberOfCardsPerPlayerException` | `distributeAllEvenly(players, DistributionOptions.DEFAULT)` |
| `distributeAllEvenly(players, opts)` | whole deck, even required | `UnevenNumberOfCardsPerPlayerException` | even check, then `distribute(players, perPlayer, opts)` |

`distributeAllEvenly` is promoted from private to public.

### Behavior preserved

- `distribute` keeps its `CannotDistributeDeckException` when the requested count exceeds deck size.
- `distributeAllEvenly` keeps its `UnevenNumberOfCardsPerPlayerException` when the deck does not divide evenly.
- Default visibility is `SHOWN`, so all existing no-options call sites behave exactly as before.
- The `SHOULD_ALWAYS_DISTRIBUTE` invariant (internal `IllegalStateException` guard) is retained where `distributeAllEvenly` delegates to `distribute` after its own checks.

## Javadoc (request #3)

Add javadoc to each public method: `distribute` (both), `distributeAll` (both), `distributeAllEvenly` (both). Each documents: what it deals, the meaning of `DistributionOptions` (notably `visibility` / hidden dealing), and the exception conditions. The private `distributeCards` gets a brief comment describing the shared assignment rule (`i % nbPlayers`).

## Breaking-change fixups (included in this change)

- **`DeckTest`** (`src/test/java/org/kevinkib/cards/domain/DeckTest.java`):
  - Lines using `distributeAll(n, DistributionOptions.IDENTICAL_CARDS_NUMBER)` → `distributeAllEvenly(n)`.
  - Add coverage for dealing hidden: distribute with `DistributionOptions.DEFAULT.hidden()` and assert dealt cards report `isHidden()`; assert default distribution still reports `isShown()`.
- **`README.md`**:
  - Replace the `IDENTICAL_CARDS_NUMBER` explanation with `distributeAllEvenly`.
  - Document the `hidden()` option.
  - Verify the `DistributionOptions.DEFAULT` example still compiles against the new record.

## Testing

- Existing distribution tests pass unchanged except the two `IDENTICAL_CARDS_NUMBER` call sites.
- New tests: hidden dealing on `distribute`, `distributeAll`, and `distributeAllEvenly`; default-shown assertion.
- Even/uneven behavior of `distributeAllEvenly` covered as before.

## Out of scope

- No change to `Card`, `Hand`, `Visibility`, or `DeckFactory`.
- No new distribution concerns beyond `visibility` (the options object is structured to accept them later).

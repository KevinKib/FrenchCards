# FrenchCards

A Java library providing reusable backend primitives for card games — deck creation, dealing, hand management, and pile event handling — so you can focus on game rules rather than card mechanics.

## Installation

This package is published to [GitHub Packages](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-apache-maven-registry). Add the repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>https://maven.pkg.github.com/KevinKib/FrenchCards</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>org.kevinkib</groupId>
        <artifactId>frenchcards</artifactId>
        <version>0.2.0</version>
    </dependency>
</dependencies>
```

GitHub Packages requires authentication even for public packages. Add your credentials to `~/.m2/settings.xml`:

```xml
<servers>
    <server>
        <id>github</id>
        <username>YOUR_GITHUB_USERNAME</username>
        <password>YOUR_GITHUB_TOKEN</password>
    </server>
</servers>
```

## Core concepts

A `Deck` is the source of cards — you create one via `CardsService`, then either deal cards into `Hand`s (one per player) or draw them one at a time. A `Hand` represents a player's current cards; they can play specific cards or play off the top. A `Pile` is a shared play area that fires events to any registered `PileSubscriber` when cards are added or the pile is cleared.

## Usage

### 1. Create a deck

```java
CardsService service = new CardsService();

// Standard 52-card French deck (shuffled)
Deck deck = service.createDeck(DeckType.FRENCH);

// With jokers (54 cards)
Deck deck = service.createDeck(DeckType.FRENCH_WITH_JOKERS);

// Cards start face-down (hidden)
Deck deck = service.createDeck(DeckType.FRENCH,
    new DeckCreationOptions(Visibility.HIDDEN));

// Reproducible shuffle: pass a seed (same seed => same order)
Deck deck = service.createDeck(DeckType.FRENCH,
    new DeckCreationOptions(Visibility.SHOWN, 42L));
```

By default each deck is shuffled with a fresh random source. Supplying a seed makes the shuffle deterministic — handy for tests, replays, and debugging.

### 2. Deal to players

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

`distributeAll` deals round-robin and allows an uneven split. `distributeAllEvenly` throws `UnevenNumberOfCardsPerPlayerException` if the deck doesn't divide evenly. Any distribute method accepts a `DistributionOptions`; pass `DistributionOptions.DEFAULT.hidden()` to deal the cards face down.

### 3. Play cards from a hand

```java
Hand hand = hands.get(0);

// Play a specific card (throws CannotPlayNonPossessedCardException if not held)
hand.play(card);

// Play the card at the top of the hand
Card played = hand.playCardOnTop();

// Inspect without playing
List<Card> cards = hand.getCards();
boolean hasCards = hand.hasAnyCards();
```

### 4. Use a pile

```java
Pile pile = new Pile();

// Add a card face-up (default)
pile.add(card);

// Add a card face-down
pile.add(card, Visibility.HIDDEN);

// Add to the bottom
pile.addBelow(card);

// Peek at the top card without removing it
Card top = pile.seeCardOnTop();

// Draw the top card
Card drawn = pile.draw();

// Clear and reclaim all cards (e.g. to reshuffle)
List<Card> returned = pile.clearAndReturnCards();
```

### 5. React to pile events

Implement `PileSubscriber` to respond when cards are added or the pile is cleared:

```java
pile.subscribe(new PileSubscriber() {
    @Override
    public void onCardAdded(Pile pile, Card addedCard, PilePosition position) {
        // e.g. check win condition, update UI state
    }

    @Override
    public void onClear(Pile pile) {
        // e.g. log, trigger reshuffle logic
    }
});
```

### 6. Inspect a card

A card carries a rank, a suit, and a binary `Visibility` (`SHOWN` / `HIDDEN`). Visibility means "can this card be seen from the structure it sits in" — face-up vs face-down — and its exact meaning is left to your game's rules.

```java
card.isShown();      // true when face-up
card.isHidden();     // true when face-down
card.show();         // flip face-up
card.hide();         // flip face-down
card.getRank();      // e.g. FrenchRank.ACE
card.getSuit();      // e.g. FrenchSuit.SPADE
card.toString();     // e.g. "ACE of SPADE" (handy for logging)
```

`FrenchRank` also exposes `getStrength()` (2–15, where ACE=14, JOKER=15) and `getValue()` (card point value, e.g. for Blackjack-style scoring).

## What this library does NOT handle

- Game rules or win conditions
- Scoring logic
- Networking or multiplayer state synchronization
- Persistence
- UI or rendering

These are intentionally left to the consuming application.

## Known limitations

- **Cards are equal by rank and suit only.** Two cards with the same rank and suit are considered equal (and have the same hash), regardless of visibility. This is by design and keeps the model simple, but it means games that use more than one deck cannot distinguish duplicate cards through equality — operations like `Hand.play(card)`, `Hand.possesses(card)`, and `Deck.remove(card)` act on the first matching card. If your game needs to tell identical cards apart, track identity at your own layer.

## Test helpers

Test helpers under `org.kevinkib.cards.testhelpers` make writing game tests easier. They ship as a separate **test-jar** so the main artifact stays free of test dependencies — add it to your build only where you need it:

```xml
<dependency>
    <groupId>org.kevinkib</groupId>
    <artifactId>frenchcards</artifactId>
    <version>0.2.0</version>
    <type>test-jar</type>
    <scope>test</scope>
</dependency>
```

The helpers rely on JUnit and Hamcrest, so pull those into `test` scope yourself if you don't already.

```java
Card card = CardBuilder.aCard()
    .withRank(FrenchRank.ACE)
    .withSuit(FrenchSuit.SPADE)
    .build();

Hand hand = HandBuilder.aHand().withCards(List.of(card)).build();
```

`CardFixtures`, `HandFixtures`, and `PileFixtures` provide ready-made instances for common test scenarios.

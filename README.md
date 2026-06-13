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
        <version>0.1.0</version>
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
```

### 2. Deal to players

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

`distributeAll` with the default options distributes cards round-robin without requiring an equal split. Pass `DistributionOptions.IDENTICAL_CARDS_NUMBER` to throw `UnevenNumberOfCardsPerPlayerException` if the deck doesn't divide evenly.

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
```

`FrenchRank` also exposes `getStrength()` (2–15, where ACE=14, JOKER=15) and `getValue()` (card point value, e.g. for Blackjack-style scoring).

## What this library does NOT handle

- Game rules or win conditions
- Scoring logic
- Networking or multiplayer state synchronization
- Persistence
- UI or rendering

These are intentionally left to the consuming application.

## Test helpers

The library ships test helpers under `org.kevinkib.cards.testhelpers` to make writing game tests easier:

```java
Card card = CardBuilder.aCard()
    .withRank(FrenchRank.ACE)
    .withSuit(FrenchSuit.SPADE)
    .build();

Hand hand = HandBuilder.aHand().withCards(List.of(card)).build();
```

`CardFixtures`, `HandFixtures`, and `PileFixtures` provide ready-made instances for common test scenarios.

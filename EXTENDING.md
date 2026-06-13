# Extending FrenchCards

This guide covers adding new deck types, custom ranks/suits, and pile event listeners. Extend the library when the built-in French deck doesn't fit your game — otherwise prefer consuming it as-is via `CardsService`.

## Adding a new deck type

**1. Add your type to `DeckType`**

```java
public enum DeckType {
    FRENCH,
    FRENCH_WITH_JOKERS,
    MY_CUSTOM_DECK   // add your type here
}
```

**2. Implement `DeckFactory`**

```java
public class MyDeckFactory extends DeckFactory {

    @Override
    public Deck generate(DeckType deckType, DeckCreationOptions options) {
        List<Card> cards = new ArrayList<>();
        // build your card list using your custom Rank/Suit implementations
        // pass options.visibility() to each Card to control whether it starts shown or hidden

        // honour a caller-provided seed so shuffles are reproducible
        Random random = options.hasSeed() ? new Random(options.seed()) : new Random();
        return new Deck(cards, random);
    }

    @Override
    public boolean canHandle(DeckType deckType) {
        return deckType == DeckType.MY_CUSTOM_DECK;
    }
}
```

**3. Register your factory with `CardsService`**

Pass your factories (with or without the built-in ones) to the `CardsService` constructor:

```java
CardsService service = new CardsService(List.of(
    new FrenchDeckFactory(),
    new MyDeckFactory()
));

Deck deck = service.createDeck(DeckType.MY_CUSTOM_DECK);
```

The no-arg `new CardsService()` still defaults to just the French factory. You can also bypass the service entirely and call your factory directly:

```java
Deck deck = new MyDeckFactory().generate(DeckType.MY_CUSTOM_DECK);
```

## Adding custom Ranks and Suits

Implement the `Rank` interface for custom rank logic:

```java
public enum MyRank implements Rank {
    LOW(1), HIGH(2);

    private final Integer strength;

    MyRank(Integer strength) { this.strength = strength; }

    @Override
    public Integer getStrength() { return strength; }
}
```

`Suit` is a marker interface with no required methods — implement it to identify your suit type:

```java
public enum MySuit implements Suit {
    RED, BLUE
}
```

Cards accept any `Rank` and `Suit` implementation:

```java
Card card = new Card(MyRank.HIGH, MySuit.RED);
```

## Reacting to Pile events

Implement `PileSubscriber` and register it on any `Pile`:

```java
public class MyGameListener implements PileSubscriber {

    @Override
    public void onCardAdded(Pile pile, Card addedCard, PilePosition position) {
        // position is PilePosition.TOP (add) or PilePosition.BOTTOM (addBelow)
    }

    @Override
    public void onClear(Pile pile) {
        // fired when pile.clear() or pile.clearAndReturnCards() is called
    }
}

pile.subscribe(new MyGameListener());
// pile.unsubscribe(listener) to remove
```

Multiple subscribers are supported; they are notified in registration order.

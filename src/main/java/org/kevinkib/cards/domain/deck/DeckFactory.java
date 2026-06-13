package org.kevinkib.cards.domain.deck;

public abstract class DeckFactory {

    public Deck generate(DeckType deckType) {
        return generate(deckType, DeckCreationOptions.DEFAULT);
    }

    public abstract Deck generate(DeckType deckType, DeckCreationOptions options);

    public abstract boolean canHandle(DeckType deckType);

}

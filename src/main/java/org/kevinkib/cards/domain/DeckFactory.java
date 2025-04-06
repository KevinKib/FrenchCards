package org.kevinkib.cards.domain;

public abstract class DeckFactory {

    public Deck generate(DeckType deckType) {
        return generate(deckType, DeckCreationOptions.DEFAULT.getCardState());
    }

    public abstract Deck generate(DeckType deckType, CardState cardState);

    public abstract boolean canHandle(DeckType deckType);

}

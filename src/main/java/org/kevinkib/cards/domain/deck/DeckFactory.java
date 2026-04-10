package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.CardState;

public abstract class DeckFactory {

    public Deck generate(DeckType deckType) {
        return generate(deckType, DeckCreationOptions.DEFAULT.getCardState());
    }

    public abstract Deck generate(DeckType deckType, CardState cardState);

    public abstract boolean canHandle(DeckType deckType);

}

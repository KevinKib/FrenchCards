package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.Visibility;

public abstract class DeckFactory {

    public Deck generate(DeckType deckType) {
        return generate(deckType, DeckCreationOptions.DEFAULT.visibility());
    }

    public abstract Deck generate(DeckType deckType, Visibility visibility);

    public abstract boolean canHandle(DeckType deckType);

}

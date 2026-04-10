package org.kevinkib.cards;

import org.kevinkib.cards.domain.deck.*;
import org.kevinkib.cards.domain.deck.french.FrenchDeckFactory;

import java.util.Arrays;
import java.util.List;

public class CardsService {

    public Deck createDeck(DeckType type) {
        return createDeck(type, DeckCreationOptions.DEFAULT);
    }

    public Deck createDeck(DeckType type, DeckCreationOptions options) {
        for (DeckFactory factory : getFactories()) {
            if (factory.canHandle(type)) {
                return factory.generate(type, options.getCardState());
            }
        }

        throw new UnhandledDeckTypeException(type);
    }

    private List<DeckFactory> getFactories() {
        return Arrays.asList(new FrenchDeckFactory());
    }

}

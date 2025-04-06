package org.kevinkib.cards;

import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckFactory;
import org.kevinkib.cards.domain.DeckType;
import org.kevinkib.cards.domain.UnhandledDeckTypeException;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;

import java.util.Arrays;
import java.util.List;

public class CardsController {

    public Deck createDeck(DeckType type) {
        for (DeckFactory factory : getFactories()) {
            if (factory.canHandle(type)) {
                return factory.generate(type);
            }
        }

        throw new UnhandledDeckTypeException(type);
    }

    private List<DeckFactory> getFactories() {
        return Arrays.asList(new FrenchDeckFactory());
    }

}

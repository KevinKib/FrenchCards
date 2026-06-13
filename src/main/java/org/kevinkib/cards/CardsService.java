package org.kevinkib.cards;

import org.kevinkib.cards.domain.deck.*;
import org.kevinkib.cards.domain.deck.french.FrenchDeckFactory;

import java.util.ArrayList;
import java.util.List;

public class CardsService {

    private final List<DeckFactory> factories;

    public CardsService() {
        this(defaultFactories());
    }

    public CardsService(List<DeckFactory> factories) {
        this.factories = new ArrayList<>(factories);
    }

    public Deck createDeck(DeckType type) {
        return createDeck(type, DeckCreationOptions.DEFAULT);
    }

    public Deck createDeck(DeckType type, DeckCreationOptions options) {
        for (DeckFactory factory : factories) {
            if (factory.canHandle(type)) {
                return factory.generate(type, options);
            }
        }

        throw new UnhandledDeckTypeException(type);
    }

    private static List<DeckFactory> defaultFactories() {
        return List.of(new FrenchDeckFactory());
    }

}

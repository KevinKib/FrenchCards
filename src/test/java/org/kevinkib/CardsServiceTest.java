package org.kevinkib;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.CardsService;
import org.kevinkib.cards.domain.Visibility;
import org.kevinkib.cards.domain.deck.Deck;
import org.kevinkib.cards.domain.deck.DeckFactory;
import org.kevinkib.cards.domain.deck.DeckType;
import org.kevinkib.cards.domain.deck.UnhandledDeckTypeException;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CardsServiceTest {

    private final CardsService service = new CardsService();

    @Test
    public void givenNullDeckType_whenCreateDeck_thenThrowUnhandledDeckTypeException() {
        assertThrows(UnhandledDeckTypeException.class, () -> {
            service.createDeck(null);
        });
    }

    @Test
    public void givenFrenchDeckType_whenCreateDeck_thenReturnDeck() {
        Deck deck = service.createDeck(DeckType.FRENCH);
        assertNotNull(deck);
    }

    @Test
    public void givenInjectedFactory_whenCreateDeck_thenUsesInjectedFactory() {
        Deck customDeck = new Deck(List.of());
        CardsService customService = new CardsService(List.of(factoryReturning(customDeck)));

        Deck deck = customService.createDeck(DeckType.FRENCH);

        assertThat(deck, is(sameInstance(customDeck)));
    }

    private static DeckFactory factoryReturning(Deck deck) {
        return new DeckFactory() {
            @Override
            public Deck generate(DeckType deckType, Visibility visibility) {
                return deck;
            }

            @Override
            public boolean canHandle(DeckType deckType) {
                return deckType == DeckType.FRENCH;
            }
        };
    }

}

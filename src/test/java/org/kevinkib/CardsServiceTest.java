package org.kevinkib;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.CardsService;
import org.kevinkib.cards.domain.deck.Deck;
import org.kevinkib.cards.domain.deck.DeckType;
import org.kevinkib.cards.domain.deck.UnhandledDeckTypeException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
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

}

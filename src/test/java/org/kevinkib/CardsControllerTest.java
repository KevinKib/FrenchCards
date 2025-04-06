package org.kevinkib;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.CardsController;
import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckType;
import org.kevinkib.cards.domain.UnhandledDeckTypeException;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class CardsControllerTest {

    private final CardsController service = new CardsController();

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

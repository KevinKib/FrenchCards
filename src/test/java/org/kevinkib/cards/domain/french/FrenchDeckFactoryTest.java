package org.kevinkib.cards.domain.french;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.domain.Deck;
import org.kevinkib.cards.domain.DeckType;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class FrenchDeckFactoryTest {

    @Test
    public void givenFrenchDeckType_whenGenerating_thenReturn52Cards() {
        FrenchDeckFactory factory = new FrenchDeckFactory();

        Deck deck = factory.generate(DeckType.FRENCH);

        assertThat(deck.getSize(), is(52));
    }

    @Test
    public void givenFrenchWithJokersDeckType_whenGenerating_thenReturn54Cards() {
        FrenchDeckFactory factory = new FrenchDeckFactory();

        Deck deck = factory.generate(DeckType.FRENCH_WITH_JOKERS);

        assertThat(deck.getSize(), is(54));
    }

}

package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.testhelpers.CardBuilder;
import org.kevinkib.cards.testhelpers.CardFixtures;
import org.kevinkib.cards.testhelpers.PileFixtures;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class PileTest {

    @Test
    public void givenHiddenState_whenAdd_thenCardShouldBeHidden() {
        Card card = CardBuilder.aCard().build();
        Pile pile = PileFixtures.createEmptyPile();

        pile.add(card, CardPileState.HIDDEN);

        assertThat(pile.seeCardOnTop().getState(), is(CardPileState.HIDDEN));
    }

    @Test
    public void givenShownState_whenAdd_thenCardShouldBeShown() {
        Card card = CardBuilder.aCard().build();
        Pile pile = PileFixtures.createEmptyPile();

        pile.add(card, CardPileState.SHOWN);

        assertThat(pile.seeCardOnTop().getState(), is(CardPileState.SHOWN));
    }

    @Test
    public void givenMultipleCards_whenAdd_thenCardsShouldBeAdded() {
        int nbCards = 4;

        List<Card> cards = CardFixtures.createNumberOfCards(nbCards);
        Pile pile = PileFixtures.createEmptyPile();

        pile.add(cards);

        assertThat(pile.getSize(), is(nbCards));
    }
}
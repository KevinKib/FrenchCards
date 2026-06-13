package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.domain.deck.french.FrenchRank;
import org.kevinkib.cards.domain.pile.Pile;
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

        pile.add(card, Visibility.HIDDEN);

        assertThat(pile.seeCardOnTop().getVisibility(), is(Visibility.HIDDEN));
    }

    @Test
    public void givenShownState_whenAdd_thenCardShouldBeShown() {
        Card card = CardBuilder.aCard().build();
        Pile pile = PileFixtures.createEmptyPile();

        pile.add(card, Visibility.SHOWN);

        assertThat(pile.seeCardOnTop().getVisibility(), is(Visibility.SHOWN));
    }

    @Test
    public void givenMultipleCards_whenAdd_thenCardsShouldBeAdded() {
        int nbCards = 4;

        List<Card> cards = CardFixtures.createNumberOfCards(nbCards);
        Pile pile = PileFixtures.createEmptyPile();

        pile.add(cards);

        assertThat(pile.getSize(), is(nbCards));
    }

    @Test
    public void givenNewCard_whenAdd_thenNewCardShouldBeAddedFirst() {
        int nbCards = 4;

        List<Card> cards = CardFixtures.createNumberOfCards(nbCards);
        Pile pile = PileFixtures.createEmptyPile();

        pile.add(cards);

        Card newCard = CardBuilder.aCard().withRank(FrenchRank.ACE).build();
        pile.add(newCard);

        assertThat(pile.seeCardOnTop(), is(newCard));
    }

    @Test
    public void givenCards_whenClearAndReturnCards_thenShouldBeEmpty() {
        int anyNumberOfCards = 5;
        Pile pile = PileFixtures.createPileWithNumberOfCards(anyNumberOfCards);

        List<Card> cards = pile.clearAndReturnCards();

        assertThat(cards.size(), is(anyNumberOfCards));
        assertThat(pile.isEmpty(), is(true));
    }
}
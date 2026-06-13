package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.domain.deck.french.FrenchRank;
import org.kevinkib.cards.domain.pile.Pile;
import org.kevinkib.cards.domain.pile.PilePosition;
import org.kevinkib.cards.domain.pile.PileSubscriber;
import org.kevinkib.cards.testhelpers.CardBuilder;
import org.kevinkib.cards.testhelpers.CardFixtures;
import org.kevinkib.cards.testhelpers.PileFixtures;

import java.util.ArrayList;
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

    @Test
    public void givenSubscriber_whenAdd_thenNotifiedOfCardAddedOnTop() {
        Pile pile = PileFixtures.createEmptyPile();
        RecordingSubscriber subscriber = new RecordingSubscriber();
        pile.subscribe(subscriber);

        Card card = CardBuilder.aCard().withRank(FrenchRank.ACE).build();
        pile.add(card);

        assertThat(subscriber.added, is(List.of(card)));
        assertThat(subscriber.positions, is(List.of(PilePosition.TOP)));
    }

    @Test
    public void givenSubscriber_whenAddBelow_thenNotifiedAtBottom() {
        Pile pile = PileFixtures.createEmptyPile();
        RecordingSubscriber subscriber = new RecordingSubscriber();
        pile.subscribe(subscriber);

        pile.addBelow(CardBuilder.aCard().build());

        assertThat(subscriber.positions, is(List.of(PilePosition.BOTTOM)));
    }

    @Test
    public void givenSubscriber_whenClear_thenNotified() {
        Pile pile = PileFixtures.createPileWithNumberOfCards(3);
        RecordingSubscriber subscriber = new RecordingSubscriber();
        pile.subscribe(subscriber);

        pile.clear();

        assertThat(subscriber.clearCount, is(1));
    }

    @Test
    public void givenUnsubscribedSubscriber_whenAdd_thenNotNotified() {
        Pile pile = PileFixtures.createEmptyPile();
        RecordingSubscriber subscriber = new RecordingSubscriber();
        pile.subscribe(subscriber);
        pile.unsubscribe(subscriber);

        pile.add(CardBuilder.aCard().build());

        assertThat(subscriber.added.isEmpty(), is(true));
    }

    private static class RecordingSubscriber implements PileSubscriber {
        final List<Card> added = new ArrayList<>();
        final List<PilePosition> positions = new ArrayList<>();
        int clearCount = 0;

        @Override
        public void onCardAdded(Pile pile, Card addedCard, PilePosition pilePosition) {
            added.add(addedCard);
            positions.add(pilePosition);
        }

        @Override
        public void onClear(Pile pile) {
            clearCount++;
        }
    }
}
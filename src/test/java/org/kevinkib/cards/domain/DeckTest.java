package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.domain.deck.CannotDistributeDeckException;
import org.kevinkib.cards.domain.deck.Deck;
import org.kevinkib.cards.domain.deck.DistributionOptions;
import org.kevinkib.cards.domain.deck.UnevenNumberOfCardsPerPlayerException;
import org.kevinkib.cards.domain.deck.french.FrenchRank;
import org.kevinkib.cards.domain.deck.french.FrenchSuit;
import org.kevinkib.cards.domain.hand.Hand;
import org.kevinkib.cards.testhelpers.CardBuilder;
import org.kevinkib.cards.testhelpers.DeckBuilder;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DeckTest {

    private Deck deck;

    @Test
    public void canDraw() {
        Card expectedCard = CardBuilder.aCard()
                .withRank(FrenchRank.ACE)
                .withSuit(FrenchSuit.DIAMOND)
                .build();

        deck = DeckBuilder.aDeck()
                .withCards(new LinkedList<>(Arrays.asList(
                        expectedCard,
                        CardBuilder.aCard().build()
                )))
                .build();

        Card card = deck.draw();

        assertThat(deck.getSize(), is(1));
        assertThat(card, is(expectedCard));
    }

    @Test
    public void givenPlayers_andNbCards_whenDistribute_thenDistributeCards() {
        deck = DeckBuilder.aDeck()
                .withNumberOfCards(30)
                .build();

        int nbPlayers = 3;
        int nbCardsPerPlayer = 10;

        assertDoesNotThrow(() -> {
            List<Hand> hands = deck.distribute(nbPlayers, nbCardsPerPlayer);

            for (int i = 0; i < nbPlayers; ++i) {
                Hand hand = hands.get(i);
                assertThat(hand.getSize(), is(nbCardsPerPlayer));
            }
        });
    }

    @Test
    public void givenTooManyPlayersAndCards_whenDistribute_thenThrowCannotDistributeDeckException() {
        deck = DeckBuilder.aDeck()
                .withNumberOfCards(30)
                .build();

        Integer nbPlayers = 6;
        Integer nbCardsPerPlayer = 300;

        assertThrows(CannotDistributeDeckException.class, () -> {
            deck.distribute(nbPlayers, nbCardsPerPlayer);
        });
    }

    @Test
    public void givenPlayers_whenDistributeAll_thenDistributeAllCards() {
        deck = DeckBuilder.aDeck()
                .withNumberOfCards(30)
                .build();

        int nbPlayers = 3;
        int expectedNbCardsPerPlayer = deck.getSize() / nbPlayers;

        assertDoesNotThrow(() -> {
            List<Hand> hands = deck.distributeAll(nbPlayers);

            for (int i = 0; i < nbPlayers; ++i) {
                Hand hand = hands.get(i);
                assertThat(hand.getSize(), is(expectedNbCardsPerPlayer));
            }
        });
    }

    @Test
    public void givenIncompatibleNbPlayersAndCards_whenDistributeAll_thenDistributeAllCardsUnevenly() {

        int nbPlayers = 3;
        int nbCardsPerPlayer = 10;
        int totalNbCards = nbPlayers * nbCardsPerPlayer + 1;

        deck = DeckBuilder.aDeck()
                .withNumberOfCards(totalNbCards)
                .build();

        assertDoesNotThrow(() -> {
            List<Hand> hands = deck.distributeAll(nbPlayers);

            assertThat(hands.get(0).getSize(), is(nbCardsPerPlayer + 1));
            assertThat(hands.get(1).getSize(), is(nbCardsPerPlayer));
            assertThat(hands.get(2).getSize(), is(nbCardsPerPlayer));
        });
    }

    @Test
    public void givenPlayers_whenDistributeAllEvenly_thenDistributeAllCards() {
        deck = DeckBuilder.aDeck()
                .withNumberOfCards(30)
                .build();

        int nbPlayers = 3;
        int expectedNbCardsPerPlayer = deck.getSize() / nbPlayers;

        assertDoesNotThrow(() -> {
            List<Hand> hands = deck.distributeAllEvenly(nbPlayers);

            for (int i = 0; i < nbPlayers; ++i) {
                Hand hand = hands.get(i);
                assertThat(hand.getSize(), is(expectedNbCardsPerPlayer));
            }
        });
    }

    @Test
    public void givenIncompatibleNbPlayersAndCards_whendistributeAllEvenly_thenThrowUnevenNumberOfCardsPerPlayerException() {
        deck = DeckBuilder.aDeck()
                .withNumberOfCards(31)
                .build();

        int nbPlayers = 3;

        assertThrows(UnevenNumberOfCardsPerPlayerException.class, () -> {
            deck.distributeAllEvenly(nbPlayers);
        });
    }

}

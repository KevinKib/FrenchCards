package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.domain.deck.french.FrenchRank;
import org.kevinkib.cards.domain.hand.CannotPlayNonPossessedCardException;
import org.kevinkib.cards.domain.hand.Hand;
import org.kevinkib.cards.domain.hand.NoCardsException;
import org.kevinkib.cards.testhelpers.CardBuilder;
import org.kevinkib.cards.testhelpers.HandBuilder;

import java.util.Arrays;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HandTest {

    @Test
    public void givenCardNotInHand_whenPlay_throwCannotPlayNonPossessedCardException() {
        Hand hand = HandBuilder.aHand()
                .withNoCards()
                .build();

        Card nonPossessedCard = CardBuilder.aCard().build();

        assertThrows(CannotPlayNonPossessedCardException.class, () -> {
            hand.play(nonPossessedCard);
        });
    }

    @Test
    public void givenCardInHand_whenPlay_thenRemoveCard() {
        Card card = CardBuilder.aCard().build();

        Hand hand = HandBuilder.aHand()
                .withCards(Arrays.asList(card))
                .build();

        assertDoesNotThrow(() -> {
            hand.play(card);
        });

        assertThat(hand.getSize(), is(0));
    }

    @Test
    public void givenNoCardsInHand_whenPlayCardOnTop_thenThrowNoCardsException() {
        Hand hand = HandBuilder.aHand()
                .withNoCards()
                .build();

        assertThrows(NoCardsException.class, hand::playCardOnTop);
    }

    @Test
    public void givenCardsInHand_whenPlayCardOnTop_thenReturnCard() {
        Card expectedCard = CardBuilder.aCard().withRank(FrenchRank.ACE).build();

        Hand hand = HandBuilder.aHand()
                .withCards(Arrays.asList(expectedCard))
                .build();

        assertDoesNotThrow(() -> {
            Card card = hand.playCardOnTop();

            assertThat(card, is(expectedCard));
        });
    }

}
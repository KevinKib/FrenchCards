package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.testhelpers.CardBuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class CardTest {

    private final Card card = CardBuilder.aCard().build();

    @Test
    public void canPutCardInHand() {
        card.putInHand();
        assertThat(card.isShownToHoldingPlayer(), is(true));
    }

    @Test
    public void canPutCardInHand_andHideIt() {
        card.hideInHand();
        assertThat(card.isHidden(), is(true));
    }

    @Test
    public void canPutCardInHand_andShowItToOtherPlayers() {
        card.putInHandAndShowToOtherPlayers();
        assertThat(card.isShownToHoldingPlayer(), is(false));
        assertThat(card.isShownToOtherPlayers(), is(true));
    }

    @Test
    public void canShowCardInPile() {
        card.showInPile();
        assertThat(card.isShown(), is(true));
    }

    @Test
    public void canHideCardInPile() {
        card.hideInPile();
        assertThat(card.isHidden(), is(true));
    }

}

package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class CardTest {

    private final Card card = CardBuilder.aCard().build();

    @Test
    public void canPutCardInHand() {
        card.putInHand();
        assertThat(card.isInHand(), is(true));
    }

    @Test
    public void canPutCardInHand_andHideIt() {
        card.hideInHand();
        assertThat(card.isInHand(), is(true));
        assertThat(card.isHidden(), is(true));
    }

    @Test
    public void canPutCardInHand_andShowItToOtherPlayers() {
        card.putInHandAndShowToOtherPlayers();
        assertThat(card.isInHand(), is(true));
        assertThat(card.isShownToHoldingPlayer(), is(false));
        assertThat(card.isShownToOtherPlayers(), is(true));
    }

    @Test
    public void canShowCardInPile() {
        card.showInPile();
        assertThat(card.isInPile(), is(true));
        assertThat(card.isShown(), is(true));
    }

    @Test
    public void canHideCardInPile() {
        card.hideInPile();
        assertThat(card.isInPile(), is(true));
        assertThat(card.isHidden(), is(true));
    }

}

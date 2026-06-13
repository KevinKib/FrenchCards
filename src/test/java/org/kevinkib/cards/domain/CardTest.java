package org.kevinkib.cards.domain;

import org.junit.jupiter.api.Test;
import org.kevinkib.cards.testhelpers.CardBuilder;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class CardTest {

    private final Card card = CardBuilder.aCard().build();

    @Test
    public void canShowCard() {
        card.show();
        assertThat(card.isShown(), is(true));
        assertThat(card.isHidden(), is(false));
    }

    @Test
    public void canHideCard() {
        card.hide();
        assertThat(card.isHidden(), is(true));
        assertThat(card.isShown(), is(false));
    }

}

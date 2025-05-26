package org.kevinkib.cards.testhelpers;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Hand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class HandBuilder {
    private List<Card> cards;

    private HandBuilder() {
    }

    public static HandBuilder aHand() {
        return new HandBuilder();
    }

    public HandBuilder withCards(List<Card> cards) {
        this.cards = cards;
        return this;
    }

    public HandBuilder withCards(Card... cards) {
        this.cards = Arrays.stream(cards).toList();
        return this;
    }

    public HandBuilder withNoCards() {
        this.cards = Collections.emptyList();
        return this;
    }

    public Hand build() {
        return new Hand(cards);
    }
}

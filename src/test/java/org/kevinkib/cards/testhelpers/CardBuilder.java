package org.kevinkib.cards.testhelpers;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Rank;
import org.kevinkib.cards.domain.Suit;

public final class CardBuilder {
    private Rank rank;
    private Suit suit;

    private CardBuilder() {
    }

    public static CardBuilder aCard() {
        return new CardBuilder();
    }

    public CardBuilder withRank(Rank rank) {
        this.rank = rank;
        return this;
    }

    public CardBuilder withSuit(Suit suit) {
        this.suit = suit;
        return this;
    }

    public Card build() {
        return new Card(rank, suit);
    }
}

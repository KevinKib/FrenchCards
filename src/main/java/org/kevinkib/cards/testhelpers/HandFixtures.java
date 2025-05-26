package org.kevinkib.cards.testhelpers;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Hand;

import java.util.Arrays;
import java.util.Collections;

public class HandFixtures {

    public static Hand createHandWithCards(Card... cards) {
        return HandBuilder.aHand()
                .withCards(Arrays.asList(cards))
                .build();
    }

    public static Hand createHandWithNoCards() {
        return HandBuilder.aHand()
                .withCards(Collections.emptyList())
                .build();
    }

}

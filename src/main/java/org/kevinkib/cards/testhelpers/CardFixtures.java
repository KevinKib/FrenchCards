package org.kevinkib.cards.testhelpers;

import org.kevinkib.cards.domain.Card;

import java.util.ArrayList;
import java.util.List;

public class CardFixtures {

    public static List<Card> createNumberOfCards(int nbCards) {
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < nbCards; ++i) {
            cards.add(CardBuilder.aCard().build());
        }

        return cards;
    }

    public static Card anyCard() {
        return CardBuilder.aCard().build();
    }

}

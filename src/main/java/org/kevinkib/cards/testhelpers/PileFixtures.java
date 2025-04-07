package org.kevinkib.cards.testhelpers;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Pile;
import org.kevinkib.cards.domain.french.FrenchRank;

public class PileFixtures {

    public static Pile createEmptyPile() {
        return new Pile();
    }

    public static Pile createPileWithCard(Card... cards) {
        Pile pile = new Pile();
        for (Card card : cards) {
            pile.add(card);
        }
        return pile;
    }

    public static Pile createPileWithNumberOfCards(int nbCards) {
        Pile pile = new Pile();
        for (int i = 0; i < nbCards; ++i) {
            pile.add(CardBuilder.aCard().build());
        }
        return pile;
    }

    public static Pile createPileWithRank(FrenchRank... ranks) {
        Pile pile = new Pile();
        for (FrenchRank rank : ranks) {
            Card card = CardBuilder.aCard()
                    .withRank(rank)
                    .build();

            pile.add(card);
        }

        return pile;
    }

}

package org.kevinkib.cards.testhelpers;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.deck.french.FrenchRank;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static class CardRanksMatcher extends TypeSafeMatcher<List<Card>> {

        private final List<FrenchRank> expectedRanks;

        public CardRanksMatcher(List<FrenchRank> expectedRanks) {
            this.expectedRanks = expectedRanks;
        }

        @Override
        public boolean matchesSafely(List<Card> cards) {

            if (cards == null || expectedRanks == null || cards.size() != expectedRanks.size()) {
                return false;
            }

            for (int i = 0; i < cards.size(); ++i) {
                Card card = cards.get(i);
                FrenchRank rank = expectedRanks.get(i);

                if (card.getRank() == null || !card.getRank().equals(rank)) {
                    return false;
                }
            }

            return true;
        }

        public void describeTo(Description description) {
            description.appendText("same ranks");
        }

        public static Matcher<List<Card>> areCardsOfRanks(FrenchRank... expectedRanks) {
            return new CardRanksMatcher(Arrays.stream(expectedRanks).toList());
        }

    }


}

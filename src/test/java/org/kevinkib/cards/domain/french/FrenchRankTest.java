package org.kevinkib.cards.domain.french;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.kevinkib.cards.domain.deck.french.FrenchRank;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

public class FrenchRankTest {

    @Test
    public void givenEqualRanks_whenComparing_thenReturnsZero() {
        FrenchRank rank = FrenchRank.FOUR;
        assertThat(rank.getStrongest(rank), is(0));
    }

    @Test
    public void givenStrongerRank_whenComparing_thenReturnsOne() {
        FrenchRank stronger = FrenchRank.EIGHT;
        FrenchRank weaker = FrenchRank.THREE;
        assertThat(stronger.getStrongest(weaker), is(1));
    }

    @Test
    public void givenWeakerRank_whenComparing_thenReturnsMinusOne() {
        FrenchRank stronger = FrenchRank.EIGHT;
        FrenchRank weaker = FrenchRank.THREE;
        assertThat(weaker.getStrongest(stronger), is(-1));
    }

    @Nested
    public class SumTest {

        @Test
        public void givenJoker_andAnyOtherRank_thenReturnNull() {
            FrenchRank anyRank = FrenchRank.NINE;
            assertNull(FrenchRank.JOKER.sum(anyRank));
        }

        @Test
        public void givenAnyRank_andJoker_thenReturnNull() {
            FrenchRank anyRank = FrenchRank.NINE;
            assertNull(anyRank.sum(FrenchRank.JOKER));
        }

        @Test
        public void givenTwoRanks_thenReturnSum() {
            assertThat(FrenchRank.EIGHT.sum(FrenchRank.FOUR), is(12));
        }

    }

}

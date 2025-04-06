package org.kevinkib.cards.domain.french;

import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

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

}

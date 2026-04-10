package org.kevinkib.cards.domain.deck.french;

import org.kevinkib.cards.domain.Rank;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum FrenchRank implements Rank {

    TWO(2, 2),
    THREE(3, 3),
    FOUR(4, 4),
    FIVE(5, 5),
    SIX(6, 6),
    SEVEN(7, 7),
    EIGHT(8, 8),
    NINE(9, 9),
    TEN(10, 10),
    JACK(11, 10),
    QUEEN(12, 10),
    KING(13, 10),
    ACE(14, 1),
    JOKER(15, null);

    private final Integer strength;
    private final Integer value;

    FrenchRank(Integer strength, Integer value) {
        this.strength = strength;
        this.value = value;
    }

    public static List<FrenchRank> getRanks() {
        return Arrays.stream(FrenchRank.values())
                .filter(rank -> rank != FrenchRank.JOKER)
                .collect(Collectors.toList());
    }

    public static FrenchRank fromStrength(Integer strengthValue) {
        for (FrenchRank rank : FrenchRank.values()) {
            if (rank.getStrength().equals(strengthValue)) {
                return rank;
            }
        }
        throw new IllegalArgumentException("Invalid strength : "+strengthValue);
    }

    public Integer sum(FrenchRank rank) {
        if (rank == null || value == null || rank.value == null) {
            return null;
        }
        return value + rank.value;
    }

    @Override
    public Integer getStrength() {
        return strength;
    }

    public Integer getValue() {
        return value;
    }

    public int getStrongest(FrenchRank o) {
        return Integer.compare(this.getStrength(), o.getStrength());
    }

    @Override
    public String toString() {
        if (strength <= 10) {
            return strength.toString();
        }

        return name();
    }
}

package org.kevinkib.cards.domain.deck;

public record DistributionOptions(boolean isIdenticalCardsNumberForced) {

    public static DistributionOptions DEFAULT = new DistributionOptions(false);

    public static DistributionOptions IDENTICAL_CARDS_NUMBER = new DistributionOptions(true);

}

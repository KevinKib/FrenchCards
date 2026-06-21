package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.Visibility;

public record DistributionOptions(Visibility visibility) {

    public static final DistributionOptions DEFAULT = new DistributionOptions(Visibility.SHOWN);

    public DistributionOptions hidden() {
        return new DistributionOptions(Visibility.HIDDEN);
    }

    public DistributionOptions shown() {
        return new DistributionOptions(Visibility.SHOWN);
    }
}

package org.kevinkib.cards.domain.pile;

import org.kevinkib.cards.domain.Card;

public interface PileSubscriber {

    void onCardAdded(Pile pile, Card addedCard, PilePosition pilePosition);

    void onClear(Pile pile);

}

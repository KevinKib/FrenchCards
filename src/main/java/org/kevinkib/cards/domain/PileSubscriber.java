package org.kevinkib.cards.domain;

public interface PileSubscriber {

    public void onCardAdded(Pile pile, Card addedCard, PilePosition pilePosition);

    public void onClear(Pile pile);

}

package org.kevinkib.cards.domain;

public interface PileSubscriber {

    public void onCardAdded(Pile pile);

    public void onClear(Pile pile);

}

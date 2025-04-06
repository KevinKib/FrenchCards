package org.kevinkib.cards.domain;

public sealed interface CardState permits CardPileState, CardHandState {

    public boolean isInHand();

    public boolean isInPile();

    public boolean isHidden();

    public boolean isShown();

    public boolean isShownToHoldingPlayer();

    public boolean isShownToOtherPlayers();

}

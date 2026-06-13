package org.kevinkib.cards.domain;

public sealed interface CardState permits CardPileState, CardHandState {

    boolean isHidden();

    boolean isShown();

    boolean isShownToHoldingPlayer();

    boolean isShownToOtherPlayers();

}

package org.kevinkib.cards.domain;

import static org.kevinkib.cards.domain.CardPosition.*;

public enum CardPileState {

    HIDDEN(PILE, false),
    SHOWN(PILE, true);

    private final CardPosition position;
    private final boolean shown;

    CardPileState(CardPosition position, boolean shown) {
        this.position = position;
        this.shown = shown;
    }

    public boolean isShownToHoldingPlayer() {
        return shown;
    }

    public boolean isShownToOtherPlayers() {
        return shown;
    }

    public boolean isInHand() {
        return false;
    }

    public boolean isInPile() {
        return true;
    }

    public boolean isHidden() {
        return !shown;
    }

    public boolean isShown() {
        return shown;
    }
}

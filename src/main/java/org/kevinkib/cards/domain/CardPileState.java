package org.kevinkib.cards.domain;

public enum CardPileState implements CardState {

    HIDDEN(false),
    SHOWN(true);

    private final boolean shown;

    CardPileState(boolean shown) {
        this.shown = shown;
    }

    public boolean isHidden() {
        return !shown;
    }

    public boolean isShown() {
        return shown;
    }

    public boolean isShownToHoldingPlayer() {
        return shown;
    }

    public boolean isShownToOtherPlayers() {
        return shown;
    }
}

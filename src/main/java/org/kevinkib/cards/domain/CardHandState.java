package org.kevinkib.cards.domain;

public enum CardHandState implements CardState {

    SHOWN_IN_HAND(true, false),
    HIDDEN_IN_HAND(false, false),
    IN_HAND_SHOWN_TO_OTHER_PLAYERS(false, true);

    private final boolean shownToHoldingPlayer;
    private final boolean shownToOtherPlayers;

    CardHandState(boolean shownToHoldingPlayer, boolean shownToOtherPlayers) {
        this.shownToHoldingPlayer = shownToHoldingPlayer;
        this.shownToOtherPlayers = shownToOtherPlayers;
    }

    public boolean isHidden() {
        return !isShownToHoldingPlayer() && !isShownToOtherPlayers();
    }

    public boolean isShown() {
        return isShownToHoldingPlayer() && isShownToOtherPlayers();
    }

    public boolean isShownToHoldingPlayer() {
        return shownToHoldingPlayer;
    }

    public boolean isShownToOtherPlayers() {
        return shownToOtherPlayers;
    }

}

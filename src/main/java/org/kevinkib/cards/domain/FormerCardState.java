package org.kevinkib.cards.domain;

import static org.kevinkib.cards.domain.CardPosition.*;

public enum FormerCardState {

    HIDDEN(PILE, false, false),
    SHOWN(PILE, true, true),
    SHOWN_IN_HAND(HAND, true, false),
    HIDDEN_IN_HAND(HAND, false, false),
    IN_HAND_SHOWN_TO_OTHER_PLAYERS(HAND, false, true);

    private final CardPosition position;
    private final boolean shownToHoldingPlayer;
    private final boolean shownToOtherPlayers;

    FormerCardState(CardPosition position, boolean shownToHoldingPlayer, boolean shownToOtherPlayers) {
        this.position = position;
        this.shownToHoldingPlayer = shownToHoldingPlayer;
        this.shownToOtherPlayers = shownToOtherPlayers;
    }

    public boolean isShownToHoldingPlayer() {
        return shownToHoldingPlayer;
    }

    public boolean isShownToOtherPlayers() {
        return shownToOtherPlayers;
    }

    public boolean isInHand() {
        return position == HAND;
    }

    public boolean isInPile() {
        return position == PILE;
    }

    public boolean isHidden() {
        return !isShownToHoldingPlayer() && !isShownToOtherPlayers();
    }

    public boolean isShown() {
        return isShownToHoldingPlayer() && isShownToOtherPlayers();
    }
}

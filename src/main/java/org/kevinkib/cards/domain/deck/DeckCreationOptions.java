package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.CardHandState;

public record DeckCreationOptions(CardHandState getCardState) {

    public static DeckCreationOptions DEFAULT = new DeckCreationOptions(CardHandState.SHOWN_IN_HAND);

}

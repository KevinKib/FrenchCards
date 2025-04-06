package org.kevinkib.cards.domain;

public record DeckCreationOptions(CardHandState getCardState) {

    public static DeckCreationOptions DEFAULT = new DeckCreationOptions(CardHandState.SHOWN_IN_HAND);

}

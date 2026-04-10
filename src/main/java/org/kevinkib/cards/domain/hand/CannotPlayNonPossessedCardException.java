package org.kevinkib.cards.domain.hand;

public class CannotPlayNonPossessedCardException extends Exception {

    public CannotPlayNonPossessedCardException() {
        super("Cannot play a card that is not in the hand.");
    }


}

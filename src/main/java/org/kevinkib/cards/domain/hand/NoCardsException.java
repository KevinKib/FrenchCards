package org.kevinkib.cards.domain.hand;

public class NoCardsException extends Exception {

    public NoCardsException() {
        super("There are no cards left.");
    }

}

package org.kevinkib.cards.domain;

public class CannotDistributeDeckException extends Exception {

    public CannotDistributeDeckException() {
        super("Cannot distribute deck, as there are too many players and cards compared to the deck size.");
    }

}

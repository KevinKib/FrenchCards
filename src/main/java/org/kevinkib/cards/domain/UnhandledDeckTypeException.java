package org.kevinkib.cards.domain;

public class UnhandledDeckTypeException extends RuntimeException {

    public UnhandledDeckTypeException(DeckType deckType) {
        super("The deck type "+(deckType != null ? deckType.name() : "null")+" cannot be handled.");
    }

}

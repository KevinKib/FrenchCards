package org.kevinkib.cards.domain.deck;

public class UnevenNumberOfCardsPerPlayerException extends Exception {

    public UnevenNumberOfCardsPerPlayerException(Integer nbPlayers, Integer totalNumberOfCards) {
        super("Cannot evenly split "+totalNumberOfCards+" cards between "+nbPlayers+" players.");
    }

}

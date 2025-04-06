package org.kevinkib.cards.domain;

public interface DeckFactory {

    public Deck generate(DeckType deckType);

    public boolean canHandle(DeckType deckType);

}

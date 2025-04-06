package org.kevinkib.cards.domain;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private final List<Card> cards;

    public Hand(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public Hand() {
        this.cards = new ArrayList<>();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public void play(Card card) throws CannotPlayNonPossessedCardException {
        if (!possesses(card)) {
            throw new CannotPlayNonPossessedCardException();
        }

        cards.remove(card);
    }

    public Card playCardOnTop() throws NoCardsException {
        if (!hasAnyCards()) {
            throw new NoCardsException();
        }

        return cards.remove(0);
    }

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public boolean possesses(Card card) {
        return cards.contains(card);
    }

    public Integer getSize() {
        return cards.size();
    }

    public boolean hasAnyCards() {
        return !cards.isEmpty();
    }
}

package org.kevinkib.cards.domain;

import java.util.*;

public class Pile {

    private final LinkedList<Card> cards;

    public Pile() {
        this.cards = new LinkedList<>();
    }

    public void add(Card card) {
        cards.add(card);
    }

    public Card draw() {
        return cards.pop();
    }

    public Card seeCardOnTop() {
        return cards.getFirst();
    }

    public Card seeCardByIndex(int index) {
        return cards.get(index);
    }

    public Integer getSize() {
        return cards.size();
    }
}

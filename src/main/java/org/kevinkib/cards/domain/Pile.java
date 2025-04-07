package org.kevinkib.cards.domain;

import java.util.LinkedList;
import java.util.List;

public class Pile {

    private final LinkedList<Card> cards;

    public Pile() {
        this.cards = new LinkedList<>();
    }

    public void add(Card card) {
        add(card, CardPileState.SHOWN);
    }

    public void add(Card card, CardPileState state) {
        if (state.isHidden()) {
            card.hideInPile();
        } else if (state.isShown()) {
            card.showInPile();
        }

        cards.add(card);
    }

    public void add(List<Card> cards) {
        for (Card card : cards) {
            add(card);
        }
    }

    public void add(List<Card> cards, CardPileState state) {
        for (Card card : cards) {
            add(card, state);
        }
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

    public Card getCard(int index) {
        return seeCardByIndex(index);
    }

    public Integer getSize() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}

package org.kevinkib.cards.domain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Pile {

    private final LinkedList<Card> cards;
    private final List<PileSubscriber> subscribers;

    public Pile() {
        this.cards = new LinkedList<>();
        this.subscribers = new ArrayList<>();
    }

    public void add(Card card) {
        add(card, CardPileState.SHOWN);
    }

    public void add(Card card, CardPileState state) {
        updateCardState(card, state);
        cards.addFirst(card);

        onCardAdded(card, PilePosition.TOP);
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

    public void addBelow(Card card) {
        addBelow(card, CardPileState.SHOWN);
    }

    public void addBelow(Card card, CardPileState state) {
        updateCardState(card, state);
        cards.addLast(card);

        onCardAdded(card, PilePosition.BOTTOM);
    }

    public void addBelow(List<Card> cards) {
        for (Card card : cards) {
            addBelow(card);
        }
    }

    public void addBelow(List<Card> cards, CardPileState state) {
        for (Card card : cards) {
            addBelow(card, state);
        }
    }

    public void clear() {
        cards.clear();

        onClear();
    }

    public List<Card> clearAndReturnCards() {
        List<Card> clearedCards = getCards();
        clear();
        return clearedCards;
    }

    public void subscribe(PileSubscriber subscriber) {
        subscribers.add(subscriber);
        // TODO: test subscription
    }

    public void unsubscribe(PileSubscriber subscriber) {
        subscribers.remove(subscriber);
        // TODO: test subscription
    }

    public void onCardAdded(Card addedCard, PilePosition pilePosition) {
        for (PileSubscriber subscriber : subscribers) {
            subscriber.onCardAdded(this, addedCard, pilePosition);
        }
    }

    public void onClear() {
        for (PileSubscriber subscriber : subscribers) {
            subscriber.onClear(this);
        }
    }

    private void updateCardState(Card card, CardPileState state) {
        if (state.isHidden()) {
            card.hideInPile();
        } else if (state.isShown()) {
            card.showInPile();
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

    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }

    public Integer getSize() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}

package org.kevinkib.cards.domain.pile;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.Visibility;

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
        add(card, Visibility.SHOWN);
    }

    public void add(Card card, Visibility visibility) {
        updateVisibility(card, visibility);
        cards.addFirst(card);

        notifyCardAdded(card, PilePosition.TOP);
    }

    public void add(List<Card> cards) {
        for (Card card : cards) {
            add(card);
        }
    }

    public void add(List<Card> cards, Visibility visibility) {
        for (Card card : cards) {
            add(card, visibility);
        }
    }

    public void addBelow(Card card) {
        addBelow(card, Visibility.SHOWN);
    }

    public void addBelow(Card card, Visibility visibility) {
        updateVisibility(card, visibility);
        cards.addLast(card);

        notifyCardAdded(card, PilePosition.BOTTOM);
    }

    public void addBelow(List<Card> cards) {
        for (Card card : cards) {
            addBelow(card);
        }
    }

    public void addBelow(List<Card> cards, Visibility visibility) {
        for (Card card : cards) {
            addBelow(card, visibility);
        }
    }

    public void clear() {
        cards.clear();

        notifyCleared();
    }

    public List<Card> clearAndReturnCards() {
        List<Card> clearedCards = getCards();
        clear();
        return clearedCards;
    }

    public void subscribe(PileSubscriber subscriber) {
        subscribers.add(subscriber);
    }

    public void unsubscribe(PileSubscriber subscriber) {
        subscribers.remove(subscriber);
    }

    private void notifyCardAdded(Card addedCard, PilePosition pilePosition) {
        for (PileSubscriber subscriber : subscribers) {
            subscriber.onCardAdded(this, addedCard, pilePosition);
        }
    }

    private void notifyCleared() {
        for (PileSubscriber subscriber : subscribers) {
            subscriber.onClear(this);
        }
    }

    private void updateVisibility(Card card, Visibility visibility) {
        if (visibility.isHidden()) {
            card.hide();
        } else {
            card.show();
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

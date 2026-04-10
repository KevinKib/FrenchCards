package org.kevinkib.cards.testhelpers;

import org.kevinkib.cards.domain.Card;
import org.kevinkib.cards.domain.deck.Deck;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public final class DeckBuilder {
    private LinkedList<Card> cards;
    private Random random = new Random(0);

    private DeckBuilder() {
    }

    public static DeckBuilder aDeck() {
        return new DeckBuilder();
    }

    public DeckBuilder withCards(Card... cards) {
        this.cards = new LinkedList<>(Arrays.asList(cards));
        return this;
    }

    public DeckBuilder withCards(LinkedList<Card> cards) {
        this.cards = cards;
        return this;
    }

    public DeckBuilder withNumberOfCards(Integer numberOfCards) {
        this.cards = new LinkedList<>();
        for (int i = 0; i < numberOfCards; ++i) {
            cards.add(CardBuilder.aCard().build());
        }
        return this;
    }

    public DeckBuilder withRandom(Random random) {
        this.random = random;
        return this;
    }

    public Deck build() {
        return new Deck(cards, random);
    }

    public Deck buildWithoutRandom() {
        return new Deck(cards);
    }
}

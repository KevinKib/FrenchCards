package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.*;
import org.kevinkib.cards.domain.hand.Hand;

import java.util.*;

public class Deck {

    private final LinkedList<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = new LinkedList<>(cards);
    }

    public Deck(List<Card> cards, Random seed) {
        this(cards);
        shuffle(seed);
    }

    public List<Hand> distribute(Integer nbPlayers, Integer nbCardsPerPlayer) throws CannotDistributeDeckException {
        int expectedNumberOfDistributedCards = nbPlayers * nbCardsPerPlayer;
        if (expectedNumberOfDistributedCards > this.getSize()) {
            throw new CannotDistributeDeckException();
        }
        return distributeCards(nbPlayers, expectedNumberOfDistributedCards);
    }

    public List<Hand> distributeAll(Integer nbPlayers) {
        return distributeCards(nbPlayers, getSize());
    }

    public List<Hand> distributeAll(Integer nbPlayers, DistributionOptions options) throws UnevenNumberOfCardsPerPlayerException {
        if (options.isIdenticalCardsNumberForced()) {
            return distributeAllEvenly(nbPlayers);
        }
        return distributeCards(nbPlayers, getSize());
    }

    public List<Hand> distributeAllEvenly(Integer nbPlayers) throws UnevenNumberOfCardsPerPlayerException {
        double cardsPerPlayer = (double) getSize() / nbPlayers;
        boolean isCardDistributionUneven = cardsPerPlayer != Math.floor(cardsPerPlayer);

        if (isCardDistributionUneven) {
            throw new UnevenNumberOfCardsPerPlayerException(nbPlayers, getSize());
        }

        Integer nbCardsPerPlayer = (int) cardsPerPlayer;

        try {
            return distribute(nbPlayers, nbCardsPerPlayer);
        } catch (CannotDistributeDeckException e) {
            throw new IllegalStateException(SHOULD_ALWAYS_DISTRIBUTE);
        }
    }

    private List<Hand> distributeCards(Integer nbPlayers, int totalCards) {
        List<Hand> hands = createHands(nbPlayers);
        for (int cardIndex = 0; cardIndex < totalCards; ++cardIndex) {
            hands.get(cardIndex % nbPlayers).add(cards.pop());
        }
        return hands;
    }

    public Card draw() {
        return cards.pop();
    }

    public void shuffle(Random seed) {
        Collections.shuffle(cards, seed);
    }

    public Integer getSize() {
        return cards.size();
    }

    private List<Hand> createHands(Integer nbPlayers) {
        List<Hand> hands = new ArrayList<>();
        for (int playerIndex = 0; playerIndex < nbPlayers; ++playerIndex) {
            hands.add(new Hand());
        }
        return hands;
    }

    public void remove(Card card) {
        cards.remove(card);
    }

    private final static String SHOULD_ALWAYS_DISTRIBUTE = "Deck should always be able to distribute decks after its checks";
}

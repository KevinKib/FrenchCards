package org.kevinkib.cards.domain;

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

        List<Hand> hands = createHands(nbPlayers);

        for (int cardIndex = 0; cardIndex < nbCardsPerPlayer; ++cardIndex) {
            for (int playerIndex = 0; playerIndex < nbPlayers; ++playerIndex) {
                Hand hand = hands.get(playerIndex);
                hand.add(cards.pop());
            }
        }

        return hands;
    }

    public List<Hand> distributeAll(Integer nbPlayers) {
        try {
            return distributeAll(nbPlayers, DistributionOptions.DEFAULT);
        } catch (UnevenNumberOfCardsPerPlayerException e) {
            throw new IllegalStateException(SHOULD_ALWAYS_DISTRIBUTE);
        }
    }

    public List<Hand> distributeAll(Integer nbPlayers, DistributionOptions options) throws UnevenNumberOfCardsPerPlayerException {
        if (options.isIdenticalCardsNumberForced()) {
            return distributeAllEvenly(nbPlayers);
        }

        List<Hand> hands = createHands(nbPlayers);
        int deckSize = getSize();

        for (int cardIndex = 0; cardIndex < deckSize; ++cardIndex) {
            int playerIndex = cardIndex % nbPlayers;

            Hand hand = hands.get(playerIndex);
            hand.add(cards.pop());
        }

        return hands;
    }

    private List<Hand> distributeAllEvenly(Integer nbPlayers) throws UnevenNumberOfCardsPerPlayerException {
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

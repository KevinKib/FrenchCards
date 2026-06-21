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

    /**
     * Deals exactly {@code nbCardsPerPlayer} cards to each of {@code nbPlayers} players,
     * dealt face up. Equivalent to {@link #distribute(Integer, Integer, DistributionOptions)}
     * with {@link DistributionOptions#DEFAULT}.
     *
     * @throws CannotDistributeDeckException if the deck holds fewer than
     *         {@code nbPlayers * nbCardsPerPlayer} cards.
     */
    public List<Hand> distribute(Integer nbPlayers, Integer nbCardsPerPlayer) throws CannotDistributeDeckException {
        return distribute(nbPlayers, nbCardsPerPlayer, DistributionOptions.DEFAULT);
    }

    /**
     * Deals exactly {@code nbCardsPerPlayer} cards to each of {@code nbPlayers} players.
     * Card {@code i} dealt goes to player {@code i % nbPlayers}. When
     * {@code options.visibility()} is {@code HIDDEN} every dealt card is turned face down.
     *
     * @param options distribution options; use {@link DistributionOptions#hidden()} to deal face down.
     * @throws CannotDistributeDeckException if the deck holds fewer than
     *         {@code nbPlayers * nbCardsPerPlayer} cards.
     */
    public List<Hand> distribute(Integer nbPlayers, Integer nbCardsPerPlayer, DistributionOptions options) throws CannotDistributeDeckException {
        int expectedNumberOfDistributedCards = nbPlayers * nbCardsPerPlayer;
        if (expectedNumberOfDistributedCards > this.getSize()) {
            throw new CannotDistributeDeckException();
        }
        return distributeCards(nbPlayers, expectedNumberOfDistributedCards, options.visibility());
    }

    /**
     * Deals the entire deck round-robin across {@code nbPlayers} players, dealt face up.
     * The split may be uneven (the first players receive the extra cards). Equivalent to
     * {@link #distributeAll(Integer, DistributionOptions)} with {@link DistributionOptions#DEFAULT}.
     */
    public List<Hand> distributeAll(Integer nbPlayers) {
        return distributeAll(nbPlayers, DistributionOptions.DEFAULT);
    }

    /**
     * Deals the entire deck round-robin across {@code nbPlayers} players. The split may be
     * uneven (the first players receive the extra cards). When {@code options.visibility()}
     * is {@code HIDDEN} every dealt card is turned face down.
     *
     * @param options distribution options; use {@link DistributionOptions#hidden()} to deal face down.
     */
    public List<Hand> distributeAll(Integer nbPlayers, DistributionOptions options) {
        return distributeCards(nbPlayers, getSize(), options.visibility());
    }

    /**
     * Deals the entire deck evenly across {@code nbPlayers} players, dealt face up. Equivalent to
     * {@link #distributeAllEvenly(Integer, DistributionOptions)} with {@link DistributionOptions#DEFAULT}.
     *
     * @throws UnevenNumberOfCardsPerPlayerException if the deck does not divide evenly
     *         across {@code nbPlayers}.
     */
    public List<Hand> distributeAllEvenly(Integer nbPlayers) throws UnevenNumberOfCardsPerPlayerException {
        return distributeAllEvenly(nbPlayers, DistributionOptions.DEFAULT);
    }

    /**
     * Deals the entire deck evenly across {@code nbPlayers} players, giving every player the
     * same number of cards. When {@code options.visibility()} is {@code HIDDEN} every dealt
     * card is turned face down.
     *
     * @param options distribution options; use {@link DistributionOptions#hidden()} to deal face down.
     * @throws UnevenNumberOfCardsPerPlayerException if the deck does not divide evenly
     *         across {@code nbPlayers}.
     */
    public List<Hand> distributeAllEvenly(Integer nbPlayers, DistributionOptions options) throws UnevenNumberOfCardsPerPlayerException {
        double cardsPerPlayer = (double) getSize() / nbPlayers;
        boolean isCardDistributionUneven = cardsPerPlayer != Math.floor(cardsPerPlayer);

        if (isCardDistributionUneven) {
            throw new UnevenNumberOfCardsPerPlayerException(nbPlayers, getSize());
        }

        Integer nbCardsPerPlayer = (int) cardsPerPlayer;

        try {
            return distribute(nbPlayers, nbCardsPerPlayer, options);
        } catch (CannotDistributeDeckException e) {
            throw new IllegalStateException(SHOULD_ALWAYS_DISTRIBUTE);
        }
    }

    private List<Hand> distributeCards(Integer nbPlayers, int totalCards, Visibility visibility) {
        List<Hand> hands = createHands(nbPlayers);
        for (int cardIndex = 0; cardIndex < totalCards; ++cardIndex) {
            Card card = cards.pop();
            if (visibility.isHidden()) {
                card.hide();
            }
            hands.get(cardIndex % nbPlayers).add(card);
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

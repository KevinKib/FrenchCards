package org.kevinkib.cards.domain;

import java.util.Objects;

public class Card {

    private final Rank rank;
    private final Suit suit;
    private CardState state;

    public Card(Rank rank, Suit suit) {
        this(rank, suit, CardHandState.SHOWN_IN_HAND);
    }

    public Card(Rank rank, Suit suit, CardState state) {
        this.rank = rank;
        this.suit = suit;
        this.state = state;
    }

    public void putInHand() {
        state = CardHandState.SHOWN_IN_HAND;
    }

    public void showInPile() {
        state = CardPileState.SHOWN;
    }

    public void hideInPile() {
        state = CardPileState.HIDDEN;
    }

    public void putInHandAndShowToOtherPlayers() {
        state = CardHandState.IN_HAND_SHOWN_TO_OTHER_PLAYERS;
    }

    public void hideInHand() {
        state = CardHandState.HIDDEN_IN_HAND;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public CardState getState() {
        return state;
    }

    public boolean isShown() {
        return state.isShownToHoldingPlayer();
    }

    public boolean isHidden() {
        return state.isHidden();
    }

    public boolean isShownToHoldingPlayer() {
        return state.isShownToHoldingPlayer();
    }

    public boolean isShownToOtherPlayers() {
        return state.isShownToOtherPlayers();
    }

    public boolean isSameRankAs(Card card) {
        return Objects.equals(rank, card.rank);
    }

    public boolean isSameSuitAs(Card card) {
        return Objects.equals(suit, card.suit);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(rank, card.rank) && Objects.equals(suit, card.suit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}

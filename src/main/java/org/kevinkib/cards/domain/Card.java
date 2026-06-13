package org.kevinkib.cards.domain;

import java.util.Objects;

public class Card {

    private final Rank rank;
    private final Suit suit;
    private Visibility visibility;

    public Card(Rank rank, Suit suit) {
        this(rank, suit, Visibility.SHOWN);
    }

    public Card(Rank rank, Suit suit, Visibility visibility) {
        this.rank = rank;
        this.suit = suit;
        this.visibility = visibility;
    }

    public void show() {
        visibility = Visibility.SHOWN;
    }

    public void hide() {
        visibility = Visibility.HIDDEN;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public boolean isShown() {
        return visibility.isShown();
    }

    public boolean isHidden() {
        return visibility.isHidden();
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

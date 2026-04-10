package org.kevinkib.cards.domain.deck.french;

import org.kevinkib.cards.domain.Suit;

import java.util.Arrays;
import java.util.List;

import static org.kevinkib.cards.domain.deck.french.Color.RED;
import static org.kevinkib.cards.domain.deck.french.Color.BLACK;

public enum FrenchSuit implements Suit {

    HEART(RED, '♥', false),
    DIAMOND(RED, '♦', false),
    CLUB(BLACK, '♣', false),
    SPADE(BLACK, '♠', false),
    RED_JOKER(RED, 'J', true),
    BLACK_JOKER(BLACK, 'J', true);

    private final Color color;
    private final Character symbol;
    private final boolean joker;

    FrenchSuit(final Color color, final Character symbol, final boolean joker) {
        this.color = color;
        this.symbol = symbol;
        this.joker = joker;
    }

    public static List<FrenchSuit> getSuits() {
        return Arrays.asList(HEART, DIAMOND, CLUB, SPADE);
    }

    public static FrenchSuit from(String suitName) {
        for (FrenchSuit suit : FrenchSuit.values()) {
            if (suit.name().equals(suitName)) {
                return suit;
            }
        }
        throw new IllegalArgumentException("Invalid suit : "+suitName);
    }

    public Color getColor() {
        return color;
    }

    public Character getSymbol() {
        return symbol;
    }

    public boolean isJoker() {
        return joker;
    }

    @Override
    public String toString() {
        return name();
    }
}

package org.kevinkib.cards.domain.french;

import org.kevinkib.cards.domain.Suit;

import java.util.Arrays;
import java.util.List;

import static org.kevinkib.cards.domain.french.Color.RED;
import static org.kevinkib.cards.domain.french.Color.BLACK;

public enum FrenchSuit implements Suit {

    HEARTS(RED, '♥', false),
    DIAMONDS(RED, '♦', false),
    CLUBS(BLACK, '♣', false),
    SPADES(BLACK, '♠', false),
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
        return Arrays.asList(HEARTS, DIAMONDS, CLUBS, SPADES);
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
        return symbol.toString();
    }
}

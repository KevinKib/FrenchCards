package org.kevinkib.cards.domain.deck.french;

import org.kevinkib.cards.domain.*;
import org.kevinkib.cards.domain.deck.Deck;
import org.kevinkib.cards.domain.deck.DeckFactory;
import org.kevinkib.cards.domain.deck.DeckType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FrenchDeckFactory extends DeckFactory {

    public FrenchDeckFactory() {

    }

    public Deck generate(DeckType deckType, CardState cardState) {
        List<Card> cards = new ArrayList<>();

        for (FrenchRank rank : FrenchRank.getRanks()) {
            for (FrenchSuit suit : FrenchSuit.getSuits()) {
                cards.add(new Card(rank, suit, cardState));
            }
        }

        if (DeckType.FRENCH_WITH_JOKERS == deckType) {
            cards.add(new Card(FrenchRank.JOKER, FrenchSuit.BLACK_JOKER));
            cards.add(new Card(FrenchRank.JOKER, FrenchSuit.RED_JOKER));
        }

        return new Deck(cards, new Random());
    }

    @Override
    public boolean canHandle(DeckType deckType) {
        return Arrays.asList(DeckType.FRENCH, DeckType.FRENCH_WITH_JOKERS).contains(deckType);
    }
}

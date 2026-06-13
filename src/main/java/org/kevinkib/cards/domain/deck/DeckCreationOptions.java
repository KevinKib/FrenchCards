package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.Visibility;

public record DeckCreationOptions(Visibility visibility) {

    public static DeckCreationOptions DEFAULT = new DeckCreationOptions(Visibility.SHOWN);

}

package org.kevinkib.cards.domain.deck;

import org.kevinkib.cards.domain.Visibility;

public record DeckCreationOptions(Visibility visibility, Long seed) {

    public static DeckCreationOptions DEFAULT = new DeckCreationOptions(Visibility.SHOWN, null);

    public DeckCreationOptions(Visibility visibility) {
        this(visibility, null);
    }

    public boolean hasSeed() {
        return seed != null;
    }

}

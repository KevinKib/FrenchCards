package org.kevinkib.cards.domain;

public enum Visibility {

    SHOWN,
    HIDDEN;

    public boolean isShown() {
        return this == SHOWN;
    }

    public boolean isHidden() {
        return this == HIDDEN;
    }

}

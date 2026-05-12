package com.patternforge.echoesarena.progression;

public enum UpgradeBranch {
    SPEED("Speed", "Move faster and dash better"),
    POWER("Power", "Deal stronger weapon damage"),
    MANA("Mana", "Cast more often and recover mana"),
    HEALTH("HP", "Survive longer with health and armor");

    private final String displayName;
    private final String description;

    UpgradeBranch(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }
}

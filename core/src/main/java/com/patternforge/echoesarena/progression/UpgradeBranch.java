package com.patternforge.echoesarena.progression;

public enum UpgradeBranch {
    HEALTH("HP", "Survive longer with more health"),
    SPEED("Speed", "Move faster"),
    POWER("Power", "Deal stronger weapon damage"),
    MANA("Mana", "Cast and strike longer with more mana");

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

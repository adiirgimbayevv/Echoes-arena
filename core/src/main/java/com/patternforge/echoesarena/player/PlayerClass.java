package com.patternforge.echoesarena.player;

import com.badlogic.gdx.graphics.Color;

public enum PlayerClass {
    BRUISER(
        "KNIGHT VANGUARD",
        "Frontline class with high survivability and strong melee pressure.",
        new Color(1f, 0.12f, 0.08f, 1f)
    ),
    RUNNER(
        "SHADOW ROGUE",
        "Mobile class with high speed, quick attacks and evasive movement.",
        new Color(0.12f, 0.65f, 1f, 1f)
    ),
    MAGE(
        "ARCANE WIZARD",
        "Spell-focused class with stronger projectiles and higher mana sustain.",
        new Color(1f, 0.86f, 0.12f, 1f)
    );

    private final String displayName;
    private final String description;
    private final Color color;

    PlayerClass(String displayName, String description, Color color) {
        this.displayName = displayName;
        this.description = description;
        this.color = color;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Color getColor() {
        return color;
    }
}

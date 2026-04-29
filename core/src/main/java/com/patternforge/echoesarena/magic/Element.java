package com.patternforge.echoesarena.magic;

public enum Element {
    NONE,
    FIRE,
    WATER,
    EARTH,
    LAVA, // Fire + Earth
    STEAM, // Fire + Water
    MUD; // Water + Earth

    public static Element getCombo(Element e1, Element e2) {
        if (e1 == null || e2 == null) return NONE;
        if (e1 == e2) return e1;
        if ((e1 == FIRE && e2 == EARTH) || (e1 == EARTH && e2 == FIRE)) return LAVA;
        if ((e1 == FIRE && e2 == WATER) || (e1 == WATER && e2 == FIRE)) return STEAM;
        if ((e1 == WATER && e2 == EARTH) || (e1 == EARTH && e2 == WATER)) return MUD;
        return NONE;
    }
}

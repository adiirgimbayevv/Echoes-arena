package com.patternforge.echoesarena.ability;

import java.util.EnumMap;
import java.util.Map;

public class ElementProgress {

    private final Map<ElementType, Float> charges;
    private static final float MAX_CHARGE = 100f;

    public ElementProgress() {
        this.charges = new EnumMap<>(ElementType.class);
        for (ElementType type : ElementType.values()) {
            charges.put(type, 0f);
        }
    }

    public void addCharge(ElementType type, float amount) {
        if (type == ElementType.NONE) {
            return;
        }
        float current = charges.getOrDefault(type, 0f);
        charges.put(type, Math.min(MAX_CHARGE, current + amount));
    }

    public float getCharge(ElementType type) {
        return charges.getOrDefault(type, 0f);
    }

    public float getChargeRatio(ElementType type) {
        return getCharge(type) / MAX_CHARGE;
    }

    public boolean isReady(ElementType type) {
        return getCharge(type) >= MAX_CHARGE;
    }

    public void consume(ElementType type) {
        charges.put(type, 0f);
    }

    public void reset() {
        for (ElementType type : ElementType.values()) {
            charges.put(type, 0f);
        }
    }
}

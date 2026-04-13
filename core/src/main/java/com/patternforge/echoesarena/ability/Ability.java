package com.patternforge.echoesarena.ability;

public abstract class Ability {

    private final String id;
    private final String displayName;
    private final ElementType element;
    private final AbilitySlot slot;
    private final float baseMagicalDamage;
    private final float basePhysicalDamage;

    protected Ability(String id, String displayName, ElementType element,
                      AbilitySlot slot, float basePhysicalDamage, float baseMagicalDamage) {
        this.id = id;
        this.displayName = displayName;
        this.element = element;
        this.slot = slot;
        this.basePhysicalDamage = basePhysicalDamage;
        this.baseMagicalDamage = baseMagicalDamage;
    }

    public abstract AbilityType getType();

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ElementType getElement() {
        return element;
    }

    public AbilitySlot getSlot() {
        return slot;
    }

    public float getBaseMagicalDamage() {
        return baseMagicalDamage;
    }

    public float getBasePhysicalDamage() {
        return basePhysicalDamage;
    }
}

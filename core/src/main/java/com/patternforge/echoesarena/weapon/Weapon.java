package com.patternforge.echoesarena.weapon;

import com.patternforge.echoesarena.ability.ElementType;

public abstract class Weapon {

    private final String id;
    private final String displayName;
    private final WeaponType weaponType;
    private final ElementType affinityElement;
    private final float baseDamage;
    private final float attackSpeed;
    private final float range;

    protected Weapon(String id, String displayName, WeaponType weaponType,
                     ElementType affinityElement, float baseDamage,
                     float attackSpeed, float range) {
        this.id = id;
        this.displayName = displayName;
        this.weaponType = weaponType;
        this.affinityElement = affinityElement;
        this.baseDamage = baseDamage;
        this.attackSpeed = attackSpeed;
        this.range = range;
    }

    public abstract void performAttack(WeaponAttackContext context);

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public ElementType getAffinityElement() {
        return affinityElement;
    }

    public float getBaseDamage() {
        return baseDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public float getRange() {
        return range;
    }
}

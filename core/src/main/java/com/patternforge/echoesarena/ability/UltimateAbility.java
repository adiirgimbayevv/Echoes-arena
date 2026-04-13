package com.patternforge.echoesarena.ability;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.StatusEffectType;

import java.util.List;

public class UltimateAbility extends Ability {

    private final float manaCost;
    private final float aoeRadius;
    private final StatusEffectType appliedEffect;
    private final float elementChargeGain;
    private final boolean requiresFullCharge;

    public UltimateAbility(String id, String displayName, ElementType element,
                            float basePhysicalDamage, float baseMagicalDamage,
                            float manaCost, float aoeRadius,
                            StatusEffectType appliedEffect, float elementChargeGain,
                            boolean requiresFullCharge) {
        super(id, displayName, element, AbilitySlot.ULTIMATE, basePhysicalDamage, baseMagicalDamage);
        this.manaCost = manaCost;
        this.aoeRadius = aoeRadius;
        this.appliedEffect = appliedEffect;
        this.elementChargeGain = elementChargeGain;
        this.requiresFullCharge = requiresFullCharge;
    }

    public boolean cast(CombatSystem.CombatTarget caster,
                        List<? extends CombatSystem.CombatTarget> targets,
                        Vector2 targetPosition,
                        CombatSystem combatSystem,
                        ElementProgress elementProgress) {
        if (requiresFullCharge && !elementProgress.isReady(getElement())) {
            return false;
        }
        elementProgress.consume(getElement());
        elementProgress.addCharge(getElement(), elementChargeGain);
        combatSystem.abilityHitAoe(caster, targets, targetPosition,
                aoeRadius, getBasePhysicalDamage(), getBaseMagicalDamage(), appliedEffect);
        return true;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.ULTIMATE;
    }

    public float getManaCost() {
        return manaCost;
    }

    public float getAoeRadius() {
        return aoeRadius;
    }

    public StatusEffectType getAppliedEffect() {
        return appliedEffect;
    }

    public float getElementChargeGain() {
        return elementChargeGain;
    }

    public boolean requiresFullCharge() {
        return requiresFullCharge;
    }
}

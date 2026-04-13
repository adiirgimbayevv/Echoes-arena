package com.patternforge.echoesarena.ability;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.StatusEffectType;

import java.util.List;

public class ActiveAbility extends Ability {

    private final float cooldown;
    private final float manaCost;
    private final float range;
    private final boolean isAoe;
    private final float aoeRadius;
    private final StatusEffectType appliedEffect;
    private final float elementChargeGain;

    private float cooldownTimer;

    public ActiveAbility(String id, String displayName, ElementType element, AbilitySlot slot,
                         float basePhysicalDamage, float baseMagicalDamage,
                         float cooldown, float manaCost, float range,
                         boolean isAoe, float aoeRadius,
                         StatusEffectType appliedEffect, float elementChargeGain) {
        super(id, displayName, element, slot, basePhysicalDamage, baseMagicalDamage);
        this.cooldown = cooldown;
        this.manaCost = manaCost;
        this.range = range;
        this.isAoe = isAoe;
        this.aoeRadius = aoeRadius;
        this.appliedEffect = appliedEffect;
        this.elementChargeGain = elementChargeGain;
        this.cooldownTimer = 0f;
    }

    public void update(float delta) {
        if (cooldownTimer > 0) {
            cooldownTimer -= delta;
        }
    }

    public boolean isReady() {
        return cooldownTimer <= 0;
    }

    public boolean cast(CombatSystem.CombatTarget caster,
                        List<? extends CombatSystem.CombatTarget> targets,
                        Vector2 targetPosition,
                        CombatSystem combatSystem,
                        ElementProgress elementProgress) {
        if (!isReady()) {
            return false;
        }
        cooldownTimer = cooldown;
        elementProgress.addCharge(getElement(), elementChargeGain);

        if (isAoe) {
            combatSystem.abilityHitAoe(caster, targets, targetPosition,
                    aoeRadius, getBasePhysicalDamage(), getBaseMagicalDamage(), appliedEffect);
        } else {
            CombatSystem.CombatTarget closest = findClosestInRange(caster, targets, targetPosition);
            if (closest != null) {
                combatSystem.abilityHit(caster, closest,
                        getBasePhysicalDamage(), getBaseMagicalDamage(), appliedEffect);
            }
        }
        return true;
    }

    private CombatSystem.CombatTarget findClosestInRange(CombatSystem.CombatTarget caster,
                                                          List<? extends CombatSystem.CombatTarget> targets,
                                                          Vector2 targetPosition) {
        CombatSystem.CombatTarget closest = null;
        float closestDist = range * range;
        for (CombatSystem.CombatTarget target : targets) {
            if (!target.isAlive()) {
                continue;
            }
            Vector2 pos = target.getPosition();
            float dx = pos.x - targetPosition.x;
            float dy = pos.y - targetPosition.y;
            float distSq = dx * dx + dy * dy;
            if (distSq <= closestDist) {
                closestDist = distSq;
                closest = target;
            }
        }
        return closest;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.ACTIVE;
    }

    public float getCooldown() {
        return cooldown;
    }

    public float getManaCost() {
        return manaCost;
    }

    public float getCooldownTimer() {
        return cooldownTimer;
    }

    public float getCooldownRatio() {
        return cooldownTimer <= 0 ? 1f : 1f - (cooldownTimer / cooldown);
    }

    public float getRange() {
        return range;
    }

    public boolean isAoe() {
        return isAoe;
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
}

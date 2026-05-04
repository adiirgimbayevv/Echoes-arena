package com.patternforge.echoesarena.weapon;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.ability.ElementType;
import com.patternforge.echoesarena.combat.StatusEffectType;

public class FocusWeapon extends Weapon {

    private final float magicalDamageRatio;
    private final float aoeRadius;
    private final StatusEffectType onHitEffect;
    private final float elementChargePerCast;

    public FocusWeapon(String id, String displayName, ElementType affinityElement,
                       float baseDamage, float attackSpeed, float range,
                       float magicalDamageRatio, float aoeRadius,
                       StatusEffectType onHitEffect, float elementChargePerCast) {
        super(id, displayName, WeaponType.FOCUS, affinityElement, baseDamage, attackSpeed, range);
        this.magicalDamageRatio = magicalDamageRatio;
        this.aoeRadius = aoeRadius;
        this.onHitEffect = onHitEffect;
        this.elementChargePerCast = elementChargePerCast;
    }

    @Override
    public void performAttack(WeaponAttackContext context) {
        float bonus = 1f + context.getCompatibilityBonus();
        float physical = getBaseDamage() * (1f - magicalDamageRatio) * bonus;
        float magical = getBaseDamage() * magicalDamageRatio * bonus;
        Vector2 targetPos = new Vector2(context.getAttacker().getPosition())
                .add(context.getAttackDirection().nor().scl(getRange() * 0.5f));

        context.getCombatSystem().abilityHitAoe(
                context.getAttacker(),
                context.getTargets(),
                targetPos,
                aoeRadius,
                physical,
                magical,
                onHitEffect
        );
        context.getElementProgress().addCharge(getAffinityElement(), elementChargePerCast);
    }

    public float getMagicalDamageRatio() {
        return magicalDamageRatio;
    }

    public float getAoeRadius() {
        return aoeRadius;
    }

    public StatusEffectType getOnHitEffect() {
        return onHitEffect;
    }

    public float getElementChargePerCast() {
        return elementChargePerCast;
    }
}

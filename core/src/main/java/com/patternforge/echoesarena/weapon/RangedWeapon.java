package com.patternforge.echoesarena.weapon;

import com.patternforge.echoesarena.ability.ElementType;
import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.combat.HitData;
import com.patternforge.echoesarena.combat.HitType;
import com.patternforge.echoesarena.combat.StatusEffectType;
import com.patternforge.echoesarena.stats.MagicalStats;

public class RangedWeapon extends Weapon {

    private final float projectileSpeed;
    private final float projectileRadius;
    private final StatusEffectType onHitEffect;
    private final float elementChargePerShot;

    public RangedWeapon(String id, String displayName, ElementType affinityElement,
                        float baseDamage, float attackSpeed, float range,
                        float projectileSpeed, float projectileRadius,
                        StatusEffectType onHitEffect, float elementChargePerShot) {
        super(id, displayName, WeaponType.RANGED, affinityElement, baseDamage, attackSpeed, range);
        this.projectileSpeed = projectileSpeed;
        this.projectileRadius = projectileRadius;
        this.onHitEffect = onHitEffect;
        this.elementChargePerShot = elementChargePerShot;
    }

    @Override
    public void performAttack(WeaponAttackContext context) {
        float effectiveDamage = getBaseDamage() * (1f + context.getCompatibilityBonus());
        CombatSystem.CombatTarget attacker = context.getAttacker();

        HitData hitData = new HitData(
                HitType.RANGED,
                effectiveDamage,
                0f,
                attacker.getCombatStats(),
                getMagicalStatsOrNull(attacker),
                onHitEffect
        );

        context.getCombatSystem().spawnProjectile(
                attacker.getPosition().x,
                attacker.getPosition().y,
                context.getAttackDirection(),
                projectileSpeed,
                projectileRadius,
                getRange(),
                hitData
        );

        context.getElementProgress().addCharge(getAffinityElement(), elementChargePerShot);
    }

    private MagicalStats getMagicalStatsOrNull(CombatSystem.CombatTarget target) {
        if (target instanceof CombatSystem.MagicalStatsProvider) {
            return ((CombatSystem.MagicalStatsProvider) target).getMagicalStats();
        }
        return new MagicalStats(0f, 0f, 0f);
    }

    public float getProjectileSpeed() {
        return projectileSpeed;
    }

    public float getProjectileRadius() {
        return projectileRadius;
    }

    public StatusEffectType getOnHitEffect() {
        return onHitEffect;
    }

    public float getElementChargePerShot() {
        return elementChargePerShot;
    }
}

package com.patternforge.echoesarena.combat;

import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;

public class HitData {

    private final HitType hitType;
    private final float basePhysicalDamage;
    private final float baseMagicalDamage;
    private final CombatStats attackerCombatStats;
    private final MagicalStats attackerMagicalStats;
    private final StatusEffectType appliedEffect;

    public HitData(HitType hitType, float basePhysicalDamage, float baseMagicalDamage,
                   CombatStats attackerCombatStats, MagicalStats attackerMagicalStats,
                   StatusEffectType appliedEffect) {
        this.hitType = hitType;
        this.basePhysicalDamage = basePhysicalDamage;
        this.baseMagicalDamage = baseMagicalDamage;
        this.attackerCombatStats = attackerCombatStats;
        this.attackerMagicalStats = attackerMagicalStats;
        this.appliedEffect = appliedEffect;
    }

    public HitType getHitType() {
        return hitType;
    }

    public float getBasePhysicalDamage() {
        return basePhysicalDamage;
    }

    public float getBaseMagicalDamage() {
        return baseMagicalDamage;
    }

    public CombatStats getAttackerCombatStats() {
        return attackerCombatStats;
    }

    public MagicalStats getAttackerMagicalStats() {
        return attackerMagicalStats;
    }

    public StatusEffectType getAppliedEffect() {
        return appliedEffect;
    }
}

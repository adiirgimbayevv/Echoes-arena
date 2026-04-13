package com.patternforge.echoesarena.combat;

import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;

public class DamageCalculator {

    private static final float MELEE_PHYSICAL_RATIO = 1.0f;
    private static final float MELEE_MAGICAL_RATIO = 0.0f;
    private static final float RANGED_PHYSICAL_RATIO = 0.8f;
    private static final float RANGED_MAGICAL_RATIO = 0.2f;
    private static final float ABILITY_PHYSICAL_RATIO = 0.2f;
    private static final float ABILITY_MAGICAL_RATIO = 0.8f;
    private static final float DEFENSE_FACTOR = 0.1f;

    public float calculate(HitData hit, CombatStats defenderStats) {
        float physicalRatio;
        float magicalRatio;

        switch (hit.getHitType()) {
            case MELEE:
                physicalRatio = MELEE_PHYSICAL_RATIO;
                magicalRatio = MELEE_MAGICAL_RATIO;
                break;
            case RANGED:
                physicalRatio = RANGED_PHYSICAL_RATIO;
                magicalRatio = RANGED_MAGICAL_RATIO;
                break;
            case ABILITY:
                physicalRatio = ABILITY_PHYSICAL_RATIO;
                magicalRatio = ABILITY_MAGICAL_RATIO;
                break;
            default:
                physicalRatio = 1.0f;
                magicalRatio = 0.0f;
        }

        float physicalPart = hit.getBasePhysicalDamage() * physicalRatio
                * hit.getAttackerCombatStats().getDamageMultiplier();

        float magicalPart = computeMagicalPart(hit, magicalRatio);

        float rawDamage = physicalPart + magicalPart;

        float defenseReduction = defenderStats.getDefense() * DEFENSE_FACTOR;
        float finalDamage = rawDamage * (1f - defenseReduction / (defenseReduction + 1f));

        return Math.max(1f, finalDamage);
    }

    private float computeMagicalPart(HitData hit, float magicalRatio) {
        MagicalStats magStats = hit.getAttackerMagicalStats();
        if (magStats == null) {
            return 0f;
        }
        float base = hit.getBaseMagicalDamage() * magicalRatio;
        return magStats.applySpellPower(base);
    }
}

package com.patternforge.echoesarena.ability;

import com.patternforge.echoesarena.combat.StatusEffectType;

public class AbilityFactory {

    private AbilityFactory() {}

    public static ActiveAbility fireball() {
        return new ActiveAbility(
                "fireball", "Fireball", ElementType.FIRE, AbilitySlot.PRIMARY,
                5f, 30f,
                3.0f, 20f, 80f,
                false, 0f,
                StatusEffectType.BURN, 25f
        );
    }

    public static ActiveAbility frostNova() {
        return new ActiveAbility(
                "frost_nova", "Frost Nova", ElementType.ICE, AbilitySlot.PRIMARY,
                0f, 20f,
                4.0f, 25f, 0f,
                true, 40f,
                StatusEffectType.SLOW, 30f
        );
    }

    public static ActiveAbility earthSpike() {
        return new ActiveAbility(
                "earth_spike", "Earth Spike", ElementType.EARTH, AbilitySlot.PRIMARY,
                15f, 10f,
                2.5f, 15f, 64f,
                false, 0f,
                StatusEffectType.STUN, 20f
        );
    }

    public static ActiveAbility lavaBurst() {
        return new ActiveAbility(
                "lava_burst", "Lava Burst", ElementType.LAVA, AbilitySlot.SECONDARY,
                10f, 50f,
                5.0f, 35f, 0f,
                true, 56f,
                StatusEffectType.BURN, 40f
        );
    }

    public static ActiveAbility steamCloud() {
        return new ActiveAbility(
                "steam_cloud", "Steam Cloud", ElementType.STEAM, AbilitySlot.SECONDARY,
                5f, 25f,
                4.5f, 30f, 0f,
                true, 64f,
                StatusEffectType.SLOW, 35f
        );
    }

    public static UltimateAbility infernoStrike() {
        return new UltimateAbility(
                "inferno_strike", "Inferno Strike", ElementType.FIRE,
                20f, 80f,
                50f, 72f,
                StatusEffectType.BURN, 50f, true
        );
    }

    public static UltimateAbility glacialRift() {
        return new UltimateAbility(
                "glacial_rift", "Glacial Rift", ElementType.ICE,
                10f, 60f,
                50f, 80f,
                StatusEffectType.FREEZE, 50f, true
        );
    }

    public static UltimateAbility seismicSlam() {
        return new UltimateAbility(
                "seismic_slam", "Seismic Slam", ElementType.EARTH,
                40f, 40f,
                50f, 64f,
                StatusEffectType.STUN, 50f, true
        );
    }

    public static PassiveAbility emberHeart() {
        return new PassiveAbility(
                "ember_heart", "Ember Heart", ElementType.FIRE,
                PassiveAbility.PassiveEffect.BONUS_DAMAGE_ON_BURN, 0.20f
        );
    }

    public static PassiveAbility frostbiteVeil() {
        return new PassiveAbility(
                "frostbite_veil", "Frostbite Veil", ElementType.ICE,
                PassiveAbility.PassiveEffect.MANA_ON_HIT, 5f
        );
    }

    public static PassiveAbility stoneResilience() {
        return new PassiveAbility(
                "stone_resilience", "Stone Resilience", ElementType.EARTH,
                PassiveAbility.PassiveEffect.RESTORE_HP_ON_KILL, 8f
        );
    }
}

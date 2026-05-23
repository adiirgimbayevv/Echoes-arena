package com.patternforge.echoesarena.ability;

import com.patternforge.echoesarena.combat.StatusEffectType;

public final class AbilityFactory {

    private AbilityFactory() {
    }

    public static ActiveAbility fireball() {
        return new ActiveAbility(
            "fireball",
            "Fireball",
            ElementType.FIRE,
            AbilitySlot.PRIMARY,
            120f,
            140f,
            1.2f,
            14f,
            520f,
            true,
            64f,
            StatusEffectType.BURN,
            18f
        );
    }

    public static ActiveAbility frostNova() {
        return new ActiveAbility(
            "frost_nova",
            "Frost Nova",
            ElementType.ICE,
            AbilitySlot.SECONDARY,
            0f,
            18f,
            5.5f,
            28f,
            120f,
            true,
            118f,
            StatusEffectType.FREEZE,
            22f
        );
    }

    public static UltimateAbility glacialRift() {
        return new UltimateAbility(
            "glacial_rift",
            "Glacial Rift",
            ElementType.ICE,
            0f,
            76f,
            0f,
            210f,
            StatusEffectType.FREEZE,
            0f,
            true
        );
    }
}

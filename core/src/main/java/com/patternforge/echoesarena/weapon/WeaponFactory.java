
package com.patternforge.echoesarena.weapon;

import com.patternforge.echoesarena.ability.ElementType;
import com.patternforge.echoesarena.combat.StatusEffectType;

public class WeaponFactory {

    private WeaponFactory() {}

    public static MeleeWeapon ironSword() {
        return new MeleeWeapon(
                "iron_sword", "Iron Sword", ElementType.NONE,
                18f, 1.2f, 24f,
                1, StatusEffectType.NONE, 5f
        );
    }

    public static MeleeWeapon volcanicBlade() {
        return new MeleeWeapon(
                "volcanic_blade", "Volcanic Blade", ElementType.FIRE,
                22f, 1.0f, 28f,
                1, StatusEffectType.BURN, 15f
        );
    }

    public static MeleeWeapon earthbreakerMaul() {
        return new MeleeWeapon(
                "earthbreaker_maul", "Earthbreaker Maul", ElementType.EARTH,
                28f, 0.7f, 32f,
                1, StatusEffectType.STUN, 20f
        );
    }

    public static RangedWeapon shortbow() {
        return new RangedWeapon(
                "shortbow", "Shortbow", ElementType.NONE,
                14f, 1.5f, 160f,
                180f, 4f,
                StatusEffectType.NONE, 8f
        );
    }

    public static RangedWeapon frostArrow() {
        return new RangedWeapon(
                "frost_arrow", "Frost Arrow", ElementType.ICE,
                16f, 1.3f, 200f,
                200f, 4f,
                StatusEffectType.SLOW, 20f
        );
    }

    public static FocusWeapon emberstave() {
        return new FocusWeapon(
                "emberstave", "Emberstave", ElementType.FIRE,
                20f, 0.9f, 100f,
                0.7f, 32f,
                StatusEffectType.BURN, 18f
        );
    }

    public static FocusWeapon earthenOrb() {
        return new FocusWeapon(
                "earthen_orb", "Earthen Orb", ElementType.EARTH,
                18f, 1.0f, 80f,
                0.5f, 40f,
                StatusEffectType.STUN, 15f
        );
    }
}

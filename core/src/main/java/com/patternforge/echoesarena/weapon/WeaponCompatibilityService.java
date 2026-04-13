package com.patternforge.echoesarena.weapon;

import com.patternforge.echoesarena.ability.ActiveAbility;
import com.patternforge.echoesarena.ability.ElementType;
import com.patternforge.echoesarena.config.BalanceConfig;

import java.util.EnumMap;
import java.util.Map;

public class WeaponCompatibilityService {

    private final Map<WeaponType, Map<ElementType, Float>> bonusTable;

    public WeaponCompatibilityService() {
        bonusTable = new EnumMap<>(WeaponType.class);
        registerDefaults();
    }

    public float getCompatibilityBonus(Weapon weapon, ActiveAbility ability) {
        if (ability == null) {
            return 0f;
        }
        return getCompatibilityBonus(weapon.getWeaponType(), ability.getElement());
    }

    public float getCompatibilityBonus(WeaponType weaponType, ElementType element) {
        Map<ElementType, Float> elementMap = bonusTable.get(weaponType);
        if (elementMap == null) {
            return 0f;
        }
        return elementMap.getOrDefault(element, 0f);
    }

    public void registerBonus(WeaponType weaponType, ElementType element, float bonus) {
        bonusTable.computeIfAbsent(weaponType, k -> new EnumMap<>(ElementType.class))
                .put(element, bonus);
    }

    private void registerDefaults() {
        registerBonus(WeaponType.MELEE, ElementType.EARTH,
                BalanceConfig.WEAPON_COMPAT_BONUS_MELEE_EARTH);
        registerBonus(WeaponType.MELEE, ElementType.FIRE,
                BalanceConfig.WEAPON_COMPAT_BONUS_MELEE_FIRE);
        registerBonus(WeaponType.RANGED, ElementType.ICE,
                BalanceConfig.WEAPON_COMPAT_BONUS_RANGED_ICE);
        registerBonus(WeaponType.RANGED, ElementType.FIRE,
                BalanceConfig.WEAPON_COMPAT_BONUS_RANGED_FIRE);
        registerBonus(WeaponType.FOCUS, ElementType.ICE,
                BalanceConfig.WEAPON_COMPAT_BONUS_FOCUS_ICE);
        registerBonus(WeaponType.FOCUS, ElementType.EARTH,
                BalanceConfig.WEAPON_COMPAT_BONUS_FOCUS_EARTH);
        registerBonus(WeaponType.MELEE, ElementType.LAVA,
                BalanceConfig.WEAPON_COMPAT_BONUS_MELEE_LAVA);
        registerBonus(WeaponType.FOCUS, ElementType.STEAM,
                BalanceConfig.WEAPON_COMPAT_BONUS_FOCUS_STEAM);
    }
}

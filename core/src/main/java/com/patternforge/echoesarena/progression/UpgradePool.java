package com.patternforge.echoesarena.progression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class UpgradePool {

    private final List<UpgradeOption> allOptions;
    private final Random random;

    public UpgradePool() {
        this.allOptions = new ArrayList<>();
        this.random = new Random();
        registerDefaults();
    }

    public void register(UpgradeOption option) {
        allOptions.add(option);
    }

    public List<UpgradeOption> drawChoices(int count, UnlockService unlockService) {
        List<UpgradeOption> available = allOptions.stream()
                .filter(opt -> !opt.hasRequirement() || unlockService.isUnlocked(opt.getRequiredUnlockId()))
                .collect(Collectors.toList());

        Collections.shuffle(available, random);
        return available.subList(0, Math.min(count, available.size()));
    }

    public List<UpgradeOption> drawChoicesByCategory(int count, UpgradeCategory category,
                                                      UnlockService unlockService) {
        List<UpgradeOption> filtered = allOptions.stream()
                .filter(opt -> opt.getCategory() == category)
                .filter(opt -> !opt.hasRequirement() || unlockService.isUnlocked(opt.getRequiredUnlockId()))
                .collect(Collectors.toList());

        Collections.shuffle(filtered, random);
        return filtered.subList(0, Math.min(count, filtered.size()));
    }

    private void registerDefaults() {
        register(new UpgradeOption("hp_boost_small", "Vitality", "+15 Max HP",
                UpgradeCategory.PHYSICAL, UpgradeTarget.MAX_HP, 15f, false));
        register(new UpgradeOption("hp_boost_large", "Fortitude", "+30 Max HP",
                UpgradeCategory.PHYSICAL, UpgradeTarget.MAX_HP, 30f, false));
        register(new UpgradeOption("defense_up", "Iron Skin", "+3 Defense",
                UpgradeCategory.PHYSICAL, UpgradeTarget.DEFENSE, 3f, false));
        register(new UpgradeOption("damage_up", "Sharpened Edge", "+10% Damage",
                UpgradeCategory.PHYSICAL, UpgradeTarget.DAMAGE_MULTIPLIER, 0.10f, true));
        register(new UpgradeOption("speed_up", "Swift Steps", "+8 Speed",
                UpgradeCategory.PHYSICAL, UpgradeTarget.SPEED, 8f, false));
        register(new UpgradeOption("dash_cooldown_down", "Agility", "-0.3s Dash Cooldown",
                UpgradeCategory.PHYSICAL, UpgradeTarget.DASH_COOLDOWN, -0.3f, false));
        register(new UpgradeOption("dash_distance_up", "Vault", "+16 Dash Distance",
                UpgradeCategory.PHYSICAL, UpgradeTarget.DASH_DISTANCE, 16f, false));
        register(new UpgradeOption("attack_speed_up", "Frenzy", "+0.1 Attack Speed",
                UpgradeCategory.PHYSICAL, UpgradeTarget.ATTACK_SPEED, 0.1f, false));

        register(new UpgradeOption("spell_power_up_small", "Arcane Focus", "+10 Spell Power",
                UpgradeCategory.MAGICAL, UpgradeTarget.SPELL_POWER, 10f, false));
        register(new UpgradeOption("spell_power_up_large", "Sorcery", "+20 Spell Power",
                UpgradeCategory.MAGICAL, UpgradeTarget.SPELL_POWER, 20f, false));
        register(new UpgradeOption("elemental_up", "Elemental Attunement", "+15% Elemental Bonus",
                UpgradeCategory.MAGICAL, UpgradeTarget.ELEMENTAL_BONUS, 0.15f, true));
        register(new UpgradeOption("cdr_up", "Nimble Casting", "+8% CDR",
                UpgradeCategory.MAGICAL, UpgradeTarget.ABILITY_CDR, 0.08f, true));

        register(new UpgradeOption("mana_pool_up", "Deep Reserves", "+20 Max Mana",
                UpgradeCategory.MANA, UpgradeTarget.MAX_MANA, 20f, false));
        register(new UpgradeOption("mana_regen_up", "Meditation", "+2 Mana/s",
                UpgradeCategory.MANA, UpgradeTarget.MANA_REGEN, 2f, false));
        register(new UpgradeOption("burst_threshold_down", "Overflow", "-10 Burst Threshold",
                UpgradeCategory.MANA, UpgradeTarget.MANA_BURST_THRESHOLD, -10f, false));

        register(new UpgradeOption("combo_fire_ice", "Steam Burst", "Unlock Fire+Ice combo",
                UpgradeCategory.COMBO, UpgradeTarget.SPELL_POWER, 0f, false, null));
    }

    public List<UpgradeOption> getAllOptions() {
        return allOptions;
    }
}


package com.patternforge.echoesarena.progression;

import com.patternforge.echoesarena.config.BalanceConfig;
import com.patternforge.echoesarena.player.PlayerBuild;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.ManaProfile;
import com.patternforge.echoesarena.stats.MagicalStats;
import com.patternforge.echoesarena.stats.PhysicalStats;

import java.util.EnumMap;
import java.util.Map;

public class LevelUpService {

    private final Map<UpgradeBranch, Integer> branchLevels;

    private int currentXp;
    private int currentLevel;
    private int pendingSkillPoints;
    private boolean pendingLevelUp;

    public LevelUpService() {
        this.branchLevels = new EnumMap<>(UpgradeBranch.class);
        for (UpgradeBranch branch : UpgradeBranch.values()) {
            branchLevels.put(branch, 0);
        }
        this.currentXp = 0;
        this.currentLevel = 1;
        this.pendingSkillPoints = 0;
        this.pendingLevelUp = false;
    }

    public void addXp(int amount) {
        currentXp += amount;
        int xpRequired = xpForNextLevel();
        if (currentXp >= xpRequired) {
            currentXp -= xpRequired;
            currentLevel++;
            pendingSkillPoints++;
            pendingLevelUp = true;
        }
    }

    public boolean applyBranchUpgrade(UpgradeBranch branch, PlayerBuild build) {
        if (pendingSkillPoints <= 0 || branch == null) {
            return false;
        }

        UpgradeOption option = createBranchUpgrade(branch);
        if (option == null) {
            return false;
        }

        applyUpgrade(option, build);
        branchLevels.put(branch, getBranchLevel(branch) + 1);
        pendingSkillPoints--;
        pendingLevelUp = pendingSkillPoints > 0;
        return true;
    }

    private UpgradeOption createBranchUpgrade(UpgradeBranch branch) {
        int branchLevel = getBranchLevel(branch);

        switch (branch) {
            case HEALTH:
                if (branchLevel == 0) {
                    return new UpgradeOption("tree_hp_1", "Vital Core", "+25 Max HP", UpgradeCategory.PHYSICAL, UpgradeTarget.MAX_HP, 25f, false);
                }
                return new UpgradeOption("tree_hp_large", "Fortified Body", "+20 Max HP", UpgradeCategory.PHYSICAL, UpgradeTarget.MAX_HP, 20f, false);
            case SPEED:
                if (branchLevel == 0) {
                    return new UpgradeOption("tree_speed_1", "Quick Step", "+18 Speed", UpgradeCategory.PHYSICAL, UpgradeTarget.SPEED, 18f, false);
                }
                return new UpgradeOption("tree_speed_2", "Fleet Footing", "+10 Speed", UpgradeCategory.PHYSICAL, UpgradeTarget.SPEED, 10f, false);
            case POWER:
                if (branchLevel == 0) {
                    return new UpgradeOption("tree_power_1", "Sharper Blade", "+12% Damage", UpgradeCategory.PHYSICAL, UpgradeTarget.DAMAGE_MULTIPLIER, 0.12f, true);
                }
                return new UpgradeOption("tree_power_heavy", "Heavy Impact", "+10% Damage", UpgradeCategory.PHYSICAL, UpgradeTarget.DAMAGE_MULTIPLIER, 0.10f, true);
            case MANA:
                if (branchLevel == 0) {
                    return new UpgradeOption("tree_mana_1", "Deep Well", "+25 Max Mana", UpgradeCategory.MAGICAL, UpgradeTarget.MAX_MANA, 25f, false);
                }
                if (branchLevel % 2 == 1) {
                    return new UpgradeOption("tree_mana_regen", "Flowing Focus", "+1.5 Mana Regen", UpgradeCategory.MAGICAL, UpgradeTarget.MANA_REGEN, 1.5f, false);
                }
                return new UpgradeOption("tree_mana_capacity", "Arcane Reserve", "+15 Max Mana", UpgradeCategory.MAGICAL, UpgradeTarget.MAX_MANA, 15f, false);
            default:
                return null;
        }
    }

    public UpgradeOption previewBranchUpgrade(UpgradeBranch branch) {
        return createBranchUpgrade(branch);
    }

    public int getBranchLevel(UpgradeBranch branch) {
        Integer level = branchLevels.get(branch);
        return level == null ? 0 : level;
    }

    public int getPendingSkillPoints() {
        return pendingSkillPoints;
    }

    public void clearLevelUpNoticeIfSpent() {
        if (pendingSkillPoints <= 0) {
            pendingLevelUp = false;
        }
    }

    public void applyChoice(int choiceIndex, PlayerBuild build) {
        UpgradeBranch[] branches = UpgradeBranch.values();
        if (choiceIndex < 0 || choiceIndex >= branches.length) {
            return;
        }
        applyBranchUpgrade(branches[choiceIndex], build);
    }

    public void applyUpgrade(UpgradeOption option, PlayerBuild build) {
        CombatStats combat = build.getCombatStats();
        PhysicalStats phys = build.getPhysicalStats();
        MagicalStats magic = build.getMagicalStats();
        ManaProfile mana = build.getManaProfile();

        switch (option.getTarget()) {
            case MAX_HP:
                combat.setMaxHp(combat.getMaxHp() + option.getValue());
                break;
            case DEFENSE:
                combat.setDefense(combat.getDefense() + option.getValue());
                break;
            case DAMAGE_MULTIPLIER:
                float dmgMult = option.isPercentage()
                        ? combat.getDamageMultiplier() * (1f + option.getValue())
                        : combat.getDamageMultiplier() + option.getValue();
                combat.setDamageMultiplier(dmgMult);
                break;
            case SPEED:
                phys.setSpeed(phys.getSpeed() + option.getValue());
                break;
            case DASH_COOLDOWN:
                phys.setDashCooldown(Math.max(0.2f, phys.getDashCooldown() + option.getValue()));
                break;
            case DASH_DISTANCE:
                phys.setDashDistance(phys.getDashDistance() + option.getValue());
                break;
            case ATTACK_SPEED:
                phys.setAttackSpeed(phys.getAttackSpeed() + option.getValue());
                break;
            case SPELL_POWER:
                magic.setSpellPower(magic.getSpellPower() + option.getValue());
                break;
            case ELEMENTAL_BONUS:
                magic.setElementalBonus(magic.getElementalBonus() + option.getValue());
                break;
            case ABILITY_CDR:
                magic.setAbilityCooldownReduction(magic.getAbilityCooldownReduction() + option.getValue());
                break;
            case MAX_MANA:
                mana.setMaxMana(mana.getMaxMana() + option.getValue());
                mana.add(option.getValue());
                break;
            case MANA_REGEN:
                mana.setManaRegenPerSecond(mana.getManaRegenPerSecond() + option.getValue());
                break;
            case MANA_BURST_THRESHOLD:
                mana.setBurstThreshold(mana.getBurstThreshold() + option.getValue());
                break;
            default:
                break;
        }
    }

    private int xpForNextLevel() {
        return BalanceConfig.XP_PER_LEVEL + (currentLevel - 1) * BalanceConfig.XP_LEVEL_SCALING;
    }

    public boolean isPendingLevelUp() {
        return pendingLevelUp;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public int getCurrentXp() {
        return currentXp;
    }

    public int getXpForNextLevel() {
        return xpForNextLevel();
    }

    public float getXpProgress() {
        return (float) currentXp / xpForNextLevel();
    }
}


package com.patternforge.echoesarena.progression;

import com.patternforge.echoesarena.config.BalanceConfig;
import com.patternforge.echoesarena.player.PlayerBuild;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;
import com.patternforge.echoesarena.stats.ManaProfile;
import com.patternforge.echoesarena.stats.PhysicalStats;

import java.util.List;

public class LevelUpService {

    private final UpgradePool upgradePool;
    private final UnlockService unlockService;

    private int currentXp;
    private int currentLevel;
    private boolean pendingLevelUp;
    private List<UpgradeOption> currentChoices;

    private static final int CHOICES_PER_LEVEL_UP = 3;

    public LevelUpService(UpgradePool upgradePool, UnlockService unlockService) {
        this.upgradePool = upgradePool;
        this.unlockService = unlockService;
        this.currentXp = 0;
        this.currentLevel = 1;
        this.pendingLevelUp = false;
    }

    public void addXp(int amount) {
        currentXp += amount;
        int xpRequired = xpForNextLevel();
        if (currentXp >= xpRequired) {
            currentXp -= xpRequired;
            currentLevel++;
            pendingLevelUp = true;
            currentChoices = upgradePool.drawChoices(CHOICES_PER_LEVEL_UP, unlockService);
        }
    }

    public void applyChoice(int choiceIndex, PlayerBuild build) {
        if (!pendingLevelUp || currentChoices == null) {
            return;
        }
        if (choiceIndex < 0 || choiceIndex >= currentChoices.size()) {
            return;
        }
        UpgradeOption chosen = currentChoices.get(choiceIndex);
        applyUpgrade(chosen, build);
        pendingLevelUp = false;
        currentChoices = null;
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
                float elemBonus = option.isPercentage()
                        ? magic.getElementalBonus() + option.getValue()
                        : magic.getElementalBonus() + option.getValue();
                magic.setElementalBonus(elemBonus);
                break;
            case ABILITY_CDR:
                float newCdr = option.isPercentage()
                        ? magic.getAbilityCooldownReduction() + option.getValue()
                        : magic.getAbilityCooldownReduction() + option.getValue();
                magic.setAbilityCooldownReduction(newCdr);
                break;
            case MAX_MANA:
                mana.setMaxMana(mana.getMaxMana() + option.getValue());
                break;
            case MANA_REGEN:
                mana.setManaRegenPerSecond(mana.getManaRegenPerSecond() + option.getValue());
                break;
            case MANA_BURST_THRESHOLD:
                mana.setBurstThreshold(Math.max(10f, mana.getBurstThreshold() + option.getValue()));
                break;
        }
    }

    private int xpForNextLevel() {
        return BalanceConfig.XP_PER_LEVEL + (currentLevel - 1) * BalanceConfig.XP_LEVEL_SCALING;
    }

    public boolean isPendingLevelUp() {
        return pendingLevelUp;
    }

    public List<UpgradeOption> getCurrentChoices() {
        return currentChoices;
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

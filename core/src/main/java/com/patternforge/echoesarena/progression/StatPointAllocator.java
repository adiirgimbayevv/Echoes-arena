package com.patternforge.echoesarena.progression;

import com.patternforge.echoesarena.player.PlayerBuild;

public class StatPointAllocator {

    private int availablePhysicalPoints;
    private int availableMagicalPoints;
    private int totalPhysicalSpent;
    private int totalMagicalSpent;

    private static final float PHYSICAL_PER_POINT_SPEED = 2f;
    private static final float PHYSICAL_PER_POINT_HP = 5f;
    private static final float PHYSICAL_PER_POINT_DEFENSE = 1f;
    private static final float PHYSICAL_PER_POINT_DAMAGE = 0.05f;

    private static final float MAGICAL_PER_POINT_SPELL_POWER = 5f;
    private static final float MAGICAL_PER_POINT_MANA = 10f;
    private static final float MAGICAL_PER_POINT_REGEN = 1f;
    private static final float MAGICAL_PER_POINT_CDR = 0.03f;

    public StatPointAllocator() {
        this.availablePhysicalPoints = 0;
        this.availableMagicalPoints = 0;
        this.totalPhysicalSpent = 0;
        this.totalMagicalSpent = 0;
    }

    public void addPhysicalPoints(int amount) {
        availablePhysicalPoints += amount;
    }

    public void addMagicalPoints(int amount) {
        availableMagicalPoints += amount;
    }

    public boolean spendPhysical(PhysicalStatField field, PlayerBuild build) {
        if (availablePhysicalPoints <= 0) {
            return false;
        }
        availablePhysicalPoints--;
        totalPhysicalSpent++;
        switch (field) {
            case SPEED:
                build.getPhysicalStats().setSpeed(build.getPhysicalStats().getSpeed() + PHYSICAL_PER_POINT_SPEED);
                break;
            case HP:
                build.getCombatStats().setMaxHp(build.getCombatStats().getMaxHp() + PHYSICAL_PER_POINT_HP);
                break;
            case DEFENSE:
                build.getCombatStats().setDefense(build.getCombatStats().getDefense() + PHYSICAL_PER_POINT_DEFENSE);
                break;
            case DAMAGE:
                build.getCombatStats().setDamageMultiplier(
                        build.getCombatStats().getDamageMultiplier() + PHYSICAL_PER_POINT_DAMAGE);
                break;
        }
        return true;
    }

    public boolean spendMagical(MagicalStatField field, PlayerBuild build) {
        if (availableMagicalPoints <= 0) {
            return false;
        }
        availableMagicalPoints--;
        totalMagicalSpent++;
        switch (field) {
            case SPELL_POWER:
                build.getMagicalStats().setSpellPower(
                        build.getMagicalStats().getSpellPower() + MAGICAL_PER_POINT_SPELL_POWER);
                break;
            case MAX_MANA:
                build.getManaProfile().setMaxMana(
                        build.getManaProfile().getMaxMana() + MAGICAL_PER_POINT_MANA);
                break;
            case MANA_REGEN:
                build.getManaProfile().setManaRegenPerSecond(
                        build.getManaProfile().getManaRegenPerSecond() + MAGICAL_PER_POINT_REGEN);
                break;
            case CDR:
                build.getMagicalStats().setAbilityCooldownReduction(
                        build.getMagicalStats().getAbilityCooldownReduction() + MAGICAL_PER_POINT_CDR);
                break;
        }
        return true;
    }

    public enum PhysicalStatField {
        SPEED, HP, DEFENSE, DAMAGE
    }

    public enum MagicalStatField {
        SPELL_POWER, MAX_MANA, MANA_REGEN, CDR
    }

    public int getAvailablePhysicalPoints() {
        return availablePhysicalPoints;
    }

    public int getAvailableMagicalPoints() {
        return availableMagicalPoints;
    }

    public int getTotalPhysicalSpent() {
        return totalPhysicalSpent;
    }

    public int getTotalMagicalSpent() {
        return totalMagicalSpent;
    }

    public boolean hasPointsToSpend() {
        return availablePhysicalPoints > 0 || availableMagicalPoints > 0;
    }
}

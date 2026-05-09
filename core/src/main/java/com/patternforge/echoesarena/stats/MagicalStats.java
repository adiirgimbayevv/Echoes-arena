
package com.patternforge.echoesarena.stats;

public class MagicalStats {

    private float spellPower;
    private float elementalBonus;
    private float abilityCooldownReduction;

    public MagicalStats(float spellPower, float elementalBonus, float abilityCooldownReduction) {
        this.spellPower = spellPower;
        this.elementalBonus = elementalBonus;
        this.abilityCooldownReduction = Math.min(abilityCooldownReduction, 0.75f);
    }

    public float getSpellPower() {
        return spellPower;
    }

    public void setSpellPower(float spellPower) {
        this.spellPower = spellPower;
    }

    public float getElementalBonus() {
        return elementalBonus;
    }

    public void setElementalBonus(float elementalBonus) {
        this.elementalBonus = elementalBonus;
    }

    public float getAbilityCooldownReduction() {
        return abilityCooldownReduction;
    }

    public void setAbilityCooldownReduction(float abilityCooldownReduction) {
        this.abilityCooldownReduction = Math.min(abilityCooldownReduction, 0.75f);
    }

    public float applySpellPower(float baseValue) {
        return baseValue * (1f + spellPower / 100f);
    }
}

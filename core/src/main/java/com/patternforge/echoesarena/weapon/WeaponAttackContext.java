package com.patternforge.echoesarena.weapon;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.ability.ElementProgress;
import com.patternforge.echoesarena.combat.CombatSystem;

import java.util.List;

public class WeaponAttackContext {

    private final CombatSystem.CombatTarget attacker;
    private final List<? extends CombatSystem.CombatTarget> targets;
    private final Vector2 attackDirection;
    private final CombatSystem combatSystem;
    private final ElementProgress elementProgress;
    private final float compatibilityBonus;

    public WeaponAttackContext(CombatSystem.CombatTarget attacker,
                               List<? extends CombatSystem.CombatTarget> targets,
                               Vector2 attackDirection,
                               CombatSystem combatSystem,
                               ElementProgress elementProgress,
                               float compatibilityBonus) {
        this.attacker = attacker;
        this.targets = targets;
        this.attackDirection = attackDirection;
        this.combatSystem = combatSystem;
        this.elementProgress = elementProgress;
        this.compatibilityBonus = compatibilityBonus;
    }

    public CombatSystem.CombatTarget getAttacker() {
        return attacker;
    }

    public List<? extends CombatSystem.CombatTarget> getTargets() {
        return targets;
    }

    public Vector2 getAttackDirection() {
        return attackDirection;
    }

    public CombatSystem getCombatSystem() {
        return combatSystem;
    }

    public ElementProgress getElementProgress() {
        return elementProgress;
    }

    public float getCompatibilityBonus() {
        return compatibilityBonus;
    }
}

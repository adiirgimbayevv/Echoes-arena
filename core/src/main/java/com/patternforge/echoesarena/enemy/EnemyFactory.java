package com.patternforge.echoesarena.enemy;

import com.patternforge.echoesarena.combat.CombatSystem;
import com.patternforge.echoesarena.enemy.ai.ChaseStrategy;
import com.patternforge.echoesarena.enemy.ai.EnemyStrategy;
import com.patternforge.echoesarena.enemy.ai.RangedStrategy;
import com.patternforge.echoesarena.enemy.ai.SupportStrategy;
import com.patternforge.echoesarena.enemy.ai.SwarmStrategy;
import com.patternforge.echoesarena.enemy.ai.TankStrategy;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.stats.CombatStats;
import com.patternforge.echoesarena.stats.MagicalStats;

public class EnemyFactory {

    private final CombatSystem combatSystem;

    public EnemyFactory(CombatSystem combatSystem) {
        this.combatSystem = combatSystem;
    }

    public Enemy create(EnemyType type, float x, float y) {
        CombatStats combatStats = new CombatStats(
                type.getBaseHp(),
                type.getBaseDefense(),
                type.getDamageMultiplier()
        );
        MagicalStats magicalStats = new MagicalStats(0f, 0f, 0f);
        EnemyStrategy strategy = buildStrategy(type);
        float hitboxSize = resolveHitboxSize(type);

        return new Enemy(type, x, y, hitboxSize, combatStats, magicalStats, strategy, combatSystem);
    }

    private EnemyStrategy buildStrategy(EnemyType type) {
        switch (type) {
            case GRUNT:
                return new ChaseStrategy(16f, 1.2f);
            case SWARM:
                return new SwarmStrategy(12f, 20f, 0.8f);
            case TANK:
                return new TankStrategy(20f, 80f, 2.0f);
            case SNIPER:
                return new RangedStrategy(120f, 60f, 2.5f);
            case SUPPORT:
                return new SupportStrategy(48f, 15f, 3.0f, 64f);
            default:
                return new ChaseStrategy(16f, 1.2f);
        }
    }

    private float resolveHitboxSize(EnemyType type) {
        switch (type) {
            case TANK:
                return 20f;
            case SWARM:
                return 8f;
            default:
                return 12f;
        }
    }
}

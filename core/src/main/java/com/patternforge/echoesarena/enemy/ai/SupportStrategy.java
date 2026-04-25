package com.patternforge.echoesarena.enemy.ai;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.enemy.EnemyState;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.List;

public class SupportStrategy implements EnemyStrategy {

    private final float healRadius;
    private final float healAmount;
    private final float healCooldown;
    private final float safeDistance;
    private float healTimer;

    public SupportStrategy(float healRadius, float healAmount, float healCooldown, float safeDistance) {
        this.healRadius = healRadius;
        this.healAmount = healAmount;
        this.healCooldown = healCooldown;
        this.safeDistance = safeDistance;
        this.healTimer = 0f;
    }

    @Override
    public void execute(Enemy self, Player target, List<Enemy> nearbyEnemies, float delta) {
        if (healTimer > 0) {
            healTimer -= delta;
        }

        Vector2 toPlayer = new Vector2(target.getPosition()).sub(self.getPosition());
        float distToPlayer = toPlayer.len();

        if (distToPlayer < safeDistance) {
            Vector2 retreat = toPlayer.nor().scl(-1f);
            float speed = self.getEffectiveSpeed();
            self.setVelocity(retreat.x * speed, retreat.y * speed);
            self.getStateMachine().transition(EnemyState.CHASE);
            return;
        }

        Enemy weakestAlly = findWeakestAlly(self, nearbyEnemies);

        if (weakestAlly != null) {
            Vector2 toAlly = new Vector2(weakestAlly.getPosition()).sub(self.getPosition());
            float distToAlly = toAlly.len();

            if (distToAlly > healRadius) {
                Vector2 dir = toAlly.nor();
                float speed = self.getEffectiveSpeed();
                self.setVelocity(dir.x * speed, dir.y * speed);
                self.getStateMachine().transition(EnemyState.CHASE);
            } else {
                self.setVelocity(0, 0);
                self.getStateMachine().transition(EnemyState.ATTACK);
                if (healTimer <= 0) {
                    weakestAlly.heal(healAmount);
                    healTimer = healCooldown;
                }
            }
        } else {
            self.setVelocity(0, 0);
            self.getStateMachine().transition(EnemyState.IDLE);
        }
    }

    private Enemy findWeakestAlly(Enemy self, List<Enemy> nearby) {
        Enemy weakest = null;
        float lowestHpRatio = 1f;
        for (Enemy ally : nearby) {
            if (ally == self || !ally.isAlive()) {
                continue;
            }
            float ratio = ally.getCombatStats().getHpRatio();
            if (ratio < lowestHpRatio) {
                lowestHpRatio = ratio;
                weakest = ally;
            }
        }
        return weakest;
    }
}

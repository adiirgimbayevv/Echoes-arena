package com.patternforge.echoesarena.enemy.ai;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.enemy.EnemyState;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.List;

public class SwarmStrategy implements EnemyStrategy {

    private final float attackRange;
    private final float separationRadius;
    private final float attackCooldown;
    private float attackTimer;

    public SwarmStrategy(float attackRange, float separationRadius, float attackCooldown) {
        this.attackRange = attackRange;
        this.separationRadius = separationRadius;
        this.attackCooldown = attackCooldown;
        this.attackTimer = 0f;
    }

    @Override
    public void execute(Enemy self, Player target, List<Enemy> nearbyEnemies, float delta) {
        if (attackTimer > 0) {
            attackTimer -= delta;
        }

        Vector2 toTarget = new Vector2(target.getPosition()).sub(self.getPosition());
        float distance = toTarget.len();

        Vector2 separation = computeSeparation(self, nearbyEnemies);

        if (distance <= attackRange) {
            self.getStateMachine().transition(EnemyState.ATTACK);
            self.setVelocity(0, 0);
            if (attackTimer <= 0) {
                self.performAttack(target);
                attackTimer = attackCooldown;
            }
            return;
        }

        Vector2 desired = toTarget.nor().add(separation.scl(0.5f)).nor();
        float speed = self.getEffectiveSpeed();
        self.setVelocity(desired.x * speed, desired.y * speed);
        self.getStateMachine().transition(EnemyState.CHASE);
    }

    private Vector2 computeSeparation(Enemy self, List<Enemy> nearby) {
        Vector2 steer = new Vector2();
        int count = 0;
        for (Enemy other : nearby) {
            if (other == self) {
                continue;
            }
            Vector2 diff = new Vector2(self.getPosition()).sub(other.getPosition());
            float dist = diff.len();
            if (dist < separationRadius && dist > 0) {
                steer.add(diff.nor().scl(1f / dist));
                count++;
            }
        }
        if (count > 0) {
            steer.scl(1f / count);
        }
        return steer;
    }
}

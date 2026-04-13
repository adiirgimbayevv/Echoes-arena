package com.patternforge.echoesarena.enemy.ai;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.enemy.EnemyState;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.List;

public class ChaseStrategy implements EnemyStrategy {

    private final float attackRange;
    private final float attackCooldown;
    private float attackTimer;

    public ChaseStrategy(float attackRange, float attackCooldown) {
        this.attackRange = attackRange;
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

        if (distance <= attackRange) {
            self.getStateMachine().transition(EnemyState.ATTACK);
            self.setVelocity(0, 0);
            if (attackTimer <= 0) {
                self.performAttack(target);
                attackTimer = attackCooldown;
            }
        } else {
            self.getStateMachine().transition(EnemyState.CHASE);
            Vector2 dir = toTarget.nor();
            float speed = self.getEffectiveSpeed();
            self.setVelocity(dir.x * speed, dir.y * speed);
        }
    }
}

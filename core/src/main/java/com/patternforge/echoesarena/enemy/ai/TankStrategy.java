package com.patternforge.echoesarena.enemy.ai;

import com.badlogic.gdx.math.Vector2;
import com.patternforge.echoesarena.enemy.EnemyState;
import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.List;

public class TankStrategy implements EnemyStrategy {

    private final float attackRange;
    private final float chargeRange;
    private final float attackCooldown;
    private float attackTimer;
    private boolean charging;

    public TankStrategy(float attackRange, float chargeRange, float attackCooldown) {
        this.attackRange = attackRange;
        this.chargeRange = chargeRange;
        this.attackCooldown = attackCooldown;
        this.attackTimer = 0f;
        this.charging = false;
    }

    @Override
    public void execute(Enemy self, Player target, List<Enemy> nearbyEnemies, float delta) {
        if (attackTimer > 0) {
            attackTimer -= delta;
        }

        Vector2 toTarget = new Vector2(target.getPosition()).sub(self.getPosition());
        float distance = toTarget.len();

        if (distance <= attackRange) {
            charging = false;
            self.setVelocity(0, 0);
            self.getStateMachine().transition(EnemyState.ATTACK);
            if (attackTimer <= 0) {
                self.performAttack(target);
                attackTimer = attackCooldown;
            }
            return;
        }

        if (distance <= chargeRange) {
            charging = true;
        }

        Vector2 dir = toTarget.nor();
        float speed = charging ? self.getEffectiveSpeed() * 1.8f : self.getEffectiveSpeed();
        self.setVelocity(dir.x * speed, dir.y * speed);
        self.getStateMachine().transition(EnemyState.CHASE);
    }
}

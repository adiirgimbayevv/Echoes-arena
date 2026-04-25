package com.patternforge.echoesarena.enemy.ai;

import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.List;

public interface EnemyStrategy {
    void execute(Enemy self, Player target, List<Enemy> nearbyEnemies, float delta);
}

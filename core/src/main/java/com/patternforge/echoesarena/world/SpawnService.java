package com.patternforge.echoesarena.world;

import com.patternforge.echoesarena.enemy.EnemyFactory;
import com.patternforge.echoesarena.enemy.EnemyType;
import com.patternforge.echoesarena.entity.Enemy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SpawnService {

    private final EnemyFactory enemyFactory;

    public SpawnService(EnemyFactory enemyFactory) {
        this.enemyFactory = enemyFactory;
    }

    public List<Enemy> spawnWave(WaveDefinition wave, List<SpawnPoint> spawnPoints) {
        Map<String, SpawnPoint> spawnPointMap = spawnPoints.stream()
                .collect(Collectors.toMap(SpawnPoint::getId, Function.identity()));

        List<Enemy> spawned = new ArrayList<>();

        for (WaveDefinition.SpawnEntry entry : wave.getEntries()) {
            SpawnPoint point = spawnPointMap.get(entry.getSpawnPointId());
            if (point == null) {
                continue;
            }
            EnemyType type = resolveType(entry.getEnemyType());
            if (type == null) {
                continue;
            }
            for (int i = 0; i < entry.getCount(); i++) {
                float offsetX = (float) (Math.random() * 16 - 8);
                float offsetY = (float) (Math.random() * 16 - 8);
                Enemy enemy = enemyFactory.create(type, point.getX() + offsetX, point.getY() + offsetY);
                spawned.add(enemy);
            }
        }

        return spawned;
    }

    private EnemyType resolveType(String name) {
        try {
            return EnemyType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

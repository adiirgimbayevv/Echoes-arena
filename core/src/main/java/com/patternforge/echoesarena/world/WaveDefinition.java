package com.patternforge.echoesarena.world;

import java.util.List;

public class WaveDefinition {

    private final int waveIndex;
    private final float delayBeforeWave;
    private final List<SpawnEntry> entries;

    public WaveDefinition(int waveIndex, float delayBeforeWave, List<SpawnEntry> entries) {
        this.waveIndex = waveIndex;
        this.delayBeforeWave = delayBeforeWave;
        this.entries = entries;
    }

    public int getWaveIndex() {
        return waveIndex;
    }

    public float getDelayBeforeWave() {
        return delayBeforeWave;
    }

    public List<SpawnEntry> getEntries() {
        return entries;
    }

    public static class SpawnEntry {

        private final String enemyType;
        private final String spawnPointId;
        private final int count;
        private final float intervalBetweenSpawns;

        public SpawnEntry(String enemyType, String spawnPointId, int count, float intervalBetweenSpawns) {
            this.enemyType = enemyType;
            this.spawnPointId = spawnPointId;
            this.count = count;
            this.intervalBetweenSpawns = intervalBetweenSpawns;
        }

        public String getEnemyType() {
            return enemyType;
        }

        public String getSpawnPointId() {
            return spawnPointId;
        }

        public int getCount() {
            return count;
        }

        public float getIntervalBetweenSpawns() {
            return intervalBetweenSpawns;
        }
    }
}

package com.patternforge.echoesarena.world;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StageRepository {

    private static final String STAGE_PATH_TEMPLATE = "stages/stage_%02d.json";
    private static final int TOTAL_STAGES = 10;

    private final MapLoader mapLoader;
    private final Map<Integer, StageDefinition> cache;

    public StageRepository(MapLoader mapLoader) {
        this.mapLoader = mapLoader;
        this.cache = new LinkedHashMap<>();
    }

    public StageDefinition getStage(int stageId) {
        if (cache.containsKey(stageId)) {
            return cache.get(stageId);
        }

        String path = String.format(STAGE_PATH_TEMPLATE, stageId);
        StageDefinition definition;

        try {
            definition = mapLoader.load(path);
        } catch (Exception e) {
            StageDefinition baseStage = mapLoader.load(String.format(STAGE_PATH_TEMPLATE, 1));
            definition = createScaledStage(baseStage, stageId);
        }

        cache.put(stageId, definition);
        return definition;
    }

    private StageDefinition createScaledStage(StageDefinition baseStage, int stageId) {
        float difficultyMultiplier = 1f + (stageId - 1) * 0.25f;
        List<WaveDefinition> scaledWaves = new ArrayList<>();

        for (WaveDefinition wave : baseStage.getWaves()) {
            List<WaveDefinition.SpawnEntry> scaledEntries = new ArrayList<>();

            for (WaveDefinition.SpawnEntry entry : wave.getEntries()) {
                int scaledCount = Math.max(1, Math.round(entry.getCount() * difficultyMultiplier));
                float scaledInterval = Math.max(0.12f, entry.getIntervalBetweenSpawns() - (stageId - 1) * 0.025f);

                scaledEntries.add(new WaveDefinition.SpawnEntry(
                    entry.getEnemyType(),
                    entry.getSpawnPointId(),
                    scaledCount,
                    scaledInterval
                ));
            }

            float scaledDelay = Math.max(1f, wave.getDelayBeforeWave() - (stageId - 1) * 0.12f);

            scaledWaves.add(new WaveDefinition(
                wave.getWaveIndex(),
                scaledDelay,
                scaledEntries
            ));
        }

        return new StageDefinition(
            stageId,
            "Arena Level " + stageId,
            baseStage.getMapPath(),
            baseStage.getMusicPath(),
            baseStage.getSpawnPoints(),
            scaledWaves,
            false,
            ""
        );
    }

    public void preloadAll() {
        for (int i = 1; i <= TOTAL_STAGES; i++) {
            getStage(i);
        }
    }

    public int getTotalStages() {
        return TOTAL_STAGES;
    }

    public void clearCache() {
        cache.clear();
    }
}

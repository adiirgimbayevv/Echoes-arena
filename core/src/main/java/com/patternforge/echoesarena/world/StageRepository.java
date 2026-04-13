package com.patternforge.echoesarena.world;

import java.util.LinkedHashMap;
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
        StageDefinition definition = mapLoader.load(path);
        cache.put(stageId, definition);
        return definition;
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

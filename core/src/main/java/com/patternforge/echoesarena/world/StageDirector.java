package com.patternforge.echoesarena.world;

import com.patternforge.echoesarena.entity.Enemy;
import com.patternforge.echoesarena.entity.Player;

import java.util.List;

public class StageDirector {

    public enum StageState {
        LOADING,
        RUNNING,
        BOSS_PHASE,
        STAGE_CLEAR,
        STAGE_FAILED
    }

    private final StageRepository stageRepository;
    private final SpawnService spawnService;

    private StageDefinition currentStage;
    private WaveDirector waveDirector;
    private StageState state;
    private int currentStageId;

    public StageDirector(StageRepository stageRepository, SpawnService spawnService) {
        this.stageRepository = stageRepository;
        this.spawnService = spawnService;
        this.state = StageState.LOADING;
    }

    public void loadStage(int stageId) {
        currentStageId = stageId;
        currentStage = stageRepository.getStage(stageId);
        waveDirector = new WaveDirector(
                currentStage.getWaves(),
                currentStage.getSpawnPoints(),
                spawnService
        );
        state = StageState.RUNNING;
        waveDirector.start();
    }

    public void update(float delta, Player player) {
        if (player.isDead()) {
            state = StageState.STAGE_FAILED;
            return;
        }

        if (state == StageState.RUNNING) {
            waveDirector.update(delta);

            List<Enemy> enemies = waveDirector.getActiveEnemies();
            for (Enemy enemy : enemies) {
                enemy.update(delta, player, enemies);
            }

            if (waveDirector.isFinished()) {
                if (currentStage.hasBoss()) {
                    state = StageState.BOSS_PHASE;
                } else {
                    state = StageState.STAGE_CLEAR;
                }
            }
        }
    }

    public List<Enemy> getActiveEnemies() {
        return waveDirector != null ? waveDirector.getActiveEnemies() : List.of();
    }

    public StageState getState() {
        return state;
    }

    public boolean isStageClear() {
        return state == StageState.STAGE_CLEAR;
    }

    public boolean isStageFailed() {
        return state == StageState.STAGE_FAILED;
    }

    public boolean isBossPhase() {
        return state == StageState.BOSS_PHASE;
    }

    public StageDefinition getCurrentStage() {
        return currentStage;
    }

    public int getCurrentWaveIndex() {
        return waveDirector != null ? waveDirector.getCurrentWaveIndex() : 0;
    }

    public int getTotalWaves() {
        return waveDirector != null ? waveDirector.getTotalWaves() : 0;
    }

    public int getCurrentStageId() {
        return currentStageId;
    }
}

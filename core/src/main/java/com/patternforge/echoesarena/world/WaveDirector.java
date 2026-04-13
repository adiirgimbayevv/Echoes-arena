package com.patternforge.echoesarena.world;

import com.patternforge.echoesarena.entity.Enemy;

import java.util.ArrayList;
import java.util.List;

public class WaveDirector {

    public enum WaveState {
        IDLE,
        WAITING,
        SPAWNING,
        ACTIVE,
        COMPLETED,
        ALL_WAVES_DONE
    }

    private final List<WaveDefinition> waves;
    private final List<SpawnPoint> spawnPoints;
    private final SpawnService spawnService;
    private final List<Enemy> activeEnemies;

    private WaveState state;
    private int currentWaveIndex;
    private float stateTimer;

    public WaveDirector(List<WaveDefinition> waves, List<SpawnPoint> spawnPoints,
                        SpawnService spawnService) {
        this.waves = waves;
        this.spawnPoints = spawnPoints;
        this.spawnService = spawnService;
        this.activeEnemies = new ArrayList<>();
        this.state = WaveState.IDLE;
        this.currentWaveIndex = 0;
        this.stateTimer = 0f;
    }

    public void start() {
        if (waves.isEmpty()) {
            state = WaveState.ALL_WAVES_DONE;
            return;
        }
        currentWaveIndex = 0;
        enterWaiting();
    }

    public void update(float delta) {
        activeEnemies.removeIf(e -> !e.isAlive());

        switch (state) {
            case WAITING:
                stateTimer -= delta;
                if (stateTimer <= 0) {
                    spawnCurrentWave();
                }
                break;
            case ACTIVE:
                if (activeEnemies.isEmpty()) {
                    state = WaveState.COMPLETED;
                    advanceWave();
                }
                break;
            default:
                break;
        }
    }

    private void enterWaiting() {
        WaveDefinition wave = waves.get(currentWaveIndex);
        stateTimer = wave.getDelayBeforeWave();
        state = WaveState.WAITING;
    }

    private void spawnCurrentWave() {
        WaveDefinition wave = waves.get(currentWaveIndex);
        List<Enemy> spawned = spawnService.spawnWave(wave, spawnPoints);
        activeEnemies.addAll(spawned);
        state = WaveState.ACTIVE;
    }

    private void advanceWave() {
        currentWaveIndex++;
        if (currentWaveIndex >= waves.size()) {
            state = WaveState.ALL_WAVES_DONE;
        } else {
            enterWaiting();
        }
    }

    public boolean isFinished() {
        return state == WaveState.ALL_WAVES_DONE;
    }

    public List<Enemy> getActiveEnemies() {
        return activeEnemies;
    }

    public WaveState getState() {
        return state;
    }

    public int getCurrentWaveIndex() {
        return currentWaveIndex;
    }

    public int getTotalWaves() {
        return waves.size();
    }
}

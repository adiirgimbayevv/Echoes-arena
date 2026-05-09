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
    private final List<PendingEntry> pendingEntries;

    private WaveState state;
    private int currentWaveIndex;
    private float stateTimer;
    private float spawnTimer;
    private int pendingEntryIndex;

    public WaveDirector(List<WaveDefinition> waves, List<SpawnPoint> spawnPoints,
                        SpawnService spawnService) {
        this.waves = waves;
        this.spawnPoints = spawnPoints;
        this.spawnService = spawnService;
        this.activeEnemies = new ArrayList<>();
        this.pendingEntries = new ArrayList<>();
        this.state = WaveState.IDLE;
        this.currentWaveIndex = 0;
        this.stateTimer = 0f;
        this.spawnTimer = 0f;
        this.pendingEntryIndex = 0;
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
                    beginSpawningCurrentWave();
                }
                break;
            case SPAWNING:
                updateSpawning(delta);
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

    private void beginSpawningCurrentWave() {
        WaveDefinition wave = waves.get(currentWaveIndex);
        pendingEntries.clear();

        for (WaveDefinition.SpawnEntry entry : wave.getEntries()) {
            if (entry.getCount() <= 0) {
                continue;
            }
            pendingEntries.add(new PendingEntry(entry));
        }

        pendingEntryIndex = 0;
        spawnTimer = 0f;
        state = pendingEntries.isEmpty() ? WaveState.ACTIVE : WaveState.SPAWNING;
    }

    private void updateSpawning(float delta) {
        if (pendingEntries.isEmpty()) {
            state = WaveState.ACTIVE;
            return;
        }

        spawnTimer -= delta;
        if (spawnTimer > 0f) {
            return;
        }

        PendingEntry current = pickNextPendingEntry();
        if (current == null) {
            pendingEntries.clear();
            state = WaveState.ACTIVE;
            return;
        }

        Enemy spawnedEnemy = spawnService.spawnSingle(current.entry, spawnPoints);
        if (spawnedEnemy != null) {
            activeEnemies.add(spawnedEnemy);
        }

        current.remainingCount--;
        if (current.remainingCount <= 0) {
            pendingEntries.remove(current);
        }

        spawnTimer = Math.max(0.05f, current.entry.getIntervalBetweenSpawns());
        if (pendingEntries.isEmpty()) {
            state = WaveState.ACTIVE;
        }
    }

    private PendingEntry pickNextPendingEntry() {
        if (pendingEntries.isEmpty()) {
            return null;
        }

        if (pendingEntryIndex >= pendingEntries.size()) {
            pendingEntryIndex = 0;
        }

        PendingEntry selected = pendingEntries.get(pendingEntryIndex);
        pendingEntryIndex++;
        return selected;
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

    private static final class PendingEntry {
        private final WaveDefinition.SpawnEntry entry;
        private int remainingCount;

        private PendingEntry(WaveDefinition.SpawnEntry entry) {
            this.entry = entry;
            this.remainingCount = entry.getCount();
        }
    }
}

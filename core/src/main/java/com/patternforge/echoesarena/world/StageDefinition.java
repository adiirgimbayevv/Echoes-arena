package com.patternforge.echoesarena.world;

import java.util.List;

public class StageDefinition {

    private final int stageId;
    private final String name;
    private final String mapPath;
    private final String musicPath;
    private final List<SpawnPoint> spawnPoints;
    private final List<WaveDefinition> waves;
    private final boolean hasBoss;
    private final String bossType;

    public StageDefinition(int stageId, String name, String mapPath, String musicPath,
                           List<SpawnPoint> spawnPoints, List<WaveDefinition> waves,
                           boolean hasBoss, String bossType) {
        this.stageId = stageId;
        this.name = name;
        this.mapPath = mapPath;
        this.musicPath = musicPath;
        this.spawnPoints = spawnPoints;
        this.waves = waves;
        this.hasBoss = hasBoss;
        this.bossType = bossType;
    }

    public int getStageId() {
        return stageId;
    }

    public String getName() {
        return name;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public List<SpawnPoint> getSpawnPoints() {
        return spawnPoints;
    }

    public List<WaveDefinition> getWaves() {
        return waves;
    }

    public boolean hasBoss() {
        return hasBoss;
    }

    public String getBossType() {
        return bossType;
    }

    public int getTotalWaves() {
        return waves.size();
    }
}

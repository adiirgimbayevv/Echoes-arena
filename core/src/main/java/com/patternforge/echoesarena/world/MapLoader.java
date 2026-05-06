package com.patternforge.echoesarena.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class MapLoader {

    private final JsonReader jsonReader;

    public MapLoader() {
        this.jsonReader = new JsonReader();
    }

    public StageDefinition load(String path) {
        JsonValue root = jsonReader.parse(Gdx.files.internal(path));

        int stageId = root.getInt("stageId");
        String name = root.getString("name");
        String mapPath = root.getString("mapPath");
        String musicPath = root.getString("musicPath", "");
        boolean hasBoss = root.getBoolean("hasBoss", false);
        String bossType = root.getString("bossType", "");

        List<SpawnPoint> spawnPoints = parseSpawnPoints(root.get("spawnPoints"));
        List<WaveDefinition> waves = parseWaves(root.get("waves"));

        return new StageDefinition(stageId, name, mapPath, musicPath, spawnPoints, waves, hasBoss, bossType);
    }

    private List<SpawnPoint> parseSpawnPoints(JsonValue array) {
        List<SpawnPoint> result = new ArrayList<>();
        if (array == null) {
            return result;
        }
        for (JsonValue sp : array) {
            String id = sp.getString("id");
            float x = sp.getFloat("x");
            float y = sp.getFloat("y");
            result.add(new SpawnPoint(id, x, y));
        }
        return result;
    }

    private List<WaveDefinition> parseWaves(JsonValue array) {
        List<WaveDefinition> result = new ArrayList<>();
        if (array == null) {
            return result;
        }
        for (JsonValue wv : array) {
            int waveIndex = wv.getInt("waveIndex");
            float delay = wv.getFloat("delayBeforeWave", 0f);
            List<WaveDefinition.SpawnEntry> entries = parseEntries(wv.get("entries"));
            result.add(new WaveDefinition(waveIndex, delay, entries));
        }
        return result;
    }

    private List<WaveDefinition.SpawnEntry> parseEntries(JsonValue array) {
        List<WaveDefinition.SpawnEntry> result = new ArrayList<>();
        if (array == null) {
            return result;
        }
        for (JsonValue entry : array) {
            String enemyType = entry.getString("enemyType");
            String spawnPointId = entry.getString("spawnPointId");
            int count = entry.getInt("count");
            float interval = entry.getFloat("interval", 0.3f);
            result.add(new WaveDefinition.SpawnEntry(enemyType, spawnPointId, count, interval));
        }
        return result;
    }
}

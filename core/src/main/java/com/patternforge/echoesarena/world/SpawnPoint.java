package com.patternforge.echoesarena.world;

public class SpawnPoint {

    private final float x;
    private final float y;
    private final String id;

    public SpawnPoint(String id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}

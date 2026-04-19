package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.math.Vector2;

class HealthPickup {
    final Vector2 position;
    final float radius;
    final float healAmount;

    HealthPickup(float x, float y, float radius, float healAmount) {
        this.position = new Vector2(x, y);
        this.radius = radius;
        this.healAmount = healAmount;
    }
}

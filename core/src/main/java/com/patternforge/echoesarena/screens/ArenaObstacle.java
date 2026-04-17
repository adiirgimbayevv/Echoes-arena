package com.patternforge.echoesarena.screens;

import com.badlogic.gdx.math.Rectangle;

class ArenaObstacle {
    final Rectangle bounds;
    final int type;

    ArenaObstacle(float x, float y, float width, float height, int type) {
        this.bounds = new Rectangle(x, y, width, height);
        this.type = type;
    }

    float centerX() {
        return bounds.x + bounds.width / 2f;
    }

    float centerY() {
        return bounds.y + bounds.height / 2f;
    }
}

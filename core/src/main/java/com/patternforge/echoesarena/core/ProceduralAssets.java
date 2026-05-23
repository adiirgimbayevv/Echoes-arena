package com.patternforge.echoesarena.core;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ProceduralAssets {

    public static Animation<TextureRegion> createPlayerWalkAnimation() {
        return createEntityAnimation(Color.CYAN, Color.BLUE);
    }

    public static Animation<TextureRegion> createEnemySwarmAnimation() {
        return createEntityAnimation(Color.ORANGE, Color.RED);
    }

    public static Animation<TextureRegion> createEnemyGruntAnimation() {
        return createEntityAnimation(Color.RED, Color.SCARLET);
    }

    public static Animation<TextureRegion> createEnemyTankAnimation() {
        return createEntityAnimation(Color.PURPLE, Color.MAROON);
    }

    private static Animation<TextureRegion> createEntityAnimation(Color bodyColor, Color outlineColor) {
        int width = 16;
        int height = 16;
        int frames = 4;
        Pixmap pixmap = new Pixmap(width * frames, height, Pixmap.Format.RGBA8888);

        for (int frame = 0; frame < frames; frame++) {
            int offsetX = frame * width;
            
            // Draw body
            pixmap.setColor(bodyColor);
            pixmap.fillRectangle(offsetX + 4, 4, 8, 8);
            
            // Draw outline
            pixmap.setColor(outlineColor);
            pixmap.drawRectangle(offsetX + 4, 4, 8, 8);

            // Draw eyes (always looking right for simplicity)
            pixmap.setColor(Color.WHITE);
            pixmap.drawPixel(offsetX + 8, 6);
            pixmap.drawPixel(offsetX + 10, 6);

            // Draw animated legs
            pixmap.setColor(outlineColor);
            if (frame == 0 || frame == 2) {
                // Standing still
                pixmap.drawLine(offsetX + 6, 12, offsetX + 6, 14);
                pixmap.drawLine(offsetX + 10, 12, offsetX + 10, 14);
            } else if (frame == 1) {
                // Right leg forward
                pixmap.drawLine(offsetX + 6, 12, offsetX + 4, 14);
                pixmap.drawLine(offsetX + 10, 12, offsetX + 12, 14);
            } else {
                // Left leg forward
                pixmap.drawLine(offsetX + 6, 12, offsetX + 8, 14);
                pixmap.drawLine(offsetX + 10, 12, offsetX + 8, 14);
            }
        }

        Texture texture = new Texture(pixmap);
        pixmap.dispose();

        TextureRegion[][] tmp = TextureRegion.split(texture, width, height);
        TextureRegion[] framesArray = new TextureRegion[frames];
        System.arraycopy(tmp[0], 0, framesArray, 0, frames);

        com.badlogic.gdx.utils.Array<TextureRegion> gdxArray = new com.badlogic.gdx.utils.Array<>(framesArray);
        return new Animation<TextureRegion>(0.15f, gdxArray, Animation.PlayMode.LOOP);
    }
}

package com.patternforge.echoesarena.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

public class ScreenRouter {

    private final Game game;
    private GameContext context;
    private Screen currentScreen;

    public ScreenRouter(Game game) {
        this.game = game;
    }

    public void setContext(GameContext context) {
        this.context = context;
    }

    public void goTo(Screen screen) {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = screen;
        game.setScreen(screen);
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public GameContext getContext() {
        return context;
    }
}

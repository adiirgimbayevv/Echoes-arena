package com.patternforge.echoesarena.core;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.ScreenAdapter;
import com.patternforge.echoesarena.EchoesArenaGame;
import com.patternforge.echoesarena.screens.BootScreen;
import com.patternforge.echoesarena.screens.GameOverScreen;
import com.patternforge.echoesarena.screens.GameplayScreen;
import com.patternforge.echoesarena.screens.MainMenuScreen;
import com.patternforge.echoesarena.screens.PauseScreen;
import com.patternforge.echoesarena.screens.VictoryRoomScreen;

public class ScreenRouter {

    private final EchoesArenaGame game;

    public ScreenRouter(EchoesArenaGame game) {
        this.game = game;
    }

    public void goToBoot() {
        game.setScreen(new BootScreen(game.context));
    }

    public void goToMainMenu() {
        game.setScreen(new MainMenuScreen(game.context));
    }

    public void goToGameplay() {
        game.setScreen(new GameplayScreen(game.context));
    }

    public void goToGameOver() {
        game.setScreen(new GameOverScreen(game.context, false));
    }

    public void goToVictory() {
        game.setScreen(new VictoryRoomScreen(game.context));
    }

    public void goToPause(ScreenAdapter previousScreen) {
        game.setScreen(new PauseScreen(game.context, previousScreen));
    }

    public void returnTo(Screen previousScreen) {
        Screen currentScreen = game.getScreen();
        game.setScreen(previousScreen);
        if (currentScreen != null && currentScreen != previousScreen) {
            currentScreen.dispose();
        }
    }
}

package com.patternforge.echoesarena;

import com.badlogic.gdx.Game;
import com.patternforge.echoesarena.core.AssetService;
import com.patternforge.echoesarena.core.AudioService;
import com.patternforge.echoesarena.core.EventBus;
import com.patternforge.echoesarena.core.GameContext;
import com.patternforge.echoesarena.core.SaveService;
import com.patternforge.echoesarena.core.ScreenRouter;
import com.patternforge.echoesarena.screens.BootScreen;

public class EchoesArenaGame extends Game {

    private GameContext context;

    @Override
    public void create() {
        EventBus eventBus = new EventBus();
        AssetService assetService = new AssetService();
        AudioService audioService = new AudioService(assetService);
        ScreenRouter screenRouter = new ScreenRouter(this);
        SaveService saveService = new SaveService();

        context = new GameContext(assetService, audioService, eventBus, screenRouter, saveService);

        screenRouter.setContext(context);
        screenRouter.goTo(new BootScreen(context));
    }

    @Override
    public void dispose() {
        context.getAssetService().dispose();
        context.getAudioService().dispose();
    }
}

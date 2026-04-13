package com.patternforge.echoesarena.core;

public class GameContext {

    private final AssetService assetService;
    private final AudioService audioService;
    private final EventBus eventBus;
    private final ScreenRouter screenRouter;
    private final SaveService saveService;

    public GameContext(AssetService assetService, AudioService audioService,
                       EventBus eventBus, ScreenRouter screenRouter,
                       SaveService saveService) {
        this.assetService = assetService;
        this.audioService = audioService;
        this.eventBus = eventBus;
        this.screenRouter = screenRouter;
        this.saveService = saveService;
    }

    public AssetService getAssetService() {
        return assetService;
    }

    public AudioService getAudioService() {
        return audioService;
    }

    public EventBus getEventBus() {
        return eventBus;
    }

    public ScreenRouter getScreenRouter() {
        return screenRouter;
    }

    public SaveService getSaveService() {
        return saveService;
    }
}

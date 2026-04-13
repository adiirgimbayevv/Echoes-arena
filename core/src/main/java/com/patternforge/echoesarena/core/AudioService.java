package com.patternforge.echoesarena.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

public class AudioService implements Disposable {

    private final AssetService assetService;
    private Music currentMusic;
    private float musicVolume;
    private float sfxVolume;

    public AudioService(AssetService assetService) {
        this.assetService = assetService;
        this.musicVolume = 0.7f;
        this.sfxVolume = 1.0f;
    }

    public void playMusic(String path, boolean looping) {
        stopMusic();
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        currentMusic.setLooping(looping);
        currentMusic.setVolume(musicVolume);
        currentMusic.play();
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }
    }

    public void playSound(String path) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sound.play(sfxVolume);
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = volume;
        if (currentMusic != null) {
            currentMusic.setVolume(volume);
        }
    }

    public void setSfxVolume(float volume) {
        this.sfxVolume = volume;
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    @Override
    public void dispose() {
        stopMusic();
    }
}

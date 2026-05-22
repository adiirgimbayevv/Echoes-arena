package com.patternforge.echoesarena.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;

import java.util.HashMap;
import java.util.Map;

public class AudioService implements Disposable {

    public static final String MUSIC_MENU = "audio/music/menu_theme.wav";
    public static final String MUSIC_ARENA = "audio/music/arena_pulse.wav";

    public static final String SFX_BUTTON = "audio/sfx/ui_select.wav";
    public static final String SFX_PLAYER_ATTACK = "audio/sfx/player_attack.wav";
    public static final String SFX_ENEMY_HIT = "audio/sfx/enemy_hit.wav";
    public static final String SFX_ENEMY_KILL = "audio/sfx/enemy_kill.wav";
    public static final String SFX_SHIELD = "audio/sfx/shield_trigger.wav";
    public static final String SFX_FROST_NOVA = "audio/sfx/frost_nova.wav";
    public static final String SFX_FIREBALL = "audio/sfx/fireball_cast.wav";
    public static final String SFX_GLACIAL_RIFT = "audio/sfx/glacial_rift.wav";
    public static final String SFX_HEALTH_PICKUP = "audio/sfx/health_pickup.wav";

    private final AssetService assetService;
    private final Map<String, Sound> soundCache;
    private Music currentMusic;
    private String currentMusicPath;
    private float musicVolume;
    private float sfxVolume;

    public AudioService(AssetService assetService) {
        this.assetService = assetService;
        this.soundCache = new HashMap<>();
        this.musicVolume = 0.7f;
        this.sfxVolume = 1.0f;
    }

    public void playMusic(String path, boolean looping) {
        if (path.equals(currentMusicPath) && currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.setVolume(musicVolume);
            return;
        }

        stopMusic();
        currentMusic = Gdx.audio.newMusic(Gdx.files.internal(path));
        currentMusicPath = path;
        currentMusic.setLooping(looping);
        currentMusic.setVolume(musicVolume);
        currentMusic.play();
    }

    public void stopMusic() {
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
            currentMusicPath = null;
        }
    }

    public void playSound(String path) {
        Sound sound = soundCache.get(path);
        if (sound == null) {
            sound = Gdx.audio.newSound(Gdx.files.internal(path));
            soundCache.put(path, sound);
        }
        sound.play(sfxVolume);
    }

    public void setMusicVolume(float volume) {
        this.musicVolume = clampVolume(volume);
        if (currentMusic != null) {
            currentMusic.setVolume(musicVolume);
        }
    }

    public void setSfxVolume(float volume) {
        this.sfxVolume = clampVolume(volume);
    }

    public float getMusicVolume() {
        return musicVolume;
    }

    public float getSfxVolume() {
        return sfxVolume;
    }

    private float clampVolume(float volume) {
        return Math.max(0f, Math.min(1f, volume));
    }

    @Override
    public void dispose() {
        stopMusic();
        for (Sound sound : soundCache.values()) {
            sound.dispose();
        }
        soundCache.clear();
    }
}

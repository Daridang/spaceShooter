package ru.geekbrains.stargame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 02/11/2018.
 */

public class SoundManager {

    private AssetManager am;
    private Sound shoot;
    private Sound explosion;

    public SoundManager(AssetManager am) {
        this.am = am;
    }

    public void getLoadedSFX() {
        shoot = am.get("laser2.mp3");
        explosion = am.get("explosion09.wav");
    }

    public Sound getExplosion() {
        return explosion;
    }

    public Sound getShoot() {
        return shoot;
    }
}

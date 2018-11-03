package ru.geekbrains.stargame;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

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
    private Music menuMusic;
    private Music gameOver;
    private Music victory;
    private Music bossTheme;
    private Music battleInTheStars;
    private Music rainOfLasers;

    private Array<Sound> sounds;
    private Array<Music> music;

    public SoundManager(AssetManager am) {
        this.am = am;
        sounds = new Array<Sound>();
        music = new Array<Music>();
    }

    public void getLoadedSFX() {
        shoot = am.get("laser2.mp3");
        explosion = am.get("explosion09.wav");
        menuMusic = am.get("bgm/Brave_Pilots_(Menu_Screen).ogg");
        gameOver = am.get("bgm/Defeated_(Game_Over_Tune).ogg");
        victory = am.get("bgm/Victory_Tune.ogg");
        bossTheme = am.get("bgm/DeathMatch_(Boss_Theme).ogg");
        battleInTheStars = am.get("bgm/Battle_in_the_Stars.ogg");
        rainOfLasers = am.get("bgm/Rain_of_Lasers.ogg");
    }

    public Array<Sound> getAllSounds() {
        am.getAll(Sound.class, sounds);
        return sounds;
    }

    public Array<Music> getAllMusic() {
        am.getAll(Music.class, music);
        return music;
    }

    public Sound getExplosion() {
        return explosion;
    }

    public Sound getShoot() {
        return shoot;
    }

    public Music getMenuMusic() {
        return menuMusic;
    }

    public Music getGameOver() {
        return gameOver;
    }

    public Music getVictory() {
        return victory;
    }

    public Music getBossTheme() {
        return bossTheme;
    }

    public Music getBattleInTheStars() {
        return battleInTheStars;
    }

    public Music getRainOfLasers() {
        return rainOfLasers;
    }

    public void dispose() {
        shoot.dispose();
        explosion.dispose();
        menuMusic.dispose();
        gameOver.dispose();
        victory.dispose();
        bossTheme.dispose();
        battleInTheStars.dispose();
        rainOfLasers.dispose();
    }

}

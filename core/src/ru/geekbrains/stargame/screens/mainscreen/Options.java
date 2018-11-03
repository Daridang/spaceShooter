package ru.geekbrains.stargame.screens.mainscreen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;

import ru.geekbrains.stargame.Global;
import ru.geekbrains.stargame.StarGame;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 03/11/2018.
 */

public class Options {

    private Stage stage;
    private BitmapFont font;
    private StarGame game;

    public Options(StarGame game, Stage stage, BitmapFont font) {
        this.game = game;
        this.stage = stage;
        this.font = font;
        options();
    }

    private void options() {
        Table mainMenu = new Table();

        Label.LabelStyle lls = new Label.LabelStyle();
        lls.font = font;

        TextButton.TextButtonStyle ttbs = new TextButton.TextButtonStyle();
        ttbs.font = font;

        TextButton btnBack = new TextButton("Back", ttbs);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(
                    InputEvent e, float x, float y, int pointer, int button
            ) {
                stage.getActors().clear();
                new MainMenu(game, stage, font);
            }
        });

        Label title = new Label("Space\tShooter", lls);
        title.setAlignment(Align.top);
        title.setFontScale(1.4f);

        Label sound = new Label("Sound", lls);
        sound.setFontScale(0.75f);
        sound.setAlignment(Align.center);

        Label music = new Label("Music", lls);
        music.setFontScale(0.75f);
        music.setAlignment(Align.center);

        Slider.SliderStyle ss = createSliderStyle();

        final Slider soundSlider = new Slider(0f, 1f, 0.1f, false, ss);
        soundSlider.setValue(Global.SOUND_VOLUME);

        final Slider musicSlider = new Slider(0f, 1f, 0.1f, false, ss);
        musicSlider.setValue(Global.MUSIC_VOLUME);

        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Sound m : new Array.ArrayIterator<Sound>(
                        game.getSm().getAllSounds())) {

                    Global.SOUND_VOLUME = soundSlider.getValue();
                    m.setVolume(m.play(), Global.SOUND_VOLUME);
                }
            }
        });

        musicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                for (Music m : new Array.ArrayIterator<Music>(
                        game.getSm().getAllMusic())) {

                    Global.MUSIC_VOLUME = musicSlider.getValue();
                    m.setVolume(Global.MUSIC_VOLUME);
                }
            }
        });

        mainMenu.add(title).padBottom(StarGame.WORLD_HEIGHT / 6);
        mainMenu.row();
        mainMenu.add(sound);
        mainMenu.row();
        mainMenu.add(soundSlider).fillX().padLeft(100f).padRight(100f);
        mainMenu.row();
        mainMenu.add(music);
        mainMenu.row();
        mainMenu.add(musicSlider).fillX().padLeft(100f).padRight(100f);
        mainMenu.row();
        mainMenu.add(btnBack);
        mainMenu.setFillParent(true);
        stage.addActor(mainMenu);
    }

    private Slider.SliderStyle createSliderStyle() {
        Slider.SliderStyle ss = new Slider.SliderStyle();
        ss.background = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("sliderGUI.png")))
        );
        ss.knob = new TextureRegionDrawable(
                new TextureRegion(new Texture(Gdx.files.internal("sc.png")))
        );
        return ss;
    }

}

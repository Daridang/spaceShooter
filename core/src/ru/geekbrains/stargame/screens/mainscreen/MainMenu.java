package ru.geekbrains.stargame.screens.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import ru.geekbrains.stargame.StarGame;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 03/11/2018.
 */

public class MainMenu {
    private Stage stage;
    private BitmapFont font;
    private StarGame game;

    public MainMenu(StarGame game, Stage stage, BitmapFont font) {
        this.game = game;
        this.stage = stage;
        this.font = font;
        btnSetUp();
    }

    private void btnSetUp() {

        Table mainMenu = new Table();

        TextButton.TextButtonStyle ttbs = new TextButton.TextButtonStyle();
        ttbs.font = font;

        Label.LabelStyle lls = new Label.LabelStyle();
        lls.font = font;

        Label title = new Label("Space\tShooter", lls);
        title.setAlignment(Align.top);
        title.setFontScale(1.4f);

        TextButton btnPlay = new TextButton("Play", ttbs);
        TextButton btnOptions = new TextButton("Options", ttbs);
        TextButton btnExit = new TextButton("Exit", ttbs);

        addBtnListeners(btnPlay, btnOptions, btnExit);

        mainMenu.add(title).padBottom(StarGame.WORLD_HEIGHT / 6);
        mainMenu.row();
        mainMenu.add(btnPlay);
        mainMenu.row();
        mainMenu.add(btnOptions);
        mainMenu.row();
        mainMenu.add(btnExit);
        mainMenu.row();
        mainMenu.setFillParent(true);
        stage.addActor(mainMenu);
    }

    private void addBtnListeners(TextButton... t) {
        t[0].addListener(new ClickListener() {
            @Override
            public void touchUp(
                    InputEvent e, float x, float y, int pointer, int button
            ) {

                stage.addAction(Actions.sequence(
                        Actions.fadeOut(1f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                game.setScreen(game.getScreenType(
                                        StarGame.ScreenType.GAME_SCREEN));
                            }
                        })
                        )
                );
            }
        });

        t[1].addListener(new ClickListener() {
            @Override
            public void touchUp(
                    InputEvent e, float x, float y, int pointer, int button
            ) {
                stage.getActors().clear();
                new Options(game, stage, font);

            }
        });

        t[2].addListener(new ClickListener() {
            @Override
            public void touchUp(
                    InputEvent e, float x, float y, int pointer, int button
            ) {
                Gdx.app.exit();
            }
        });
    }
}

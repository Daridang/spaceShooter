package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Arrays;

import ru.geekbrains.stargame.animations.Star3D;
import ru.geekbrains.stargame.screens.Base2DScreen;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 19/10/2018.
 */

public class MainScreen extends Base2DScreen {

    private StarGame game;
    private Stage stage;
    private BitmapFont font;
    //private SpriteBatch batch;

    private ShapeRenderer renderer;
    private Array<Star3D> stars;
    private float speed = 20f;
    private int numberOfStars = 500;

    StarGameHud hud;
    public boolean drawHud = false;

    public MainScreen(StarGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        super.show();
        stage = new Stage(new FitViewport(
                StarGame.WORLD_WIDTH,
                StarGame.WORLD_HEIGHT,
                new OrthographicCamera())
        );

        font = game.getAssetManager().get("space_font.fnt");

        hud = new StarGameHud(font);

        renderer = new ShapeRenderer();
        stars = new Array<Star3D>();
        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star3D());
        }

        batch = new SpriteBatch();
        btnSetUp();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height);
    }

    @Override
    public void render(float delta) {

        stage.getViewport().getCamera().update();
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        for (Star3D star : stars) {
            star.update(speed);
            star.draw(renderer);
        }
        renderer.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        renderer.dispose();
        batch.dispose();
        font.dispose();
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

                stage.getRoot().addAction(Actions.sequence(
                        Actions.fadeIn(0.5f),
                        Actions.fadeOut(0.5f),
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
                // TODO OptionsScreen
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

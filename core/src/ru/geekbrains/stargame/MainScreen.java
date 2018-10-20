package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 19/10/2018.
 */

public class MainScreen extends ScreenAdapter {

    private StarGame game;
    private Stage stage;
    private BitmapFont font;
    private SpriteBatch batch;

    private ShapeRenderer renderer;
    private Array<ru.geekbrains.stargame.animations.Star3D> stars;
    private float speed = 20f;
    private int numberOfStars = 500;

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

        renderer = new ShapeRenderer();
        stars = new Array<>();
        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new ru.geekbrains.stargame.animations.Star3D());
        }

        Image background =
                new Image((Texture) game.getAssetManager().get("starField.jpg"));

        batch = new SpriteBatch();
        font = game.getAssetManager().get("space_font.fnt");
        //stage.addActor(background);
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
        for (ru.geekbrains.stargame.animations.Star3D star : stars) {
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
    }

    private void btnSetUp() {

        Table mainMenu = new Table();

        TextButton.TextButtonStyle ttbs = new TextButton.TextButtonStyle();
        ttbs.font = font;

        TextButton btnPlay = new TextButton("Play", ttbs);
        TextButton btnOptions = new TextButton("Options", ttbs);
        TextButton btnExit = new TextButton("Exit", ttbs);
        TextButton homeWork = new TextButton("HomeWork", ttbs);

        addBtnListeners(btnPlay, btnOptions, btnExit, homeWork);

        mainMenu.add(btnPlay);
        mainMenu.row();
        mainMenu.add(btnOptions);
        mainMenu.row();
        mainMenu.add(btnExit);
        mainMenu.row();
        mainMenu.add(homeWork);
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
                        Actions.fadeIn(0.3f),
                        Actions.fadeOut(0.3f),
                        Actions.run(() -> game.setScreen(game.getScreenType(
                                        StarGame.ScreenType.GAME_SCREEN)
                                )
                        )
                ));
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

        t[3].addListener(new ClickListener() {
            @Override
            public void touchUp(
                    InputEvent e, float x, float y, int pointer, int button
            ) {
                // TODO homeWork screen
            }
        });

    }
}

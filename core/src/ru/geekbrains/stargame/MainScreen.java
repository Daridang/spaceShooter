package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
    private GlyphLayout text;

    private TextButton btnPlay, btnOptions, btnExit;

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

        batch = new SpriteBatch();
        font = game.getAssetManager().get("space_font.fnt");
        text = new GlyphLayout();
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

        TextButton.TextButtonStyle ttbs = new TextButton.TextButtonStyle();
        ttbs.font = font;

        btnPlay = new TextButton("Play", ttbs);
        btnOptions = new TextButton("Options", ttbs);
        btnExit = new TextButton("Exit", ttbs);

        btnPlay.setPosition(
                StarGame.WORLD_WIDTH / 2 - btnPlay.getWidth() / 2,
                StarGame.WORLD_HEIGHT / 2 - btnPlay.getHeight() /2
        );

        btnOptions.setPosition(
                btnPlay.getWidth() / 2 - btnOptions.getWidth() / 2,
                //btnPlay.getX(),
                btnPlay.getY() - btnOptions.getHeight()
//                StarGame.WORLD_WIDTH / 2 - btnPlay.getWidth() / 2,
//                StarGame.WORLD_HEIGHT / 2 - btnPlay.getHeight() /2
        );

        btnPlay.addListener(new ClickListener() {
            @Override
            public void touchUp(
                    InputEvent e, float x, float y, int pointer, int button
            ) {
                game.setScreen(game.getScreenType(StarGame.ScreenType.GAME_SCREEN));
            }
        });

        stage.addActor(btnPlay);
        stage.addActor(btnOptions);
    }
}

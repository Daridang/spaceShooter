package ru.geekbrains.stargame.screens.mainscreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.geekbrains.stargame.Global;
import ru.geekbrains.stargame.StarGame;
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
    private ShapeRenderer renderer;
    private Array<Star3D> stars;
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

        font = game.getAssetManager().get("space_font.fnt");
        renderer = new ShapeRenderer();
        stars = new Array<Star3D>();
        for (int i = 0; i < numberOfStars; i++) {
            stars.add(new Star3D());
        }

        game.getSm().getLoadedSFX();
        //game.getSm().getMenuMusic().setVolume(Global.MUSIC_VOLUME);
        game.getSm().getMenuMusic().setLooping(true);
        game.getSm().getMenuMusic().play();

        new MainMenu(game, stage, font);

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
        game.getSm().getMenuMusic().dispose();
    }
}

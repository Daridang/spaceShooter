package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import static ru.geekbrains.stargame.StarGame.WORLD_HEIGHT;
import static ru.geekbrains.stargame.StarGame.WORLD_WIDTH;

public class LoadingScreen extends ScreenAdapter {

    // progress bar size for LoadingScreen
    public static final float PROGRESS_BAR_WIDTH = 100;
    public static final float PROGRESS_BAR_HEIGHT = 25;

    private ShapeRenderer renderer;
    private Viewport viewport;
    private OrthographicCamera camera;

    private float progress = 0;

    private final StarGame game;

    public LoadingScreen(StarGame game) {
        this.game = game;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        renderer = new ShapeRenderer();

        game.getAssetManager().load("texture_asset.atlas", TextureAtlas.class);
        game.getAssetManager().load("shields.atlas", TextureAtlas.class);

        BitmapFontLoader.BitmapFontParameter bfp =
                new BitmapFontLoader.BitmapFontParameter();
        bfp.atlasName = "texture_asset.atlas";

        game.getAssetManager().load("alien_boss.png", Texture.class);
        game.getAssetManager().load("laser_beam.png", Texture.class);
        game.getAssetManager().load("space_font.fnt", BitmapFont.class, bfp);
        game.getAssetManager().load("laser2.mp3", Sound.class);
        game.getAssetManager().load("explosion09.wav", Sound.class);

        game.getAssetManager().load("bgm/Alone_Against_Enemy.ogg", Music.class);
        game.getAssetManager().load("bgm/Battle_in_the_Stars.ogg", Music.class);
        game.getAssetManager().load(
                "bgm/Brave_Pilots_(Menu_Screen).ogg", Music.class
        );
        game.getAssetManager().load(
                "bgm/DeathMatch_(Boss_Theme).ogg", Music.class
        );
        game.getAssetManager().load(
                "bgm/Defeated_(Game_Over_Tune).ogg", Music.class
        );
        game.getAssetManager().load("bgm/Epic_End.ogg", Music.class);
        game.getAssetManager().load("bgm/Rain_of_Lasers.ogg", Music.class);
        game.getAssetManager().load(
                "bgm/SkyFire_(Title_Screen).ogg", Music.class
        );
        game.getAssetManager().load("bgm/Space_Heroes.ogg", Music.class);
        game.getAssetManager().load("bgm/Victory_Tune.ogg", Music.class);
        game.getAssetManager().load("bgm/Without_Fear.ogg", Music.class);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update();
        draw();
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }

    private void update() {
        if (game.getAssetManager().update()) {
            game.setScreen(game.getScreenType(StarGame.ScreenType.MAIN_SCREEN));
        } else {
            progress = game.getAssetManager().getProgress();
        }
    }

    private void draw() {
        renderer.setProjectionMatrix(camera.combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.rect(
                (WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2,
                WORLD_HEIGHT / 2 - PROGRESS_BAR_HEIGHT / 2,
                progress * PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT
        );
        renderer.end();
    }
}

package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 16/10/2018.
 */

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private StarGame starGame;
    private TextureAtlas atlas;

    private BitmapFont font;
    private GlyphLayout text;

    private Background background;

    private Player player;

    public GameScreen(StarGame starGame) {
        this.starGame = starGame;

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
    }

    public void show() {
        super.show();
        camera = new OrthographicCamera();
        viewport = new FitViewport(
                StarGame.WORLD_WIDTH,
                StarGame.WORLD_HEIGHT,
                camera
        );
        viewport.apply(true);

        camera.position.set(
                viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0
        );

        batch = new SpriteBatch();

        text = new GlyphLayout();

        //font = starGame.getAssetManager().get("space_font.fnt");              // Error
        font = new BitmapFont(Gdx.files.internal("space_font.fnt"));
        atlas = starGame.getAssetManager().get("assets.atlas");

        background = new Background(atlas);
        player = new Player(atlas);
    }

    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);

        stopLeavingTheScreen();

        batch.begin();
        // прорисовка текстур здесь
        background.update(delta, batch);
        player.update(batch, delta);

        text.setText(font, "Space\tShooter");
        font.draw(batch, text, StarGame.WORLD_WIDTH / 4, StarGame.WORLD_HEIGHT - 50);
        batch.end();
    }

    private void stopLeavingTheScreen() {
        if (player.getPosition().y < 0) {
            player.setPosition(new Vector2(player.getPosition().x, 0));
        }
        if (player.getPosition().x < 0) {
            player.setPosition(new Vector2(0, player.getPosition().y));
        }

        if (player.getPosition().x + player.getRegion()
                .getRegionWidth() > StarGame.WORLD_WIDTH) {
            player.setPosition(new Vector2(
                    StarGame.WORLD_WIDTH - player.getRegion().getRegionHeight(),
                    player.getPosition().y
                    )
            );
        }

    }

    @Override
    public void dispose() {
        batch.dispose();
        atlas.dispose();
    }
}

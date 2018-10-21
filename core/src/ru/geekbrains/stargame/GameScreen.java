package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.geekbrains.stargame.animations.Background;
import ru.geekbrains.stargame.animations.LightningAnimation;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 16/10/2018.
 */

public class GameScreen extends ScreenAdapter {
    private OrthographicCamera camera;
    private Stage stage;
    private SpriteBatch batch;
    private StarGame game;
    private TextureAtlas atlas;
    private TextureAtlas atlasLightning;

    private BitmapFont font;
    private GlyphLayout score;
    private GlyphLayout scoreNr;
    private GlyphLayout lives;
    private GlyphLayout livesNr;

    private Background background;

    private Player player;

    private DelayedRemovalArray<Enemy> enemies;
    private DelayedRemovalArray<LightningAnimation> lightning;

    private LightningAnimation lightningAnimation;

    public GameScreen(StarGame game) {
        this.game = game;

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    public void show() {
        super.show();
        camera = new OrthographicCamera();
        stage = new Stage(new FitViewport(
                StarGame.WORLD_WIDTH,
                StarGame.WORLD_HEIGHT,
                camera)
        );

        batch = new SpriteBatch();

        score = new GlyphLayout();
        scoreNr = new GlyphLayout();
        lives = new GlyphLayout();
        livesNr = new GlyphLayout();

        font = game.getAssetManager().get("space_font.fnt");
        atlas = game.getAssetManager().get("assets.atlas");
        atlasLightning = game.getAssetManager().get("lightning.atlas");

        setUpSound();

        background = new Background(atlas);

        player = new Player(atlas);
        enemies = new DelayedRemovalArray<Enemy>();
        enemies.add(new Enemy(atlas));

        lightning = new DelayedRemovalArray<LightningAnimation>();

        Gdx.input.setInputProcessor(stage);
    }

    private void setUpSound() {
        getMusic().setLooping(true);
        getMusic().setVolume(0.1f);
        getMusic().play();
    }

    private Music getMusic() {
        return game
                .getAssetManager()
                .get("through_space.ogg", Music.class);
    }

    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getViewport().getCamera().update();
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);

        stopLeavingTheScreen();

        batch.begin();
        // прорисовка текстур здесь
        background.render(delta, batch);
        player.render(batch, delta);
        updateMovement();

        enemies.begin();
        randomEnemySpawn();
        for (Enemy e : enemies) {
            e.render(batch, delta);
        }
        removeEnemy();
        enemies.end();

        lightning.begin();
        for (LightningAnimation a : lightning) {
            a.render(batch, delta);
            if (a.getLightningAnim().isAnimationFinished(a.getElapsedTime())) {
                lightning.removeValue(a, false);
            }
        }
        lightning.end();

        // TODO HUD display class
        score.setText(font, "Score");
        lives.setText(font, "Lives");
        scoreNr.setText(font, "0");
        livesNr.setText(font, "3");

        font.draw(batch, score, 10, StarGame.WORLD_HEIGHT - 10);
        font.draw(batch, scoreNr, 10, StarGame.WORLD_HEIGHT - score.height * 1.5f);
        font.draw(batch, lives, StarGame.WORLD_WIDTH - lives.width, StarGame.WORLD_HEIGHT - 10);
        font.draw(batch, livesNr, StarGame.WORLD_WIDTH - livesNr.width, StarGame.WORLD_HEIGHT - lives.height * 1.5f);
        batch.end();
    }

    private void updateMovement() {

        if (Gdx.input.isTouched()) {

            Vector2 v = stage.getViewport().unproject(new Vector2(
                    Gdx.input.getX(),
                    Gdx.input.getY())
            );

            lightningAnimation = new LightningAnimation(atlasLightning);
            lightningAnimation.setPosition(v);

            if (lightning.size == 0) {
                lightning.add(lightningAnimation);
            }

            player.setTargetPosition(v);
            player.setTargetSet(true);
        }
    }

    private float spawnDelta = MathUtils.random(1000f, 5000f);

    private void randomEnemySpawn() {
        long currentTime = TimeUtils.millis();

        if (currentTime - enemies.peek().getSpawnTime() > spawnDelta) {

            enemies.add(new Enemy(atlas));
        }
    }

    private void removeEnemy() {
        for (int i = 0; i < enemies.size; i++) {
            if (enemies.get(i).isOutOfScreen()) {
                enemies.removeIndex(i);
            }
        }
    }

    private void stopLeavingTheScreen() {
        if (player.getPosition().y - player.getRegion().getRegionHeight() / 2 < 0) {
            player.setPosition(new Vector2(
                    player.getPosition().x, player.getRegion().getRegionWidth() / 2));
        }

        if (player.getPosition().x - player.getRegion().getRegionWidth() / 2 < 0) {
            player.setPosition(new Vector2(
                    player.getRegion().getRegionHeight() / 2, player.getPosition().y));
        }

        if (player.getPosition().x + player.getRegion()
                .getRegionWidth() / 2 > StarGame.WORLD_WIDTH) {
            player.setPosition(new Vector2(
                            StarGame.WORLD_WIDTH - player.getRegion().getRegionHeight() / 2,
                            player.getPosition().y
                    )
            );
        }

        if (player.getPosition().y + player.getRegion()
                .getRegionHeight() / 2 > StarGame.WORLD_HEIGHT) {
            player.setPosition(new Vector2(
                            player.getPosition().x,
                            StarGame.WORLD_HEIGHT - player.getRegion().getRegionHeight() / 2
                    )
            );
        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        atlas.dispose();
        getMusic().dispose();
    }
}

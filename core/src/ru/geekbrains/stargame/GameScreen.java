package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.geekbrains.stargame.animations.Background;
import ru.geekbrains.stargame.animations.LightningAnimation;
import ru.geekbrains.stargame.screens.Base2DScreen;
import ru.geekbrains.stargame.gameobjects.*;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 16/10/2018.
 */

public class GameScreen extends Base2DScreen {
    private OrthographicCamera camera;
    private Stage stage;
    private SpriteBatch batch;
    private StarGame game;
    private TextureAtlas atlas;

    private BitmapFont font;
    private GlyphLayout score;
    private GlyphLayout scoreNr;
    private GlyphLayout lives;
    private GlyphLayout livesNr;

    private Background background;

    private Player player;

    private DelayedRemovalArray<Enemy> enemies;
    private DelayedRemovalArray<Asteroids> asteroids;
    private DelayedRemovalArray<LightningAnimation> lightning;

    private LightningAnimation lightningAnimation;

    private ShapeRenderer r;

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
        atlas = game.getAssetManager().get("texture_asset.atlas");

        //setUpSound();

        background = new Background(atlas);

        player = new Player(atlas);
        enemies = new DelayedRemovalArray<Enemy>();
        asteroids = new DelayedRemovalArray<Asteroids>();
        enemies.add(new Enemy(atlas));
        asteroids.add(new Asteroids(atlas));

        lightning = new DelayedRemovalArray<LightningAnimation>();

        r = new ShapeRenderer();
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

        enemies.begin();
        randomEnemySpawn();
        for (Enemy e : enemies) {
            e.render(batch, delta);
        }
        removeEnemy();
        enemies.end();

        asteroids.begin();
        randomAsteroidsSpawn();
        for (Asteroids a : asteroids) {
            a.render(batch, delta);
        }
        removeAsteroid();
        asteroids.end();

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


        r.begin(ShapeRenderer.ShapeType.Line);
        r.rect(
                player.getHitBox().x,
                player.getHitBox().y,
                player.getHitBox().width,
                player.getHitBox().height
        );

        System.out.println(player.getHitBox().width);
        System.out.println(player.getHitBox().x);

//        for (Enemy e : enemies) {
//            r.rect(
//                    e.getHitBox().x,
//                    e.getHitBox().y,
//                    e.getHitBox().width,
//                    e.getHitBox().height
//            );
//        }


        r.end();

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 v = stage.getViewport().unproject(new Vector2(
                screenX,
                screenY)
        );

        lightningAnimation = new LightningAnimation(atlas);
        lightningAnimation.setPosition(v);

        if (lightning.size == 0) {
            lightning.add(lightningAnimation);
        }

        player.setTargetPosition(v);
        player.setTargetSet(true);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private float spawnDelta = MathUtils.random(1000f, 5000f);

    private void randomEnemySpawn() {
        long currentTime = TimeUtils.millis();

        if (currentTime - enemies.peek().getSpawnTime() > spawnDelta) {

            enemies.add(new Enemy(atlas));
        }
    }

    private void randomAsteroidsSpawn() {
        long currentTime = TimeUtils.millis();

        if (currentTime - asteroids.peek().getSpawnTime() > spawnDelta) {

            asteroids.add(new Asteroids(atlas));
        }
    }

    private void removeEnemy() {
        for (int i = 0; i < enemies.size; i++) {
            if (enemies.get(i).isOutOfScreen()) {
                enemies.removeIndex(i);
            }
        }
    }

    private void removeAsteroid() {
        for (int i = 0; i < asteroids.size; i++) {
            if (asteroids.get(i).isOutOfScreen()) {
                asteroids.removeIndex(i);
            }
        }
    }

    private void stopLeavingTheScreen() {
//        if (player.getPosition().y - player.getRegion().getRegionHeight() / 2 < 0) {
//            player.setPosition(new Vector2(
//                    player.getPosition().x, player.getRegion().getRegionWidth() / 2));
//        }
        if (player.getPosition().y < 0) {
            player.setPosition(
                    new Vector2(player.getPosition().x, StarGame.WORLD_HEIGHT)
            );
        }

//        if (player.getPosition().x - player.getRegion().getRegionWidth() / 2 < 0) {
//            player.setPosition(new Vector2(
//                    player.getRegion().getRegionHeight() / 2, player.getPosition().y));
//        }

        if (player.getPosition().x < 0) {
            player.setPosition(
                    new Vector2(StarGame.WORLD_WIDTH, player.getPosition().y)
            );
        }

//        if (player.getPosition().x + player.getRegion()
//                .getRegionWidth() / 2 > StarGame.WORLD_WIDTH) {
//            player.setPosition(new Vector2(
//                            StarGame.WORLD_WIDTH - player.getRegion().getRegionHeight() / 2,
//                            player.getPosition().y
//                    )
//            );
//        }

        if (player.getPosition().x > StarGame.WORLD_WIDTH) {
            player.setPosition(
                    new Vector2(0, player.getPosition().y)
            );
        }

//        if (player.getPosition().y + player.getRegion()
//                .getRegionHeight() / 2 > StarGame.WORLD_HEIGHT) {
//            player.setPosition(new Vector2(
//                            player.getPosition().x,
//                            StarGame.WORLD_HEIGHT - player.getRegion().getRegionHeight() / 2
//                    )
//            );
//        }

        if (player.getPosition().y > StarGame.WORLD_HEIGHT) {
            player.setPosition(
                    new Vector2(player.getPosition().x, 0)
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

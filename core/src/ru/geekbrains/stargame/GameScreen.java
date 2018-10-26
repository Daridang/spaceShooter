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
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.geekbrains.stargame.animations.Background;
import ru.geekbrains.stargame.animations.ExplosionAnimation;
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
    //private SpriteBatch batch;
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
    private DelayedRemovalArray<ExplosionAnimation> explosions;

    private LightningAnimation lightningAnimation;

    private ShapeRenderer r;
    private StarGameHud hud;

    private int pScore = 0;
    private int pLives = 3;

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

        hud = new StarGameHud(font);
        //setUpSound();

        background = new Background(atlas);

        player = new Player(atlas);
        enemies = new DelayedRemovalArray<Enemy>();
        asteroids = new DelayedRemovalArray<Asteroids>();
        enemies.add(new Enemy(atlas));
        asteroids.add(new Asteroids(atlas));

        lightning = new DelayedRemovalArray<LightningAnimation>();
        explosions = new DelayedRemovalArray<ExplosionAnimation>();

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
        if (player.isAlive()) {
            player.render(batch, delta);
        }

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

        explosions.begin();
        for (ExplosionAnimation a : explosions) {
            a.render(batch, delta);
            if (a.getExplosionAnim().isAnimationFinished(a.getElapsedTime())) {
                explosions.removeValue(a, false);
            }
        }
        explosions.end();

        lightning.begin();
        for (LightningAnimation a : lightning) {
            a.render(batch, delta);
            if (a.getLightningAnim().isAnimationFinished(a.getElapsedTime())) {
                lightning.removeValue(a, false);
            }
        }
        lightning.end();

        BulletEmitter.getInstance().render(batch);
        hud.render(batch, pLives, pScore);

        batch.end();


        // Debug
        r.setProjectionMatrix(camera.combined);
        r.begin(ShapeRenderer.ShapeType.Line);
        r.rect(
                player.getHitBox().x,
                player.getHitBox().y,
                player.getHitBox().width,
                player.getHitBox().height
        );
        r.circle(player.getHitCircle().x, player.getHitCircle().y, player.getHitCircle().radius);

        for (Enemy e : enemies) {
            r.rect(
                    e.getHitBox().x,
                    e.getHitBox().y,
                    e.getHitBox().width,
                    e.getHitBox().height
            );
        }


        for (Asteroids a : asteroids) {
            r.circle(
                    a.getHitBox().x,
                    a.getHitBox().y,
                    a.getHitBox().radius
            );
        }

        r.end();
        BulletEmitter.getInstance().update(delta);
        collisionDetection();
    }

    private void collisionDetection() {
        for (Enemy e : enemies) {
            if (e.getHitBox().overlaps(player.getHitBox()) && player.isAlive()) {
                if (explosions.size == 0) {
                    explosions.add(new ExplosionAnimation(atlas));
                    explosions.get(0).setPosition(player.getPosition());
                    player.setAlive(false);
                    pLives--;
                    if (pLives < 1) {
                        // TODO game over screen
                        pLives = 3;
                    }
                }
            }
            for (Bullet b : BulletEmitter.getInstance().bullets) {
                if (b.active) {
                    if (e.getHitBox().overlaps(b.getHitBox())) {
                        e.setOutOfScreen(true);
                        b.destroy();
                        if (explosions.size == 0) {
                            explosions.add(new ExplosionAnimation(atlas));
                            explosions.get(0).setPosition(
                                    new Vector2(
                                            e.getPosition().x + e.getHitBox().width / 2,
                                            e.getPosition().y + e.getHitBox().height / 2
                                    )
                            );
                        }
                        pScore++;
                    }
                }
            }

        }

        for (Asteroids a : asteroids) {
            if (a.getHitBox().overlaps(player.getHitCircle()) && player.isAlive()) {
                if (explosions.size == 0) {
                    explosions.add(new ExplosionAnimation(atlas));
                    explosions.get(0).setPosition(player.getPosition());
                    player.setAlive(false);
                    pLives--;
                    if (pLives < 1) {
                        //game over screen
                        pLives = 3;
                    }
                }
            }
        }
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

package ru.geekbrains.stargame;

import com.badlogic.gdx.Application;
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
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.geekbrains.stargame.animations.Background;
import ru.geekbrains.stargame.animations.ExplosionAnimation;
import ru.geekbrains.stargame.pools.AsteroidsPool;
import ru.geekbrains.stargame.pools.EnemiesPool;
import ru.geekbrains.stargame.pools.ExplosionPool;
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

    // количество врагов и астероидов присутствующих на экране одновременно.
    private static final int ASTEROIDS = 6;
    private static final int ENEMIES = 6;

    private Stage stage;
    private StarGame game;
    private TextureAtlas atlas;
    private BitmapFont font;
    private Background background;
    private Player player;
    private DelayedRemovalArray<Enemy> activeEnemies;
    private DelayedRemovalArray<Asteroids> activeAsteroids;
    private DelayedRemovalArray<LightningAnimation> lightning;
    private DelayedRemovalArray<ExplosionAnimation> activeExplosions;
    private ExplosionPool explosionPool;
    private AsteroidsPool asteroidsPool;
    private EnemiesPool enemiesPool;
    private StarGameHud hud;
    private GameOverOverlay gameOver;
    private ShapeRenderer r;

    private int pScore = 0;
    //private int pLives = 3;

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
        stage = new Stage(new FitViewport(
                StarGame.WORLD_WIDTH,
                StarGame.WORLD_HEIGHT,
                new OrthographicCamera())
        );

        batch = new SpriteBatch();

        font = game.getAssetManager().get("space_font.fnt");
        atlas = game.getAssetManager().get("texture_asset.atlas");

        // Основная информация на игровом экране: очки, жизни.
        hud = new StarGameHud(font);
        gameOver = new GameOverOverlay(font);
        setUpSound();

        background = new Background(atlas);

        player = new Player(game);

        enemiesPool = new EnemiesPool(game);
        activeEnemies = new DelayedRemovalArray<Enemy>();

        asteroidsPool = new AsteroidsPool(atlas);
        activeAsteroids = new DelayedRemovalArray<Asteroids>();

        // анимация молнии по клику, пока просто так.
        lightning = new DelayedRemovalArray<LightningAnimation>();

        activeExplosions = new DelayedRemovalArray<ExplosionAnimation>();
        explosionPool = new ExplosionPool(atlas);

        r = new ShapeRenderer();
    }

    private void setUpSound() {
        getMusic().setLooping(true);
        getMusic().setVolume(0.2f);
        getMusic().play();
    }

    private Music getMusic() {
        return game
                .getAssetManager()
                .get("bgm/Battle_in_the_Stars.ogg", Music.class);
    }

    private Music gameOver() {
        return game
                .getAssetManager()
                .get("bgm/Defeated_(Game_Over_Tune).ogg", Music.class);
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
        // Рисуем игрока, если жив.
        background.render(delta, batch);
        if (player.isAlive()) {
            player.render(batch, delta);
        }

        // Враги
        activeEnemies.begin();
        randomEnemySpawn();

        // Если в списке находятся активные противники, рендерим.
        for (Enemy e : activeEnemies) {
            e.render(batch, delta);
        }
        removeEnemy();
        activeEnemies.end();

        // Астероиды
        activeAsteroids.begin();
        randomAsteroidsSpawn();
        for (Asteroids a : activeAsteroids) {
            a.render(batch, delta);
        }
        removeAsteroid();
        activeAsteroids.end();

        // Анимация взрывов.
        activeExplosions.begin();
        for (ExplosionAnimation a : activeExplosions) {
            a.render(batch, delta);
        }
        activeExplosions.end();

        activeExplosions.begin();
        for (ExplosionAnimation a : activeExplosions) {
            if (a.getExplosionAnim().isAnimationFinished(a.getElapsedTime())) {
                explosionPool.free(a);
                activeExplosions.removeValue(a, true);
            }
        }
        activeExplosions.end();

        lightning.begin();
        for (LightningAnimation a : lightning) {
            a.render(batch, delta);
            if (a.getLightningAnim().isAnimationFinished(a.getElapsedTime())) {
                lightning.removeValue(a, false);
            }
        }
        lightning.end();

        BulletEmitter.getInstance().render(batch);
        if (hud.isShown()) {
            hud.render(batch, player.getLives(), pScore);
        }

        if (gameOver.isShown()) {
            gameOver.render(batch);
//            getMusic().dispose();
//            gameOver().setVolume(0.1f);
//            gameOver().play();
        }
        batch.end();

        BulletEmitter.getInstance().update(delta);
        collisionDetection();
    }

    private void collisionDetection() {
        for (Enemy e : activeEnemies) {
            if (e.getHitBox().overlaps(player.getHitBox()) && player.isAlive()) {
                ExplosionAnimation ea = explosionPool.obtain();

                // При столкновении с кораблем противника взрыв корабля игрока
                ea.setActive(true);
                ea.setPosition(player.getPosition());
                activeExplosions.add(ea);
                game.getSm().getExplosion().play(1f);

                // и корабля противника
                e.setIsActive(false);
                ea = explosionPool.obtain();
                ea.setPosition(e.getPosition());
                activeExplosions.add(ea);

                player.setAlive(false);
                player.setLives(-1);
                player.setAlive(true);
                player.setPosition(player.getRespawnPosition().cpy());

                if (player.getLives() < 1) {
                    // TODO game over screen
                    hud.setShown(false);
                    gameOver.setShown(true);
                    player.setAlive(false);
                }
            }
            for (Bullet b : BulletEmitter.getInstance().bullets) {
                if (b.active) {
                    if (e.getHitBox().overlaps(b.getHitBox())) {
                        e.setIsActive(false);
                        b.destroy();
                        game.getSm().getExplosion().play(1f);
                        ExplosionAnimation ea = explosionPool.obtain();
                        ea.setActive(true);
                        ea.setPosition(
                                new Vector2(
                                        e.getPosition().x + e.getHitBox().width / 2,
                                        e.getPosition().y + e.getHitBox().height / 2
                                )
                        );
                        activeExplosions.add(ea);
                        pScore++;
                    }
                }
            }

        }

        for (Asteroids a : activeAsteroids) {
            if (a.getHitBox().overlaps(player.getHitCircle()) && player.isAlive()) {
                ExplosionAnimation ea = explosionPool.obtain();
                ea.setActive(true);
                ea.setPosition(player.getPosition());
                activeExplosions.add(ea);
                game.getSm().getExplosion().play(1f);
                player.setAlive(false);
                player.setLives(-1);
                player.setAlive(true);
                player.setPosition(player.getRespawnPosition().cpy());
                if (player.getLives() < 1) {
                    // TODO game over screen
                    hud.setShown(false);
                    gameOver.setShown(true);
                    player.setAlive(false);
                }
            }
            for (Bullet b : BulletEmitter.getInstance().bullets) {
                if (b.active) {
                    if (a.getHitBox().contains(b.getPosition())) {
                        a.setIsActive(false);
                        b.destroy();
                        game.getSm().getExplosion().play(1f);
                        ExplosionAnimation ea = explosionPool.obtain();
                        ea.setActive(true);
                        ea.setPosition(
                                new Vector2(
                                        a.getPosition().x + a.getHitBox().radius / 2,
                                        a.getPosition().y + a.getHitBox().radius / 2
                                )
                        );
                        activeExplosions.add(ea);
                        pScore++;
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

        LightningAnimation lightningAnimation = new LightningAnimation(atlas);
        lightningAnimation.setPosition(v);

        if (lightning.size == 0) {
            lightning.add(lightningAnimation);
        }

        player.setTargetPosition(v);
        player.setTargetSet(true);

        if (!player.isAlive()) {
            player.respawn();
            gameOver.setShown(false);
            hud.setShown(true);
        }
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void randomEnemySpawn() {
        // Если активных врагов меньше чем заданное количество,
        // достаем из пула и добавляем в список.
        Enemy e;
        if (activeEnemies.size < ENEMIES) {
            e = enemiesPool.obtain();
            e.setIsActive(true);
            activeEnemies.add(e);
        }
    }

    private void randomAsteroidsSpawn() {
        Asteroids a;
        if (activeAsteroids.size < ASTEROIDS) {
            a = asteroidsPool.obtain();
            a.setIsActive(true);
            activeAsteroids.add(a);

        }
    }

    private void removeEnemy() {
        for (Enemy e : activeEnemies) {
            if (!e.isActive()) {
                enemiesPool.free(e);
                activeEnemies.removeValue(e, true);
            }
        }
    }

    private void removeAsteroid() {
        for (Asteroids a : activeAsteroids) {
            if (!a.isActive()) {
                asteroidsPool.free(a);
                activeAsteroids.removeValue(a, true);
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
        font.dispose();
        stage.dispose();
    }
}

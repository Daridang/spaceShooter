package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import ru.geekbrains.stargame.animations.Background;
import ru.geekbrains.stargame.animations.ExplosionAnimation;
import ru.geekbrains.stargame.pools.AsteroidsPool;
import ru.geekbrains.stargame.pools.BonusPool;
import ru.geekbrains.stargame.pools.EnemiesPool;
import ru.geekbrains.stargame.pools.ExplosionPool;
import ru.geekbrains.stargame.animations.LightningAnimation;
import ru.geekbrains.stargame.screens.Base2DScreen;
import ru.geekbrains.stargame.gameobjects.*;
import ru.geekbrains.stargame.screens.MsgOverlay;

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
    private TextureAtlas bonusAtlas;
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
    private DelayedRemovalArray<Bonus> bonuses;
    private BonusPool bonusPool;

    private int pScore = 0;

    private float gamePlayTime;
    private long gameStartTime;
    private boolean isBossHere = false;
    private TheBoss boss;

    private Bonus bonus;

    private MsgOverlay beginningMessage;
    private long beginningMsgStartTime;
    private float messageDuration = 5000f;
    private long bossKilledTime;
    private long playerDeathTime;

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
        bonusAtlas = game.getAssetManager().get("shields.atlas");

        // Основная информация на игровом экране: очки, жизни.
        hud = new StarGameHud(font);
        gameOver = new GameOverOverlay(font);

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

        game.getSm().getBattleInTheStars().setLooping(true);
        game.getSm().getBattleInTheStars().play();
        r = new ShapeRenderer();

        gameStartTime = TimeUtils.millis();
        boss = new TheBoss(game);

        bonuses = new DelayedRemovalArray<Bonus>();
        bonusPool = new BonusPool(bonusAtlas);

        beginningMessage = new MsgOverlay();
        beginningMsgStartTime = TimeUtils.millis();

        //Gdx.input.setCatchBackKey(true);

        // TESTING IN PROGRESS
        InputMultiplexer im = new InputMultiplexer();
        im.addProcessor(stage);
        im.addProcessor(this);
        Gdx.input.setInputProcessor(im);
    }

    public void render(float delta) {
        super.render(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        if (Gdx.input.isKeyPressed(Input.Keys.BACK)) {
//            game.setScreen(game.getScreenType(StarGame.ScreenType.MAIN_SCREEN));
//        }

        gamePlayTime = TimeUtils.millis() - gameStartTime;

        stage.getViewport().getCamera().update();
        batch.setProjectionMatrix(stage.getViewport().getCamera().combined);

        stopLeavingTheScreen();

        batch.begin();
        // прорисовка текстур здесь
        background.render(delta, batch);

        // Рисуем игрока, если жив.
        if (player.isAlive()) {
            player.render(batch, delta);
        }

        // Астероиды
        activeAsteroids.begin();
        randomAsteroidsSpawn();
        for (Asteroids a : activeAsteroids) {
            a.render(batch, delta);
        }
        removeAsteroid();
        activeAsteroids.end();

        enterTheBoss(delta);

        // Враги
        activeEnemies.begin();
        if (!isBossHere) {
            randomEnemySpawn();
        }

        // Если в списке находятся активные противники, рендерим.
        for (Enemy e : activeEnemies) {
            e.render(batch, delta);
        }
        removeEnemy();
        activeEnemies.end();

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

        for (Bonus b : bonuses) {
            b.draw(batch, delta);
        }

        BulletEmitter.getInstance().render(batch);
        if (hud.isShown()) {
            hud.render(batch, player.getLives(), pScore, player.getHitPoints());
        }

        if (gameOver.isShown()) {
            gameOver.render(batch);
        }

        msgDraw();

        batch.end();

        BulletEmitter.getInstance().update(delta);
        collisionDetection();

        stage.act(delta);
        stage.draw();
    }

    private void msgDraw() {

        if (TimeUtils.timeSinceMillis(beginningMsgStartTime) < messageDuration) {
            beginningMessage.render(batch, Global.START_MSG);
        }

        if (boss.isKilled()
                && TimeUtils.timeSinceMillis(bossKilledTime) < messageDuration) {
            beginningMessage.render(batch, Global.CLEAR_MSG);
        }

        if (gameOver.isShown()
                && TimeUtils.timeSinceMillis(playerDeathTime) < messageDuration) {
            beginningMessage.render(batch, Global.LOST_MSG);
        }
    }

    private void enterTheBoss(float delta) {
        if (gamePlayTime > 20000 && !boss.isKilled()) {
            activeEnemies.clear();
            isBossHere = true;
            boss.setIsActive(true);
            game.getSm().getBattleInTheStars().stop();
            game.getSm().getBossTheme().play();
            boss.render(batch, delta);
        }
    }

    private void playerGameOver() {
        if (player.getLives() < 1) {
            if (game.getSm().getBossTheme().isPlaying()) {
                game.getSm().getBossTheme().stop();
            } else if (game.getSm().getBattleInTheStars().isPlaying()) {
                game.getSm().getBattleInTheStars().stop();
            }
            game.getSm().getGameOver().play();
            hud.setShown(false);
            gameOver.setShown(true);
            player.setAlive(false);
            if (playerDeathTime == 0) {
                playerDeathTime = TimeUtils.millis();
            }
        }
    }

    private void explode(Vector2 position) {
        ExplosionAnimation ea = explosionPool.obtain();
        ea.setActive(true);
        ea.setPosition(position);
        activeExplosions.add(ea);
        game.getSm().getExplosion().play(Global.SOUND_VOLUME);
    }

    private void forBoss() {
        if (boss.getHitBox().overlaps(player.getHitBox())
                && boss.isActive() && !boss.isKilled()) {
            explode(player.getPosition());
            player.setAlive(false);
            player.setLives(-1);
            player.setAlive(true);
            player.setPosition(player.getRespawnPosition().cpy());
            player.setHitPoints(10);
        }

        for (Bullet b : boss.getActiveBullets()) {
            if (b.active) {
                if (b.getHitBox().overlaps(player.getHitBox()) && player.isAlive()) {
                    player.setGotHit(true);
                    b.destroy();
                    boss.getBulletPool().free(b);

                    if (player.getSh() != null && player.getSh().isActive()) {
                        player.getSh().setPower(-1);
                    } else {

                        player.setHitPoints(-1);
                        if (player.getHitPoints() < 1) {
                            explode(player.getPosition());
                            player.setAlive(false);
                            player.setLives(-1);
                            player.setAlive(true);
                            player.setPosition(player.getRespawnPosition().cpy());
                            player.setHitPoints(10);
                        }
                    }
                }
                playerGameOver();
            }
        }

        for (Bullet b : BulletEmitter.getInstance().bullets) {
            if (b.active) {
                if (boss.getHitBox().overlaps(b.getHitBox())
                        && boss.getHitPoints() > 0) {
                    boss.setHitPoints(-1);
                    if (boss.getHitPoints() < 1) {
                        boss.setIsActive(false);
                        boss.setIsKilled(true);
                        if (game.getSm().getBossTheme().isPlaying()) {
                            game.getSm().getBossTheme().stop();
                        }

                        bossKilledTime = TimeUtils.millis();
                        game.getSm().getVictory().play();
                        // playerWins();
                    }
                    b.destroy();

                    explode(new Vector2(
                            boss.getPosition().x + boss.getHitBox().width / 2,
                            boss.getPosition().y + boss.getHitBox().height / 2
                    ));
                    pScore++;
                }
            }
        }
    }

    private void forEnemy() {
        for (Enemy e : activeEnemies) {
            if (e.getHitBox().overlaps(player.getHitBox()) && player.isAlive()) {
                player.setGotHit(true);
                e.setIsActive(false);
                explode(e.getPosition());

                if (player.getSh() != null && player.getSh().isActive()) {
                    player.getSh().setPower(-1);
                } else {

                    player.setHitPoints(-1);

                    if (player.getHitPoints() < 1) {
                        explode(player.getPosition());
                        player.setAlive(false);
                        player.setLives(-1);
                        player.setAlive(true);
                        player.setPosition(player.getRespawnPosition().cpy());
                        player.setHitPoints(10);
                    }
                }

                playerGameOver();
            }

            // При попадании пули противника в корабль игрока
            for (Bullet b : e.getActiveBullets()) {
                if (b.active) {
                    if (b.getHitBox().overlaps(player.getHitBox()) &&
                            player.isAlive()) {
                        player.setGotHit(true);
                        b.destroy();
                        e.getBulletPool().free(b);
                        if (player.getSh() != null && player.getSh().isActive()) {
                            player.getSh().setPower(-1);
                        } else {
                            player.setHitPoints(-1);

                            if (player.getHitPoints() < 1) {
                                explode(player.getPosition());
                                player.setAlive(false);
                                player.setLives(-1);
                                player.setAlive(true);
                                player.setPosition(player.getRespawnPosition().cpy());
                                player.setHitPoints(10);
                            }
                        }
                    }
                }
                playerGameOver();
            }

            for (Bullet b : BulletEmitter.getInstance().bullets) {
                if (b.active) {
                    if (e.getHitBox().overlaps(b.getHitBox())) {
                        e.setIsActive(false);
                        b.destroy();

                        explode(new Vector2(
                                e.getPosition().x + e.getHitBox().width / 2,
                                e.getPosition().y + e.getHitBox().height / 2
                        ));
                        pScore++;
                    }
                }
            }

        }
    }

    private void forAsteroids() {
        for (Asteroids a : activeAsteroids) {
            if (a.getHitBox().overlaps(player.getHitCircle()) && player.isAlive()) {
                player.setGotHit(true);
                explode(a.getPosition());
                a.setIsActive(false);

                if (player.getSh() != null && player.getSh().isActive()) {
                    player.getSh().setPower(-1);
                } else {

                    player.setHitPoints(-1);
                    if (player.getHitPoints() < 1) {
                        explode(player.getPosition());
                        player.setAlive(false);
                        player.setLives(-1);
                        player.setAlive(true);
                        player.setPosition(player.getRespawnPosition().cpy());
                        player.setHitPoints(10);
                    }
                 }

                playerGameOver();
            }
            for (Bullet b : BulletEmitter.getInstance().bullets) {
                if (b.active) {
                    if (a.getHitBox().contains(b.getPosition())) {
                        a.setIsActive(false);
                        b.destroy();

                        explode(new Vector2(
                                a.getPosition().x + a.getHitBox().radius / 2,
                                a.getPosition().y + a.getHitBox().radius / 2
                        ));
                        pScore++;
                        bonus = bonusPool.obtain();
                        bonus.setActive(true);
                        bonus.setPos(a.getPosition());
                        bonus.setSpeed(a.getSpeed());
                        bonuses.add(bonus);
                    }
                }
            }
        }
    }

    private void forBonuses() {
        for (Bonus b : bonuses) {
            if (b.getHitBox().overlaps(player.getHitBox())) {
                b.setActive(false);
                bonuses.removeValue(b, true);
                bonusPool.free(b);
                player.setShield();
            }
        }
    }

    private void collisionDetection() {
        forEnemy();
        forAsteroids();
        forBoss();
        forBonuses();
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
            activeEnemies.clear();
            activeAsteroids.clear();
            player.respawn();
            gameOver.setShown(false);
            hud.setShown(true);
            pScore = 0;
            game.getSm().getGameOver().stop();
            game.getSm().getBattleInTheStars().play();
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
        if (player.getPosition().y - player.getRegion().getRegionHeight() / 2 < 0) {
            player.setPosition(new Vector2(
                    player.getPosition().x, player.getRegion().getRegionWidth() / 2));
        }
//        if (player.getPosition().y < 0) {
//            player.setPosition(
//                    new Vector2(player.getPosition().x, StarGame.WORLD_HEIGHT)
//            );
//        }

        if (player.getPosition().x - player.getRegion().getRegionWidth() / 2 < 0) {
            player.setPosition(new Vector2(
                    player.getRegion().getRegionHeight() / 2, player.getPosition().y));
        }

//        if (player.getPosition().x < 0) {
//            player.setPosition(
//                    new Vector2(StarGame.WORLD_WIDTH, player.getPosition().y)
//            );
//        }

        if (player.getPosition().x + player.getRegion()
                .getRegionWidth() / 2 > StarGame.WORLD_WIDTH) {
            player.setPosition(new Vector2(
                            StarGame.WORLD_WIDTH - player.getRegion().getRegionHeight() / 2,
                            player.getPosition().y
                    )
            );
        }

//        if (player.getPosition().x > StarGame.WORLD_WIDTH) {
//            player.setPosition(
//                    new Vector2(0, player.getPosition().y)
//            );
//        }

        if (player.getPosition().y + player.getRegion()
                .getRegionHeight() / 2 > StarGame.WORLD_HEIGHT) {
            player.setPosition(new Vector2(
                            player.getPosition().x,
                            StarGame.WORLD_HEIGHT - player.getRegion().getRegionHeight() / 2
                    )
            );
        }

//        if (player.getPosition().y > StarGame.WORLD_HEIGHT) {
//            player.setPosition(
//                    new Vector2(player.getPosition().x, 0)
//            );
//        }
    }

    @Override
    public void dispose() {
        batch.dispose();
        atlas.dispose();
        font.dispose();
        stage.dispose();
    }
}

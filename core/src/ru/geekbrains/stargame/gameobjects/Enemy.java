package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.DelayedRemovalArray;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

import ru.geekbrains.stargame.Global;
import ru.geekbrains.stargame.StarGame;
import ru.geekbrains.stargame.pools.BulletPool;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 19/10/2018.
 */

public class Enemy implements Pool.Poolable {

    protected Rectangle hitBox;
    protected Vector2 position;
    private TextureRegion region;
    private TextureAtlas atlas;
    private Random random;
    protected float speed;
    protected boolean isActive = false;
    private StarGame game;

    protected BulletPool bulletPool;
    protected DelayedRemovalArray<Bullet> activeBullets;

    protected float deltaTime;
    protected long fireTime;

    public Enemy(StarGame game) {
        this.game = game;
        atlas = game.getAssetManager().get("texture_asset.atlas");
        region = atlas.findRegion("stateczek");
        init();
        bulletPool = new BulletPool(game.getAssetManager());
        activeBullets = new DelayedRemovalArray<Bullet>();
        deltaTime = MathUtils.random(2000f, 5000f);
        fireTime = TimeUtils.millis();
    }

    public void fire() {
        Bullet b = bulletPool.obtain();

        b.fireBullet(
                position.x + getHitBox().width / 2,
                position.y + getHitBox().height / 2,
                -400 * MathUtils.cosDeg(90),
                -400 * MathUtils.sinDeg(90)
        );

        activeBullets.add(b);

        game.getSm().getShoot().play(Global.SOUND_VOLUME);
        fireTime = TimeUtils.millis();
    }

    private void init() {
        // ??? every 2nd is not flipped ???
        region.flip(false, true);
        if (!region.isFlipY()) {
            region.flip(false, true);
        }

        random = new Random();
        position = new Vector2(
                random.nextInt((int) StarGame.WORLD_WIDTH - region.getRegionWidth()),
                StarGame.WORLD_HEIGHT * 2);
        speed = MathUtils.random(100f, 300f);
        hitBox = new Rectangle(
                position.x,
                position.y,
                region.getRegionWidth(),
                region.getRegionHeight()
        );
    }

    private void update() {
        hitBox.setPosition(position);

        if (TimeUtils.millis() - fireTime > deltaTime) {
            fire();
        }
    }

    public void render(SpriteBatch batch, float delta) {
        position.y -= speed * delta;
        if (position.y + region.getRegionHeight() * 2 < 0) {
            isActive = false;
        }

        for (Bullet b : activeBullets) {
            if (!b.active) {
                bulletPool.free(b);
                activeBullets.removeValue(b, true);
            }
            b.updateEnemyBullets(delta);
            batch.setColor(0.9f, 0.0f, 0.0f, 0.8f);
            //b.render(batch);
            batch.draw(
                    b.getBullet().getTexture(),
                    b.getPosition().x,
                    b.getPosition().y
            );
        }
        batch.setColor(Color.WHITE);
        batch.draw(region, position.x, position.y);
        update();
    }


    public BulletPool getBulletPool() {
        return bulletPool;
    }

    public DelayedRemovalArray<Bullet> getActiveBullets() {
        return activeBullets;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    @Override
    public void reset() {
        init();
        setIsActive(false);
    }
}

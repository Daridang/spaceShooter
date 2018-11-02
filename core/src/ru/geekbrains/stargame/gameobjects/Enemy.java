package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

import ru.geekbrains.stargame.StarGame;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 19/10/2018.
 */

public class Enemy implements Pool.Poolable{

    private Rectangle hitBox;
    private Vector2 position;
    private TextureRegion region;
    private TextureAtlas atlas;
    private Random random;
    private float speed;
    private boolean isActive = false;
    private StarGame game;

    public Enemy(StarGame game) {
        this.game = game;
        atlas = game.getAssetManager().get("texture_asset.atlas");
        region = atlas.findRegion("stateczek");
        init();
    }

    public void fire() {
        Bullet[] bl = BulletEmitter.getInstance().bullets;
        for (Bullet b : bl) {
            if (!b.active) {
                b.fireBullet(
                        position.x,
                        position.y,
                        400 * MathUtils.cosDeg(90),
                        400 * MathUtils.sinDeg(90)
                );
                game.getSm().getShoot().play(1f);
                break;
            }
        }
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
    }

    public void render(SpriteBatch batch, float delta) {
        position.y -= speed * delta;
        if (position.y + region.getRegionHeight() * 2 < 0) {
            isActive = false;
        }
        batch.draw(region, position.x, position.y);
        update();
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

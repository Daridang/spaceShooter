package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 10/11/2018.
 */

public class Bonus implements Pool.Poolable {


    private Vector2 pos;
    private Array<TextureAtlas.AtlasRegion> bonusRegion;
    private Animation<TextureAtlas.AtlasRegion> bonusAnim;
    private boolean active;
    private float elapsedTime;
    private float speed;
    private Rectangle hitBox;

    public Bonus(TextureAtlas atlas) {
        active = false;
        pos = new Vector2();
        bonusRegion = atlas.findRegions("bonus");
        bonusAnim = new Animation<>(
                0.03f, bonusRegion, Animation.PlayMode.LOOP
        );
        hitBox = new Rectangle();
    }

    public void draw(SpriteBatch batch, float delta) {
        pos.y -= speed * delta;
        elapsedTime += delta;
        TextureRegion textureRegion = bonusAnim.getKeyFrame(elapsedTime);
        hitBox.set(
                pos.x, pos.y,
                textureRegion.getRegionWidth(), textureRegion.getRegionHeight()
        );
        batch.draw(bonusAnim.getKeyFrame(elapsedTime), pos.x, pos.y);
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Array<TextureAtlas.AtlasRegion> getBonusRegion() {
        return bonusRegion;
    }

    public void setBonusRegion(Array<TextureAtlas.AtlasRegion> bonusRegion) {
        this.bonusRegion = bonusRegion;
    }

    public Animation<TextureAtlas.AtlasRegion> getBonusAnim() {
        return bonusAnim;
    }

    public void setBonusAnim(Animation<TextureAtlas.AtlasRegion> bonusAnim) {
        this.bonusAnim = bonusAnim;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(float elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void reset() {
        active = false;
    }
}

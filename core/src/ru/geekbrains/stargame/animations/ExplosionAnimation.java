package ru.geekbrains.stargame.animations;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 24/10/2018.
 */

public class ExplosionAnimation implements Pool.Poolable {
    private Vector2 position;

    private Array<TextureAtlas.AtlasRegion> explosion;

    private Animation<TextureAtlas.AtlasRegion> explAnimation;

    private TextureRegion region;
    private boolean active;
    private float elapsedTime = 0f;

    public ExplosionAnimation(TextureAtlas atlas) {
        position = new Vector2();
        explosion = atlas.findRegions("expl_02");
        explAnimation = new Animation<TextureAtlas.AtlasRegion>(
                0.04f, explosion, Animation.PlayMode.NORMAL
        );
    }

    //    public void draw (TextureRegion region, float x, float y, float originX, float originY, float width, float height,
//                      float scaleX, float scaleY, float rotation);
    public void render(SpriteBatch batch, float delta) {
        elapsedTime += delta;
        region = explAnimation.getKeyFrame(elapsedTime);

        batch.draw(
                region,
                position.x - region.getRegionWidth() / 2,
                position.y - region.getRegionHeight() / 2,
                region.getRegionWidth() / 2, region.getRegionHeight() / 2,
                region.getRegionWidth(), region.getRegionHeight(),
                3f,
                3f,
                0
        );


        if (explAnimation.isAnimationFinished(elapsedTime)) {
            setActive(false);
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        active = true;
    }

    public Animation<TextureAtlas.AtlasRegion> getExplosionAnim() {
        return explAnimation;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    @Override
    public void reset() {
        elapsedTime = 0f;
        active = false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}

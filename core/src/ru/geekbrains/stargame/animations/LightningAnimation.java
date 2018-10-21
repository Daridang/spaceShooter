package ru.geekbrains.stargame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 21/10/2018.
 */

public class LightningAnimation {

    private Vector2 position;

    private Array<AtlasRegion> lightning;

    private Animation<AtlasRegion> lightningAnim;

    private TextureRegion region;


    private float elapsedTime = 0f;

    public LightningAnimation(TextureAtlas atlas) {
        position = new Vector2();
        lightning = atlas.findRegions("lighteningball");
        lightningAnim = new Animation<AtlasRegion>(0.05f, lightning);
    }

    public void render(SpriteBatch batch, float delta) {
        elapsedTime += delta;
        region = lightningAnim.getKeyFrame(elapsedTime);
        batch.draw(
                region,
                position.x - region.getRegionWidth() / 2,
                position.y - region.getRegionHeight() / 2
        );
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Animation<AtlasRegion> getLightningAnim() {
        return lightningAnim;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

}

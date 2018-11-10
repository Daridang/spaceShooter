package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 10/11/2018.
 */

public class Shield implements Pool.Poolable {

    public enum TYPE {
        SHIELD_1,
        SHIELD_2
    }

    private TextureRegion textureRegion;
    private TextureAtlas atlas;
    private Vector2 shieldPos;
    private TYPE type;

    private Array<TextureAtlas.AtlasRegion> shieldRegion;
    private Animation<TextureAtlas.AtlasRegion> shieldAnim;
    private float elapsedTime = 0f;

    private int power;

    private boolean active;

    private long shieldAvailable;
    private long timeShieldActivated;

    public Shield(TextureAtlas atlas) {
        this.atlas = atlas;
        shieldPos = new Vector2();
        type = TYPE.values()[MathUtils.random(TYPE.values().length - 1)];
        randomBonus();
        shieldAnim = new Animation<TextureAtlas.AtlasRegion>(
                0.05f, shieldRegion, Animation.PlayMode.LOOP
        );
        timeShieldActivated = TimeUtils.millis();
    }

    private void randomBonus() {
        switch (type) {
            case SHIELD_1:
                shieldRegion = atlas.findRegions("d");
                power = 20;
                shieldAvailable = 10000;
                break;
            case SHIELD_2:
                shieldRegion = atlas.findRegions("s");
                power = 10;
                shieldAvailable = 20000;
        }
    }

    public void render(SpriteBatch batch, float delta) {
        elapsedTime += delta;
        textureRegion = shieldAnim.getKeyFrame(elapsedTime);
        batch.draw(
                textureRegion,
                shieldPos.x - textureRegion.getRegionWidth() / 2,
                shieldPos.y - textureRegion.getRegionHeight() / 2
        );
        checkShieldAvailability();
    }

    public void checkShieldAvailability() {
        if (TimeUtils.millis() - timeShieldActivated > shieldAvailable || power < 1) {
            active = false;
        }
    }

    public Vector2 getShieldPos() {
        return shieldPos;
    }

    public void setShieldPos(Vector2 shieldPos) {
        this.shieldPos = shieldPos;
    }

    public Animation<TextureAtlas.AtlasRegion> getLightningAnim() {
        return shieldAnim;
    }

    public float getElapsedTime() {
        return elapsedTime;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power += power;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public void reset() {
        active = false;
        timeShieldActivated = 0L;
    }

}

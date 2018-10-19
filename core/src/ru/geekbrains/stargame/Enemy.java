package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 19/10/2018.
 */

public class Enemy {

    private Vector2 position;
    private TextureRegion region;
    private Random random;
    private float speed;
    private boolean isOutOfScreen = false;

    public Enemy(TextureAtlas atlas) {
        region = atlas.findRegion("stateczek");
        region.flip(false, true);
        random = new Random();
        position = new Vector2(
                random.nextInt((int) StarGame.WORLD_WIDTH - region.getRegionWidth()),
                StarGame.WORLD_HEIGHT * 2);
        speed = random.nextFloat() * 300f;
    }

    public void render(SpriteBatch batch, float delta) {
        position.y -= speed * delta;
        if (position.y + region.getRegionHeight() * 2 < 0) {
            isOutOfScreen = true;
        }
        batch.draw(region, position.x, position.y);
        Gdx.app.log("TAG", String.valueOf(isOutOfScreen));
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

    public boolean isOutOfScreen() {
        return isOutOfScreen;
    }

    public void setOutOfScreen(boolean outOfScreen) {
        isOutOfScreen = outOfScreen;
    }

}

package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Random;

import ru.geekbrains.stargame.StarGame;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 22/10/2018.
 */

public class Asteroids {
    private Vector2 position;
    private TextureRegion region;
    private Random random;
    private float speed;
    private boolean isOutOfScreen = false;
    private long spawnTime;
    private float angle;

    public Asteroids(TextureAtlas atlas) {
        region = atlas.findRegion("Asteroids_64x64");
        spawnTime = TimeUtils.millis();

        random = new Random();
        position = new Vector2(
                random.nextInt((int) StarGame.WORLD_WIDTH - region.getRegionWidth()),
                StarGame.WORLD_HEIGHT * 2);
        speed = MathUtils.random(50f, 150f);
        angle = MathUtils.random(0f, 360f);
    }

    public void render(SpriteBatch batch, float delta) {
        angle += speed * delta;
        position.y -= speed * delta;
        if (position.y + region.getRegionHeight() * 2 < 0) {
            isOutOfScreen = true;
        }
        batch.draw(
                region,
                position.x,
                position.y,
                region.getRegionWidth() / 2, region.getRegionHeight() / 2,
                region.getRegionWidth(), region.getRegionHeight(),
                1f, 1f, angle
        );
        //batch.draw(region, position.x, position.y);
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

    public long getSpawnTime() {
        return spawnTime;
    }
}

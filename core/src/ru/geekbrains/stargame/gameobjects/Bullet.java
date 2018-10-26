package ru.geekbrains.stargame.gameobjects;


import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.sql.Time;

import ru.geekbrains.stargame.StarGame;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 22/10/2018.
 */

public class Bullet {
    Vector2 position;
    Vector2 velocity;
    public boolean active;
    private Rectangle hitBox;

    public Bullet() {
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
        hitBox = new Rectangle();
    }

    public void setup(float x, float y, float vx, float vy) {
        position.set(x, y);
        velocity.set(vx, vy);
        active = true;
    }

    public void destroy() {
        active = false;
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        hitBox.setPosition(position);
        if (position.x < -20 || position.x > StarGame.WORLD_WIDTH
                || position.y < -20 || position.y > StarGame.WORLD_HEIGHT) {
            destroy();
        }
    }

    public void setHitBox(int width, int height) {
        hitBox.set(position.x, position.y, width, height);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

}

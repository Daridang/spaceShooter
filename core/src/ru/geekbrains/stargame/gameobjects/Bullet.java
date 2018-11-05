package ru.geekbrains.stargame.gameobjects;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.StarGame;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 22/10/2018.
 */

public class Bullet implements Pool.Poolable{
    Vector2 position;
    Vector2 velocity;
    public boolean active;
    private Rectangle hitBox;
    private AssetManager manager;

    public Sprite getBullet() {
        return bullet;
    }

    private Sprite bullet;

    public Bullet(AssetManager manager) {
        this.manager = manager;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.active = false;
        bullet = new Sprite(manager.get("laser_beam.png", Texture.class));
        hitBox = new Rectangle(
                position.x, position.y, bullet.getWidth(), bullet.getHeight()
        );
    }

    public Bullet() {
        this.position = new Vector2(-40, -40);
        this.velocity = new Vector2(0, 0);
        this.active = false;

        hitBox = new Rectangle(
                position.x, position.y, 16f, 32f
        );
    }

    public void fireBullet(float xpos, float ypos, float xvel, float yvel){
        position.set(xpos, ypos);
        velocity.set(xvel, yvel);
        active = true;
    }

    public void render(SpriteBatch batch) {
        bullet.draw(batch);
    }

    public void destroy() {
        active = false;
    }

    public void updateEnemuBullets(float delta) {
        position.mulAdd(velocity, delta);
        hitBox.setPosition(position);
        if (position.y < -20) {
            destroy();
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        hitBox.setPosition(position);
        if (position.x < -20 || position.x > StarGame.WORLD_WIDTH
                || position.y < -20 || position.y > StarGame.WORLD_HEIGHT) {
            destroy();
        }
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public void reset() {
        System.out.println("Bullet: " + getClass().getSimpleName()   + " reset.");
        destroy();
    }

    public Vector2 getPosition() {
        return position;
    }

}

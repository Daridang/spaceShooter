package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 24/10/2018.
 */

public class BulletEmitter {
    private static final BulletEmitter ourInstance = new BulletEmitter();

    public static BulletEmitter getInstance() {
        return ourInstance;
    }

    Texture texture;
    public Bullet[] bullets;

    private BulletEmitter() {
        texture = new Texture("laser_beam.png");
        bullets = new Bullet[200];
        for (int i = 0; i < bullets.length; i++) {
            bullets[i] = new Bullet();
            bullets[i].setHitBox(texture.getWidth(), texture.getHeight());
        }
    }

    public void update(float dt) {
        for (Bullet o : bullets) {
            if (o.active) {
                o.update(dt);
            }
        }
    }

    public void render(SpriteBatch batch) {
        for (Bullet o : bullets) {
            if (o.active) {
                batch.draw(texture, o.position.x - 16, o.position.y - 16);
            }
        }
    }
}


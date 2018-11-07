package ru.geekbrains.stargame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import ru.geekbrains.stargame.gameobjects.Bullet;
import ru.geekbrains.stargame.gameobjects.Enemy;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 07/11/2018.
 */

public class TheBoss extends Enemy {

    private StarGame game;

    private Texture boss;
    private boolean atLeft;
    private boolean atRight;

    private int hitPoints = 20;

    public TheBoss(StarGame game) {
        super(game);
        this.game = game;
        boss = game.getAssetManager().get("alien_boss.png");
        isActive = false;
        position = new Vector2(Global.WIDTH / 3, Global.HEIGHT + boss.getHeight() * 2);
        hitBox.set(position.x, position.y, boss.getWidth(), boss.getHeight());
        atRight = true;
        deltaTime = MathUtils.random(1000f, 2000f);
        fireTime = TimeUtils.millis();
    }

    public void render(SpriteBatch batch, float delta) {
        if (hitPoints > 0) {
            bossEnter(delta);
            for (Bullet b : activeBullets) {
                if (!b.active) {
                    bulletPool.free(b);
                    activeBullets.removeValue(b, true);
                }
                b.updateEnemyBullets(delta);
                batch.setColor(0.9f, 0.0f, 0.0f, 0.8f);
                batch.draw(
                        b.getBullet().getTexture(),
                        b.getPosition().x,
                        b.getPosition().y
                );
            }
            batch.setColor(Color.WHITE);
            batch.draw(boss, getPosition().x, getPosition().y);
            update();
        }
    }

    public void fire() {
        Bullet a = bulletPool.obtain();
        Bullet b = bulletPool.obtain();

        a.fireBullet(
                position.x + getHitBox().width * 0.25f,
                position.y + getHitBox().height * 0.1f,
                -400 * MathUtils.cosDeg(90),
                -400 * MathUtils.sinDeg(90)
        );

        activeBullets.add(a);

        b.fireBullet(
                position.x + getHitBox().width * 0.75f,
                position.y + getHitBox().height * 0.1f,
                -400 * MathUtils.cosDeg(90),
                -400 * MathUtils.sinDeg(90)
        );

        activeBullets.add(b);

        game.getSm().getShoot().play(Global.SOUND_VOLUME);
        fireTime = TimeUtils.millis();
    }

    private void update() {
        hitBox.setPosition(position.x, position.y);
        if (TimeUtils.millis() - fireTime > deltaTime) {
            fire();
        }
    }

    private void bossEnter(float delta) {
        if (position.y > Global.HEIGHT - (boss.getHeight() + 80)) {
            position.y -= speed * delta;
        } else {
            if (atRight) {
                moveLeft(delta);
            }
            if (atLeft) {
                moveRight(delta);
            }
        }
    }

    private void moveRight(float delta) {
        position.x += speed * delta;
        if (position.x + boss.getWidth() > Global.WIDTH) {
            atLeft = false;
            atRight = true;
        }
    }

    private void moveLeft(float delta) {
        position.x -= speed * delta;
        if (position.x < 0) {
            atRight = false;
            atLeft = true;
        }
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints += hitPoints;
    }

}

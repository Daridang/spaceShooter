package ru.geekbrains.stargame.gameobjects;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.Global;
import ru.geekbrains.stargame.StarGame;
import ru.geekbrains.stargame.animations.PlayerAnimation;



public class Player {
    public static final float WIDTH = 80f;
    public static final float HEIGHT = 80f;
    private PlayerAnimation animation;
    private TextureRegion region;
    private Vector2 position;
    private Vector2 respawnPosition;
    private float enginePower = 200f;
    private Vector2 targetPosition;
    private boolean isTargetSet = false;
    private boolean isAlive = true;
    private float angle = 0f;
    private TextureAtlas atlas;
    private int lives = 3;

    private Rectangle hitBox;
    private Circle hitCircle;
    private StarGame game;

    public Player(StarGame game) {
        this.game = game;
        atlas = game.getAssetManager().get("texture_asset.atlas");
        animation = new PlayerAnimation(atlas);
        region = animation.getIdle().first();
        position = new Vector2(
                StarGame.WORLD_WIDTH / 2 - WIDTH / 2,
                HEIGHT / 2 + 100f
        );
        respawnPosition = new Vector2(StarGame.WORLD_WIDTH / 2 - WIDTH / 2,
                HEIGHT / 2 + 100f);
        targetPosition = new Vector2();
        hitBox = new Rectangle(
                position.x - WIDTH / 2,
                position.y - HEIGHT / 2,
                WIDTH,
                HEIGHT
        );
        hitCircle = new Circle(position, WIDTH / 2);
    }

    private void update() {
        hitBox.set(
                position.x - WIDTH / 2,
                position.y - HEIGHT / 2,
                WIDTH,
                HEIGHT
        );
        hitCircle.setPosition(position);
    }

    // Метод, который занимается выстреливанием пули
    public void fire() {
        Bullet[] bl = BulletEmitter.getInstance().bullets;
        for (Bullet b : bl) {
            if (!b.active) {
                b.fireBullet(
                        position.x,
                        position.y,
                        400 * MathUtils.cosDeg(90),
                        400 * MathUtils.sinDeg(90)
                );
                game.getSm().getShoot().play(Global.SOUND_VOLUME);
                break;
            }
        }
    }

    public void render(SpriteBatch batch, float delta) {

        if (Application.ApplicationType.Android == Gdx.app.getType()) {
            if (Gdx.input.justTouched()) {

                fire();
            }
            if (targetPosition.x < getPosition().x) {
                flyLeft(delta);
            }

            if (targetPosition.x > getPosition().x) {
                flyRight(delta);
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            fire();
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            flyLeft(delta);

        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            flyRight(delta);


        } else if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            flyUp(delta);

        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            flyDown(delta);

        } else {
            region = animation.getIdle().first();
        }
        update();

        batch.draw(
                region,
                position.x - region.getRegionWidth() / 2,
                position.y - region.getRegionHeight() / 2,
                region.getRegionWidth() / 2, region.getRegionHeight() / 2,
                region.getRegionWidth(), region.getRegionHeight(),
                1f, 1f, angle
        );
    }


    private void goToTheTarget(float delta) {

        float distance = targetPosition.dst(position);

        Vector2 d = targetPosition.cpy().sub(position.cpy()).nor();
        //angle = d.angle();

        position.add(
                targetPosition
                        .cpy()
                        .sub(position.cpy())
                        .nor()
                        .scl(delta * enginePower)
        );
        System.out.println(targetPosition.cpy().nor());

        if (distance < 3f) {
            isTargetSet = false;
            position.set(targetPosition);
        }
    }

    public void flyLeft(float delta) {
        region = animation.getAnimationLeft().getKeyFrame(delta);
        position.x -= delta * enginePower;
    }

    public void flyRight(float delta) {
        region = animation.getAnimationRight().getKeyFrame(delta);
        position.x += delta * enginePower;
    }

    private void flyUp(float delta) {
        region = animation.getIdle().first();
        position.y += delta * enginePower;
    }

    private void flyDown(float delta) {
        region = animation.getIdle().first();
        position.y -= delta * enginePower;
    }

    public void respawn() {
        position = respawnPosition.cpy();
        setAlive(true);
        lives = 3;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitCircle() {
        return hitCircle;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public TextureRegion getRegion() {
        return region;
    }

    public Vector2 getTargetPosition() {
        return targetPosition;
    }

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }

    public boolean isTargetSet() {
        return isTargetSet;
    }

    public void setTargetSet(boolean targetSet) {
        isTargetSet = targetSet;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void setHitBox(Rectangle hitBox) {
        this.hitBox = hitBox;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives += lives;
    }

    public Vector2 getRespawnPosition() {
        return respawnPosition;
    }

    public void setRespawnPosition(Vector2 respawnPosition) {
        this.respawnPosition = respawnPosition;
    }
}

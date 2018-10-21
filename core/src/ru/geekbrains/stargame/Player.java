package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import ru.geekbrains.stargame.animations.PlayerAnimation;


public class Player {
    private PlayerAnimation animation;
    private TextureRegion region;
    private Vector2 position;
    private float speed = 200f;
    private Vector2 targetPosition;
    private boolean isTargetSet = false;
    private float angle = 0f;

    public Player(TextureAtlas atlas) {
        animation = new PlayerAnimation(atlas);
        region = animation.getIdle().first();
        position = new Vector2(
                StarGame.WORLD_WIDTH / 2 - region.getRegionWidth() / 2,
                region.getRegionHeight() / 2 + 100f
        );
        targetPosition = new Vector2();
    }

    public void render(SpriteBatch batch, float delta) {

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            flyLeft(delta);
            region = animation.getAnimationLeft().getKeyFrame(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            region = animation.getAnimationRight().getKeyFrame(delta);
            flyRight(delta);

        } else if (isTargetSet) {
            region = animation.getIdle().first();
            goToTheTarget(delta);

        } else {
            region = animation.getIdle().first();
        }

        // batch.draw(region, position.x, position.y);
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

        Vector2 v1 = new Vector2(targetPosition);
        v1.nor();
        v1.scl(speed * delta);
        position.add(v1);

        System.out.println("pos: " + position + "\n" + "trg: " + targetPosition);

        if (distance < 20) {
            isTargetSet = false;
        }
    }

    private void flyLeft(float delta) {
        animation.getAnimationLeft().getKeyFrame(delta);
        position.x -= delta * speed;
    }

    private void flyRight(float delta) {
        animation.getAnimationRight().getKeyFrame(delta);
        position.x += delta * speed;
    }

    public Vector2 getPosition() {
        return position;
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

}

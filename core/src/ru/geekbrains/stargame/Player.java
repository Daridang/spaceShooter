package ru.geekbrains.stargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 17/10/2018.
 */

public class Player {
    private PlayerAnimation animation;
    private TextureRegion region;
    private Vector2 position;
    private float speed = 200f;

    public Player(TextureAtlas atlas) {
        animation = new PlayerAnimation(atlas);
        region = animation.getIdle().first();
        position = new Vector2(StarGame.WORLD_WIDTH / 2, region.getRegionHeight());
    }

    public void update(SpriteBatch batch, float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            flyLeft(delta);
            region = animation.getAnimationLeft().getKeyFrame(delta);
        } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            region = animation.getAnimationRight().getKeyFrame(delta);
            flyRight(delta);

        } else {
            region = animation.getIdle().first();
        }

        batch.draw(region, position.x, position.y);
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


}

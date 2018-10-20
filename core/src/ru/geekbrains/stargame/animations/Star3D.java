package ru.geekbrains.stargame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 20/10/2018.
 */

public class Star3D {
    private Vector3 position;
    private Vector3 velocity;
    private float width;
    private float height;

    private float depth_end = 1000;
    private float depth_start = 100;

    private float velocity_min = 0.5f;
    private float velocity_max = 5f;

    private float maxRadius = 5f;

    public Star3D() {
        position = new Vector3();
        velocity = new Vector3();
        init();
    }

    public void init() {
        width = Gdx.graphics.getWidth();
        height = Gdx.graphics.getHeight();
        position.x = MathUtils.random(-(width), width);
        position.y = MathUtils.random(-(height), height);
        position.z = MathUtils.random(depth_start, depth_end);
        velocity.z = MathUtils.random(velocity_min, velocity_max);
    }

    public void update(float speed) {
        if ((position.z < 0) || (position.z > depth_end) ||
                (position.y > height) || (position.x > width)) {
            init();
        }

        float t = Gdx.graphics.getDeltaTime();
        sub(
                (velocity.x * speed) * t,
                (velocity.y * speed) * t,
                (velocity.z * speed) * t
        );
    }

    public void draw(ShapeRenderer renderer) {
        float c = getColor();
        float b = getRandomColor();
        float x = ((position.x / position.z) * 100) + width * 0.5f;
        float y = ((position.y / position.z) * 100) + height * 0.5f;
        float radius = ((maxRadius - ((position.z * maxRadius) * 0.001f)) * velocity.z) * 0.2f;
        renderer.setColor(c, c, b, 1f);
        renderer.circle(x, y, radius);
    }

    public float getColor() {
        return ((1.0f - ((position.z * 1.0f) * .001f)) * velocity.z) * 0.2f;
    }

    public float getRandomColor() {
        return MathUtils.random(0.1f, 1f);
    }

    public void sub(float x, float y, float z) {
        position.sub(x, y, z);
    }

    public void add(float x, float y, float z) {
        position.add(x, y, z);
    }


    public Vector3 getPosition() {
        return position;
    }

    public void setPosition(Vector3 position) {
        this.position = position;
    }

    public Vector3 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3 velocity) {
        this.velocity = velocity;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getDepth_end() {
        return depth_end;
    }

    public void setDepth_end(float depth_end) {
        this.depth_end = depth_end;
    }

    public float getDepth_start() {
        return depth_start;
    }

    public void setDepth_start(float depth_start) {
        this.depth_start = depth_start;
    }

    public float getVelocity_min() {
        return velocity_min;
    }

    public void setVelocity_min(float velocity_min) {
        this.velocity_min = velocity_min;
    }

    public float getVelocity_max() {
        return velocity_max;
    }

    public void setVelocity_max(float velocity_max) {
        this.velocity_max = velocity_max;
    }

    public float getMaxRadius() {
        return maxRadius;
    }

    public void setMaxRadius(float maxRadius) {
        this.maxRadius = maxRadius;
    }

}

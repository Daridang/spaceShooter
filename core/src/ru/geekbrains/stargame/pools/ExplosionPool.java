package ru.geekbrains.stargame.pools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.animations.ExplosionAnimation;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 30/10/2018.
 */

public class ExplosionPool extends Pool<ExplosionAnimation> {
    private static final int EXPLOSIONS_COUNT = 24;
    private TextureAtlas atlas;

    public ExplosionPool(TextureAtlas atlas) {
        super(EXPLOSIONS_COUNT, EXPLOSIONS_COUNT);
        this.atlas = atlas;
    }

    @Override
    protected ExplosionAnimation newObject() {
        return new ExplosionAnimation(atlas);
    }
}

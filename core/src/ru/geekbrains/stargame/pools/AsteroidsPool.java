package ru.geekbrains.stargame.pools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.gameobjects.Asteroids;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 30/10/2018.
 */

public class AsteroidsPool extends Pool<Asteroids> {
    private static final int ASTEROIDS_COUNT = 24;
    private TextureAtlas atlas;

    public AsteroidsPool(TextureAtlas atlas) {
        super(ASTEROIDS_COUNT);
        this.atlas = atlas;
    }

    @Override
    protected Asteroids newObject() {
        return new Asteroids(atlas);
    }
}

package ru.geekbrains.stargame.pools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.gameobjects.Enemy;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 30/10/2018.
 */

public class EnemiesPool extends Pool<Enemy> {
    private static final int ENEMIES_COUNT = 24;
    private TextureAtlas atlas;

    public EnemiesPool(TextureAtlas atlas) {
        super(ENEMIES_COUNT);
        this.atlas = atlas;
    }
    @Override
    protected Enemy newObject() {
        return new Enemy(atlas);
    }
}

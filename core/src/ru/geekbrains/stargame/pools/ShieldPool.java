package ru.geekbrains.stargame.pools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.gameobjects.Shield;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 10/11/2018.
 */

public class ShieldPool extends Pool<Shield> {

    private TextureAtlas atlas;

    public ShieldPool(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    @Override
    protected Shield newObject() {
        return new Shield(atlas);
    }
}

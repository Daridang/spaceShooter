package ru.geekbrains.stargame.pools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.gameobjects.Bonus;


/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 10/11/2018.
 */

public class BonusPool extends Pool<Bonus> {
    private TextureAtlas atlas;

    public BonusPool(TextureAtlas atlas) {
        super();
        this.atlas = atlas;
    }
    @Override
    protected Bonus newObject() {
        return new Bonus(atlas);
    }
}

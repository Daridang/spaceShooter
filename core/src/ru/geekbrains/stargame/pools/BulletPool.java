package ru.geekbrains.stargame.pools;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.gameobjects.Bullet;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 02/11/2018.
 */

public class BulletPool extends Pool<Bullet> {
    private AssetManager manager;

    public BulletPool(AssetManager manager) {
        super();
        this.manager = manager;
    }
    @Override
    protected Bullet newObject() {
        return new Bullet(manager);
    }
}

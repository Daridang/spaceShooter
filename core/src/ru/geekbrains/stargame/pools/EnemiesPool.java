package ru.geekbrains.stargame.pools;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pool;

import ru.geekbrains.stargame.StarGame;
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
    private StarGame game;

    public EnemiesPool(StarGame game) {
        super(ENEMIES_COUNT);
        this.game = game;
    }
    @Override
    protected Enemy newObject() {
        return new Enemy(game);
    }
}

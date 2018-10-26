package ru.geekbrains.stargame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 26/10/2018.
 */

public class StarGameHud {
    final BitmapFont font;

    public StarGameHud(BitmapFont font) {
        this.font = font;
        font.getData().setScale(1);
    }


    public void render(SpriteBatch batch, int lives, int score) {
        final String scoreStr = "Score" + score;
        final String livesStr = "Lives" + lives;

        font.draw(batch, scoreStr, 20, StarGame.WORLD_HEIGHT - 20);
        font.draw(batch, livesStr, StarGame.WORLD_WIDTH - 200, StarGame.WORLD_HEIGHT - 20);
    }


}

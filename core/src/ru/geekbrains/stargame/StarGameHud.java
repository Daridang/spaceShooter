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
    private final BitmapFont font;
    private boolean isShown;

    public StarGameHud(BitmapFont font) {
        this.font = font;
        isShown = true;
    }


    public void render(SpriteBatch batch, int lives, int score) {
        final String scoreStr = "Score\n" + score;
        final String livesStr = "Lives\n" + lives;

        font.draw(batch, scoreStr, 20, StarGame.WORLD_HEIGHT - 20);
        font.draw(batch, livesStr, StarGame.WORLD_WIDTH - 200, StarGame.WORLD_HEIGHT - 20);
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

}

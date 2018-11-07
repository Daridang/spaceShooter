package ru.geekbrains.stargame;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 31/10/2018.
 */

public class GameOverOverlay {
    private final BitmapFont font;
    private boolean isShown;

    public GameOverOverlay(BitmapFont font) {
        this.font = font;
        isShown = false;
    }

    public void render(SpriteBatch batch) {
        final String gameOver = "Game\tOver";

        font.draw(batch, gameOver, Global.WIDTH / 3, Global.HEIGHT / 2);
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

}

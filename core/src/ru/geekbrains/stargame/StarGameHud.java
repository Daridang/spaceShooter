package ru.geekbrains.stargame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
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

    private Texture hpBackground;
    private Texture hpLeft;
    private int playerHitPoints = 10;
    private float hp;

    public StarGameHud(BitmapFont font) {
        this.font = font;
        isShown = true;
        hpBackground = new Texture("hpBackground.png");
        hpLeft = new Texture("hpFull.png");
        hp = hpBackground.getWidth() / playerHitPoints;
    }


    public void render(SpriteBatch batch, int lives, int score, int playerHitPoints) {
        final String scoreStr = "Score\n" + score;
        final String livesStr = "Lives\n" + lives;

        font.draw(batch, scoreStr, 20, StarGame.WORLD_HEIGHT - 20);
        font.draw(
                batch, livesStr, StarGame.WORLD_WIDTH - 200, StarGame.WORLD_HEIGHT - 20
        );

        batch.draw(
                hpBackground,
                StarGame.WORLD_WIDTH / 2 - hpBackground.getWidth() / 2,
                StarGame.WORLD_HEIGHT - 40
        );
        batch.draw(
                hpLeft,
                StarGame.WORLD_WIDTH / 2 - hpBackground.getWidth() / 2,
                StarGame.WORLD_HEIGHT - 40,
                (int) (hp * playerHitPoints), hpLeft.getHeight()
        );

    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

}

package ru.geekbrains.stargame;


import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 16/10/2018.
 */

public class Background {
    private TextureRegion back;
    public static int SPEED = 80;
    private float y1, y2;

    public Background(TextureAtlas atlas) {

        back = atlas.findRegion("space_bck4");
        y1 = 0;
        y2 = back.getRegionHeight();
    }

    public void render(float deltaTime, SpriteBatch batch) {

        y1 -= SPEED * deltaTime;
        y2 -= SPEED * deltaTime;

        if (y1 + back.getRegionHeight() <= 0)
            y1 = y2 + back.getRegionHeight();

        if (y2 + back.getRegionHeight() <= 0)
            y2 = y1 + back.getRegionHeight();

        //Render
        batch.draw(back, 0, y1, back.getRegionWidth(), back.getRegionHeight());
        batch.draw(back, 0, y2, back.getRegionWidth(), back.getRegionHeight());
    }
}

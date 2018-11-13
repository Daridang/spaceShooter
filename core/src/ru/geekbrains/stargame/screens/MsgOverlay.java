package ru.geekbrains.stargame.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ru.geekbrains.stargame.Global;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 13/11/2018.
 */

public class MsgOverlay {
    private BitmapFont font;
    private Texture texture;

    public MsgOverlay() {
        font = new BitmapFont();
        font.getData().setScale(3f);
        font.setColor(Color.WHITE);
        texture = new Texture(Gdx.files.internal("siluet.png"));
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }

    public void render(SpriteBatch batch, String msg) {
        batch.draw(texture, Global.WIDTH / 8, 600);
        font.draw(batch, msg, Global.WIDTH / 8, 600);
    }
}

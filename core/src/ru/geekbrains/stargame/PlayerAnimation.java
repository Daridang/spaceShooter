package ru.geekbrains.stargame;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * Created by
 * +-+-+-+-+-+-+-+-+
 * |D|a|r|i|d|a|n|g|
 * +-+-+-+-+-+-+-+-+
 * on 17/10/2018.
 */

public class PlayerAnimation {
    private TextureAtlas atlas;
    private Animation<TextureRegion> animationLeft;
    private Animation<TextureRegion> animationRight;

    private Array<TextureRegion> idle;

    public PlayerAnimation(TextureAtlas atlas) {
        this.atlas = atlas;

        initAnimation();

    }

    private void initAnimation() {
        Array<TextureRegion> regionsLeft = new Array<TextureRegion>();
        Array<TextureRegion> regionsRight = new Array<TextureRegion>();
        idle = new Array<TextureRegion>();

        regionsLeft.add(atlas.findRegion("ship-1"));
        regionsLeft.add(atlas.findRegion("ship-2"));
        regionsLeft.add(atlas.findRegion("ship-3"));
        regionsLeft.add(atlas.findRegion("ship-4"));
        regionsLeft.add(atlas.findRegion("ship-5"));

        regionsRight.add(atlas.findRegion("ship1"));
        regionsRight.add(atlas.findRegion("ship2"));
        regionsRight.add(atlas.findRegion("ship3"));
        regionsRight.add(atlas.findRegion("ship4"));
        regionsRight.add(atlas.findRegion("ship5"));

        animationLeft = new Animation<TextureRegion>(
                1/30, regionsLeft, Animation.PlayMode.LOOP);

        animationRight = new Animation<TextureRegion>(
                1/30, regionsRight, Animation.PlayMode.LOOP);

        idle.add(atlas.findRegion("ship0"));
    }

    public Animation<TextureRegion> getAnimationLeft() {
        return animationLeft;
    }

    public Animation<TextureRegion> getAnimationRight() {
        return animationRight;
    }

    public Array<TextureRegion> getIdle() {
        return idle;
    }

}

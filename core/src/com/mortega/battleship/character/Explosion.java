package com.mortega.battleship.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Explosion {

    private static final float FRAME_LENGTH = 0.15f;
    private static final int OFFSET = 8;
    private static final int SIZE = 90;
    public static final int IMAGE_SIZE = 143;

    private static Animation anim = null;
    private float x, y;
    private float statetime;

    public boolean remove = false;

    public Explosion (float x, float y) {
        this.x = x - OFFSET;
        this.y = y - OFFSET;
        statetime = 0;

        if (anim == null)
            anim = new Animation(FRAME_LENGTH, TextureRegion.split(new Texture("explosion.hasgraphics.png"),IMAGE_SIZE, IMAGE_SIZE)[0]);
    }

    public void update (float deltatime) {
        statetime += deltatime;
        if (anim.isAnimationFinished(statetime))
            remove = true;
    }

    public void render (SpriteBatch batch) {
        batch.draw((TextureRegion) anim.getKeyFrame(statetime), x+370, y, SIZE, SIZE);
    }
}

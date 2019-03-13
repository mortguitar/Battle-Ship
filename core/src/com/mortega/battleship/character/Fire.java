package com.mortega.battleship.character;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mortega.battleship.BattleShip;
import com.mortega.battleship.screen.ElectionShip;
import com.mortega.battleship.tools.Colisiones;

import static com.mortega.battleship.screen.ElectionShip.Tipo.ULTIMATE;

public class Fire {

    public static int SPEED = 500;
    public static final int DEFAULT_Y = 40;
    public static final int WIDTH = 3;
    public static final int HEIGHT = 12;
    private static Texture texture;

    float x, y;
    Colisiones rect;
    public boolean remove = false;

    public Fire (float x) {
        this.x = x;
        this.y = DEFAULT_Y;
        this.rect = new Colisiones (x, y, WIDTH, HEIGHT);

        Sound fireSound = Gdx.audio.newSound(Gdx.files.internal("music/Laser - Sound Effect.mp3"));
        fireSound.play();

        if (ElectionShip.tipo == ULTIMATE) {
            texture = new Texture("bullet_ultimate.png");
            SPEED = 2000;
        }
        else if (texture == null)
            texture = new Texture("bullet.png");

    }

    public void update (float deltaTime) {
        y += SPEED * deltaTime;
        if (y > BattleShip.HEIGHT)
            remove = true;

        rect.move(x, y);
    }

    public void render (SpriteBatch batch) {
        batch.draw(texture, x, y);
    }

    public Colisiones getCollisionRect () {
        return rect;
    }
}

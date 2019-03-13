package com.mortega.battleship.character;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mortega.battleship.BattleShip;
import com.mortega.battleship.screen.GameScreen;
import com.mortega.battleship.tools.Colisiones;

import static com.mortega.battleship.character.Asteroid.Tipo.*;

public class Asteroid {

    public enum Tipo {
        NORMAL,
        METAL,
        FUEGO,
        HIELO,
        BIG_CHUNGUS
    }

    private static int SPEED = 150;
    public static final int WIDTH = 32;
    private static final int HEIGHT = 32;

    private static Texture texture;

    public static Tipo tipo;
    public static int vida = 1;
    public static int contador;

    private float x;
    private float y;
    private Colisiones colisiones;
    public boolean remove = false;

    private int rotation = 0;
    private int originX = (int) ((x + WIDTH)/2);
    private int originY = (int) ((y + HEIGHT)/2);

    public Asteroid(float x) {

        this.x = x;
        this.y = BattleShip.HEIGHT;

        this.colisiones = new Colisiones(x,y,WIDTH,HEIGHT);

        if (texture == null) {
            texture = new Texture("asteroid.png");
            tipo = Tipo.NORMAL;

        } else if (GameScreen.score > 4000) {
            texture = new Texture("asteroid_metal.png");
            tipo = Tipo.METAL;
            SPEED = 200;
            vida = 2;
        }

        if (GameScreen.score > 10000) {
            texture = new Texture("asteroid_fire.png");
            tipo = FUEGO;
            SPEED = 250;
            vida = 3;
        }

        if (GameScreen.score > 25000) {
            texture = new Texture("asteroid.png");
            SPEED = 300;
            contador = 2;
            tipo = Tipo.NORMAL;
        }

        if (GameScreen.score > 37000) {
            texture = new Texture("asteroid_ice.png");
            tipo = HIELO;
            SPEED = 600;
            vida = 2;
        }

        if (GameScreen.score > 60000) {
            texture = new Texture("asteroid.png");
            contador = 3;
            SPEED = 400;
            tipo = Tipo.NORMAL;
        }

        if (GameScreen.score > 70000) {
            texture = new Texture("big_chungus.png");
            tipo = BIG_CHUNGUS;
            SPEED = 500;
            vida = 100;
        }

        if (x > 1100)
            remove = true;
    }

    public void update (float delta) {

        y -= SPEED * delta;

        if (y < -HEIGHT)
            remove = true;

        if (x > 1100)
            remove = true;

        colisiones.move(x + 370,y);

        rotation += 1;
    }

    public void render (SpriteBatch batch) {

        if (tipo == FUEGO)  { batch.draw(texture, x + 370, y,100,100);
        } else if(tipo == METAL) { batch.draw(texture, x + 370, y, 64, 64);
        } else if (tipo == BIG_CHUNGUS) { batch.draw(texture, x + 370, y, 500, 500);
        } else {
            batch.draw(texture, x + 370, y, WIDTH, HEIGHT);
        }
    }

    public Colisiones getColisiones () { return colisiones; }
    public float getPosicionX () { return x; }
    public float getPosicionY () { return y; }
}

package com.mortega.battleship.tools;

public class Colisiones {

    private float x, y;
    private int width, height;

    public Colisiones (float x, float y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void move (float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean collidesWith (Colisiones rect) {
        return x < rect.x + rect.width && y < rect.y + rect.height && x + width > rect.x && y + height > rect.y;
    }
}

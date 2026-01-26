package io.github.pescador;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Anzuelo {

    public Sprite sprite;
    private float posicionCero;
    public float x, y;
    public float width, height;

    public float speedX = 400f;
    public float speedY = 400f;

    private int lastDirection = 1;

    public Anzuelo(Pescador pescador) {
        this.x = 200;
        posicionCero = 270 + pescador.getPescadorY();
        this.y = posicionCero;

        sprite = new Sprite(new Texture("anzuelo.png"));
        sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
        sprite.setFlip(true, false);

        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void update(float delta) {
    }

    public void moveLeft(float delta) {
        x -= speedX * delta;
        lastDirection = 0;
        sprite.setFlip(false, false);
    }

    public void moveRight(float delta) {
        x += speedX * delta;
        lastDirection = 1;
        sprite.setFlip(true, false);
    }

    public void reelOut() {
        while (y > 0) {
            y -= speedY;
        }
        reelIn();
    }

    private void reelIn() {
        while (y < posicionCero) {
            y += speedY;
        }
    }

    public int getLastDirection() {
        return lastDirection;
    }
}

package io.github.juego_prueba;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Personaje {

    public Sprite sprite;

    public float x, y;
    public float width, height;

    // Movimiento
    public float speedX = 400f;
    public float velocityY = 0;

    // Física
    public float gravity = -2000f;
    public float jumpForce = 900f;
    public boolean isGrounded = false;
    public boolean isFalling = false;

    public int lastDirection = 1;

    public Personaje(float x, float y) {
        this.x = x;
        this.y = y;

        sprite = new Sprite(new Texture("grounded.png"));
        sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);

        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void update(float delta) {

        if (dead) {
            velocityY += gravity * delta;
            y += velocityY * delta;
            return;
        }

        velocityY += gravity * delta;
        y += velocityY * delta;

        if (y <= 0) {
            y = 0;
            velocityY = 0;
            isGrounded = true;
        }
    }

    public void jump() {
        if (isGrounded) {
            velocityY = jumpForce;
            isGrounded = false;
        }
    }

    public void groundPound() {
        if (!isGrounded) {
            velocityY = -jumpForce;
        }
    }

    public void moveLeft(float delta) {
        x -= speedX * delta;
        lastDirection = 0;
        sprite.setFlip(true, false);
    }

    public void moveRight(float delta) {
        x += speedX * delta;
        lastDirection = 1;
        sprite.setFlip(false, false);
    }

    public boolean dead = false;

    public void die() {
        if (dead) return;

        dead = true;
        sprite.setTexture(new Texture("falling.png"));
        velocityY = 900f;
        isGrounded = false;
    }


    public void setTexture(String texture) {
        sprite.setTexture(new Texture(texture));
    }
}

package io.github.pescador2;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Pescador {

    public Sprite sprite;
    public float x, y;
    public float width, height;

    public float speedX = 0f;
    private int lastDirection = 1;

    public Pescador(float x, float y) {
        this.x = x;
        this.y = y;

        sprite = new Sprite(new Texture("pescador.png"));
        sprite.setSize(sprite.getWidth() * 0.2f, sprite.getHeight() * 0.2f);

        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void update(float delta) {
        x += speedX * delta;
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

    public void setTexture(String texture) {
        sprite.setTexture(new Texture(texture));
    }

}

package io.github.pescador;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Pescador {

    public Sprite sprite;
    public float x, y;
    public float width, height;

    public float speedX = 400f;
    int lastDirection = 1;

    public Pescador() {
        this.x = 200;
        this.y = 300;

        sprite = new Sprite(new Texture("pescador.png"));
        sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);

        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void update(float delta) {
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

    public float getPescadorY() {
        return y;
    }


    public void setTexture(String texture) {
        sprite.setTexture(new Texture(texture));
    }

}

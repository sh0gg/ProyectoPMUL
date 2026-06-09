package io.github.pescador;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Pescador {

    public Sprite sprite;
    public float x, y;
    public float width, height;

    public int lastDirection = 1; // 0 izquierda, 1 derecha

    public Pescador() {
        sprite = new Sprite(new Texture("pescador.png"));
        sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);

        width = sprite.getWidth();
        height = sprite.getHeight();

        x = Mundo.ANCHO / 2f - width / 2f;
        y = Mundo.PESCADOR_Y;
    }

    public void update(float delta) {
        // si luego quieres animación/estado, va aquí
    }

    public void moveLeft(float delta) {
        x -= Mundo.PESCADOR_SPEED_X * delta;
        lastDirection = 0;
        sprite.setFlip(true, false);
    }

    public void moveRight(float delta) {
        x += Mundo.PESCADOR_SPEED_X * delta;
        lastDirection = 1;
        sprite.setFlip(false, false);
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}

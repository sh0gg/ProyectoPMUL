package io.github.juego_prueba;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class Enemigo {

    public Sprite sprite;
    public float x, y;
    public float width, height;

    private float speed = 100f;
    private int direction = 1;

    private float velocityY = 0;
    private float gravity = -1800f;

    // Estados
    public boolean alive = true;
    public boolean squashed = false;
    public boolean blinking = false;
    public boolean remove = false;
    public boolean canMove = false;  // Nuevo: solo se mueve si está en la cámara

    private float stateTime = 0f;
    private boolean visible = true;

    private Texture normalTex;
    private Texture deadTex;

    public Enemigo(float x, float y) {
        this.x = x;
        this.y = y;

        normalTex = new Texture("gomuba.png");
        deadTex = new Texture("gomubaDed.png");

        sprite = new Sprite(normalTex);
        sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);

        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void update(float delta) {

        stateTime += delta;


        if (canMove && alive) {
            // Movimiento normal
            x += speed * direction * delta;

            if (x <= 0) {
                direction *= -1;
                sprite.setFlip(direction < 0, false);
            }

            velocityY += gravity * delta;
            y += velocityY * delta;

            if (y <= 0) {
                y = 0;
                velocityY = 0;
            }
        }

        // Aplastado: quieto
        if (squashed && !blinking) {
            if (stateTime >= 0.4f) {
                blinking = true;
                stateTime = 0;
            }
        }

        // Parpadeo (2 veces)
        if (blinking) {
            visible = ((int)(stateTime * 10) % 2) == 0;

            if (stateTime >= 0.8f) {
                remove = true;
            }
        }
    }

    public void die() {
        if (!alive) return;

        alive = false;
        squashed = true;

        sprite.setTexture(deadTex);

        // Aplastar visualmente
        sprite.setSize(width, height * 0.4f);
        height = sprite.getHeight();

        speed = 0;
        velocityY = 0;
        stateTime = 0;
    }

    public boolean isVisible() {
        return visible;
    }
}

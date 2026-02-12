package io.github.pescador;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Pez {

    public float x, y;
    public float width = 50f;
    public float height = 30f;

    private final Texture texture;

    private float speed;
    private int dir; // 1 derecha, -1 izquierda

    // Hitbox de “boca” (pequeña, delante)
    private final Rectangle mouthHitBox = new Rectangle();

    public static Pez crearAleatorio() {
        Texture t = MathUtils.randomBoolean()
                ? new Texture("peixe1.png")
                : new Texture("peixe2.png");

        float y = MathUtils.random(0f, Mundo.ALTO_MAR - 30f);

        boolean vaDerecha = MathUtils.randomBoolean();
        float x = vaDerecha ? -50f : Mundo.ANCHO;

        float speed = MathUtils.random(40f, 120f);

        return new Pez(t, x, y, speed, vaDerecha ? 1 : -1);
    }

    private Pez(Texture texture, float x, float y, float speed, int dir) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.dir = dir;
        syncMouth();
    }

    public void update(float delta) {
        x += dir * speed * delta;
        syncMouth();
    }

    private void syncMouth() {
        // boca como un rectángulo pequeño delante del pez
        float mw = width / 3f;
        float mh = height * 0.3f;

        float mx = (dir == 1) ? (x + 2f * mw) : x; // delante según dirección
        float my = y + height * 0.35f;

        mouthHitBox.set(mx, my, mw, mh);
    }

    public Rectangle getMouthHitBox() {
        return mouthHitBox;
    }

    public void draw(SpriteBatch batch) {
        // espejo según dirección
        if (dir == 1) {
            batch.draw(texture, x, y, width, height);
        } else {
            batch.draw(texture, x + width, y, -width, height);
        }
    }

    public boolean estaFueraDelMundo() {
        return (dir == 1 && x > Mundo.ANCHO) || (dir == -1 && x < -width);
    }

    public void dispose() {
        texture.dispose();
    }
}

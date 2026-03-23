package io.github.examenDBR;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Vehiculo {
    private final Texture texture;
    public float x, y;
    public float width = 50f;
    public float height = 30f;
    private float speed;

    private Vehiculo(Texture texture, float x, float y, float speed) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public static Vehiculo crearAleatorio() {
        Texture t = new Texture("moto.png");

        float y = MathUtils.random(0f, Mundo.ALTO - 30f);

        float x = -50f;

        float speed = MathUtils.random(40f, 120f);

        return new Vehiculo(t, x, y, speed);
    }

    public void update(float delta) {
        x += speed * delta;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public boolean estaFueraDelMundo() {
        return (x > Mundo.ANCHO) || (x < -width);
    }

    public void dispose() {
        texture.dispose();
    }
}

package io.github.naves;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.Pool;

public class Bala implements Pool.Poolable {

    private final Circle circle = new Circle();
    private float velocidadX = 420f;

    public boolean destruida = false;

    public void init(float x, float y, float radio, float velocidadX) {
        circle.set(x, y, radio);
        this.velocidadX = velocidadX;
        this.destruida = false;
    }

    public void update(float delta) {
        if (destruida) return;

        circle.x += velocidadX * delta;

        if (circle.x - circle.radius > Mundo.ANCHO) {
            destruida = true;
        }
    }

    public void render(ShapeRenderer sr) {
        if (!destruida) {
            sr.circle(circle.x, circle.y, circle.radius);
        }
    }

    public Circle getCircle() {
        return circle;
    }

    @Override
    public void reset() {
        circle.set(0, 0, 0);
        velocidadX = 420f;
        destruida = false;
    }
}

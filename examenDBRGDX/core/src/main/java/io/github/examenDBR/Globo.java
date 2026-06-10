package io.github.examenDBR;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

public class Globo {
    float x;
    float y;

    float width;
    float height;

    float speed;

    Color color;

    boolean destroyed = false;

    Globo() {
        color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
        width = MathUtils.random(40,120);
        height = (float) (width * 1.5);
        x = MathUtils.random(width, Mundo.ANCHO-width);
        y = -height;
        speed = MathUtils.random(Mundo.velocidadMin, Mundo.velocidadMax);
    }

    void update(float delta) {
        y += speed * delta;
    }

    void render(ShapeRenderer sr) {
        sr.setColor(color);
        sr.ellipse(x, y, width, height);

        if (Mundo.DEBUG) {
            sr.setColor(Color.RED);
            sr.ellipse(x, y, width, height);
        }
    }

    public boolean isHit(float tx, float ty) {
        float cx = x + width/2;
        float cy = y + height/2;
        float dx = (tx - cx) / (width/2);
        float dy = (ty - cy) / (height/2);
        return dx*dx + dy*dy <= 1f;
    }

    void renderDebug(ShapeRenderer sr) {
        sr.setColor(Color.RED);
        sr.ellipse(x, y, width, height);
    }
}

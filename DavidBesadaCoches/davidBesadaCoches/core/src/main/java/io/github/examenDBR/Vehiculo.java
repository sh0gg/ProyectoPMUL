package io.github.examenDBR;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;


public class Vehiculo {
    float x, y;
    Color color;
    int direccion;
    Rectangle rect;
    float velocidad;
    boolean destruido = false;
    float tam = Mundo.getTamCasilla();


    public Vehiculo(float x) {
        this.x = x;
        this.color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
        this.direccion = (MathUtils.random(0, 1) == 1) ? 1 : -1;
        this.velocidad = MathUtils.random(100f, 300f);

        // primero calcular y
        this.y = (this.direccion == 1) ? -tam : Mundo.ALTO + tam;

        // luego crear el rect con la y correcta
        this.rect = new Rectangle(x, y, tam, tam * 0.8f);
    }

    void update(float delta) {
        y += velocidad * delta * direccion;
        rect.setPosition(x, y);
        if (y > Mundo.ALTO + tam || y < -tam) destruido = true;
    }

    void render(ShapeRenderer sr) {
        float ventSize = tam * 0.4f;
        float ventX = x + tam * 0.3f;

        // Cuerpo
        sr.setColor(color);
        sr.rect(x, y, tam, tam * 0.8f);

        // Ventanilla — arriba si sube, abajo si baja
        sr.setColor(Color.DARK_GRAY);
        if (direccion == 1) {
            sr.rect(ventX, y + tam - ventSize - tam * 0.1f, ventSize, ventSize);
        } else {
            sr.rect(ventX, y + tam * 0.1f, ventSize, ventSize);
        }
    }
}

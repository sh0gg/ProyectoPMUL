package io.github.bender;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Cerveza extends Objeto {
    public Cerveza(float x, float ancho, float alto) {
        super(x, ancho, alto);
    }

    @Override
    public void dibujar(ShapeRenderer sr) {
        sr.rect(x, y, ancho, alto, Color.GREEN, Color.GREEN, Color.GREEN, Color.GREEN);
    }
}

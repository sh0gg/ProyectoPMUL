package io.github.bender;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Manzana extends Objeto {
    public Manzana(float x, float ancho, float alto) {
        super(x, ancho, alto);
    }

    @Override
    public void dibujar(ShapeRenderer sr) {
        sr.rect(x, y, ancho, alto, Color.RED, Color.RED, Color.RED, Color.RED);
    }
}

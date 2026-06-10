package io.github.examenDBR;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SombraCuadrada extends Sombra {
    public SombraCuadrada() {
        super(Cazador.Forma.CUADRADO);
    }

    @Override
    void render(ShapeRenderer sr) {
        sr.setColor(Color.DARK_GRAY);
        sr.rect(x, y, tam, tam);
    }
}

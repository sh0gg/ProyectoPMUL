package io.github.examenDBR;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class SombraCircular extends Sombra {
    public SombraCircular() {
        super(Cazador.Forma.CIRCULO);
    }

    @Override
    void render(ShapeRenderer sr) {
        sr.setColor(Color.DARK_GRAY);
        sr.circle(x + tam/2, y + tam/2, tam/2);
    }
}

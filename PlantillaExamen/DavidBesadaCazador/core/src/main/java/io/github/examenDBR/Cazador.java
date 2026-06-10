package io.github.examenDBR;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Cazador {
    float x, y, tam;
    Rectangle hitbox;
    enum Forma { CUADRADO, CIRCULO }
    Forma forma = Forma.CUADRADO;

    public Cazador() {
        tam = 40f;
        x = 0;
        y = Mundo.MITAD_ALTO - tam / 2;
        hitbox = new Rectangle(x, y, tam, tam);
    }

    void mover(int dir) {
        y = MathUtils.clamp(y + dir * tam, 0, Mundo.ALTO - tam);
        hitbox.setPosition(x, y);
    }

    void cambiarForma() {
        forma = (forma == Forma.CUADRADO) ? Forma.CIRCULO : Forma.CUADRADO;
    }

    void render(ShapeRenderer sr) {
        sr.setColor(Color.CYAN);
        if (forma == Forma.CUADRADO) {
            sr.rect(x, y, tam, tam);
        } else {
            sr.circle(x + tam/2, y + tam/2, tam/2);
        }
        // brillo exterior
        sr.setColor(Color.WHITE);
        sr.rect(x, y, tam, 2); // línea blanca abajo
    }
}
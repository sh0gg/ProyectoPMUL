package io.github.examenDBR;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Bala {
    float x, y, tam;
    Rectangle hitbox;
    Cazador.Forma forma;
    boolean destruida = false;
    float velocidad = 400f;

    public Bala(float x, float y, Cazador.Forma forma) {
        this.x = x;
        this.y = y;
        this.forma = forma;
        this.tam = 15f;
        this.hitbox = new Rectangle(x, y, tam, tam);
    }

    void update(float delta) {
        x += velocidad * delta;
        hitbox.setPosition(x, y);
        if (x > Mundo.ANCHO) destruida = true;
    }

    void render(ShapeRenderer sr) {
        sr.setColor(Color.YELLOW);
        if (forma == Cazador.Forma.CUADRADO) {
            sr.rect(x, y, tam, tam);
        } else {
            sr.circle(x + tam/2, y + tam/2, tam/2);
        }
    }
}

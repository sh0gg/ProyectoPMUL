package io.github.examenDBR;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public abstract class Sombra {
    float x, y, tam;
    Rectangle hitbox;
    boolean destruida = false;
    float velocidad;
    Cazador.Forma forma;

    public Sombra(Cazador.Forma forma) {
        this.forma = forma;
        this.tam = 40f;
        this.velocidad = MathUtils.random(80f, 200f);
        this.x = Mundo.ANCHO + tam;
        this.y = MathUtils.random(0, Mundo.ALTO - tam);
        this.hitbox = new Rectangle(x, y, tam, tam);
    }

    void update(float delta) {
        x -= velocidad * delta;
        hitbox.setPosition(x, y);
        if (x < -tam) destruida = true;
    }

    abstract void render(ShapeRenderer sr);
}

package io.github.bender;


import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public abstract class Objeto {
    static final Random random = new Random();
    static final float VELOCIDAD = 200;
    static final float DIMENSION = 40;

    float x;
    float y;
    float ancho;
    float alto;
    float velocidad;
    Rectangle hitbox;

    public Objeto(float x, float ancho, float alto) {
        this.x = x;
        this.y = Mundo.ALTO;
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = VELOCIDAD;
        hitbox = new Rectangle(x, y, ancho, alto);
    }

    public static Objeto creaNuevoObjeto() {
        boolean tipo = random.nextBoolean();
        if (tipo) return new Manzana(random.nextInt(Mundo.ANCHO) - DIMENSION, DIMENSION, DIMENSION);
        else return new Cerveza(random.nextInt(Mundo.ANCHO) - DIMENSION, DIMENSION, DIMENSION);
    }

    public void actualizar(float delta) {
        y -= velocidad * delta;
        hitbox.setPosition(x, y);
        if (y>Mundo.ALTO) Mundo.eliminar(this);
    }

    abstract public void dibujar(ShapeRenderer sr);
}

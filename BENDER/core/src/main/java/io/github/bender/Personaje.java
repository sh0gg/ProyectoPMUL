package io.github.bender;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Personaje {

    public enum Estado {IZQUIERDA, DERECHA, PARADO}

    float x;
    float y;
    float ancho;
    float alto;
    float velocidad;
    boolean cargado = false;
    Estado estado = Estado.PARADO;
    Rectangle pibe;

    public void derecha(){
        estado = Estado.DERECHA;
    }

    public void izquierda(){
        estado = Estado.IZQUIERDA;
    }
    public void parar(){
        estado = Estado.PARADO;
    }


    public Personaje(float x, float y, float ancho, float alto) {
        this.x = x;
        this.y = y;
        this.ancho = ancho;
        this.alto = alto;
        this.velocidad = 200;
        pibe = new Rectangle(x, y, ancho, alto);
    }

    public void actualizar(float delta) {
        if (estado == Estado.PARADO) return;
        switch (estado) {
            case DERECHA:
                x += velocidad * delta;
                float xMax = Mundo.ANCHO - ancho;
                if (x > xMax) x = xMax;
                break;
            case IZQUIERDA:
                x -= velocidad * delta;
                if (x < 0) x = 0;
                break;
        }
        pibe.setPosition(x, y);
    }

    public void dibujar(ShapeRenderer sr) {
        if (cargado)  sr.rect(x, y, ancho, alto, Color.FOREST, Color.FOREST, Color.FOREST, Color.FOREST);
        else sr.rect(x, y, ancho, alto, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
    }

}

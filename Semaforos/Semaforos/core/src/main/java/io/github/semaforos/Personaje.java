package io.github.semaforos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class Personaje {
    float x,y;
    float tamanoX;
    float tamanoY;
    float velocidad;

    Texture textura; //AÑADIDO

    public Personaje(float x, float y, float tamanoX, float tamanoY, float velocidad, Texture textura) {
        this.x = x;
        this.y = y;
        this.tamanoX = tamanoX;
        this.tamanoY = tamanoY;
        this.velocidad = velocidad;
        this.textura = textura;
    }

    public abstract void actualizar(float delta);
    public abstract void dibujar(SpriteBatch batch);
}

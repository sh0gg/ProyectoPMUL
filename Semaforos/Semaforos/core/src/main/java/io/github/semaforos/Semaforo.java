package io.github.semaforos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Semaforo extends Personaje {

    //no tiene hitbox
    public enum Estado {ROJO,AMARILLO,VERDE}
    Estado estado = Estado.VERDE; //inicial


    public Semaforo(float x, float y, float tamanoX, float tamanoY, float velocidad, Texture textura) {
        super(x,
            y,
            tamanoX,
            tamanoY,
            velocidad,
            textura);

    }

    @Override
    public void actualizar(float delta) {
        switch (estado) {
            case VERDE:
                textura = Pantalla.juego.txtSemaforoVerde;
                break;

            case AMARILLO:
                textura = Pantalla.juego.txtSemaforoAmarillo;
                break;
            case ROJO:
                textura = Pantalla.juego.txtSemaforoRojo;
                break;
        }

    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(textura,x,y,tamanoX,tamanoY);
    }
}

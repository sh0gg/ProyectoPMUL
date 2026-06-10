package io.github.semaforos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;


public class Coche extends Personaje{

    public enum Estado {PARADO,MARCHA}
    Estado estado = Estado.MARCHA;
    Rectangle hitbox;
    public Coche(float x, float y, float tamanoX, float tamanoY, float velocidad, Texture textura) {
        super(x, y, tamanoX, tamanoY, velocidad, textura);
        hitbox = new Rectangle( x,y, tamanoX, tamanoY);
    }

    @Override
    public void actualizar(float delta) {
        //sólo van de izq a derecha
        switch (estado) {
            case PARADO:
                break;
            case MARCHA:
                //sólo van de izq a der
                x += velocidad * delta;
        }

        //límites:
        if (x<0) x =0; //izquierda
        //no tiene limites por la derecha, si se sale en rojo se pierde
        //if (x > Mundo.ANCHO - tamanoX) x = Mundo.ANCHO - tamanoX; //derecha

        hitbox.setPosition(x,y);
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(textura,x,y,tamanoX,tamanoY);
    }
}

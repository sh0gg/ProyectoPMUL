package io.github.semaforos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Piedra extends Personaje {
    Rectangle hitbox;

    public Piedra(float x, float y, float tamanoX, float tamanoY, float velocidad, Texture textura) {
        super(x, y, tamanoX, tamanoY, velocidad, textura);
        hitbox = new Rectangle(x,y,tamanoX,tamanoY);
    }

    @Override
    public void actualizar(float delta) {
        //la piedra pues pobrecita no hace nada ms que estar
        hitbox.setPosition(x,y);
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        batch.draw(textura,x,y,tamanoX,tamanoY);
    }
}

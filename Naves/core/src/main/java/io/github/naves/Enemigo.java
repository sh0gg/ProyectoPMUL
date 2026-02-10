package io.github.naves;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

    public class Enemigo {
    public Rectangle rect;
    private float velocidad;

    public Enemigo(Texture textura) {
        rect = new Rectangle();
        rect.width = textura.getWidth();
        rect.height = textura.getHeight();
        // Aparece fuera de pantalla por la derecha
        rect.x = Mundo.ANCHO + MathUtils.random(50, 200);
        // Posición Y aleatoria (entre el suelo y el techo)
        rect.y = MathUtils.random(40, Mundo.ALTO - rect.height);
        velocidad = MathUtils.random(100, 250);
    }

    public void update(float delta) {
        rect.x -= velocidad * delta;
    }

    public void render(SpriteBatch sb, Texture textura) {
        sb.draw(textura, rect.x, rect.y);
    }
}

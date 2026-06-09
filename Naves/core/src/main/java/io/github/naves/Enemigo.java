package io.github.naves;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Enemigo {

    public final Rectangle rect = new Rectangle();
    private float velocidadX;

    public Enemigo(Texture texNaveEnemiga) {
        // Ajusta tamaño (si la nave es grande)
        float w = texNaveEnemiga.getWidth() * 0.7f;
        float h = texNaveEnemiga.getHeight() * 0.7f;

        float x = Mundo.ANCHO; // entra por la derecha
        float y = MathUtils.random(Mundo.ALTO_BARRA_INFO, Mundo.ALTO - h);

        rect.set(x, y, w, h);

        velocidadX = MathUtils.random(80f, 200f);
    }

    public void update(float delta) {
        rect.x -= velocidadX * delta;
    }

    public void render(SpriteBatch sb, Texture texNaveEnemiga) {
        // espejado para que “mire” hacia la izquierda
        sb.draw(texNaveEnemiga, rect.x + rect.width, rect.y, -rect.width, rect.height);
    }
}

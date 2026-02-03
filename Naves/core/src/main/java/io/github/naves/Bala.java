package io.github.naves;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Pool;

public class Bala implements Pool.Poolable {
    private Rectangle rect;
    private float velocidad = 400;
    public boolean destruida = false;
    private Texture textura; // Referencia a la textura (no la carga ella misma)

    public Bala() {
        rect = new Rectangle();
        rect.width = 10;
        rect.height = 10;
        destruida = false;
    }

    // Método para "revivir" la bala desde la pool
    public void init(float x, float y, Texture textura) {
        rect.x = x;
        rect.y = y;
        this.textura = textura;
        destruida = false;
    }

    public void update(float delta) {
        if (destruida) return;

        rect.x += velocidad * delta;

        // Si sale de la pantalla, se marca para morir
        if (rect.x > Mundo.ANCHO) {
            destruida = true;
        }
    }

    public void render(SpriteBatch sb) {
        if (!destruida && textura != null) {
            sb.draw(textura, rect.x, rect.y, rect.width, rect.height);
        }
    }

    @Override
    public void reset() {
        rect.x = 0;
        rect.y = 0;
        destruida = false;
        textura = null; // Liberamos referencia
    }
}

package io.github.pescador;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Anzuelo {

    public enum Estado {
        PARADO, BAJANDO, SUBIENDO
    }

    public Sprite sprite;
    public float x, y;
    public float width, height;

    private final float posicionCero; // y donde “descansa” arriba
    private Estado estado = Estado.PARADO;

    private final Rectangle hitBox = new Rectangle();

    public Anzuelo(Pescador pescador) {
        sprite = new Sprite(new Texture("anzuelo.png"));
        sprite.setSize(sprite.getWidth() * 0.5f, sprite.getHeight() * 0.5f);
        sprite.setFlip(true, false);

        width = sprite.getWidth();
        height = sprite.getHeight();

        posicionCero = Mundo.ANZUELO_Y;
        y = posicionCero;

        // x se engancha en Main cuando está PARADO
        syncHitbox();
    }

    public void update(float delta) {
        switch (estado) {
            case BAJANDO:
                y -= Mundo.ANZUELO_SPEED_Y * delta;
                if (y <= 0) {
                    y = 0;
                    estado = Estado.SUBIENDO; // si toca fondo, sube
                }
                break;

            case SUBIENDO:
                y += Mundo.ANZUELO_SPEED_Y * delta;
                if (y >= posicionCero) {
                    y = posicionCero;
                    estado = Estado.PARADO;
                }
                break;

            case PARADO:
            default:
                break;
        }

        syncHitbox();
    }

    private void syncHitbox() {
        // Hitbox general del anzuelo
        hitBox.set(x, y, width, height);
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean estaParado() {
        return estado == Estado.PARADO;
    }

    public boolean estaBajando() {
        return estado == Estado.BAJANDO;
    }

    public boolean estaSubiendo() {
        return estado == Estado.SUBIENDO;
    }

    public void empezarBajada() {
        if (estado == Estado.PARADO)
            estado = Estado.BAJANDO;
    }

    public void empezarSubida() {
        if (estado == Estado.BAJANDO)
            estado = Estado.SUBIENDO;
    }

    public void dispose() {
        sprite.getTexture().dispose();
    }
}

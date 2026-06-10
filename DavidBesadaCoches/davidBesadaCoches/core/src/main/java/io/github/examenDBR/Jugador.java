package io.github.examenDBR;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Jugador {
    float x, y;
    float tam;
    Rectangle hitbox;
    int vidas = 3;

    // Para el parpadeo de invencibilidad
    float tiempoInvencible = 0f;
    boolean invencible = false;

    public Jugador() {
        tam = Mundo.getTamCasilla();
        x = 0;
        y = Mundo.MITAD_ALTO - tam / 2;
        hitbox = new Rectangle(x, y, tam, tam);
    }

    void mover(int dx, int dy) {
        // dx/dy son -1, 0 o 1 en casillas
        x = MathUtils.clamp(x + dx * tam, 0, Mundo.ANCHO - tam);
        y = MathUtils.clamp(y + dy * tam, 0, Mundo.ALTO - tam);
        hitbox.setPosition(x, y);
    }

    void update(float delta) {
        if (invencible) {
            tiempoInvencible -= delta;
            if (tiempoInvencible <= 0) invencible = false;
        }
    }

    void render(ShapeRenderer sr) {
        // Parpadeo — no dibuja en ciertos frames si es invencible
        if (invencible && ((int)(tiempoInvencible * 10) % 2 == 0)) return;

        float radio = tam / 2;
        float cx = x + radio;
        float cy = y + radio;

        // Círculo exterior (brillo)
        sr.setColor(Color.WHITE);
        sr.circle(cx, cy, radio);

        // Círculo interior (color del jugador)
        sr.setColor(Color.CYAN);
        sr.circle(cx, cy, radio * 0.7f);
    }

    void golpear() {
        if (invencible) return;
        vidas--;
        invencible = true;
        tiempoInvencible = 2f; // 2 segundos de invencibilidad
        x = 0; // vuelve al inicio
        y = Mundo.MITAD_ALTO - tam / 2;
        hitbox.setPosition(x, y);
    }

    boolean hasCruzado() {
        return x >= Mundo.ANCHO - tam;
    }

    void resetPosicion() {
        x = 0;
        y = Mundo.MITAD_ALTO - tam / 2;
        hitbox.setPosition(x, y);
    }


}

package io.github.examenDBR;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaResultado extends Pantalla {

    public PantallaResultado(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);

        batch.begin();
        if (Mundo.victoria) {
            font.draw(batch, "¡VICTORIA!", Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO + 50);
            font.draw(batch, "Tiempo: " + String.format("%.2f", Mundo.tiempoFinal) + "s", Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO + 20);
        } else {
            font.draw(batch, "GAME OVER", Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO + 50);
        }

        font.draw(batch, "Puntos: " + (Mundo.puntos - Mundo.escapados) + " DE " + Mundo.TOTAL_GLOBOS, Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO);
        font.draw(batch, "ESPACIO para volver al inicio", 20, 30);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            game.setScreen(new PantallaInicio(game));
        }

        return true;
    }
}

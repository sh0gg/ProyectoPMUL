package io.github.examenDBR;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaResultado extends Pantalla {

    public PantallaResultado(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        if (Mundo.victoria) {
            font.setColor(Color.YELLOW);
            font.draw(batch, "VICTORIA!", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 80);
        } else {
            font.setColor(Color.RED);
            font.draw(batch, "GAME OVER", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 80);
        }

        font.setColor(Color.WHITE);
        font.draw(batch, "Sombras destruidas: " + Mundo.puntos, Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 40);
        font.draw(batch, "Tiempo: " + String.format("%.2f", Mundo.tiempoFinal) + "s", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 10);
        font.draw(batch, "Record: " + String.format("%.2f", Mundo.getRecord()) + "s", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO - 20);
        font.draw(batch, "ESPACIO para volver", 20, 25);
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

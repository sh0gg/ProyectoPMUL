package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla{
    Preferences prefs = Gdx.app.getPreferences("record.pref");
    int recPuntos = prefs.getInteger("recordPuntos", 0);
    float recTiempo = prefs.getFloat("recordTiempo", 0);
    int dificultadSeleccionada = 1; // 0=fácil, 1=normal, 2=difícil
    String[] nombres = {"FACIL", "NORMAL", "DIFICIL"};

    public PantallaInicio(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);

        batch.begin();
        font.draw(batch, "GLOBOS", Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO + 50);
        font.draw(batch, "Record: " + recPuntos + " pts en " + String.format("%.2f", recTiempo) + "s", Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO);
        font.draw(batch, "ESPACIO para jugar  |  R para borrar record", 20, 30);
        for (int i = 0; i < nombres.length; i++) {
            if (i == dificultadSeleccionada) {
                font.setColor(com.badlogic.gdx.graphics.Color.YELLOW);
            } else {
                font.setColor(com.badlogic.gdx.graphics.Color.GRAY);
            }
            font.draw(batch, nombres[i], 80 + i * 180, Mundo.MITAD_ALTO - 30);
        }
        font.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        batch.end();

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            switch (dificultadSeleccionada) {
                case 0: Mundo.setFacil(); break;
                case 1: Mundo.setNormal(); break;
                case 2: Mundo.setDificil(); break;
            }
            game.setScreen(new PantallaJuego(game));
        }

        if (keycode == Input.Keys.R) {
            prefs.clear();
            prefs.flush();
            recPuntos = 0;
            recTiempo = Float.MAX_VALUE;
        }

        if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT) {
            dificultadSeleccionada = Math.min(2, dificultadSeleccionada + 1);
        }

        if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT) {
            dificultadSeleccionada = Math.max(0, dificultadSeleccionada - 1);
        }

        if  (keycode == Input.Keys.ESCAPE) {
            Gdx.app.exit();
        }
        return true;
    }
}

package cubes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla {

    float stateTime = 0f;
    float tempCambioTexturas = 0f;
    float tiempoCambioInsecto = 1f;
    Texture imagenInsecto;

    public PantallaInicio(Main game) {
        super(game);
        layout.setText(fuente, "Insectos");
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stateTime += delta;

        if (stateTime >= tempCambioTexturas) {
            tempCambioTexturas += tiempoCambioInsecto;
            imagenInsecto = game.imagenesInsectos.get(MathUtils.random(game.imagenesInsectos.size - 1));
        }

        // Barra superior
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(Color.DARK_GRAY);
        sr.rect(0, Mundo.ALTO_MUNDO, Mundo.ANCHO_MUNDO, Mundo.ALTURA_MENU_SUPERIOR);
        sr.end();

        batch.begin();

        // Título en barra
        fuente.setColor(Color.WHITE);
        fuente.draw(batch, "INSECTOS", 10, Mundo.ALTO_MUNDO + Mundo.ALTURA_MENU_SUPERIOR - 8);

        // Imagen que cambia cada segundo (en el centro, zona juego)
        if (imagenInsecto != null) {
            batch.draw(
                    imagenInsecto,
                    (Mundo.ANCHO_MUNDO - game.tamanoTextura) / 2f,
                    (Mundo.ALTO_MUNDO - game.tamanoTextura) / 2f,
                    game.tamanoTextura,
                    game.tamanoTextura);
        }

        // Instrucciones (zona juego)
        fuente.draw(batch, "Pulsa 1..9 para jugar con N insectos", 20, 120);
        fuente.draw(batch, "R: borrar records | F: salir", 20, 95);

        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.F) {
            Gdx.app.exit();
        } else if (keycode == Input.Keys.R) {
            deleteRecords();
        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            int numInsectos = keycode - Input.Keys.NUM_0;
            game.irAPantallaJuego(numInsectos);
        }
        return true;
    }

    private void deleteRecords() {
        Preferences preferences = Gdx.app.getPreferences("record.prefs");
        preferences.clear();
        preferences.flush();
    }
}

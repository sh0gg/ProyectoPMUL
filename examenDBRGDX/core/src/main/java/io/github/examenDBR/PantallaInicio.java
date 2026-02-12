package io.github.examenDBR;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.SPACE;

import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla {

    public PantallaInicio(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        batch.begin();
        font.draw(batch, "EXAMEN LibGDX", 160, 220);
        font.draw(batch, "ENTER/SPACE para empezar", 130, 180);
        font.draw(batch, "(Plantilla base: unproject, HUD, records)", 70, 150);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == ENTER || keycode == SPACE) {
            game.setScreen(new PantallaJuego(game));
            return true;
        }
        return false;
    }
}

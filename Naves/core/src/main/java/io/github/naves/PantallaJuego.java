package io.github.naves;

import static com.badlogic.gdx.Input.Keys.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends Pantalla implements InputProcessor {

    private final Mundo mundo;

    private boolean pulsandoArriba = false;
    private boolean pulsandoAbajo = false;

    public PantallaJuego(MainNaves game) {
        super(game);
        this.mundo = game.mundo;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Delegar input al mundo
        mundo.setMoviendoArriba(pulsandoArriba);
        mundo.setMoviendoAbajo(pulsandoAbajo);

        // Update
        mundo.actualiza(delta);

        // Draw
        game.sb.begin();
        mundo.dibuja(game.sb, game.sr, game.font);
        game.sb.end();

        // Fin de partida
        if (mundo.gameOver()) {
            mundo.guardarRecordSiMejora();
            game.setScreen(new PantallaFin(game));
        }
    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == UP || keycode == W) {
            pulsandoArriba = true;
            pulsandoAbajo = false;
            return true;
        }

        if (keycode == DOWN || keycode == S) {
            pulsandoAbajo = true;
            pulsandoArriba = false;
            return true;
        }

        if (keycode == SPACE) {
            mundo.nuevaBala();
            return true;
        }

        if (keycode == H) {
            mundo.activarHiperespacio();
            return true;
        }

        if (keycode == ESCAPE) {
            game.setScreen(new PantallaInicio(game));
            return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == UP || keycode == W) {
            pulsandoArriba = false;
            return true;
        }
        if (keycode == DOWN || keycode == S) {
            pulsandoAbajo = false;
            return true;
        }
        return false;
    }

    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int x, int y, int p, int b) { return false; }
    @Override public boolean touchUp(int x, int y, int p, int b) { return false; }
    @Override public boolean touchCancelled(int x, int y, int p, int b) { return false; }
    @Override public boolean touchDragged(int x, int y, int p) { return false; }
    @Override public boolean mouseMoved(int x, int y) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}

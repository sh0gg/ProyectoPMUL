package io.github.naves;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.SPACE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaFin extends Pantalla implements InputProcessor {

    public PantallaFin(MainNaves game) {
        super(game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        int record = game.mundo.getRecord();

        game.sb.begin();

        // ok.png centrado
        float w = game.texOk.getWidth();
        float h = game.texOk.getHeight();
        float x = (Mundo.ANCHO - w) / 2f;
        float y = (Mundo.ALTO - h) / 2f + 30;

        game.sb.draw(game.texOk, x, y);

        game.font.draw(game.sb, "FIN DEL JUEGO", 185, 285);
        game.font.draw(game.sb, "Capturas: " + game.mundo.capturas, 185, 245);
        game.font.draw(game.sb, "Record: " + record, 185, 225);

        game.font.draw(game.sb, "ENTER/SPACE para reiniciar", 140, 80);
        game.font.draw(game.sb, "ESC para volver al menu", 160, 60);

        game.sb.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == ENTER || keycode == SPACE) {
            game.mundo.reset();
            game.setScreen(new PantallaJuego(game));
            return true;
        }
        if (keycode == ESCAPE) {
            game.setScreen(new PantallaInicio(game));
            return true;
        }
        return false;
    }

    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int x, int y, int p, int b) { return false; }
    @Override public boolean touchUp(int x, int y, int p, int b) { return false; }
    @Override public boolean touchCancelled(int x, int y, int p, int b) { return false; }
    @Override public boolean touchDragged(int x, int y, int p) { return false; }
    @Override public boolean mouseMoved(int x, int y) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
}

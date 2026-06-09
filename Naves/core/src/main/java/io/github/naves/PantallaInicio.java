package io.github.naves;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.SPACE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla implements InputProcessor {

    public PantallaInicio(MainNaves game) {
        super(game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        game.sb.begin();

        // jugar.png centrado
        float w = game.texJugar.getWidth();
        float h = game.texJugar.getHeight();
        float x = (Mundo.ANCHO - w) / 2f;
        float y = (Mundo.ALTO - h) / 2f;

        game.sb.draw(game.texJugar, x, y);

        game.font.draw(game.sb, "ENTER/SPACE para jugar", 145, 60);
        game.font.draw(game.sb, "UP/DOWN mover | SPACE disparar | H hiperespacio", 75, 40);

        game.sb.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == ENTER || keycode == SPACE) {
            game.mundo.reset();
            game.setScreen(new PantallaJuego(game));
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

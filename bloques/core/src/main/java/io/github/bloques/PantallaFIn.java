package io.github.bloques;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.ESCAPE;
import static com.badlogic.gdx.Input.Keys.SPACE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaFin extends InputAdapter implements Screen {

    private final MainGame game;

    public PantallaFin(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.08f, 0.08f, 0.08f, 1f);

        int record = game.mundo.getCurrentRecord();

        game.batch.setProjectionMatrix(game.camara.combined);
        game.batch.begin();

        game.font.draw(game.batch, "GAME OVER", 170, 240);
        game.font.draw(game.batch, "Modo: " + (game.mundo.isColorMode ? "COLORES" : "NUMEROS"), 20, 200);
        game.font.draw(game.batch, "Tiempo: " + (int) game.mundo.lastScoreTime + "s", 20, 180);
        game.font.draw(game.batch, "Record modo: " + record + "s", 20, 160);
        game.font.draw(game.batch, "Motivo: " + game.mundo.gameOverReason, 20, 130);

        game.font.draw(game.batch, "ENTER/SPACE: reiniciar", 20, 70);
        game.font.draw(game.batch, "ESC: menu", 20, 50);

        game.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == ENTER || keycode == SPACE) {
            // Mantener modo (si estabas en colores, sigues)
            game.mundo.reset(true);
            game.setScreen(new GameScreen(game));
            return true;
        }
        if (keycode == ESCAPE) {
            game.setScreen(new PantallaInicio(game));
            return true;
        }
        return false;
    }

    @Override
    public void resize(int width, int height) {
        game.actualizarCamara();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }
}

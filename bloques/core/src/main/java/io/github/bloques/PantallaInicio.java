package io.github.bloques;

import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.SPACE;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends InputAdapter implements Screen {

    private final MainGame game;

    public PantallaInicio(MainGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.12f, 0.12f, 0.12f, 1f);

        game.batch.setProjectionMatrix(game.camara.combined);
        game.batch.begin();

        game.font.draw(game.batch, "BLOCKS", 200, 240);
        game.font.draw(game.batch, "ENTER / SPACE para jugar", 120, 200);

        game.font.draw(game.batch, "Reglas:", 20, 160);
        game.font.draw(game.batch, "- Si un bloque toca arriba: pierdes", 20, 140);
        game.font.draw(game.batch, "- Toca 2 seguidos con mismo numero: se eliminan", 20, 120);
        game.font.draw(game.batch, "- Si el segundo es distinto: pierdes", 20, 100);
        game.font.draw(game.batch, "- Si tocas vacio: pierdes", 20, 80);

        game.font.draw(game.batch, "Huevo de pascua:", 20, 50);
        game.font.draw(game.batch, "Primer toque < 1s en esquina sup derecha => modo COLORES", 20, 30);

        game.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == ENTER || keycode == SPACE) {
            game.mundo.reset(false);
            game.setScreen(new GameScreen(game));
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

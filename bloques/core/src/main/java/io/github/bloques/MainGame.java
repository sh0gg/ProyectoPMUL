package io.github.bloques;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;

    public OrthographicCamera camara;
    public Viewport viewport;

    public Mundo mundo;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        camara = new OrthographicCamera();
        viewport = new FitViewport(Mundo.ANCHO, Mundo.ALTO, camara);
        viewport.apply(true);
        camara.position.set(Mundo.ANCHO / 2f, Mundo.ALTO / 2f, 0);
        camara.update();

        // Mundo del juego
        mundo = new Mundo();

        setScreen(new PantallaInicio(this));
    }

    public void actualizarCamara() {
        viewport.update(com.badlogic.gdx.Gdx.graphics.getWidth(), com.badlogic.gdx.Gdx.graphics.getHeight(), true);
        camara.update();
    }

    @Override
    public void resize(int width, int height) {
        actualizarCamara();
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }
}

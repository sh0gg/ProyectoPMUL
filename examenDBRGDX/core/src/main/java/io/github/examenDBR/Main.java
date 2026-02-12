package io.github.examenDBR;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends Game {

    public SpriteBatch batch;
    public ShapeRenderer sr;
    public BitmapFont font;
    public GlyphLayout layout;

    public OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        sr = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO + Mundo.TOP_BAR);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        // En examen puedes empezar directo en juego o en inicio
        setScreen(new PantallaInicio(this));
        // setScreen(new PantallaJuego(this)); // si vas a saco
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO + Mundo.TOP_BAR);
        camera.update();

        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        sr.dispose();
        font.dispose();
    }
}


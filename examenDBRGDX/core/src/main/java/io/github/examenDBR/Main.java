package io.github.examenDBR;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends Game {

    public SpriteBatch batch;
    public ShapeRenderer sr;
    public BitmapFont font;
    public OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        sr = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO + Mundo.TOP_BAR);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        setScreen(new PantallaInicio(this));
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        super.dispose();

        if (font != null) font.dispose();
        if (sr != null) sr.dispose();
        if (batch != null) batch.dispose();
    }
}


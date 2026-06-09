package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Pantalla extends InputAdapter implements Screen {

    protected final Main game;

    public OrthographicCamera camera;
    public SpriteBatch batch;
    public ShapeRenderer sr;
    public BitmapFont font;
    public GlyphLayout layout;

    public Pantalla(Main game) {
        this.game = game;

        this.camera = game.camera;
        this.batch = game.batch;
        this.sr = game.sr;
        this.font = game.font;
        this.layout = game.layout;

        // Por si alguien cambia matrices en otra pantalla
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}

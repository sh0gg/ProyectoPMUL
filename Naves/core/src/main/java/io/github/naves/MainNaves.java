package io.github.naves;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class MainNaves extends Game {

    public SpriteBatch sb;
    public ShapeRenderer sr;
    public BitmapFont font;

    public OrthographicCamera camara;

    public Mundo mundo;

    // UI
    public Texture texJugar; // jugar.png
    public Texture texOk;    // ok.png

    @Override
    public void create() {
        sb = new SpriteBatch();
        sr = new ShapeRenderer();
        font = new BitmapFont();

        camara = new OrthographicCamera();
        camara.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO);
        camara.update();

        sb.setProjectionMatrix(camara.combined);
        sr.setProjectionMatrix(camara.combined);

        // Mundo (cerebro)
        mundo = new Mundo();

        // UI
        texJugar = new Texture("jugar.png");
        texOk = new Texture("ok.png");

        setScreen(new PantallaInicio(this));
    }

    @Override
    public void resize(int width, int height) {
        // Patrón profe/apuntes: reajustar cámara y matrices
        camara.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO);
        camara.update();

        sb.setProjectionMatrix(camara.combined);
        sr.setProjectionMatrix(camara.combined);

        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();

        if (mundo != null) mundo.dispose();

        if (texJugar != null) texJugar.dispose();
        if (texOk != null) texOk.dispose();

        if (font != null) font.dispose();
        if (sr != null) sr.dispose();
        if (sb != null) sb.dispose();
    }
}

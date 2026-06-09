package cubes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public abstract class Pantalla extends InputAdapter implements Screen {
    protected Main game;
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public ShapeRenderer sr;
    public BitmapFont fuente;
    public GlyphLayout layout;

    // Centro vertical de la barra superior (para textos centrados si lo necesitas)
    public static float topBarY = Mundo.ALTO_MUNDO + Mundo.ALTURA_MENU_SUPERIOR / 2f;

    public Pantalla(Main game) {
        this.game = game;

        batch = game.spriteBatch;
        sr = game.shapeRenderer;
        fuente = game.font;
        layout = game.layout;

        camera = new OrthographicCamera();
        // Área de juego: 0..ALTO_MUNDO y barra arriba: ALTO_MUNDO..ALTO_MUNDO+ALTURA_MENU_SUPERIOR
        camera.setToOrtho(false, Mundo.ANCHO_MUNDO, Mundo.ALTO_MUNDO + Mundo.ALTURA_MENU_SUPERIOR);

        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
    }

    @Override
    public abstract void render(float delta);

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }
}

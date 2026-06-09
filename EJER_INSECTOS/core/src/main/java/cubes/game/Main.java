package cubes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Main extends Game {

    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public GlyphLayout layout;

    public Array<Texture> imagenesInsectos = new Array<>();
    public Array<Vector2> puntos = new Array<>();

    public float tamanoTextura = 64f;
    public float mitadTamanoTextura = tamanoTextura / 2f;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();

        // Cargar 9 texturas insecto_1.png ... insecto_9.png
        for (int i = 1; i <= 9; i++) {
            imagenesInsectos.add(new Texture("insecto_" + i + ".png"));
        }

        setScreen(new PantallaInicio(this));
    }

    public void irAPantallaInicio() {
        setScreen(new PantallaInicio(this));
    }

    public void irAPantallaJuego(int n) {
        setScreen(new PantallaJuego(this, n));
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        for (Texture t : imagenesInsectos)
            t.dispose();
    }
}

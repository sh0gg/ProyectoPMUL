package cubes.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Main extends Game {
    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public GlyphLayout layout;

    public final float textureSize = 128f;
    public final float halfTextureSize = textureSize / 2;
    public Array<Texture> insectsTextures = new Array<Texture>();
    public Array<Vector2> points = new Array<Vector2>();

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
        font = new BitmapFont();
        layout = new GlyphLayout();

        for (int i = 1; i < 10; i++) {
            insectsTextures.add(new Texture(String.format("insecto_%d.png",i)));
        }

        irAPantallaInicio();
    }

    public void irAPantallaInicio() {
        setScreen(new PantallaInicio(this));
    }

    public void irAPantallaJuego(int numInsects) {
        setScreen(new PantallaJuego(this, numInsects));
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        shapeRenderer.dispose();
        font.dispose();
        if (getScreen() != null) getScreen().dispose();
    }

}

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
    public SpriteBatch spriteBatch;
    public ShapeRenderer shapeRenderer;
    public BitmapFont font;
    public GlyphLayout layout;
    public static float topBarY = World.gameHeight + World.topBarHeight / 2;

    public Pantalla(Main game) {
        this.game = game;
        spriteBatch = game.spriteBatch;
        shapeRenderer = game.shapeRenderer;
        font = game.font;
        layout = game.layout;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, World.gameWidth, World.gameHeight +World.topBarHeight);

        // Apply camera
        spriteBatch.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);

    }

    @Override
    public abstract void render(float delta);

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }
}

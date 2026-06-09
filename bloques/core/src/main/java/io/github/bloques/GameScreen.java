package io.github.bloques;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends InputAdapter implements Screen {

    private final MainGame game;
    private final Mundo mundo;

    private ShapeRenderer shape;

    // Para unproject
    private final Vector3 tmp = new Vector3();

    public GameScreen(MainGame game) {
        this.game = game;
        this.mundo = game.mundo;
    }

    @Override
    public void show() {
        shape = new ShapeRenderer();
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1f);

        // Update mundo
        mundo.update(delta);

        // Si terminó -> pantalla fin
        if (mundo.isGameOver()) {
            game.setScreen(new PantallaFin(game));
            return;
        }

        // Dibujo
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.camara.combined);
        shape.setProjectionMatrix(game.camara.combined);

        // 1) Bloques (ShapeRenderer)
        shape.begin(ShapeRenderer.ShapeType.Filled);

        float bs = mundo.getBlockSize();

        for (Block b : mundo.blocks) {
            float x = mundo.getVisualX(b);
            float y = mundo.getVisualY(b);

            // Color: si es modo números, gris; si colores, paleta
            if (!mundo.isColorMode) {
                shape.setColor(0.75f, 0.75f, 0.75f, 1f);
            } else {
                shape.setColor(colorFromValue(b.value));
            }

            shape.rect(x, y, bs, bs);
        }

        // selección resaltada
        Block sel = mundo.getSelectedBlock();
        if (sel != null) {
            shape.setColor(Color.YELLOW);
            shape.rect(mundo.getVisualX(sel), mundo.getVisualY(sel), bs, bs);
        }

        shape.end();

        // 2) Barra info (línea)
        shape.begin(ShapeRenderer.ShapeType.Line);
        shape.setColor(Color.WHITE);
        shape.line(0, Mundo.ALTO_BARRA_INFO, Mundo.ANCHO, Mundo.ALTO_BARRA_INFO);
        shape.end();

        // 3) HUD (SpriteBatch)
        game.batch.begin();
        game.font.draw(game.batch, "Time: " + (int) mundo.stateTime, 10, 25);
        game.font.draw(game.batch, "Mode: " + (mundo.isColorMode ? "COLORS" : "NUMBERS"), 140, 25);
        game.font.draw(game.batch, "Rec N: " + mundo.getRecordNumbers(), 260, 25);
        game.font.draw(game.batch, "Rec C: " + mundo.getRecordColors(), 360, 25);
        game.batch.end();
    }

    private Color colorFromValue(int v) {
        // 1..5
        switch (v) {
            case 1:
                return Color.RED;
            case 2:
                return Color.GREEN;
            case 3:
                return Color.BLUE;
            case 4:
                return Color.ORANGE;
            default:
                return Color.PURPLE;
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // Convertir screen -> world (apuntes/profe)
        tmp.set(screenX, screenY, 0);
        game.camara.unproject(tmp);

        mundo.onTouch(
                tmp.x, tmp.y,
                screenX, screenY,
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        return true;
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
        if (shape != null)
            shape.dispose();
    }
}

package io.github.bloques;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen extends InputAdapter implements Screen {
    // Configuración del tablero
    private static final int COLS = 6;
    final MainGame game;
    private float blockSize;

    // Estado del juego
    private Array<Block> blocks;
    private float stateTime = 0;
    private float moveSpeed = 15f; // Píxeles por segundo
    private float verticalOffset = 0;

    private boolean gameOver = false;
    private boolean isColorMode = false;
    private boolean firstTouchDone = false;

    private Block selectedBlock = null;

    private ShapeRenderer shapeRenderer;

    private Rectangle restartBtnBounds;

    public GameScreen(MainGame game) {
        this.game = game;
        this.blocks = new Array<>();
        this.shapeRenderer = new ShapeRenderer();

        blockSize = Gdx.graphics.getWidth() / (float) COLS;

        // Inicializar filas
        for (int i = 0; i < 2; i++) spawnRow(i);

        restartBtnBounds = new Rectangle(0, 0, 200, 50);

        Gdx.input.setInputProcessor(this);
    }

    private void spawnRow(int yIndex) {
        for (int x = 0; x < COLS; x++) {
            int value = MathUtils.random(1, 5);
            blocks.add(new Block(x, yIndex, value));
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.2f, 0.2f, 0.2f, 1);

        if (gameOver) {
            drawGameOver();
            if (Gdx.input.justTouched()) game.setScreen(new GameScreen(game));
            return;
        }

        update(delta);
        draw();
    }

    private void update(float delta) {
        stateTime += delta;
        verticalOffset += moveSpeed * delta;

        if (verticalOffset >= blockSize) {
            verticalOffset -= blockSize;
            moveBlocksUp();
            addNewRowBottom();
        }

        for (Block b : blocks) {
            if (getVisualY(b) + blockSize >= Gdx.graphics.getHeight()) {
                triggerGameOver();
            }
        }
    }

    private void moveBlocksUp() {
        for (Block b : blocks) b.gridY++;
    }

    private void addNewRowBottom() {
        for (int x = 0; x < COLS; x++) blocks.add(new Block(x, 0, MathUtils.random(1, 5)));
    }

    private float getVisualY(Block b) {
        return (b.gridY * blockSize) + verticalOffset;
    }

    private void draw() {
        // Dibujamos bloques
        if (!isColorMode) {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.WHITE);
            for (Block b : blocks) {
                shapeRenderer.rect(b.gridX * blockSize, getVisualY(b), blockSize, blockSize);
            }
            shapeRenderer.end();
        } else {
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            for (Block b : blocks) {
                shapeRenderer.setColor(getColorForValue(b.value));
                shapeRenderer.rect(b.gridX * blockSize, getVisualY(b), blockSize, blockSize);
            }
            shapeRenderer.end();
        }

        game.batch.begin();
        // Números
        if (!isColorMode) {
            for (Block b : blocks) {
                game.font.draw(game.batch, String.valueOf(b.value),
                    b.gridX * blockSize + blockSize / 2.5f,
                    getVisualY(b) + blockSize / 1.5f);
            }
        }
        // UI
        game.font.setColor(Color.YELLOW);
        game.font.draw(game.batch, "Time: " + (int) stateTime, 20, Gdx.graphics.getHeight() - 10);
        game.font.setColor(Color.WHITE);
        game.batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        float fixedY = Gdx.graphics.getHeight() - screenY;

        // --- 1. LÓGICA DE GAME OVER (BOTÓN RESTART) ---
        if (gameOver) {
            // Solo reiniciamos si el toque cae DENTRO del rectángulo del botón
            if (restartBtnBounds.contains(screenX, fixedY)) {
                game.setScreen(new GameScreen(game));
            }
            return true; // Consumimos el evento para no procesar nada más
        }

        // --- 2. EASTER EGG (Mantenido) ---
        if (!firstTouchDone) {
            firstTouchDone = true;
            if (stateTime <= 1.0f && screenX > Gdx.graphics.getWidth() * 0.9f && screenY < Gdx.graphics.getHeight() * 0.1f) {
                isColorMode = true;
                return true;
            }
        }

        // --- 3. LÓGICA DEL JUEGO ---
        Block touchedBlock = getBlockAt(screenX, fixedY);

        // Si tocamos el fondo vacío -> PERDER
        if (touchedBlock == null) {
            triggerGameOver();
            return true;
        }

        if (selectedBlock == null) {
            // Primer toque: seleccionamos el bloque
            selectedBlock = touchedBlock;
        } else {
            // Segundo toque

            // NUEVA REGLA: Si tocas EL MISMO bloque que ya tenías seleccionado -> PERDER
            if (selectedBlock == touchedBlock) {
                triggerGameOver();
                return true;
            }


            if (selectedBlock.value == touchedBlock.value) {
                // Iguales y consecutivos -> ELIMINAR
                blocks.removeValue(selectedBlock, true);
                blocks.removeValue(touchedBlock, true);
                selectedBlock = null;
            } else {
                // Consecutivos pero distinto valor -> PERDER
                triggerGameOver();
            }
        }
        return true;
    }

    private Block getBlockAt(float x, float y) {
        for (Block b : blocks) {
            float bY = getVisualY(b);
            if (x >= b.gridX * blockSize && x < (b.gridX + 1) * blockSize && y >= bY && y < bY + blockSize) {
                return b;
            }
        }
        return null;
    }

    private boolean areConsecutive(Block b1, Block b2) {
        float dist = Vector3.dst(b1.gridX * blockSize, getVisualY(b1), 0, b2.gridX * blockSize, getVisualY(b2), 0);
        return dist <= blockSize * 1.1f;
    }

    private void triggerGameOver() {
        gameOver = true;
        String key = isColorMode ? "record_colors" : "record_numbers";
        int currentRecord = game.prefs.getInteger(key, 0);
        if (stateTime > currentRecord) {
            game.prefs.putInteger(key, (int) stateTime);
            game.prefs.flush();
        }
    }

    private void drawGameOver() {
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        // Dimensiones y posición del botón
        float btnWidth = 200;
        float btnHeight = 50;
        float btnX = w / 2 - btnWidth / 2;
        float btnY = h / 2 - 100;

        // Actualizamos el rectángulo invisible para detectar el clic en touchDown
        restartBtnBounds.set(btnX, btnY, btnWidth, btnHeight);

        // 1. Dibujar la caja del botón (ShapeRenderer)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(btnX, btnY, btnWidth, btnHeight);
        shapeRenderer.end();

        // 2. Dibujar Textos
        game.batch.begin();

        // Texto GAME OVER y Récord
        game.font.setColor(Color.WHITE);
        game.font.draw(game.batch, "GAME OVER", w / 2f - 40, h / 2f + 50);

        String key = isColorMode ? "record_colors" : "record_numbers";
        game.font.draw(game.batch, "Record: " + game.prefs.getInteger(key, 0),
            w / 2f - 40, h / 2f);

        // Texto dentro del botón (ajustado para centrarlo visualmente)
        game.font.draw(game.batch, "RESTART", btnX + 60, btnY + 35);

        game.batch.end();
    }

    private Color getColorForValue(int val) {
        switch (val) {
            case 1:
                return Color.RED;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.GREEN;
            case 4:
                return Color.YELLOW;
            case 5:
                return Color.ORANGE;
            default:
                return Color.GRAY;
        }
    }

    @Override
    public void show() {
    }

    @Override
    public void resize(int width, int height) {
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
        shapeRenderer.dispose();
    }
}

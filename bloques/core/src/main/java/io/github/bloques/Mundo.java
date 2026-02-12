package io.github.bloques;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Mundo {

    // ====== Mundo lógico (apuntes/profe) ======
    public static final float ANCHO = 480f;
    public static final float ALTO = 320f;

    public static final float ALTO_BARRA_INFO = 40f;

    public static final int COLS = 6;
    public static final int FILAS_INICIALES = 2;

    // Velocidad de desplazamiento (unidades de mundo / segundo)
    public float moveSpeed = 45f;

    // ====== Estado ======
    public final Array<Block> blocks = new Array<>();
    public float stateTime = 0f;

    private float verticalOffset = 0f; // cuánto “ha subido” la fila actual
    private float blockSize = ANCHO / COLS;

    private boolean gameOver = false;
    public String gameOverReason = "";

    // Modo números / colores
    public boolean isColorMode = false;

    // Easter egg (primer toque)
    private boolean firstTouchDone = false;

    // Selección (dos toques consecutivos)
    private Block selectedBlock = null;

    // Records por modo
    private final Preferences prefs;
    private static final String PREFS_NAME = "BlockGameRecordsV3";
    private static final String KEY_REC_NUM = "record_numbers";
    private static final String KEY_REC_COL = "record_colors";

    // Para mostrar récord actual en fin
    public float lastScoreTime = 0f;

    public Mundo() {
        prefs = Gdx.app.getPreferences(PREFS_NAME);
        reset(false);
    }

    public void reset(boolean keepMode) {
        stateTime = 0f;
        verticalOffset = 0f;

        gameOver = false;
        gameOverReason = "";

        selectedBlock = null;
        firstTouchDone = false;

        if (!keepMode)
            isColorMode = false;

        blocks.clear();
        for (int i = 0; i < FILAS_INICIALES; i++)
            spawnRow(i);
    }

    // ====== Update principal ======
    public void update(float delta) {
        if (gameOver)
            return;

        stateTime += delta;
        verticalOffset += moveSpeed * delta;

        if (verticalOffset >= blockSize) {
            verticalOffset -= blockSize;
            moveBlocksUp();
            addNewRowBottom();
        }

        // Regla: si un bloque toca la parte superior => fin
        for (Block b : blocks) {
            float vy = getVisualY(b);
            if (vy + blockSize >= ALTO) {
                triggerGameOver("Un bloque tocó la parte superior");
                return;
            }
        }
    }

    // ====== Input: toque ======
    // worldX/worldY -> coordenadas de mundo (ya unproject)
    // screenX/screenY + screenW/screenH -> para el huevo de pascua (zona sup dcha)
    public void onTouch(float worldX, float worldY, int screenX, int screenY, int screenW, int screenH) {
        if (gameOver)
            return;

        // Easter egg: primer toque en el primer segundo, esquina superior derecha
        if (!firstTouchDone) {
            if (stateTime <= 1.0f &&
                    screenX > screenW * 0.90f &&
                    screenY < screenH * 0.10f) {

                isColorMode = true;
            }
            firstTouchDone = true;
        }

        Block touched = getBlockAtWorld(worldX, worldY);

        // Regla: tocar vacío => fin
        if (touched == null) {
            triggerGameOver("Tocaste una posición vacía");
            return;
        }

        // Primer bloque
        if (selectedBlock == null) {
            selectedBlock = touched;
            return;
        }

        // Si toca el mismo bloque de nuevo, lo ignoramos (no penaliza)
        if (selectedBlock == touched) {
            return;
        }

        // Segundo toque: si coincide valor/color => eliminamos ambos
        if (selectedBlock.value == touched.value) {
            blocks.removeValue(selectedBlock, true);
            blocks.removeValue(touched, true);
            selectedBlock = null;
            return;
        }

        // Si no coincide => fin
        triggerGameOver("Seleccionaste dos bloques distintos");
    }

    // ====== Utilidades tablero ======

    private void spawnRow(int yIndex) {
        for (int x = 0; x < COLS; x++) {
            int value = MathUtils.random(1, 5); // 1..5
            blocks.add(new Block(x, yIndex, value));
        }
    }

    private void moveBlocksUp() {
        for (Block b : blocks) {
            b.gridY += 1;
        }
    }

    private void addNewRowBottom() {
        // Recoloca filas si crecen demasiado (mantener números pequeños)
        int minY = Integer.MAX_VALUE;
        for (Block b : blocks)
            minY = Math.min(minY, b.gridY);

        // Queremos que exista una fila “0” como base lógica
        if (minY > 0) {
            for (Block b : blocks)
                b.gridY -= minY;
        }

        // Añadimos nueva fila en y=0
        for (int x = 0; x < COLS; x++) {
            int value = MathUtils.random(1, 5);
            blocks.add(new Block(x, 0, value));
        }
    }

    public float getBlockSize() {
        return blockSize;
    }

    public float getVerticalOffset() {
        return verticalOffset;
    }

    public Block getSelectedBlock() {
        return selectedBlock;
    }

    // y visual: filas “suben” porque verticalOffset crece
    public float getVisualY(Block b) {
        return b.gridY * blockSize + verticalOffset;
    }

    public float getVisualX(Block b) {
        return b.gridX * blockSize;
    }

    // Encuentra el bloque en la posición tocada (coordenadas de mundo)
    private Block getBlockAtWorld(float worldX, float worldY) {
        // Fuera del tablero o dentro de la barra inferior
        if (worldX < 0 || worldX >= ANCHO)
            return null;
        if (worldY < ALTO_BARRA_INFO || worldY >= ALTO)
            return null;

        float bx, by;
        for (Block b : blocks) {
            bx = getVisualX(b);
            by = getVisualY(b);

            if (worldX >= bx && worldX < bx + blockSize &&
                    worldY >= by && worldY < by + blockSize) {
                return b;
            }
        }
        return null;
    }

    // ====== Game over + records ======

    public boolean isGameOver() {
        return gameOver;
    }

    public int getRecordNumbers() {
        return prefs.getInteger(KEY_REC_NUM, 0);
    }

    public int getRecordColors() {
        return prefs.getInteger(KEY_REC_COL, 0);
    }

    public int getCurrentRecord() {
        return isColorMode ? getRecordColors() : getRecordNumbers();
    }

    private void triggerGameOver(String reason) {
        gameOver = true;
        gameOverReason = reason;
        lastScoreTime = stateTime;

        int score = (int) lastScoreTime;

        if (isColorMode) {
            int rec = getRecordColors();
            if (score > rec) {
                prefs.putInteger(KEY_REC_COL, score);
                prefs.flush();
            }
        } else {
            int rec = getRecordNumbers();
            if (score > rec) {
                prefs.putInteger(KEY_REC_NUM, score);
                prefs.flush();
            }
        }
    }
}

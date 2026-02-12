package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends Pantalla {

    private float stateTime = 0f;

    // Records (si no los usas, puedes borrar este bloque)
    private final Preferences prefs = Gdx.app.getPreferences("record.prefs");
    private float record = 0f;
    private boolean gameOver = false;
    private String motivo = "";

    // Vector reutilizable
    private final Vector3 v3 = new Vector3();
    private final Vector2 touch = new Vector2();

    public PantallaJuego(Main game) {
        super(game);
        record = prefs.getFloat("record", 0f);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.12f, 0.12f, 0.12f, 1f);

        if (!gameOver) {
            stateTime += delta;
            update(delta);
        }

        // HUD + sprites
        batch.begin();
        drawHud();
        drawSprites();
        batch.end();

        // Shapes (debug, balas circulares, líneas HUD)
        drawShapes();
    }

    private void update(float delta) {
        // TODO: aquí metes tu lógica del ejercicio:
        // - movimiento con delta
        // - spawn por timer
        // - colisiones
        // - condiciones de fin => triggerGameOver("...");
    }

    private void drawHud() {
        // Barra superior
        if (Mundo.TOP_BAR > 0) {
            // Texto en la barra (zona y > Mundo.ALTO)
            font.setColor(Color.WHITE);
            font.draw(batch, "Time: " + String.format("%.2f", stateTime), 10, Mundo.ALTO + Mundo.TOP_BAR - 8);
            font.draw(batch, "Record: " + String.format("%.2f", record), 170, Mundo.ALTO + Mundo.TOP_BAR - 8);

            if (gameOver) {
                font.draw(batch, "GAME OVER: " + motivo, 10, Mundo.ALTO + 18);
                font.draw(batch, "TOCA para reiniciar", 10, Mundo.ALTO + 6);
            }
        } else {
            // HUD sin barra
            font.setColor(Color.WHITE);
            font.draw(batch, "Time: " + String.format("%.2f", stateTime), 10, 20);
        }
    }

    private void drawSprites() {
        // TODO: batch.draw(textura, x,y,w,h);
    }

    private void drawShapes() {
        // Separador HUD (línea en y = Mundo.ALTO)
        if (Mundo.TOP_BAR > 0) {
            sr.begin(com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Filled);
            sr.setColor(Color.WHITE);
            sr.rect(0, Mundo.ALTO, Mundo.ANCHO, 1);
            sr.end();
        }

        // TODO: debug rects / círculos balas:
        // sr.begin(Filled); sr.circle(...); sr.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // Screen -> World (la receta)
        v3.set(screenX, screenY, 0);
        camera.unproject(v3);
        touch.set(v3.x, v3.y);

        if (gameOver) {
            reset();
            return true;
        }

        // TODO: aquí metes input del ejercicio:
        // - comprobar si tocó sprite: rect.contains(touch)
        // - selección de bloques
        // - disparo
        // - etc.

        return true;
    }

    private void triggerGameOver(String motivo) {
        this.gameOver = true;
        this.motivo = motivo;

        // Record (si es “más tiempo mejor”)
        if (stateTime > record) {
            record = stateTime;
            prefs.putFloat("record", record);
            prefs.flush();
        }
    }

    private void reset() {
        stateTime = 0f;
        gameOver = false;
        motivo = "";
        record = prefs.getFloat("record", 0f);

        // TODO: reset de arrays/entidades/spawn timers...
    }
}

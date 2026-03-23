package io.github.examenDBR;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class PantallaJuego extends Pantalla {

    private float stateTime = 0f;

    // Records (si no los usas, puedes borrar este bloque)
//    private final Preferences prefs = Gdx.app.getPreferences("record.prefs");
//    private float record = 0f;
    private boolean gameOver = false;
    private String motivo = "";

    int vehiculosEscapados;

    Array<Vehiculo> vehiculos = new Array<>();

    private float tiempoRestante;

    // Vector reutilizable
    Vector3 v3 = new Vector3(screenX, screenY, 0);
    private final Vector2 touch = new Vector2();

    public PantallaJuego(Main game) {
        super(game);
        crearVehiculos();
        vehiculosEscapados = 0;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.12f, 0.12f, 0.12f, 1f);

        if (!gameOver) {
            stateTime += delta;
            update(delta);

            tiempoRestante -= delta;
            if (tiempoRestante <= 0) {
                tiempoRestante = 0;
                gameOver = true;
            }
        }

        // HUD + sprites
        batch.begin();
        drawHud();
        drawSprites();
        batch.end();
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
            font.draw(batch, "Tiempo: " + String.format("%.2f", tiempoRestante), 10, Mundo.ALTO + Mundo.TOP_BAR - 8);
            //font.draw(batch, "Record: " + String.format("%.2f", record), 170, Mundo.ALTO + Mundo.TOP_BAR - 8);
            font.draw(batch, "Vehiculos: " + String.format("%.2f", vehiculosEscapados), 30, Mundo.ALTO + Mundo.TOP_BAR - 8);

            if (gameOver) {
                font.draw(batch, "GAME OVER: " + motivo, 10, Mundo.ALTO + 18);
                font.draw(batch, "TOCA para reiniciar", 10, Mundo.ALTO + 6);
            }
        } else {
            // HUD sin barra
            font.setColor(Color.BLACK);
            font.draw(batch, "Time: " + String.format("%.2f", stateTime), 10, 20);
        }
    }

    private void drawSprites() {
        // TODO: batch.draw(textura, x,y,w,h);
        batch.begin();

        for (Vehiculo v : vehiculos) {
            v.draw(batch);
        }

        batch.end();

    }

    private void crearVehiculos() {
        vehiculos.clear();
        Random rnd = new Random();
        @SuppressWarnings("NewApi") int nVehiculos = rnd.nextInt(1,20);
        for (int i = 0; i > nVehiculos; i++) {
            vehiculos.add(Vehiculo.crearAleatorio());
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        v3.set(screenX, screenY, 0);
        camera.unproject(v3);
        touch.set(v3.x, v3.y);

        if (gameOver) {
            reset();
            return true;
        }

        for (Vehiculo v : vehiculos) {
            if (v.esTocado(v3))
        }

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

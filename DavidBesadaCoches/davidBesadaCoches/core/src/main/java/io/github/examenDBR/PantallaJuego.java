package io.github.examenDBR;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends Pantalla {

    float stateTime = 0f;

    float spawnTimer = 2f;

    Array<Vehiculo> vehiculos = new Array<>();

    Jugador jugador = new Jugador();

    float tiempoGracia = 0f;

    int crucesParaGanar = Mundo.CRUCES_VICTORIA;

    public PantallaJuego(Main game) {
        super(game);
        Mundo.puntos = 0;
        Mundo.vidas = 3;
        Mundo.victoria = false;
        jugador.vidas = Mundo.vidas;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
        stateTime += delta;

        // 1. GRACIA
        if (tiempoGracia > 0) {
            tiempoGracia -= delta;
        }

        // 2. SPAWN
        if (tiempoGracia <= 0) {
            spawnTimer -= delta;
            if (spawnTimer <= 0) {
                spawnCoche();
                spawnTimer = MathUtils.random(0.5f, 2f);
            }
        }

        // 3. UPDATE
        jugador.update(delta);
        for (Vehiculo v : vehiculos) v.update(delta);

        // 4. COLISIONES Y LIMPIEZA
        for (int i = vehiculos.size - 1; i >= 0; i--) {
            Vehiculo v = vehiculos.get(i);
            if (v.destruido) { vehiculos.removeIndex(i); continue; }
            if (!jugador.invencible && jugador.hitbox.overlaps(v.rect)) {
                vehiculos.removeIndex(i);
                jugador.golpear();
                tiempoGracia = 0.5f;
                if (jugador.vidas <= 0) terminarPartida(false);
            }
        }

        // 5. CRUCE
        if (jugador.hasCruzado()) {
            Mundo.puntos++;
            jugador.resetPosicion();
            tiempoGracia = 0.5f;
            if (Mundo.puntos >= crucesParaGanar) terminarPartida(true);
        }

        // 6. DIBUJAR
        sr.begin(ShapeRenderer.ShapeType.Filled);
        dibujarCarriles();
        for (Vehiculo v : vehiculos) v.render(sr);
        jugador.render(sr);
        sr.end();

        // 7. HUD
        batch.begin();
        font.draw(batch, "Cruces: " + Mundo.puntos + "/" + crucesParaGanar, 10, Mundo.ALTO + Mundo.TOP_BAR - 10);
        font.draw(batch, "Vidas: " + jugador.vidas, 300, Mundo.ALTO + Mundo.TOP_BAR - 10);
        font.draw(batch, "Tiempo: " + String.format("%.2f", stateTime), 500, Mundo.ALTO + Mundo.TOP_BAR - 10);
        batch.end();
    }

    private void spawnCoche() {
        float tam = Mundo.getTamCasilla();
        // carril aleatorio — x fija en múltiplo de tam
        int carril = MathUtils.random(0, Mundo.NUM_CARRILES - 1);
        float x = carril * tam;
        vehiculos.add(new Vehiculo(x));
    }

    private void dibujarCarriles() {
        float tam = Mundo.getTamCasilla();
        sr.setColor(new Color(0.2f, 0.2f, 0.2f, 1f)); // gris oscuro
        for (int i = 0; i < Mundo.NUM_CARRILES; i++) {
            if (i % 2 == 0) {
                sr.setColor(new Color(0.15f, 0.15f, 0.15f, 1f));
            } else {
                sr.setColor(new Color(0.25f, 0.25f, 0.25f, 1f));
            }
            sr.rect(i * tam, 0, tam, Mundo.ALTO);
        }
    }

    // --- INPUT ---
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:    jugador.mover(0, 1);  break;
            case Input.Keys.DOWN:  jugador.mover(0, -1); break;
            case Input.Keys.RIGHT: jugador.mover(1, 0);  break;
            case Input.Keys.LEFT:  jugador.mover(-1, 0); break;
            case Input.Keys.ESCAPE: game.setScreen(new PantallaInicio(game)); break;
        }
        return true;
    }

    // --- FIN DE PARTIDA ---
    private void terminarPartida(boolean victoria) {
        Mundo.victoria = victoria;
        Mundo.tiempoFinal = stateTime;
        Mundo.guardarRecord(stateTime);
        game.setScreen(new PantallaResultado(game));
    }
}

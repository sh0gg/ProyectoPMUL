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
    Cazador cazador = new Cazador();
    Array<Bala> balas = new Array<>();
    Array<Sombra> sombras = new Array<>();
    int vidas = 3;

    public PantallaJuego(Main game) {
        super(game);
        Mundo.puntos = 0;
        Mundo.victoria = false;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stateTime += delta;

        // SPAWN
        spawnTimer -= delta;
        if (spawnTimer <= 0) {
            sombras.add(MathUtils.randomBoolean() ? new SombraCuadrada() : new SombraCircular());
            spawnTimer = MathUtils.random(0.5f, 2f);
        }

        // UPDATE
        for (Bala b : balas) b.update(delta);
        for (Sombra s : sombras) s.update(delta);

        // COLISIONES balas-sombras
        for (int i = balas.size - 1; i >= 0; i--) {
            Bala b = balas.get(i);
            if (b.destruida) { balas.removeIndex(i); continue; }
            for (int j = sombras.size - 1; j >= 0; j--) {
                Sombra s = sombras.get(j);
                if (b.hitbox.overlaps(s.hitbox)) {
                    if (b.forma == s.forma) {
                        // formas iguales → destruir ambos
                        s.destruida = true;
                        Mundo.puntos++;
                    }
                    b.destruida = true;
                    break;
                }
            }
        }

        // LIMPIAR balas y sombras destruidas
        for (int i = balas.size - 1; i >= 0; i--)
            if (balas.get(i).destruida) balas.removeIndex(i);
        for (int i = sombras.size - 1; i >= 0; i--) {
            Sombra s = sombras.get(i);
            if (s.destruida) { sombras.removeIndex(i); continue; }
            // sombra llega al borde izquierdo sin destruir → pierde vida
            if (s.x < 0) {
                sombras.removeIndex(i);
                vidas--;
                if (vidas <= 0) terminarPartida(false);
            }
        }

        // WIN condition
        if (Mundo.puntos >= 10) terminarPartida(true);

        // DIBUJAR
        sr.begin(ShapeRenderer.ShapeType.Filled);
        cazador.render(sr);
        for (Bala b : balas) b.render(sr);
        for (Sombra s : sombras) s.render(sr);
        sr.end();

        // HUD
        batch.begin();
        font.draw(batch, "Puntos: " + Mundo.puntos, 10, Mundo.ALTO + Mundo.TOP_BAR - 10);
        font.draw(batch, "Vidas: " + vidas, 250, Mundo.ALTO + Mundo.TOP_BAR - 10);
        font.draw(batch, "Forma: " + cazador.forma, 450, Mundo.ALTO + Mundo.TOP_BAR - 10);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.UP:    cazador.mover(1);  break;
            case Input.Keys.DOWN:  cazador.mover(-1); break;
            case Input.Keys.SPACE:
                if (balas.size < Mundo.MAX_BALAS) {
                    balas.add(new Bala(cazador.x + cazador.tam, cazador.y + cazador.tam/2, cazador.forma));
                }
                break;
            case Input.Keys.Z:
                cazador.cambiarForma(); break;
            case Input.Keys.ESCAPE:
                game.setScreen(new PantallaInicio(game)); break;
        }
        return true;
    }

    private void terminarPartida(boolean victoria) {
        Mundo.victoria = victoria;
        Mundo.tiempoFinal = stateTime;
        Mundo.guardarRecord(stateTime);
        game.setScreen(new PantallaResultado(game));
    }
}

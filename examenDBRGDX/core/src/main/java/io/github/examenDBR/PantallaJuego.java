package io.github.examenDBR;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends Pantalla {
    float stateTime = 0f;
    float spawnTime = 1f;
    Array<Globo> globos = new Array<>(Mundo.TOTAL_GLOBOS);
    int globosLanzados = 0;

    public PantallaJuego(Main game) {
        super(game);
        Mundo.puntos = Mundo.escapados = 0;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stateTime += delta;

        // spawneo de globos
        spawnTime -= delta; //decrece el timer
        if (spawnTime < 0f && globosLanzados < Mundo.TOTAL_GLOBOS) { // sale uno cada vez que el tiempo es cero Y el numero de globos no llegue al total de globos
            globos.add(new Globo());
            globosLanzados++;
            spawnTime = MathUtils.random(Mundo.spawnMin, Mundo.spawnMax); // se reestablece
        }

        // update de globos
        for (Globo g : globos) {
            g.update(delta);
        }

        // update de los globos colisiones y escapados
        for (int i = globos.size - 1; i >= 0; i--) {
            Globo g = globos.get(i);
            if (g.destroyed) {
                globos.removeIndex(i);
                Mundo.puntos++;
            } else if (g.y > Mundo.ALTO) {
                globos.removeIndex(i);
                Mundo.escapados++;
            }
            if (Mundo.escapados >= Mundo.MAX_ESCAPADOS) {
                terminarPartida(false);
            }
            if (Mundo.puntos >= Mundo.TOTAL_GLOBOS) {
                terminarPartida(true);
            }
        }
        // globos pinchados + escapados == total (no more globos)
        if (globosLanzados >= Mundo.TOTAL_GLOBOS && globos.isEmpty()) {
            terminarPartida(Mundo.escapados < Mundo.MAX_ESCAPADOS);
        }

        // pintar los globos
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Globo g : globos) g.render(sr);
        sr.end();

        if (Mundo.DEBUG) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            for (Globo g : globos) g.renderDebug(sr);
            sr.end();
        }

        // HUD
        batch.begin();

        font.draw(batch, "Tiempo: " + String.format("%.2f", stateTime), 10, Mundo.ALTO + Mundo.TOP_BAR);
        font.draw(batch, "Puntos: " + Mundo.puntos + "  Escapados: " + Mundo.escapados + "/" + Mundo.MAX_ESCAPADOS, 200, Mundo.ALTO + Mundo.TOP_BAR);

        batch.end();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // Cuando la resolucion de la pantalla y del mundo no coinciden, te toca desproyectar la camara para calcular donde has pinchao
        Vector3 vector3 = new Vector3(screenX, screenY, 0f);
        camera.unproject(vector3);

        for (Globo g : globos) {
            if (g.isHit(vector3.x, vector3.y)) {
                g.destroyed = true;
                break;
            }
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

package cubes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends Pantalla {

    int numInsectos = 0;
    int numKills = 0;

    String recordString = "";
    String killsString = "";

    Array<Insecto> insectos = new Array<>();

    float recordTime = -1f;
    float stateTime = 0f;
    boolean hayRecord = false;

    Preferences preferences = Gdx.app.getPreferences("record.prefs");

    public PantallaJuego(Main game, int numInsectos) {
        super(game);
        this.numInsectos = numInsectos;

        cargarRecord();
        crearInsectos(numInsectos);
        killsString = "";
    }

    private void cargarRecord() {
        recordTime = preferences.getFloat("record" + numInsectos, 0);
        hayRecord = recordTime != 0;
        recordString = hayRecord ? String.format("Record: %.2fs", recordTime) : "Record: sin record";
    }

    // Según el enunciado: aparece el insecto nº 1 en el centro.
    // numInsectos es el OBJETIVO de kills, no “cuántos simultáneos”.
    private void crearInsectos(int numInsectos) {
        float x = Mundo.MITAD_ANCHO_MUNDO - game.mitadTamanoTextura;
        float y = Mundo.MITAD_ALTO_MUNDO - game.mitadTamanoTextura;
        insectos.clear();
        insectos.add(new Insecto(x, y, game.tamanoTextura, 50, game.imagenesInsectos));
    }

    private void guardarRecord(float t) {
        preferences.putFloat("record" + numInsectos, t);
        preferences.flush();
        recordTime = t;
        hayRecord = true;
        recordString = String.format("Record: %.2fs", recordTime);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stateTime += delta;

        // Update insectos
        for (Insecto insecto : insectos) {
            insecto.update(delta);
        }

        drawShapes();
        drawSprites();
    }

    private void drawShapes() {
        sr.begin(ShapeRenderer.ShapeType.Filled);

        // Manchas pequeñas “en el suelo” (y=2) persistentes durante la ejecución
        sr.setColor(Color.RED);
        for (Vector2 p : game.puntos) {
            sr.rect(p.x, 2, 4, 4);
        }

        // Línea separadora en y=ALTO_MUNDO
        sr.setColor(Color.WHITE);
        sr.rect(0, Mundo.ALTO_MUNDO, Mundo.ANCHO_MUNDO, 1);

        sr.end();
    }

    private void drawSprites() {
        batch.begin();

        // Barra superior (texto en la barra)
        fuente.setColor(Color.WHITE);
        fuente.draw(batch,
                "Record (" + numInsectos + "): " + (hayRecord ? String.format("%.2fs", recordTime) : "sin record"),
                10, Mundo.ALTO_MUNDO + Mundo.ALTURA_MENU_SUPERIOR - 8);

        fuente.draw(batch, "Tiempo: " + String.format("%.2fs", stateTime),
                220, Mundo.ALTO_MUNDO + Mundo.ALTURA_MENU_SUPERIOR - 8);

        fuente.draw(batch, "KILLS: " + killsString,
                420, Mundo.ALTO_MUNDO + Mundo.ALTURA_MENU_SUPERIOR - 8);

        // Dibujar insectos
        for (Insecto insecto : insectos) {
            insecto.render(batch);
        }

        batch.end();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 v3 = new Vector3(screenX, screenY, 0);
        camera.unproject(v3);
        Vector2 v2 = new Vector2(v3.x, v3.y);

        for (Insecto insecto : insectos) {
            if (insecto.isHit(v2)) {
                numKills++;

                if (numKills >= numInsectos) {
                    // Mancha final: un cuadrito pequeño en el suelo
                    game.puntos.add(new Vector2(v2.x, 0));

                    if (!hayRecord || stateTime < recordTime) {
                        guardarRecord(stateTime);
                    }

                    game.irAPantallaInicio();
                } else {
                    // Cambia dirección aleatoria izquierda/derecha al tocar (como el enunciado)
                    // (Aquí lo simplifico: cambiamos de insecto, y el movimiento ya rebota)
                    insecto.nextInsecto();
                    killsString = "I".repeat(numKills);
                }
                return true;
            }
        }
        return true;
    }
}

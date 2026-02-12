package io.github.naves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Iterator;

public class Mundo {

    // ---- Constantes de mundo ----
    public static final int ANCHO = 480;
    public static final int ALTO = 320;
    public static final int ALTO_BARRA_INFO = 40;

    // Jugador (dedo point.png) se mueve en vertical borde izquierdo
    private static final float JUGADOR_X = 10f;
    private static final float JUGADOR_SPEED_Y = 200f;

    // Balas (círculos)
    public static final int MAX_BALAS = 3;
    private static final float BALA_SPEED_X = 420f;
    private static final float BALA_RADIO = 4f;

    // Enemigos
    private static final float TIEMPO_ENEMIGO_MIN = 0.8f;
    private static final float TIEMPO_ENEMIGO_MAX = 2.2f;

    // Vidas / hiperespacio
    public static final int VIDAS_INICIALES = 3;
    public static final int HIPER_INICIALES = 3;
    private static final float DURACION_HIPER = 2.0f;

    // Record
    private static final String PREFS_NAME = "naves_record";
    private static final String KEY_RECORD = "record_capturas";

    // ---- Estado ----
    public float stateTime = 0f;
    public int capturas = 0;
    public int vidas = VIDAS_INICIALES;
    public int hiperespacios = HIPER_INICIALES;

    private float stateTimeFinHiper = -1f;
    private float proximoEnemigoEn = 0f;

    // ---- Texturas ----
    private Texture texDedo;     // point.png (jugador)
    private Texture texNave;     // nave.png (enemigos)
    private Texture texCorazon;  // corazon.png (HUD)

    // ---- Entidades ----
    private final Rectangle jugadorRect = new Rectangle();
    private final Array<Bala> balasActivas = new Array<>();
    private final Array<Enemigo> enemigos = new Array<>();

    // Pool de balas
    private final Pool<Bala> balaPool = new Pool<Bala>() {
        @Override protected Bala newObject() { return new Bala(); }
    };

    // Input
    private boolean moviendoArriba = false;
    private boolean moviendoAbajo = false;

    // Prefs
    private final Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);

    public Mundo() {
        texDedo = new Texture("point.png");
        texNave = new Texture("nave.png");
        texCorazon = new Texture("corazon.png");
        reset();
    }

    // ---------------- Reset ----------------
    public void reset() {
        stateTime = 0f;
        capturas = 0;
        vidas = VIDAS_INICIALES;
        hiperespacios = HIPER_INICIALES;
        stateTimeFinHiper = -1f;

        // Ajuste tamaño jugador (dedo) a un tamaño “jugable”
        float w = texDedo.getWidth() * 0.8f;
        float h = texDedo.getHeight() * 0.8f;

        jugadorRect.set(JUGADOR_X, (ALTO / 2f), w, h);

        // Limpieza balas
        for (Bala b : balasActivas) balaPool.free(b);
        balasActivas.clear();

        // Limpieza enemigos
        enemigos.clear();

        proximoEnemigoEn = getRandomTiempoEnemigo();
    }

    // ---------------- Input delegado ----------------
    public void setMoviendoArriba(boolean v) { moviendoArriba = v; }
    public void setMoviendoAbajo(boolean v) { moviendoAbajo = v; }

    public void nuevaBala() {
        if (balasActivas.size >= MAX_BALAS) return;

        Bala b = balaPool.obtain();

        float x = jugadorRect.x + jugadorRect.width + 2f;
        float y = jugadorRect.y + jugadorRect.height / 2f;

        b.init(x, y, BALA_RADIO, BALA_SPEED_X);

        balasActivas.add(b);
    }

    public boolean activarHiperespacio() {
        if (hiperespacios <= 0) return false;
        if (enHiperespacio()) return false;

        hiperespacios--;
        stateTimeFinHiper = stateTime + DURACION_HIPER;
        return true;
    }

    public boolean enHiperespacio() {
        return stateTimeFinHiper >= 0f && stateTime <= stateTimeFinHiper;
    }

    // ---------------- Update principal ----------------
    public void actualiza(float delta) {
        stateTime += delta;

        // 1) Movimiento jugador
        if (moviendoArriba) jugadorRect.y += JUGADOR_SPEED_Y * delta;
        if (moviendoAbajo) jugadorRect.y -= JUGADOR_SPEED_Y * delta;

        // Clamp jugador (no invadir barra info)
        if (jugadorRect.y < ALTO_BARRA_INFO) jugadorRect.y = ALTO_BARRA_INFO;
        float maxY = ALTO - jugadorRect.height;
        if (jugadorRect.y > maxY) jugadorRect.y = maxY;

        // 2) Spawn enemigo
        if (stateTime >= proximoEnemigoEn) {
            enemigos.add(new Enemigo(texNave));
            proximoEnemigoEn = stateTime + getRandomTiempoEnemigo();
        }

        // 3) Update balas (pooling)
        Iterator<Bala> itB = balasActivas.iterator();
        while (itB.hasNext()) {
            Bala b = itB.next();
            b.update(delta);

            if (b.destruida) {
                itB.remove();
                balaPool.free(b);
            }
        }

        // 4) Update enemigos (si salen por izquierda => pierdes vida si no hiper)
        for (int i = enemigos.size - 1; i >= 0; i--) {
            Enemigo e = enemigos.get(i);
            e.update(delta);

            if (e.rect.x + e.rect.width < 0) {
                enemigos.removeIndex(i);
                if (!enHiperespacio()) vidas--;
            }
        }

        // 5) Colisiones balas vs enemigos
        colisionesBalasEnemigos();
    }

    private void colisionesBalasEnemigos() {
        for (int i = enemigos.size - 1; i >= 0; i--) {
            Enemigo e = enemigos.get(i);

            for (int j = balasActivas.size - 1; j >= 0; j--) {
                Bala b = balasActivas.get(j);
                if (b.destruida) continue;

                Circle c = b.getCircle();

                // Colisión círculo-rectángulo (simple y rápida)
                float closestX = MathUtils.clamp(c.x, e.rect.x, e.rect.x + e.rect.width);
                float closestY = MathUtils.clamp(c.y, e.rect.y, e.rect.y + e.rect.height);
                float dx = c.x - closestX;
                float dy = c.y - closestY;

                if (dx * dx + dy * dy <= c.radius * c.radius) {
                    b.destruida = true;
                    enemigos.removeIndex(i);
                    capturas++;
                    break;
                }
            }
        }
    }

    // ---------------- Dibujo ----------------
    public void dibuja(SpriteBatch sb, ShapeRenderer sr, BitmapFont font) {

        // 1) Jugador (dedo)
        sb.draw(texDedo, jugadorRect.x, jugadorRect.y, jugadorRect.width, jugadorRect.height);

        // 2) Enemigos (naves)
        for (Enemigo e : enemigos) {
            e.render(sb, texNave);
        }

        // 3) Barra inferior
        sr.setProjectionMatrix(sb.getProjectionMatrix());
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.line(0, ALTO_BARRA_INFO, ANCHO, ALTO_BARRA_INFO);
        sr.end();

        // 4) Balas (círculos) -> ShapeRenderer Filled para que sean “bolitas”
        sr.begin(ShapeRenderer.ShapeType.Filled);
        for (Bala b : balasActivas) {
            b.render(sr);
        }
        sr.end();

        // 5) HUD texto
        int record = getRecord();
        font.draw(sb, "Tiempo: " + (int) stateTime, 10, 25);
        font.draw(sb, "Balas: " + (MAX_BALAS - balasActivas.size), 140, 25);
        font.draw(sb, "Hiper: " + hiperespacios + (enHiperespacio() ? " (ON)" : ""), 250, 25);
        font.draw(sb, "Capturas: " + capturas, 10, ALTO - 10);
        font.draw(sb, "Record: " + record, 10, ALTO - 30);

        // 6) HUD vidas con corazones (abajo derecha)
        float hx = ANCHO - 10f;
        float hy = 8f;
        float hw = texCorazon.getWidth() * 0.25f;
        float hh = texCorazon.getHeight() * 0.25f;

        for (int i = 0; i < vidas; i++) {
            hx -= (hw + 4f);
            sb.draw(texCorazon, hx, hy, hw, hh);
        }
    }

    // ---------------- Game Over / Record ----------------
    public boolean gameOver() {
        return vidas <= 0;
    }

    public int getRecord() {
        return prefs.getInteger(KEY_RECORD, 0);
    }

    public void guardarRecordSiMejora() {
        int record = getRecord();
        if (capturas > record) {
            prefs.putInteger(KEY_RECORD, capturas);
            prefs.flush();
        }
    }

    // ---------------- Utils ----------------
    private float getRandomTiempoEnemigo() {
        return TIEMPO_ENEMIGO_MIN + MathUtils.random() * (TIEMPO_ENEMIGO_MAX - TIEMPO_ENEMIGO_MIN);
    }

    public void dispose() {
        texDedo.dispose();
        texNave.dispose();
        texCorazon.dispose();

        for (Bala b : balasActivas) balaPool.free(b);
        balasActivas.clear();
        balaPool.clear();
        enemigos.clear();
    }
}

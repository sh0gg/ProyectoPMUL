package io.github.naves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import java.util.Iterator;

public class PantallaJuego extends Pantalla {

    // --- Assets y Lógica del Jugador (Dedo) ---
    private Texture texturaDedo;
    private Rectangle dedoRect;
    private float velocidadDedo = 200; // Píxeles por segundo

    // --- Lógica de Disparo (Pooling) ---
    private Texture texturaBala;      // La imagen de la bala (se carga una sola vez)
    private Array<Bala> balasActivas; // Lista de balas que están volando ahora mismo
    private Pool<Bala> balaPool;      // La "piscina" de balas reciclables
    private final int MAX_BALAS = 3;  // Límite de balas simultáneas en pantalla

    // Banderas de movimiento
    private boolean moviendoArriba = false;
    private boolean moviendoAbajo = false;

    // --- Configuración del Mundo ---
    // Altura de la barra inferior de info
    private final int ALTO_BARRA = 40;

    // Herramienta para dibujar líneas
    private ShapeRenderer shapeRenderer;

    public PantallaJuego() {
        // 1. Inicializar Assets
        texturaDedo = new Texture(Gdx.files.internal("point.png"));
        // Usamos la misma textura para la bala por ahora
        texturaBala = new Texture(Gdx.files.internal("point.png"));

        shapeRenderer = new ShapeRenderer();

        // 2. Configurar Jugador
        dedoRect = new Rectangle();
        dedoRect.x = 10; // Margen izquierdo
        dedoRect.y = Mundo.ALTO / 2f;
        dedoRect.width = texturaDedo.getWidth();
        dedoRect.height = texturaDedo.getHeight();

        // 3. Configurar el sistema de Pooling de Balas
        balasActivas = new Array<>();

        // Definimos cómo se crean las balas nuevas si la piscina está vacía
        balaPool = new Pool<Bala>() {
            @Override
            protected Bala newObject() {
                return new Bala();
            }
        };
    }

    @Override
    public void render(float delta) {
        // 1. Limpiar pantalla (Fondo Blanco)
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 2. Actualizar Lógica (Movimiento y Balas)
        actualizarMovimiento(delta);
        actualizarBalas(delta);

        // 3. Dibujar Sprites
        juego.sb.begin();

        // Dibujar el Dedo
        juego.sb.draw(texturaDedo, dedoRect.x, dedoRect.y);

        // Dibujar todas las balas activas
        for (Bala b : balasActivas) {
            b.render(juego.sb);
        }

        // Dibujar Texto de Info (Color negro para que se vea en fondo blanco)
        juego.fuente.setColor(Color.BLACK);
        juego.fuente.draw(juego.sb, "Balas: " + (MAX_BALAS - balasActivas.size), 20, 25);
        juego.fuente.draw(juego.sb, "Vidas: 3", 100, 25); // Ejemplo

        juego.sb.end();

        // 4. Dibujar Líneas (Suelo)
        shapeRenderer.setProjectionMatrix(juego.sb.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BROWN);
        shapeRenderer.line(0, ALTO_BARRA, Mundo.ANCHO, ALTO_BARRA);
        shapeRenderer.end();
    }

    private void actualizarMovimiento(float delta) {
        if (moviendoArriba) {
            dedoRect.y += velocidadDedo * delta;
        }
        if (moviendoAbajo) {
            dedoRect.y -= velocidadDedo * delta;
        }

        // Restricciones de pantalla
        // No bajar de la barra de información
        if (dedoRect.y < ALTO_BARRA) dedoRect.y = ALTO_BARRA;
        // No subir más allá del techo
        if (dedoRect.y > Mundo.ALTO - dedoRect.height) dedoRect.y = Mundo.ALTO - dedoRect.height;
    }

    private void actualizarBalas(float delta) {
        // Usamos iterador para poder eliminar elementos de la lista mientras la recorremos safely
        Iterator<Bala> iter = balasActivas.iterator();
        while (iter.hasNext()) {
            Bala b = iter.next();
            b.update(delta);

            // Si la bala debe ser destruida (salió de pantalla o chocará luego con enemigos)
            if (b.destruida) {
                iter.remove();      // La quitamos de la lista visible
                balaPool.free(b);   // La devolvemos a la piscina para reciclarla
            }
        }
    }

    private void disparar() {
        // Solo disparamos si no hemos alcanzado el límite
        if (balasActivas.size < MAX_BALAS) {
            Bala b = balaPool.obtain(); // Dame una bala (nueva o reciclada)

            // Calculamos posición inicial (punta del dedo)
            float xBala = dedoRect.x + dedoRect.width;
            float yBala = dedoRect.y + dedoRect.height / 2 - 5; // -5 para centrar aprox

            // Inicializamos la bala
            b.init(xBala, yBala, texturaBala);

            // La añadimos a la lista de renderizado
            balasActivas.add(b);
        }
    }

    // --- Input (Heredado de Pantalla) ---

    @Override
    public boolean teclaAbajo(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            moviendoArriba = true;
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            moviendoAbajo = true;
        }
        if (keycode == Input.Keys.SPACE) {
            disparar();
        }
        if (keycode == Input.Keys.ESCAPE) {
            juego.irAPantallaInicio();
        }
        return true;
    }

    @Override
    public boolean teclaArriba(int keycode) {
        if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            moviendoArriba = false;
        }
        if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            moviendoAbajo = false;
        }
        return true;
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
    public void dispose() {
        texturaDedo.dispose();
        texturaBala.dispose();
        shapeRenderer.dispose();
        // Opcional: limpiar pool
        balasActivas.clear();
        balaPool.clear();
    }
}

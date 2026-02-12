package io.github.pescador;

import static com.badlogic.gdx.Input.Keys.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Main extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;

    private OrthographicCamera camara;
    private Viewport viewport;

    private Texture fondo;
    private Texture pixel;

    private Pescador pescador;
    private Anzuelo anzuelo;
    private Array<Pez> peces;

    private boolean movingLeft, movingRight;

    // HUD
    private BitmapFont font;
    private float tiempoRestante;
    private int capturas;

    // Record
    private Preferences prefs;
    private static final String PREFS_NAME = "pescador_record";
    private static final String KEY_RECORD = "record_capturas";

    // Spawn peces
    private float stateTime;
    private float proximoSpawnEn;

    // Debug hitbox
    private ShapeRenderer shape;

    // Estado de partida
    private boolean gameOver;

    @Override
    public void create() {
        batch = new SpriteBatch();

        camara = new OrthographicCamera();
        viewport = new FitViewport(Mundo.ANCHO, Mundo.ALTO, camara);
        viewport.apply(true);
        camara.position.set(Mundo.ANCHO / 2f, Mundo.ALTO / 2f, 0);
        camara.update();
        batch.setProjectionMatrix(camara.combined);

        fondo = new Texture("fondo.jpg");
        pixel = new Texture("pixel.png");

        pescador = new Pescador();
        anzuelo = new Anzuelo(pescador);
        peces = new Array<>();

        font = new BitmapFont(); // sin asset extra
        tiempoRestante = Mundo.TIEMPO_INICIAL;
        capturas = 0;

        prefs = Gdx.app.getPreferences(PREFS_NAME);

        stateTime = 0f;
        proximoSpawnEn = getRandomSpawn();

        shape = new ShapeRenderer();

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camara.update();
        batch.setProjectionMatrix(camara.combined);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        float delta = Gdx.graphics.getDeltaTime();

        // Si acabó, solo pintamos HUD (y dejamos el juego congelado)
        if (!gameOver) {
            stateTime += delta;

            // Tiempo
            tiempoRestante -= delta;
            if (tiempoRestante <= 0) {
                tiempoRestante = 0;
                gameOver = true;
                guardarRecordSiMejora();
            }

            // Movimiento pescador (bloqueado si pesca desde keyDown)
            if (movingLeft)
                pescador.moveLeft(delta);
            if (movingRight)
                pescador.moveRight(delta);

            // Clamp pescador
            if (pescador.x < 0)
                pescador.x = 0;
            float xMax = Mundo.ANCHO - pescador.width;
            if (pescador.x > xMax)
                pescador.x = xMax;

            // Update entidades
            pescador.update(delta);
            anzuelo.update(delta);

            // Enganchar anzuelo solo cuando está parado arriba
            if (anzuelo.estaParado()) {
                if (pescador.lastDirection == 0) {
                    anzuelo.x = pescador.x - (anzuelo.width / 2f) + 3f;
                } else {
                    anzuelo.x = pescador.x + pescador.width - (anzuelo.width * 0.65f);
                }
            }

            // Spawn peces
            if (stateTime >= proximoSpawnEn) {
                peces.add(Pez.crearAleatorio());
                proximoSpawnEn = stateTime + getRandomSpawn();
            }

            // Update peces
            for (int i = peces.size - 1; i >= 0; i--) {
                Pez pez = peces.get(i);
                pez.update(delta);

                if (pez.estaFueraDelMundo()) {
                    pez.dispose();
                    peces.removeIndex(i);
                }
            }

            // Colisión SOLO cuando el anzuelo SUBE
            if (anzuelo.estaSubiendo()) {
                for (int i = peces.size - 1; i >= 0; i--) {
                    Pez pez = peces.get(i);

                    if (anzuelo.getHitBox().overlaps(pez.getMouthHitBox())) {
                        // Captura
                        capturas++;
                        tiempoRestante += Mundo.BONUS_POR_PEZ;

                        pez.dispose();
                        peces.removeIndex(i);
                        break;
                    }
                }
            }
        }

        // Dibujo
        viewport.apply();
        batch.setProjectionMatrix(camara.combined);

        batch.begin();

        batch.draw(fondo, 0, 0, Mundo.ANCHO, Mundo.ALTO);

        // Sedal
        float distance = (pescador.y + pescador.height) - (anzuelo.y + anzuelo.height);
        if (distance < 0)
            distance = 0;

        int pixelSize = 2;
        for (float i = 0; i < distance; i++) {
            batch.draw(pixel,
                    pescador.x + pescador.width / 2f,
                    pescador.y + pescador.height - i,
                    pixelSize, pixelSize);
        }

        // Peces
        for (Pez pez : peces) {
            pez.draw(batch);
        }

        // Pescador + anzuelo
        batch.draw(pescador.sprite, pescador.x, pescador.y, pescador.width, pescador.height);
        batch.draw(anzuelo.sprite, anzuelo.x, anzuelo.y, anzuelo.width, anzuelo.height);

        // HUD (arriba a la izquierda)
        int record = prefs.getInteger(KEY_RECORD, 0);
        font.draw(batch, "Tiempo: " + (int) tiempoRestante, 10, Mundo.ALTO - 10);
        font.draw(batch, "Capturas: " + capturas, 10, Mundo.ALTO - 30);
        font.draw(batch, "Record: " + record, 10, Mundo.ALTO - 50);

        if (gameOver) {
            font.draw(batch, "GAME OVER (ENTER para reiniciar)", 100, Mundo.ALTO / 2f);
        }

        batch.end();

        // Debug hitboxes (opcional)
        if (Mundo.DEBUG_HITBOX) {
            shape.setProjectionMatrix(camara.combined);
            shape.begin(ShapeRenderer.ShapeType.Line);

            // anzuelo
            shape.rect(anzuelo.getHitBox().x, anzuelo.getHitBox().y,
                    anzuelo.getHitBox().width, anzuelo.getHitBox().height);

            // bocas peces
            for (Pez pez : peces) {
                shape.rect(pez.getMouthHitBox().x, pez.getMouthHitBox().y,
                        pez.getMouthHitBox().width, pez.getMouthHitBox().height);
            }

            shape.end();
        }
    }

    private float getRandomSpawn() {
        return Mundo.SPAWN_MIN + MathUtils.random() * (Mundo.SPAWN_MAX - Mundo.SPAWN_MIN);
    }

    private void guardarRecordSiMejora() {
        int record = prefs.getInteger(KEY_RECORD, 0);
        if (capturas > record) {
            prefs.putInteger(KEY_RECORD, capturas);
            prefs.flush();
        }
    }

    private void reiniciarPartida() {
        // Limpia peces
        for (Pez pez : peces) {
            pez.dispose();
        }
        peces.clear();

        // Reinicia contadores
        tiempoRestante = Mundo.TIEMPO_INICIAL;
        capturas = 0;

        // Reinicia spawns
        stateTime = 0f;
        proximoSpawnEn = getRandomSpawn();

        // Reinicia anzuelo: al ser estado interno, lo fácil es recrearlo
        anzuelo.dispose();
        anzuelo = new Anzuelo(pescador);

        gameOver = false;
    }

    @Override
    public void dispose() {
        batch.dispose();

        fondo.dispose();
        pixel.dispose();

        pescador.dispose();
        anzuelo.dispose();

        for (Pez pez : peces) {
            pez.dispose();
        }

        font.dispose();
        shape.dispose();
    }

    // ---------------- INPUT ----------------

    @Override
    public boolean keyDown(int keycode) {

        if (gameOver) {
            if (keycode == ENTER)
                reiniciarPartida();
            return true;
        }

        // Si el anzuelo NO está parado, no permitimos mover el pescador
        if (!anzuelo.estaParado() && (keycode == LEFT || keycode == RIGHT || keycode == A || keycode == D)) {
            return true;
        }

        if (keycode == A || keycode == LEFT)
            movingLeft = true;
        if (keycode == D || keycode == RIGHT)
            movingRight = true;

        // DOWN (o S) -> empieza bajada y bloquea movimiento
        if (keycode == S || keycode == DOWN) {
            movingLeft = false;
            movingRight = false;
            anzuelo.empezarBajada();
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (gameOver)
            return true;

        if (keycode == A || keycode == LEFT)
            movingLeft = false;
        if (keycode == D || keycode == RIGHT)
            movingRight = false;

        // SOLTAR DOWN (o S) -> empieza subida
        if (keycode == S || keycode == DOWN) {
            anzuelo.empezarSubida();
        }

        return true;
    }

    // No usados
    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

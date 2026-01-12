package io.github.color_simon;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Main extends ApplicationAdapter implements InputProcessor {

    enum State { COUNTDOWN, SHOW_COLOR, GAME_OVER }

    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;

    private State state = State.COUNTDOWN;

    private float timer;
    private int countdown = 3;

    private float countdownSpeed = 1f;
    private float reactionTime = 1.5f;

    private float colorTimer;
    private boolean spacePressed;

    private Color currentColor;
    private boolean isRed;

    private int score;
    private int lives = 3;

    private final Random random = new Random();

    // Texturas para acierto/fallo
    private Texture yeahTexture;
    private Texture noTexture;

    // Para mostrar la imagen durante el countdown según resultado anterior
    private enum LastResult { NONE, SUCCESS, FAIL }
    private LastResult lastResult = LastResult.NONE;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        font.getData().setScale(3);
        layout = new GlyphLayout();

        // Cargar texturas
        yeahTexture = new Texture(Gdx.files.internal("yeah.png"));
        noTexture = new Texture(Gdx.files.internal("no.png"));

        Gdx.input.setInputProcessor(this);
        resetGame();
    }

    private void resetGame() {
        score = 0;
        lives = 3;
        countdownSpeed = 1f;
        reactionTime = 1.5f;
        lastResult = LastResult.NONE;
        startCountdown();
        state = State.COUNTDOWN;
    }

    private void startCountdown() {
        countdown = 3;
        timer = 0;
        spacePressed = false;
    }

    private void nextColor() {
        int r = random.nextInt(4);
        isRed = (r == 0);

        switch (r) {
            case 0:
                currentColor = Color.RED;
                break;
            case 1:
                currentColor = Color.GREEN;
                break;
            case 2:
                currentColor = Color.BLUE;
                break;
            case 3:
                currentColor = Color.YELLOW;
                break;
        }

        colorTimer = 0;
        spacePressed = false;
        state = State.SHOW_COLOR;
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        update(delta);

        ScreenUtils.clear(Color.BLACK);

        batch.begin();

        if (state == State.COUNTDOWN) {
            // Centrar el countdown
            String countdownText = String.valueOf(countdown);
            layout.setText(font, countdownText);
            float xCountdown = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
            float yCountdown = Gdx.graphics.getHeight() / 2f + layout.height / 2f;
            font.draw(batch, countdownText, xCountdown, yCountdown);

            // Mostrar vidas arriba a la izquierda
            String corazones;
            switch (lives) {
                case 2:
                    corazones = "<3 <3 </3";
                    break;
                case 1:
                    corazones = "<3 </3 </3";
                    break;
                default:
                    corazones = "<3 <3 <3";
                    break;
            }

            String livesText = "Vidas: " + corazones;
            layout.setText(font, livesText);
            font.draw(batch, livesText, 20, Gdx.graphics.getHeight() - 20);

            // Mostrar imagen según resultado anterior en esquina inferior izquierda
            if (lastResult == LastResult.SUCCESS) {
                batch.draw(yeahTexture, 10, 10, 120, 100);
            } else if (lastResult == LastResult.FAIL) {
                batch.draw(noTexture, 10, 10, 120, 100);
            }
        }

        if (state == State.SHOW_COLOR) {
            Gdx.gl.glClearColor(
                currentColor.r,
                currentColor.g,
                currentColor.b,
                1
            );
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        if (state == State.GAME_OVER) {
            String gameOverText = "GAME OVER";
            String scoreText = "Puntuacion: " + score;
            String restartText = "Pulsa ESPACIO para reiniciar";

            // Centrar texto GAME OVER
            layout.setText(font, gameOverText);
            float xGameOver = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
            float yGameOver = Gdx.graphics.getHeight() / 2f + layout.height;

            font.draw(batch, gameOverText, xGameOver, yGameOver);

            // Centrar texto puntuación
            layout.setText(font, scoreText);
            float xScore = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
            float yScore = Gdx.graphics.getHeight() / 2f;
            font.draw(batch, scoreText, xScore, yScore);

            // Centrar texto reiniciar
            layout.setText(font, restartText);
            float xRestart = Gdx.graphics.getWidth() / 2f - layout.width / 2f;
            float yRestart = Gdx.graphics.getHeight() / 2f - layout.height;
            font.draw(batch, restartText, xRestart, yRestart);
        }

        batch.end();
    }

    private void update(float delta) {
        switch (state) {
            case COUNTDOWN: {
                timer += delta;
                if (timer >= countdownSpeed) {
                    countdown--;
                    timer = 0;
                    if (countdown == 0) {
                        nextColor();
                    }
                }
                break;
            }

            case SHOW_COLOR: {
                colorTimer += delta;
                if (colorTimer >= reactionTime) {
                    if (isRed) {
                        // No pulsaste espacio cuando salió rojo → pierde vida
                        lives--;
                        lastResult = LastResult.FAIL;
                        if (lives <= 0) {
                            state = State.GAME_OVER;
                        } else {
                            startCountdown();
                            state = State.COUNTDOWN;
                        }
                    } else {
                        // No pulsaste espacio cuando no era rojo → suma punto
                        score++;
                        lastResult = LastResult.SUCCESS;
                        levelUp();
                    }
                }
                break;
            }
        }
    }

    private int level = 1;  // Empezamos en el nivel 1
    private final int levelUpThreshold = 5;  // Cada 5 puntos subimos de nivel

    private void levelUp() {
        // Subimos de nivel si el puntaje alcanza el umbral
        if (score >= level * levelUpThreshold) {
            level++;  // Subimos de nivel
        }

        // Ajustamos la velocidad de la cuenta atrás y el tiempo de reacción por nivel
        // Base de velocidad un poco más rápida
        float baseSpeed = 1.0f;  // Puedes hacer que esto sea más rápido si quieres
        countdownSpeed = baseSpeed - 0.1f * level;  // A medida que subes de nivel, la cuenta atrás se hace más rápida

        // La reacción se hace más rápida pero de forma gradual
        float baseReactionTime = 1.5f;
        reactionTime = baseReactionTime - 0.05f * level;  // Reducir lentamente la reacción por cada nivel

        // Aseguramos que los valores no sean negativos ni demasiado rápidos
        countdownSpeed = Math
        state = State.COUNTDOWN;.max(0.2f, countdownSpeed);  // Velocidad mínima de la cuenta atrás
        reactionTime = Math.max(0.5f, reactionTime);  // Tiempo de reacción mínimo

        startCountdown();
    }


    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {

            if (state == State.SHOW_COLOR) {
                if (spacePressed) return false;  // Ignorar si ya pulsaste espacio esta ronda
                spacePressed = true;

                if (isRed) {
                    // Pulsaste espacio cuando era rojo → suma punto
                    score++;
                    lastResult = LastResult.SUCCESS;
                    levelUp();
                } else {
                    // Pulsaste espacio cuando NO era rojo → pierde vida
                    lives--;
                    lastResult = LastResult.FAIL;
                    if (lives <= 0) {
                        state = State.GAME_OVER;
                    } else {
                        startCountdown();
                        state = State.COUNTDOWN;
                    }
                }
                return true;
            }

            if (state == State.GAME_OVER) {
                resetGame();
                return true;
            }
        }
        return false;
    }

    // Métodos obligatorios del InputProcessor
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean touchDown(int x, int y, int p, int b) { return false; }
    @Override public boolean touchUp(int x, int y, int p, int b) { return false; }
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) { return false; }
    @Override public boolean touchDragged(int x, int y, int p) { return false; }
    @Override public boolean mouseMoved(int x, int y) { return false; }
    @Override public boolean scrolled(float x, float y) { return false; }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
        yeahTexture.dispose();
        noTexture.dispose();
    }
}

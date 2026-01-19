package old;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.H;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Main extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;
    private Sprite personaje;
    private Sprite gomuba;

    // Posición
    private float x = 100;
    private float y = 100;

    private float xEnemigo = 400;
    private float yEnemigo = 100;


    // Velocidad
    private float speedX = 400f;
    private float speedY = 300f;

    // Física
    private float velocityY = 0;
    private float gravity = -2000f;     // fuerza de gravedad
    private float jumpForce = 900f;      // fuerza del salto
    private boolean isGrounded = false;
    private boolean isFalling = false;

    private float personajeWidth;
    private float personajeHeight;
    private float gomubaWidth;
    private float gomubaHeight;
    private Color currentBg;
    private int lastDirection = 1;

    private float stateTime;

    private Random random;

    private Texture whitePixel;

    // Plataformas
    private float[][] platforms = {
        {200, 150, 200, 20},
        {500, 300, 250, 20},
        {150, 450, 180, 20}
    };

    // Variables de entrada
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean jumping = false;
    private boolean groundPound = false;
    private boolean doingAction = false;

    @Override
    public void create() {
        stateTime = 0;
        batch = new SpriteBatch();

        random = new Random();

        personaje = new Sprite(new Texture("grounded.png"));
        gomuba = new Sprite(new Texture("gomuba.png"));

        personaje.setSize(personaje.getWidth() * 0.5f, personaje.getHeight() * 0.5f);
        personajeWidth = personaje.getWidth();
        personajeHeight = personaje.getHeight();

        gomuba.setSize(gomuba.getWidth() * 0.5f, gomuba.getHeight() * 0.5f);
        gomubaWidth = gomuba.getWidth();
        gomubaHeight = gomuba.getHeight();

        whitePixel = new Texture("white.png");

        currentBg = new Color(0.6f, 0.2f, 0.1f, 1);

        // Registramos el InputProcessor
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (velocityY < 0) {
            isFalling = true;
        } else {
            isFalling = false;
        }

        if (isFalling) {
            isGrounded = false;
            personaje = new Sprite(new Texture("falling.png"));
        }

        if (isGrounded) {
            personaje = new Sprite(new Texture("grounded.png"));
        }

        if (!isGrounded && !isFalling) {
            personaje = new Sprite(new Texture("jumping.png"));
        }

        // Salto
        if (jumping && isGrounded) {
            velocityY = jumpForce;
            isGrounded = false;
        }

        // GROUND POUND!!!
        if (groundPound && !isGrounded) {
            velocityY = -jumpForce;
        }

        // Cambiar a imagen de acción
        if (doingAction && isGrounded && !isFalling) {
            personaje = new Sprite(new Texture("fuckyou.png"));
        }

        // Aplicar gravedad
        velocityY += gravity * delta;
        y += velocityY * delta;

        // Suelo
        if (y <= 0) {
            y = 0;
            velocityY = 0;
            isGrounded = true;
        }

        // Movimiento
        if (movingLeft) {
            x -= speedX * delta;
            lastDirection = 0;  // Última dirección fue izquierda
        }
        if (movingRight) {
            x += speedX * delta;
            lastDirection = 1;  // Última dirección fue derecha
        }

        // Mantener la dirección cuando no se mueve
        if (!movingLeft && !movingRight) {
            if (lastDirection == 0) {
                personaje.flip(true, false);  // Mira hacia la izquierda
            } else {
                personaje.flip(false, false);  // Mira hacia la derecha
            }
        } else {
            if (lastDirection == 0) {
                personaje.flip(true, false);  // Mira hacia la izquierda
            } else {
                personaje.flip(false, false);  // Mira hacia la derecha
            }
        }

        // Colisión con plataformas
        for (float[] p : platforms) {
            float px = p[0];
            float py = p[1];
            float pw = p[2];
            float ph = p[3];

            boolean overlapX = x + personajeWidth > px && x < px + pw;
            boolean falling = velocityY <= 0;
            boolean closeToTop = y <= py + ph && y >= py + ph - 20;

            if (overlapX && falling && closeToTop) {
                y = py + ph;
                velocityY = 0;
                isGrounded = true;
            }
        }

        // Límites laterales
        if (x <= 0) {
            x = 0;
        }
        if (x + personajeWidth >= Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() - personajeWidth;
        }

        if (x + gomubaWidth >= Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() - gomubaWidth;
        }

        ScreenUtils.clear(currentBg);

        batch.begin();
        batch.draw(personaje, x, y, personajeWidth, personajeHeight);
        batch.draw(gomuba, xEnemigo, yEnemigo, gomubaWidth, gomubaHeight);

        // Dibujar plataformas
        for (float[] p : platforms) {
            batch.draw(whitePixel, p[0], p[1], p[2], p[3]);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        whitePixel.dispose();
        batch.dispose();
        personaje.getTexture().dispose();
    }

    // InputProcessor
    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case W:
            case UP:
            case SPACE:
                jumping = true;
                break;
            case S:
            case DOWN:
                groundPound = true;
                break;
            case H:
                doingAction = true;
                break;
            case A:
            case LEFT:
                movingLeft = true;
                break;
            case D:
            case RIGHT:
                movingRight = true;
                break;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case W:
            case UP:
            case SPACE:
                jumping = false;
                break;
            case S:
            case DOWN:
                groundPound = false;
                break;
            case H:
                doingAction = false;
                break;
            case A:
            case LEFT:
                movingLeft = false;
                break;
            case D:
            case RIGHT:
                movingRight = false;
                break;
        }
        return false;
    }

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

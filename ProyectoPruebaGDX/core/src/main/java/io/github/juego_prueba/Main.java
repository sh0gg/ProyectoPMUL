package io.github.juego_prueba;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.H;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

public class Main extends ApplicationAdapter {

    private SpriteBatch batch;
    private Sprite image;

    // Posición
    private float x = 100;
    private float y = 100;

    // Velocidad
    private float speedX = 400f;
    private float speedY = 300f;

    // Física
    private float velocityY = 0;
    private float gravity = -2000f;     // fuerza de gravedad
    private float jumpForce = 900f;      // fuerza del salto
    private boolean isGrounded = false;
    private boolean isFalling = false;

    private float logoWidth;
    private float logoHeight;
    private Color currentBg;

    private float stateTime;

    private Random random;

    private Texture whitePixel;

    // Plataformas
    private float[][] platforms = {
        {200, 150, 200, 20},
        {500, 300, 250, 20},
        {150, 450, 180, 20}
    };


    @Override
    public void create() {
        stateTime = 0;
        batch = new SpriteBatch();

        random = new Random();

        image = new Sprite(new Texture("grounded.png"));

        image.setSize(image.getWidth() * 0.5f, image.getHeight() * 0.5f);
        logoWidth = image.getWidth();
        logoHeight = image.getHeight();

        whitePixel = new Texture("white.png");

        currentBg = new Color(0.6f, 0.2f, 0.1f, 1);
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
            image = new Sprite(new Texture("falling.png"));
        }

        if (isGrounded) {
            image = new Sprite(new Texture("grounded.png"));
        }

        if (!isGrounded && !isFalling) {
            image = new Sprite(new Texture("jumping.png"));
        }

        // Movimiento
        if (Gdx.input.isKeyPressed(A) || Gdx.input.isKeyPressed(LEFT)) {
            x -= speedX * delta;
        }
        if (Gdx.input.isKeyPressed(D) || Gdx.input.isKeyPressed(RIGHT)) {
            x += speedX * delta;
        }

        // Salto
        if ((Gdx.input.isKeyJustPressed(W) || Gdx.input.isKeyJustPressed(UP)) && isGrounded) {
            velocityY = jumpForce;
            isGrounded = false;
        }

        // GROUND POUND ?!?
        if ((Gdx.input.isKeyJustPressed(S) || Gdx.input.isKeyJustPressed(DOWN)) && !isGrounded) {
            velocityY = -jumpForce;
        }

        // FUCK YOU !!
        if (Gdx.input.isKeyPressed(H) && isGrounded && !isFalling) {
            image = new Sprite(new Texture("fuckyou.png"));
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

        // Colisión con plataformas
        for (float[] p : platforms) {
            float px = p[0];
            float py = p[1];
            float pw = p[2];
            float ph = p[3];

            boolean overlapX = x + logoWidth > px && x < px + pw;
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
        if (x + logoWidth >= Gdx.graphics.getWidth()) {
            x = Gdx.graphics.getWidth() - logoWidth;
        }

        ScreenUtils.clear(currentBg);

        batch.begin();
        batch.draw(image, x, y, logoWidth, logoHeight);
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
        image.getTexture().dispose();
    }
}



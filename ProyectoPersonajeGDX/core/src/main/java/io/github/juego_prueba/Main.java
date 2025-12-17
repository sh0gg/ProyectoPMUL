package io.github.juego_prueba;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.W;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    private float logoWidth;
    private float logoHeight;
    private Color currentBg;

    private float stateTime;

    private Random random;

    @Override
    public void create() {
        stateTime = 0;
        batch = new SpriteBatch();

        random = new Random();

        image = new Sprite(new Texture("isaac.png"));

        logoWidth = image.getWidth();
        logoHeight = image.getHeight();

        currentBg = new Color(0.1f, 0.1f, 0.2f, 1);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        stateTime += delta;


        Input.Keys[] keys = Input.Keys.class.getEnumConstants();

        if (Gdx.input.isKeyPressed(A)) {
            x -= speedX * delta;
        }
        if (Gdx.input.isKeyPressed(D)) {
            x += speedX * delta;
        }
        if (Gdx.input.isKeyPressed(W)) {
            y += speedY * delta;
        }
        if (Gdx.input.isKeyPressed(S)) {
            y -= speedY * delta;
        }

        // Rebotes
        if (x <= 0 || x + logoWidth >= Gdx.graphics.getWidth()) {
            speedX *= -1;
            image.flip(random.nextBoolean(), false);
            logoWidth = image.getWidth();
            logoHeight = image.getHeight();
            // changeBackgroundColor();
        }
        if (y <= 0 || y + logoHeight >= Gdx.graphics.getHeight()) {
            speedY *= -1;
            image.flip(random.nextBoolean(), false);
            logoWidth = image.getWidth();
            logoHeight = image.getHeight();
            // changeBackgroundColor();
        }


        // Limpiar pantalla con color actual
        ScreenUtils.clear(currentBg);

        batch.begin();
        batch.draw(image, x, y, logoWidth, logoHeight);
        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
        image.getTexture().dispose();
    }
}

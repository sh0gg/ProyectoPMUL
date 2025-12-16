package io.github.juego_prueba;

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
    private Texture[] imageList;

    // Posición
    private float x = 100;
    private float y = 100;

    // Velocidad
    private float speedX = 400f;
    private float speedY = 300f;

    private float logoWidth;
    private float logoHeight;
    private Color[] bgColors;
    private Color currentBg;

    private float stateTime;

    private Random random;

    @Override
    public void create() {
        stateTime = 0;
        batch = new SpriteBatch();

        random = new Random();

        imageList = new Texture[]{
            new Texture("gnomes.gif"),
            new Texture("libgdx.png"),
            new Texture("isaac.png"),
        };

        image = new Sprite(imageList[0]);

        logoWidth = image.getWidth();
        logoHeight = image.getHeight();

        bgColors = new Color[]{
            new Color(0.1f, 0.1f, 0.2f, 1),
            new Color(0.2f, 0.6f, 0.3f, 1),
            new Color(0.6f, 0.2f, 0.2f, 1),
            new Color(0.6f, 0.6f, 0.2f, 1),
            new Color(0.4f, 0.2f, 0.6f, 1),
            new Color(0.1f, 0.5f, 0.6f, 1)
        };

        currentBg = bgColors[0];
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        stateTime += delta;

        // Movimiento tipo DVD
        x += speedX * delta;
        y += speedY * delta;

        // Rebotes
        if (x <= 0 || x + logoWidth >= Gdx.graphics.getWidth()) {
            speedX *= -1;
            changeImage();
            image.flip(random.nextBoolean(), false);
            logoWidth = image.getWidth();
            logoHeight = image.getHeight();
            changeBackgroundColor();
        }
        if (y <= 0 || y + logoHeight >= Gdx.graphics.getHeight()) {
            speedY *= -1;
            changeImage();
            image.flip(random.nextBoolean(), false);
            logoWidth = image.getWidth();
            logoHeight = image.getHeight();
            changeBackgroundColor();
        }

        if (stateTime )

        // Limpiar pantalla con color actual
        ScreenUtils.clear(currentBg);

        batch.begin();
        batch.draw(image, x, y, logoWidth, logoHeight);
        batch.end();
    }

    private void changeBackgroundColor() {
        currentBg = bgColors[random.nextInt(bgColors.length)];
    }

    private void changeImage() {
        image = new Sprite(imageList[random.nextInt(imageList.length)]);
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture texture : imageList) {
            texture.dispose();
        }
    }
}

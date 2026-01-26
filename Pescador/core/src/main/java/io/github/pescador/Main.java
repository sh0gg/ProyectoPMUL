package io.github.pescador;

import static com.badlogic.gdx.Input.Keys.A;
import static com.badlogic.gdx.Input.Keys.D;
import static com.badlogic.gdx.Input.Keys.DOWN;
import static com.badlogic.gdx.Input.Keys.ENTER;
import static com.badlogic.gdx.Input.Keys.LEFT;
import static com.badlogic.gdx.Input.Keys.RIGHT;
import static com.badlogic.gdx.Input.Keys.S;
import static com.badlogic.gdx.Input.Keys.SPACE;
import static com.badlogic.gdx.Input.Keys.UP;
import static com.badlogic.gdx.Input.Keys.W;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.List;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends ApplicationAdapter implements InputProcessor {
    private SpriteBatch batch;
    int pescadorX = 200;
    int anzueloX= 200;
    int pescadorY = 300;
    int anzueloY = 270;
    private Pescador pescador;
    private Anzuelo anzuelo;
    private Texture pixel;
    private Texture fondo;
    private List<Pez> peces;

    private BitmapFont font;

    private boolean movingLeft, movingRight, reelingOut, reelingIn;

    @Override
    public void create() {
        batch = new SpriteBatch();
        fondo = new Texture("fondo.jpg");
        pescador = new Pescador();
        anzuelo = new Anzuelo(pescador);
        pixel = new Texture("pixel.png");

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);


        float delta = Gdx.graphics.getDeltaTime();

        if (movingLeft) {
            pescador.moveLeft(delta);
            anzuelo.moveLeft(delta);
        }

        if (movingRight) {
            pescador.moveRight(delta);
            anzuelo.moveRight(delta);
        }

        if (reelingOut) {
            movingLeft = false;
            movingRight = false;
            anzuelo.reelOut();

        }

        if (pescador.lastDirection == 0) {
            anzuelo.x = pescador.x - (anzuelo.width / 2) + 3;
        } else if (pescador.lastDirection == 1) {
            anzuelo.x = (float) (pescador.x + pescador.width - (anzuelo.width * 0.65));
        }

        float distance = pescador.y + pescador.height - (anzuelo.y + anzuelo.height);

        if (pescador.x <= 0) {
            pescador.x = 0;
            anzuelo.x = 0;
        }
        if (pescador.x + pescador.width >= Gdx.graphics.getWidth()) {
            pescador.x = Gdx.graphics.getWidth() - pescador.width;
            anzuelo.x = Gdx.graphics.getWidth() - anzuelo.width;
        }

        pescador.update(delta);
        anzuelo.update(delta);

        batch.begin();
        batch.draw(fondo, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        int pixelSize = 2;
        for (float i = 0; i < distance; i++) {
            batch.draw(pixel, pescador.x + pescador.width / 2, pescador.y + pescador.height - i, pixelSize, pixelSize);
        }
        batch.draw(pescador.sprite, pescador.x, pescador.y, pescador.width, pescador.height);
        batch.draw(anzuelo.sprite, anzuelo.x, anzuelo.y, anzuelo.width, anzuelo.height);

        batch.end();
    }


    @Override
    public void dispose() {
        batch.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == A || keycode == LEFT) movingLeft = true;
        if (keycode == D || keycode == RIGHT) movingRight = true;
        if (keycode == S || keycode == DOWN) reelingOut = true;

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == A || keycode == LEFT) movingLeft = false;
        if (keycode == D || keycode == RIGHT) movingRight = false;
        if (keycode == S || keycode == DOWN) reelingOut = false;

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

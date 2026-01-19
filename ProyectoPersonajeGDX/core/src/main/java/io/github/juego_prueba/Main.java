package io.github.juego_prueba;

import static com.badlogic.gdx.Input.Keys.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;
    private Personaje personaje;

    private Texture whitePixel;
    private boolean gameOver = false;
    private BitmapFont font;

    private boolean movingLeft, movingRight, jumping, groundPound;

    private float[][] platforms = {
        {200, 150, 200, 20},
        {500, 300, 250, 20},
        {150, 450, 180, 20},
        {600, 600, 220, 20},
        {800, 750, 250, 20},
        {1200, 900, 180, 20},
        {1500, 1050, 220, 20}
    };

    private Enemigo[] enemigos = {
        new Enemigo(400, 100),
        new Enemigo(1000, 300),
        new Enemigo(1500, 600),
        new Enemigo(2000, 400),
        new Enemigo(2200, 800),
    };


    @Override
    public void create() {
        batch = new SpriteBatch();

        personaje = new Personaje(100, 100);

        whitePixel = new Texture("white.png");

        font = new BitmapFont();
        font.getData().setScale(2f);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        // INPUT
        if (!personaje.dead) {
            if (movingLeft) personaje.moveLeft(delta);
            if (movingRight) personaje.moveRight(delta);
            if (jumping) personaje.jump();
            if (groundPound) personaje.groundPound();
        }


        // UPDATE
        personaje.update(delta);

        // Actualizar enemigos (les pasamos la cámara)
        for (Enemigo enemigo : enemigos) {
            enemigo.update(delta);
        }

        // Dibujar enemigos
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.remove && enemigo.isVisible()) {
                batch.draw(enemigo.sprite, enemigo.x, enemigo.y, enemigo.width, enemigo.height);
            }
        }

        if (personaje.dead && personaje.y + personaje.height < 0) {
            gameOver = true;
        }

        // COLISIÓN JUGADOR - ENEMIGO
        if (!personaje.dead) {
            for (Enemigo enemigo : enemigos) {
                if (enemigo.alive) {  // Solo verificamos enemigos vivos

                    boolean overlapX =
                        personaje.x < enemigo.x + enemigo.width &&
                            personaje.x + personaje.width > enemigo.x;

                    boolean overlapY =
                        personaje.y < enemigo.y + enemigo.height &&
                            personaje.y + personaje.height > enemigo.y;

                    if (overlapX && overlapY) {

                        boolean stomp =
                            personaje.velocityY < 0 &&
                                personaje.y > enemigo.y + enemigo.height / 2;

                        if (stomp) {
                            enemigo.die();
                            personaje.velocityY = 600f;
                        } else {
                            personaje.die();
                        }
                    }
                }
            }
        }



        // Plataformas
        if (!personaje.dead) {
            for (float[] p : platforms) {
                float px = p[0];
                float py = p[1];
                float pw = p[2];
                float ph = p[3];

                boolean overlapX = personaje.x + personaje.width > px && personaje.x < px + pw;
                boolean falling = personaje.velocityY <= 0;
                boolean closeTop = personaje.y <= py + ph && personaje.y >= py + ph - 20;

                if (overlapX && falling && closeTop) {
                    personaje.y = py + ph;
                    personaje.velocityY = 0;
                    personaje.isGrounded = true;
                }
            }
        }


        ScreenUtils.clear(new Color(0.6f, 0.2f, 0.1f, 1));

        if (gameOver) {
            ScreenUtils.clear(Color.BLACK);

            batch.begin();
            font.draw(batch,
                "HAS MUERTO\n\nPRESIONA ENTER PARA REINICIAR",
                Gdx.graphics.getWidth() / 2f - 200,
                Gdx.graphics.getHeight() / 2f
            );
            batch.end();

            return;
        }

        // Dibujar personajes y plataformas
        batch.begin();
        batch.draw(personaje.sprite, personaje.x, personaje.y, personaje.width, personaje.height);
        for (Enemigo enemigo : enemigos) {
            if (!enemigo.remove && enemigo.isVisible()) {
                batch.draw(enemigo.sprite, enemigo.x, enemigo.y, enemigo.width, enemigo.height);
            }
        }

        for (float[] p : platforms) {
            batch.draw(whitePixel, p[0], p[1], p[2], p[3]);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        whitePixel.dispose();
    }

    // INPUT
    @Override
    public boolean keyDown(int keycode) {
        if (keycode == A || keycode == LEFT) movingLeft = true;
        if (keycode == D || keycode == RIGHT) movingRight = true;
        if (keycode == W || keycode == UP || keycode == SPACE) jumping = true;
        if (keycode == S || keycode == DOWN) groundPound = true;
        if (gameOver && keycode == ENTER) {
            dispose();
            create();
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if (keycode == A || keycode == LEFT) movingLeft = false;
        if (keycode == D || keycode == RIGHT) movingRight = false;
        if (keycode == W || keycode == UP || keycode == SPACE) jumping = false;
        if (keycode == S || keycode == DOWN) groundPound = false;
        return false;
    }

    // Métodos no usados
    public boolean keyTyped(char c) {
        return false;
    }

    public boolean touchDown(int x, int y, int p, int b) {
        return false;
    }

    public boolean touchUp(int x, int y, int p, int b) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public boolean touchDragged(int x, int y, int p) {
        return false;
    }

    public boolean mouseMoved(int x, int y) {
        return false;
    }

    public boolean scrolled(float x, float y) {
        return false;
    }
}

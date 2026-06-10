package io.github.semaforos;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public ShapeRenderer sr;

    public OrthographicCamera camara = new OrthographicCamera();

    Preferences prefs;
    Texture txtSemaforoRojo;
    Texture txtSemaforoAmarillo;
    Texture txtSemaforoVerde;
    Texture txtCoche;
    Texture txtPiedra;

    public static Pantalla[] pantallas = {new PantallaInicio(), new PantallaJuego(),new PantallaFin(),new PantallaPausa()};

    //private Texture image;

    //aquí inicias todos
    @Override
    public void create() {
        batch = new SpriteBatch();
        sr = new ShapeRenderer();
        font = new BitmapFont();
        font.getData().setScale(2f);


        //texturas de todos los personajes
        txtCoche = new Texture("coche.png");
        txtPiedra = new Texture("pedra.jpg");
        txtSemaforoAmarillo = new Texture("amarillo.jpg");
        txtSemaforoRojo = new Texture("rojo.jpg");
        txtSemaforoVerde = new Texture("verde.jpg");


        camara.setToOrtho(false,Mundo.ANCHO,Mundo.ALTO);
        camara.update();

        batch.setProjectionMatrix(camara.combined); // SpriteBatch
        sr.setProjectionMatrix(camara.combined); // ShapeRenderer

        Pantalla.setJuego(this);
        irAPantallaInicio();

        prefs = Gdx.app.getPreferences("Semaforo");
    }


    @Override
    public void resize(int width, int height) {
        camara.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO);
        camara.update();
        batch.setProjectionMatrix(camara.combined); // SpriteBatch
        sr.setProjectionMatrix(camara.combined); // ShapeRenderer
    }


    @Override
    public void render() {
        super.render();
//        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
//        batch.begin();
//        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public void irAPantallaInicio() {
        setScreen(pantallas[0]);
    }
    public void irAPantallaJuego(){
        setScreen(pantallas[1]);
    }

    public void irAPantallaFin(){
        setScreen(pantallas[2]);
    }
    public void irAPantallaPausa(){
        setScreen(pantallas[3]);
    }
}

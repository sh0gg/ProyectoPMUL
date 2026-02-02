package io.github.naves;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MainNaves extends Game {

    private static OrthographicCamera camara;
    public static SpriteBatch sb;
    public static BitmapFont fuente;


    @Override
    public void create() {
        camara = new OrthographicCamera();
        sb = new SpriteBatch();
        Pantalla.setJuego(this);
        Gdx.input.setInputProcessor(new ProcesadorDeEntrada(this));
        setScreen(new PantallaInicio());
    }

    @Override
    public void resize(int width, int height) {
        camara.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO);
        camara.update();
        sb.setProjectionMatrix(camara.combined);
        getScreen().resize(width,height);
    }
    @Override
    public void dispose () {
        getScreen().dispose();
        sb.dispose();
        fuente.dispose();
    }

    public void irAPantallaInicio() { setScreen(new PantallaInicio()); }
    public void irAPantallaJuego() { setScreen(new PantallaJuego()); }
    public void irAPantallaFin(float tiempoDelJuego) {
        setScreen(new PantallaFin(tiempoDelJuego));
    }

}

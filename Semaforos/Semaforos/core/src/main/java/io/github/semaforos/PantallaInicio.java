package io.github.semaforos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla {
    float posicionXTextos = Mundo.ANCHO / 2 - 100;

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0f,0f,0f,0.1f);

        juego.batch.begin();

        //Récord para la configuracion actual / sin record
        //R resetea el récord para la configuracion actual
        //otra tecla empieza a jugar
        juego.font.setColor(Color.WHITE);
        //La configuracion es el numero de segundos del juego
        juego.font.draw(juego.batch,"DIFICULTAD:",posicionXTextos, Mundo.ALTO / 2 - 200);
        juego.font.draw(juego.batch,"F: facil  N: normal  D: dificil",posicionXTextos, Mundo.ALTO / 2 - 230 );
        juego.font.draw(juego.batch,"Record actual: " + Mundo.getRecord(), posicionXTextos, Mundo.ALTO / 2 - 270 );
        juego.font.draw(juego.batch,"R para resetear el record",posicionXTextos, Mundo.ALTO / 2 - 300 );

        juego.batch.end();
    }


    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.F:
                Mundo.dificultad = 60f;
                juego.irAPantallaJuego();
                return true;

            case Input.Keys.N:
                Mundo.dificultad = 35f;
                juego.irAPantallaJuego();
                return true;

            case Input.Keys.D:
                Mundo.dificultad = 15f;
                juego.irAPantallaJuego();
                return true;

            case Input.Keys.R:
                //resetea el record solo de la dificultad actual
                Mundo.prefs.remove(Mundo.getDificultadActual());
                Mundo.prefs.flush();
                //Mundo.prefs.clear(); borra todo
                return true;

            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                return true;
        }

        return false;
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        //Mundo.crearSemaforo();
        Mundo.reset();
    }
}

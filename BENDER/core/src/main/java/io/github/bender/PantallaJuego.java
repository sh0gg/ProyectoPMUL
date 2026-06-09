package io.github.bender;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends Pantalla {

    @Override
    public boolean keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.D:
                if (Gdx.input.isKeyPressed(Input.Keys.A)) Mundo.personaje.izquierda();
                else Mundo.personaje.parar();
                break;
            case Input.Keys.A:
                if (Gdx.input.isKeyPressed(Input.Keys.D)) Mundo.personaje.derecha();
                else Mundo.personaje.parar();
                break;
            case Input.Keys.ESCAPE:
                Mundo.fin();
                return true;
            default:
                return false;
        }
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
                Mundo.personaje.estado = Personaje.Estado.IZQUIERDA;
                break;

            case Input.Keys.D:
                Mundo.personaje.estado = Personaje.Estado.DERECHA;
                break;
        }
        return true;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Mundo.stateTime += delta;
        if (Mundo.stateTime > Mundo.stateTimeProximoObjeto) Mundo.creaObjeto();
        Mundo.actualizar(delta);

        juego.sr.begin(ShapeRenderer.ShapeType.Line);
        juego.batch.begin();
        Mundo.dibujar(juego.sr, juego.font, juego.batch);
        juego.batch.end();
        juego.sr.end();

        if (Mundo.fin) {
            if (juego.prefs.getFloat("Tiempo") < Mundo.TiempoTotalDeJuego){
                juego.prefs.putFloat("Tiempo", Mundo.TiempoTotalDeJuego);
                juego.prefs.flush();
            }
            juego.irAPantallaInicio();
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void hide() {
        Mundo.reset();
    }
}

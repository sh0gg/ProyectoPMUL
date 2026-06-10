package io.github.semaforos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaJuego extends Pantalla {
//Mundo.reset en PI.hide

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.95f,0.95f,0.95f,1f);

        Mundo.actualizar(delta);

        juego.batch.begin();

        juego.font.setColor(Color.BLACK);
        //el tiempo restante es dificultad - statetime
        juego.font.draw(juego.batch,"P para PAUSA - Tiempo: " + String.format("%.2f)",Mundo.dificultad - Mundo.stateTime),10,Mundo.ALTO - 20);

        Mundo.dibujar(juego.batch); //dibuja todo

        juego.batch.end();

        if (Mundo.fin) {
            Mundo.setRecord(Mundo.stateTime); //el set comprueba si es record o no
            //Mundo.reset();
            juego.irAPantallaFin();
        }
    }

    @Override
    public void show() {
        //Mundo.crearSemaforo(); mal, al volver de pausa el semáforo se crea otra vez
        Gdx.input.setInputProcessor(this);

    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.P:
                juego.irAPantallaPausa();
                break;
        }
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        Vector3 v = new Vector3(screenX,screenY,0);
        Pantalla.juego.camara.unproject(v);

        boolean tocado = false;

        for (Coche coche : Mundo.coches) {
            if (coche.hitbox.contains(v.x,v.y)){
                tocado = true;
                //podría ser un booleando parado != parado pero bueno
                if (coche.estado == Coche.Estado.MARCHA) {
                    //== not a statement
                    coche.estado = Coche.Estado.PARADO;
                } else {
                    coche.estado = Coche.Estado.MARCHA;
                }
            }
            //MAL creaba piedras donde no tocaba aunque se tocase el coche
//            else {
//                //sólo le mandas las coordenadas
//                Mundo.crearPiedra(v.x,v.y);
//            }
        }
        if (!tocado) {
            Mundo.crearPiedra(v.x,v.y);
        }
        return true;
    }

}



package io.github.semaforos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaPausa extends Pantalla {

    float posicionXTextos = Mundo.ANCHO / 2 - 90;

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0f, 0f, 0f, 1f);

        juego.batch.begin();

        juego.font.setColor(Color.WHITE);
        //juego.batch.draw(juego.fondoPausa,0,0,Mundo.ANCHO,Mundo.ALTO);

        juego.font.draw(juego.batch, "  PAUSA", posicionXTextos, Mundo.ALTO/2);
        juego.font.draw(juego.batch, "V - Volver", posicionXTextos, Mundo.ALTO/2 - 30);
        juego.font.draw(juego.batch, "ESC - Salir", posicionXTextos, Mundo.ALTO/2 - 60);

        juego.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {

            case Input.Keys.V:
                juego.irAPantallaJuego();
                return true;

            case Input.Keys.ESCAPE:
                juego.irAPantallaInicio();
                return true;
        }

        return false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }
}

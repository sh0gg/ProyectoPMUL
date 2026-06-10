package io.github.semaforos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaFin extends Pantalla {

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.2f, 0f, 0f, 1f);

        juego.batch.begin();

        juego.font.setColor(Color.WHITE);

        juego.font.draw(juego.batch,
            "HAS PERDIDO", 50, 150
        );

        juego.font.draw(juego.batch,
            "Tu tiempo: " + String.format("%.2f", Mundo.stateTime), 50, 130
        );

        juego.font.draw(juego.batch,
            "Record: " + String.format("%.2f", Mundo.getRecord()), 50, 90
        );

        juego.font.draw(juego.batch,
            "ESPACIO para volver al inicio y ESC para salir.",
            50,
            60
        );

        juego.batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {

            case Input.Keys.SPACE:
                juego.irAPantallaInicio();
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
        //Mundo.reset();
    }
}

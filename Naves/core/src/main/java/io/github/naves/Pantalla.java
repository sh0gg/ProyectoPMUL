package io.github.naves;

import com.badlogic.gdx.Screen;

public abstract class Pantalla implements Screen {

    protected final MainNaves game;

    public Pantalla(MainNaves game) {
        this.game = game;
    }

    @Override public void show() {}
    @Override public void render(float delta) {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        // No liberar aquí assets globales; los libera MainNaves.dispose()
    }
}

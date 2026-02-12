package cubes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ScreenUtils;

public class PantallaInicio extends Pantalla {

    float stateTime = 0f;
    float textureSwapTimer = 0f;
    float swapTimeSec = 1f;
    Texture insectTexture;

    public PantallaInicio(Main game) {
        super(game);
        layout.setText(font,"Insectos");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0,0,0,1);

        stateTime += delta;

        if (stateTime >= textureSwapTimer){
            textureSwapTimer += swapTimeSec;
            insectTexture = game.insectsTextures.get(MathUtils.random(game.insectsTextures.size-1));
        }


        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(0,World.gameHeight,World.gameWidth,World.topBarHeight);

        shapeRenderer.end();


        spriteBatch.begin();

        font.draw(spriteBatch, layout, (World.gameWidth -layout.width)/2, topBarY + layout.height / 2);
        spriteBatch.draw(insectTexture,(World.gameWidth - game.textureSize)/2,0,game.textureSize,game.textureSize);

        spriteBatch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.F) {
            Gdx.app.exit();

        } else  if(keycode == Input.Keys.R) {
            deleteRecords();

        } else if (keycode >= Input.Keys.NUM_1 && keycode <= Input.Keys.NUM_9) {
            int numInsects = keycode - Input.Keys.NUM_0;
            game.irAPantallaJuego(numInsects);
        }

        return true;
    }

    private void deleteRecords() {
        Preferences preferences = Gdx.app.getPreferences("record.prefs");
        preferences.clear();
        preferences.flush();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}

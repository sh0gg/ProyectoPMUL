package cubes.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Vector2;
import cubes.game.entities.Inset;


public class PantallaJuego extends Pantalla {

    int numInsects = 0;
    int numKills = 0;

    String recordString = "";
    String killsString = "";
    Array<Inset> insects  = new Array<Inset>();

    float recordTime = -1f;
    float stateTime = 0;
    boolean hasRecord = false;

    Preferences preferences = Gdx.app.getPreferences("record.prefs");

    public PantallaJuego(Main game, int numInsects) {
        super(game);
        this.numInsects = numInsects;

        loadRecord();
        createInsects(numInsects);
    }

    private void loadRecord() {
        recordTime = preferences.getFloat("record_"+numInsects,-1);
        hasRecord = recordTime != -1;
        recordString = hasRecord? String.format("Record: %.2fs",recordTime) : "Record: sin record";
    }

    private void createInsects(int numInsects) {
        float x = World.halfGameWidth - game.halfTextureSize;
        float y = World.halfGameHeight - game.halfTextureSize;
        insects.add(new Inset(x,y,game.textureSize,50,game.insectsTextures));
    }

    private void saveRecord(float stateTime) {
        preferences.putFloat("record_"+numInsects, stateTime);
        preferences.flush();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        stateTime += delta;

        drawShapes(delta);
        drawSprites(delta);
    }

    public void drawShapes(float delta){
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        for (Vector2 point : game.points){
            shapeRenderer.circle(point.x,point.y,game.halfTextureSize);
        }
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(0,World.gameHeight,World.gameWidth,World.topBarHeight);
        shapeRenderer.end();
    }


    public void drawSprites(float delta){
        spriteBatch.begin();
        layout.setText(font,String.format("%s%nTimer: %.2fs%nKills: %s",recordString,stateTime,killsString));
        font.draw(spriteBatch, layout, 8,topBarY + layout.height / 2);

        for (Inset inset : insects) {
            inset.update(delta);
            inset.render(spriteBatch);
        }

        spriteBatch.end();
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates (pixels) to world coordinates (game units)
        Vector3 v3ScreenCoord = new Vector3(screenX, screenY, 0);
        Vector3 v3WorldCoord = camera.unproject(v3ScreenCoord);

        Vector2 v2 = new Vector2(v3WorldCoord.x, v3WorldCoord.y);

        for (Inset inset : insects) {
            //System.out.println(inset);
            if (inset.isHit(v2)){
                numKills++;
                game.points.add(v2);

                if(numKills >= numInsects){
                    if (hasRecord){
                        if (stateTime < recordTime){
                            saveRecord(stateTime);
                        }

                    }else{
                        saveRecord(stateTime);
                    }

                    game.irAPantallaInicio();
                }
                else{
                    inset.nextInsect();
                    killsString = "I".repeat(numKills);
                }

                return true;
            }
        }
        return true;
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



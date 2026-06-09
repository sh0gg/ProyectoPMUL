package cubes.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import cubes.game.World;

public class Inset {

    public float x,y;
    float size;
    Rectangle hitbox;

    float speed;
    int dirX = -1, dirY = -1;

    //Out of Bounce
    float OOBOffsetX, OOBOffsetY;

    int textureID = 0;
    Texture texture;
    Array<Texture> textures;


    public Inset(float x, float y, float size, float speed, Array<Texture> textures) {
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(x,y,size,size);
        this.size = size;
        this.speed = speed;
        this.textures = textures;
        texture = textures.get(textureID);
        OOBOffsetX = World.gameWidth - size;
        OOBOffsetY = World.gameHeight - size;
    }

    public void update(float delta){
        x += (speed*dirX) * delta;
        y += (speed*dirY) * delta;

        if (x < 0 || x > OOBOffsetX) {
            dirX = -dirX;
            x = Math.min(Math.max(x,0),OOBOffsetX);

        }
        if (y < 0 || y > OOBOffsetY){
            dirY = -dirY;
            y = Math.min(Math.max(y,0),OOBOffsetY);
        }

        hitbox.x = x;
        hitbox.y = y;
    }

    public void render(SpriteBatch sb) {
        sb.draw(texture, x, y, size,size);
    }

    public boolean isHit(Vector2 v2){
        return hitbox.contains(v2);
    }

    public void nextInsect() {
        texture = textures.get(++textureID);
    }

    @Override
    public String toString() {
        return hitbox.toString();
    }
}

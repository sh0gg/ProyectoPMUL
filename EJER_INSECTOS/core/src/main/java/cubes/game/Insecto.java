package cubes.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public class Insecto {

    public float x, y;
    float size;
    Rectangle hitbox;

    float velocidad;
    int direccionX = -1, direccionY = -1;

    int textureID = 0;
    Texture texture;
    Array<Texture> textures;

    public Insecto(float x, float y, float size, float velocidad, Array<Texture> textures) {
        this.x = x;
        this.y = y;
        this.hitbox = new Rectangle(x, y, size, size);
        this.size = size;
        this.velocidad = velocidad;
        this.textures = textures;
        texture = textures.get(textureID);
    }

    public void update(float delta) {
        x += (velocidad * direccionX) * delta;
        y += (velocidad * direccionY) * delta;

        // LÍMITES X
        if (x < 0) {
            x = 0;
            direccionX = 1;
        } else if (x > Mundo.ANCHO_MUNDO - size) {
            x = Mundo.ANCHO_MUNDO - size;
            direccionX = -1;
        }

        // LÍMITES Y (SOLO área de juego 0..ALTO_MUNDO)
        if (y < 0) {
            y = 0;
            direccionY = 1;
        } else if (y > Mundo.ALTO_MUNDO - size) {
            y = Mundo.ALTO_MUNDO - size;
            direccionY = -1;
        }

        hitbox.setPosition(x, y);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, x, y, size, size);
    }

    public boolean isHit(Vector2 v2) {
        return hitbox.contains(v2);
    }

    public void nextInsecto() {
        textureID++;
        if (textureID >= textures.size)
            textureID = 0;
        texture = textures.get(textureID);
    }
}

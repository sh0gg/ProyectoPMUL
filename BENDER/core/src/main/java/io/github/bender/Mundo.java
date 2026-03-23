package io.github.bender;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mundo {
    public static final int ANCHO = 800;
    public static final int ALTO = 600;
    private static float TIEMPO_MIN_ENTRE_OBJETOS = 1f;
    private static float TIEMPO_MAX_ENTRE_OBJETOS = 2f;
    private static float TIEMPO_INICIAL = 10f;
    private static float TIEMPO_BONUS = 5f;
    private static float DIMENSION = 100;
    public static final int VIDAS_INICIALES = 3;
    static Random random = new Random();
    static Personaje personaje = new Personaje(0, 0, DIMENSION, DIMENSION);
    static Array<Objeto> objetos = new Array<>();
    static int vidas = VIDAS_INICIALES;
    static boolean fin = false;

    static Rectangle deposito = new Rectangle(ANCHO - DIMENSION, 0, DIMENSION, DIMENSION);
    public static float TiempoTotalDeJuego, stateTime, stateTimeProximoObjeto, tiempoRestante = TIEMPO_INICIAL;

    public static void creaObjeto() {
        objetos.add(Objeto.creaNuevoObjeto());
        stateTimeProximoObjeto = stateTime + getRandomProximoObjeto();
    }

    public static void eliminar(Objeto objeto) {
        objetos.removeValue(objeto, true);
    }

    public static float getRandomProximoObjeto() {
        return TIEMPO_MIN_ENTRE_OBJETOS + random.nextFloat() * (TIEMPO_MAX_ENTRE_OBJETOS - TIEMPO_MIN_ENTRE_OBJETOS);
    }

    public static void actualizar(float delta) {
        TiempoTotalDeJuego += delta;
        tiempoRestante -= delta;
        if (tiempoRestante <= 0) {
            fin();
        }
        personaje.actualizar(delta);
        for (int i = 0; i < objetos.size; i++) {
            objetos.get(i).actualizar(delta);
            if (Intersector.overlaps(personaje.pibe, objetos.get(i).hitbox)) {
                if (objetos.get(i) instanceof Cerveza) {
                    personaje.cargado = true;
                    eliminar(objetos.get(i));
                } else if (objetos.get(i) instanceof Manzana) {
                    vidas--;
                    eliminar(objetos.get(i));
                }
            }
        }

        if (Intersector.overlaps(personaje.pibe, deposito) && personaje.cargado) {
            personaje.cargado = false;
            tiempoRestante += TIEMPO_BONUS;
        }

        if (vidas <= 0) {
            fin();
        }
    }

    public static void fin() {
        fin = true;
    }

    public static void reset() {
        vidas = VIDAS_INICIALES;
        personaje.x = 0;
        personaje.cargado = false;
        objetos.clear();
        tiempoRestante = TIEMPO_INICIAL;
        stateTime = 0;
        stateTimeProximoObjeto = 0;
        TiempoTotalDeJuego = 0;
        fin = false;
    }

    public static void dibujar(ShapeRenderer sr, BitmapFont font, SpriteBatch batch) {
        personaje.dibujar(sr);
        sr.rect(deposito.x, deposito.y, deposito.width, deposito.height);
        for (int i = 0; i < objetos.size; i++) {
            objetos.get(i).dibujar(sr);
        }
        font.draw(batch, "Tiempo: " + String.valueOf((int) tiempoRestante), 0, ALTO);
        font.draw(batch, "Vidas: " + String.valueOf(vidas), ANCHO - DIMENSION, ALTO);
    }
}

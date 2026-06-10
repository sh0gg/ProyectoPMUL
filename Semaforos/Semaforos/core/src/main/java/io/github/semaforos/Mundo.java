package io.github.semaforos;

import static io.github.semaforos.Semaforo.Estado.ROJO;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class Mundo {

    public static final float ALTO = 800;
    public static final float ANCHO = 1000;
    public static final float ALTO_JUEGO = 650; //hay una barra superior

    public static Semaforo semaforo;
    public static Array<Coche> coches = new Array<>();
    public static Array<Piedra> piedras = new Array<>();

    public static float dificultad = 25f; //variable por dificultad?
    public static final float TIEMPO_BONUS = 5f;
    public static float TPO_SEMAFORO = 4f;
    public static float ultimoCambioSemaforo = 0;


    public static float posicionXSemaforo = Mundo.ANCHO - 200;
    public static float posicionYSemaforo = Mundo.ALTO_JUEGO - 50;


    public static float tamanhoPersonajes = 90; //es una jaimitada? si pero supongo que es mejor que dejar el 40 en el random
    static float stateTime = 0;
    public static Preferences prefs = Gdx.app.getPreferences("Semaforo");

    static boolean fin = false;

    public static void crearCoche() {
        //los coches salen aleatoriamente en alto y velocidad de izq a derecha
        float y = MathUtils.random(0,ALTO_JUEGO);
        float velocidad = MathUtils.random(50,100);
        coches.add(new Coche(0,y,tamanhoPersonajes,tamanhoPersonajes,velocidad,Pantalla.juego.txtCoche));
    }

    public static void crearPiedra(float x, float y){
        //la piedra sale sólo si haces click y no hay coche
        piedras.add(new Piedra(x,y,tamanhoPersonajes,tamanhoPersonajes,0,Pantalla.juego.txtPiedra));
    }

    public static void crearSemaforo(){
        semaforo = new Semaforo(posicionXSemaforo,posicionYSemaforo,tamanhoPersonajes,tamanhoPersonajes,0,Pantalla.juego.txtSemaforoVerde);
    }


    public static void actualizar (float delta) {
        stateTime += delta;
        ultimoCambioSemaforo += delta;

        //cambios de semaforo
        if (ultimoCambioSemaforo > TPO_SEMAFORO) {
            cambiarSemaforo();
            ultimoCambioSemaforo = 0;
        }

        //actualizar todo
        semaforo.actualizar(delta);

        for (Coche coche: coches) {
            coche.actualizar(delta);
        }

        for (Piedra piedra : piedras) {
            piedra.actualizar(delta);
        }

        //los coches salen aleatoriamente
        //0.01f  -> 1% de posibilidad por frame de que se cree un coche
        if (MathUtils.randomBoolean(0.01f)) {
            crearCoche();
        }

        //comprobar colisiones coches
        comprobarColisionesCoches(delta);

        //fin si se pasa del tiempo
        if (stateTime >= dificultad) {
            fin = true;
        }
    }

    public static void cambiarSemaforo() {
        switch (semaforo.estado) {
            case ROJO:
                semaforo.estado = Semaforo.Estado.VERDE;
                semaforo.textura = Pantalla.juego.txtSemaforoVerde;
                //breakn't
                break;

            case AMARILLO:
                semaforo.estado = ROJO;
                semaforo.textura = Pantalla.juego.txtSemaforoRojo;
                break;

            case VERDE:
                semaforo.estado = Semaforo.Estado.AMARILLO;
                semaforo.textura = Pantalla.juego.txtSemaforoAmarillo;
                break;
        }
    }

    public static void comprobarColisionesCoches(float delta) {
        //sólo desaparede el coche, no la piedra

        for (int i = 0 ; i < coches.size; i++ ){
            Coche coche = coches.get(i);

            coche.actualizar(delta);
            for (Piedra piedra : piedras) {
                if (coche.hitbox.overlaps(piedra.hitbox)) {
                    coches.removeIndex(i);
                    i--;

                    //corregido: sin el break sigue chocando y logicamente te mueres
                    break;
                }
            }

            //se pierde si se sale por la derecha y el estado es rojo
                //por la derecha no es coche.x >= 0
            //si no, se da un bonus de tiempo
            if (coche.x >= ANCHO) {
                if (semaforo.estado == ROJO) {
                    fin = true;
                }
                else {
                    dificultad += TIEMPO_BONUS;
                }

                //FALTABA: eliminar coche
                coches.removeIndex(i);
                i--;
            }


        }


    }

    //cambio de sr a batch
    public static void dibujar(SpriteBatch batch) {
        semaforo.dibujar(batch);

        for (Coche coche : coches) {
            coche.dibujar(batch);
        }

        for (Piedra piedra : piedras ) {
            piedra.dibujar(batch);
        }

    }

    public static void reset() {
        //reiniciar tiempo, dificultad, coches, semaforo, fin y manchas
        crearSemaforo();
        coches.clear();
        piedras.clear();

        stateTime = 0;
        fin = false;
    }

    public static String getDificultadActual(){
       if (dificultad == 60f) return "facil";
       if (dificultad == 35f) return "normal";
       return "dificil";
    }

    public static float getRecord() {
        return prefs.getFloat(getDificultadActual(), 0);
    }

    public static void setRecord(float tiempo) {
        float recordActual = getRecord();

        if (tiempo > recordActual) {
            prefs.putFloat(getDificultadActual(), tiempo);
            prefs.flush();
        }
    }

}

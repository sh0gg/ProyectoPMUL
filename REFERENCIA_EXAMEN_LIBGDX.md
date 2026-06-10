# REFERENCIA EXAMEN LIBGDX — Todo lo relevante

> Documento de consulta rápida. Usa Ctrl+F para buscar.
> Basado en los ejercicios: Globos, Coches (Frogger), Cazador de Sombras, Semáforos.
> Estructura del índice abajo para saltar rápido.

---

## ÍNDICE (busca con Ctrl+F el texto entre [corchetes])

- [INDICE-CHECKLIST] — Qué hacer al recibir el enunciado
- [INDICE-ESTRUCTURA] — Los 6 ficheros base y para qué sirve cada uno
- [INDICE-MAIN] — Main.java explicado
- [INDICE-MUNDO] — Mundo.java explicado
- [INDICE-PANTALLA] — Pantalla.java (clase base) explicado
- [INDICE-INICIO] — PantallaInicio explicado
- [INDICE-JUEGO] — PantallaJuego explicado
- [INDICE-RESULTADO] — PantallaResultado explicado
- [INDICE-ENTIDADES] — Cómo diseñar entidades
- [INDICE-INPUT] — Teclado y touch
- [INDICE-CAMARA] — Cámara, unproject, coordenadas
- [INDICE-SPAWN] — Spawn de entidades
- [INDICE-COLISIONES] — Todos los tipos de colisión
- [INDICE-ARRAYS] — Gestión de arrays (eliminar mientras iteras)
- [INDICE-HUD] — Barras de información
- [INDICE-RENDER] — Orden de dibujado, sr vs batch
- [INDICE-RECORDS] — Preferences
- [INDICE-ESTADOS] — Máquina de estados, invencibilidad
- [INDICE-HERENCIA] — Jerarquía de clases
- [INDICE-FORMAS] — Dibujar con ShapeRenderer
- [INDICE-DIFICULTAD] — Modos y selector
- [INDICE-MATHUTILS] — Funciones útiles
- [INDICE-ERRORES] — Errores comunes y soluciones
- [INDICE-TRUCOS] — Trucos de los ejercicios concretos

---

## [INDICE-CHECKLIST] CHECKLIST AL RECIBIR EL ENUNCIADO

Antes de tocar código, lee el enunciado y marca:

1. **INPUT** — ¿teclado, touch/click, o ambos?
2. **ENTIDADES** — ¿una sola (un personaje) o varias (array)?
3. **MOVIMIENTO** — ¿fluido (con delta) o por casillas (saltos)?
4. **COLISIONES** — ¿rect-rect, click sobre sprite, círculo-rect?
5. **HUD** — ¿barra arriba (TOP_BAR) o abajo (ALTO_BARRA)?
6. **RECORD** — ¿sí/no? ¿por tiempo, por puntos? ¿por modo?
7. **FIN** — ¿pantalla resultado o reinicio dentro de juego?
8. **DIFICULTADES** — ¿hay modos? ¿qué cambia entre ellos?

Orden de trabajo recomendado:
1. Ajustar constantes en Mundo (ANCHO, ALTO, TOP_BAR)
2. Que compile y dibuje (pantalla + texto del tiempo)
3. Input correcto (teclado o unproject)
4. Una entidad que se mueva
5. Array + spawn
6. Colisiones
7. Game over + reset
8. Records (si da tiempo)
9. Pantallas extra y estética

REGLA DE ORO: primero que funcione el núcleo, luego colisiones y fin, luego lo accesorio.

---

## [INDICE-ESTRUCTURA] LOS 6 FICHEROS BASE

| Fichero | Qué hace |
|---|---|
| Main.java | extends Game. Crea recursos (batch, sr, font, camera). Arranca primera pantalla. |
| Mundo.java | Constantes del juego + estado global static + métodos de records. |
| Pantalla.java | Clase abstracta base. extends InputAdapter implements Screen. NO TOCAR. |
| PantallaInicio.java | Título, record, selección de dificultad, tecla para jugar. |
| PantallaJuego.java | TODA la lógica del juego: update, colisiones, dibujado, HUD. |
| PantallaResultado.java | Muestra victoria/derrota, tiempo, record. Tecla para volver. |

Las entidades (Globo, Vehiculo, Jugador, Cazador, Bala, Sombra...) son ficheros aparte.

---

## [INDICE-MAIN] Main.java

```java
public class Main extends Game {
    public SpriteBatch batch;
    public ShapeRenderer sr;
    public BitmapFont font;
    public OrthographicCamera camera;

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        sr = new ShapeRenderer();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO + Mundo.TOP_BAR);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);

        setScreen(new PantallaInicio(this));
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO + Mundo.TOP_BAR);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (sr != null) sr.dispose();
        if (font != null) font.dispose();
    }
}
```

CLAVES:
- `extends Game` (NO ApplicationAdapter — el profe usa Game + pantallas).
- Los recursos (batch, sr, font, camera) son PUBLIC para que las pantallas los compartan.
- `setToOrtho(false, ...)` — el `false` significa eje Y hacia arriba (origen abajo-izquierda).
- El alto de la cámara es `ALTO + TOP_BAR` para dejar hueco a la barra de HUD arriba.
- `combined` es la matriz de proyección que conecta cámara con batch/sr.
- `resize` repite la config de cámara para que no se deforme al cambiar tamaño de ventana.

---

## [INDICE-MUNDO] Mundo.java

```java
public class Mundo {
    public static final float ANCHO = 640;   // ajusta según enunciado
    public static final float ALTO  = 480;
    public static final float TOP_BAR = 40;   // 0 si no hay barra arriba

    public static final float MITAD_ANCHO = ANCHO / 2f;
    public static final float MITAD_ALTO  = ALTO  / 2f;

    public static final boolean DEBUG = false;

    // Constantes del juego (ejemplos)
    public static final int MAX_ESCAPADOS = 5;
    public static final int TOTAL_OBJETIVOS = 15;
    public static final int MAX_BALAS = 5;

    // Estado global (se resetea al empezar partida)
    public static int puntos = 0;
    public static int vidas = 3;
    public static boolean victoria = false;
    public static float tiempoFinal = 0f;

    // Dificultades (ejemplo)
    public static int NUM_CARRILES = 10;
    public static void setNormal()  { NUM_CARRILES = 10; }
    public static void setDificil() { NUM_CARRILES = 20; }

    // Métodos auxiliares
    public static float getTamCasilla() { return ALTO / NUM_CARRILES; }

    // RECORDS
    public static void guardarRecord(float tiempo) {
        Preferences prefs = Gdx.app.getPreferences("record.pref");
        float recAnterior = prefs.getFloat("record", Float.MAX_VALUE);
        if (tiempo < recAnterior) {     // "menos tiempo mejor"
            prefs.putFloat("record", tiempo);
            prefs.flush();
        }
    }

    public static float getRecord() {
        return Gdx.app.getPreferences("record.pref").getFloat("record", 0f);
    }

    public static void borrarRecord() {
        Preferences prefs = Gdx.app.getPreferences("record.pref");
        prefs.clear();
        prefs.flush();
    }
}
```

CLAVES:
- TODO es `static` — Mundo no se instancia, se accede como `Mundo.puntos`, `Mundo.ANCHO`.
- Las constantes con `final`, el estado mutable sin `final`.
- El estado global (puntos, vidas, victoria, tiempoFinal) vive aquí para que TODAS las pantallas lo lean sin pasarlo por constructor.
- Para acceder/modificar desde cualquier clase: `Mundo.puntos++`, `Mundo.vidas = 3`, etc. NO necesitas getters.

---

## [INDICE-PANTALLA] Pantalla.java (NO TOCAR)

```java
public abstract class Pantalla extends InputAdapter implements Screen {
    protected final Main game;
    public OrthographicCamera camera;
    public SpriteBatch batch;
    public ShapeRenderer sr;
    public BitmapFont font;

    public Pantalla(Main game) {
        this.game = game;
        this.camera = game.camera;
        this.batch = game.batch;
        this.sr = game.sr;
        this.font = game.font;
        batch.setProjectionMatrix(camera.combined);
        sr.setProjectionMatrix(camera.combined);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);   // CRÍTICO
    }

    @Override public void resize(int w, int h) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
```

CLAVES:
- `extends InputAdapter` — para no tener que implementar los 8 métodos de input, solo los que uses.
- `implements Screen` — para que funcione con setScreen().
- Toma los recursos de Main (no crea nuevos).
- `show()` registra esta pantalla como receptora del input — ESTO ES CRÍTICO.
- Los métodos vacíos de Screen están aquí para no repetirlos en cada pantalla.

ERROR MORTAL: Si en PantallaJuego sobreescribes `show()` con un cuerpo VACÍO, anulas el `setInputProcessor` y EL INPUT DEJA DE FUNCIONAR. Si no necesitas nada especial en show(), NO lo sobreescribas y deja que actúe el de Pantalla. Si lo sobreescribes, llama a `super.show()`.

---

## [INDICE-INICIO] PantallaInicio.java

```java
public class PantallaInicio extends Pantalla {

    public PantallaInicio(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        font.draw(batch, "TITULO", Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO + 60);
        font.draw(batch, "Record: " + String.format("%.2f", Mundo.getRecord()) + "s",
                  Mundo.MITAD_ANCHO - 30, Mundo.MITAD_ALTO + 20);
        font.draw(batch, "ESPACIO para jugar  |  R para borrar record", 20, 25);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.SPACE:
                game.setScreen(new PantallaJuego(game));
                break;
            case Input.Keys.R:
                Mundo.borrarRecord();
                break;
            case Input.Keys.ESCAPE:
                Gdx.app.exit();
                break;
        }
        return true;
    }
}
```

CLAVES:
- `render` solo dibuja texto (título, record, instrucciones).
- `keyDown` recibe `int keycode` (NO Input.Keys), se compara con `Input.Keys.X`.
- SIEMPRE `return true` al final de keyDown.
- ScreenUtils.clear(r,g,b,a) limpia la pantalla con un color.

---

## [INDICE-JUEGO] PantallaJuego.java — ESTRUCTURA GENERAL

Esqueleto del render() en el orden correcto:

```java
@Override
public void render(float delta) {
    ScreenUtils.clear(0.1f, 0.1f, 0.1f, 1);
    stateTime += delta;

    // 1. TIMERS (gracia, etc.)
    if (tiempoGracia > 0) tiempoGracia -= delta;

    // 2. SPAWN
    spawnTimer -= delta;
    if (spawnTimer <= 0) {
        entidades.add(new MiEntidad());
        spawnTimer = MathUtils.random(0.5f, 2f);
    }

    // 3. UPDATE de todas las entidades
    jugador.update(delta);
    for (MiEntidad e : entidades) e.update(delta);

    // 4. COLISIONES Y LIMPIEZA (recorre al revés)
    for (int i = entidades.size - 1; i >= 0; i--) {
        MiEntidad e = entidades.get(i);
        if (e.destruido) { entidades.removeIndex(i); continue; }
        if (jugador.hitbox.overlaps(e.hitbox)) {
            // ... colisión
        }
    }

    // 5. CONDICIONES DE FIN
    if (Mundo.vidas <= 0) terminarPartida(false);
    if (Mundo.puntos >= OBJETIVO) terminarPartida(true);

    // 6. DIBUJAR FORMAS (ShapeRenderer)
    sr.begin(ShapeRenderer.ShapeType.Filled);
    for (MiEntidad e : entidades) e.render(sr);
    jugador.render(sr);
    sr.end();

    // 7. DEBUG hitboxes (opcional)
    if (Mundo.DEBUG) {
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.RED);
        // sr.rect(e.hitbox.x, e.hitbox.y, e.hitbox.width, e.hitbox.height);
        sr.end();
    }

    // 8. HUD (SpriteBatch)
    batch.begin();
    font.draw(batch, "Tiempo: " + String.format("%.2f", stateTime), 10, Mundo.ALTO + Mundo.TOP_BAR - 10);
    batch.end();
}

private void terminarPartida(boolean victoria) {
    Mundo.victoria = victoria;
    Mundo.tiempoFinal = stateTime;
    Mundo.guardarRecord(stateTime);
    game.setScreen(new PantallaResultado(game));
}
```

CLAVES:
- El orden importa: timers → spawn → update → colisiones → fin → dibujar → HUD.
- `terminarPartida()` centraliza el fin para no repetir código.
- NO pongas `game.setScreen` justo después de llamar `terminarPartida` (ya lo hace dentro → setScreen doble).
- El reset del estado va en el CONSTRUCTOR de PantallaJuego, no en el render.

---

## [INDICE-RESULTADO] PantallaResultado.java

```java
public class PantallaResultado extends Pantalla {

    public PantallaResultado(Main game) {
        super(game);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();

        if (Mundo.victoria) {
            font.setColor(Color.YELLOW);
            font.draw(batch, "VICTORIA!", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 60);
        } else {
            font.setColor(Color.RED);
            font.draw(batch, "GAME OVER", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 60);
        }
        font.setColor(Color.WHITE);
        font.draw(batch, "Tiempo: " + String.format("%.2f", Mundo.tiempoFinal) + "s", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO + 20);
        font.draw(batch, "Record: " + String.format("%.2f", Mundo.getRecord()) + "s", Mundo.MITAD_ANCHO - 40, Mundo.MITAD_ALTO - 10);
        font.draw(batch, "ESPACIO para volver", 20, 25);
        batch.end();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            game.setScreen(new PantallaInicio(game));
        }
        return true;
    }
}
```

CLAVES:
- Lee el estado de Mundo (victoria, tiempoFinal, puntos) que se guardó antes de cambiar de pantalla.
- `font.setColor()` cambia el color del texto. SIEMPRE resetea a WHITE después si usas colores.

---

## [INDICE-ENTIDADES] DISEÑO DE ENTIDADES

Patrón general de una entidad:

```java
public class MiEntidad {
    float x, y, ancho, alto;
    float velocidad;
    int direccion = 1;       // 1, -1, o 0
    Rectangle hitbox;
    boolean destruido = false;
    Color color;

    public MiEntidad(float x, float y) {
        this.x = x;
        this.y = y;
        this.ancho = 40;
        this.alto = 40;
        this.velocidad = 150f;
        this.color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
        this.hitbox = new Rectangle(x, y, ancho, alto);
    }

    void update(float delta) {
        x += velocidad * direccion * delta;   // movimiento con delta
        hitbox.setPosition(x, y);              // CRÍTICO: actualizar hitbox
        if (x > Mundo.ANCHO) destruido = true; // marcar para eliminar
    }

    void render(ShapeRenderer sr) {
        sr.setColor(color);
        sr.rect(x, y, ancho, alto);
    }

    boolean isHit(float tx, float ty) {        // para click
        return hitbox.contains(tx, ty);
    }
}
```

REGLAS DE ORO DE ENTIDADES:
1. SIEMPRE `hitbox.setPosition(x, y)` al final de cada `update`. Si no, el hitbox se queda donde estaba y las colisiones/clicks fallan.
2. El movimiento SIEMPRE multiplica por `delta`: `x += velocidad * delta`. Sin delta, la velocidad depende de los FPS.
3. El color se asigna UNA VEZ en el constructor, no en render (si no, parpadea).
4. Usar `boolean destruido` para marcar y eliminar después, no eliminar dentro del update.

---

## [INDICE-INPUT] INPUT — TECLADO Y TOUCH

### Teclado básico (acción instantánea, ej: disparar, saltar de casilla)
```java
@Override
public boolean keyDown(int keycode) {
    switch (keycode) {
        case Input.Keys.UP:    jugador.mover(0, 1);  break;
        case Input.Keys.DOWN:  jugador.mover(0, -1); break;
        case Input.Keys.LEFT:  jugador.mover(-1, 0); break;
        case Input.Keys.RIGHT: jugador.mover(1, 0);  break;
        case Input.Keys.SPACE: disparar();           break;
        case Input.Keys.ESCAPE: game.setScreen(new PantallaInicio(game)); break;
    }
    return true;
}
```

### Teclado con movimiento continuo (mantener pulsado, ej: Dedo subiendo)
Se usa keyDown para empezar y keyUp para parar:
```java
@Override
public boolean keyDown(int keycode) {
    if (keycode == Input.Keys.UP)   personaje.direccion = 1;
    if (keycode == Input.Keys.DOWN) personaje.direccion = -1;
    return true;
}
@Override
public boolean keyUp(int keycode) {
    if (keycode == Input.Keys.UP || keycode == Input.Keys.DOWN)
        personaje.direccion = 0;   // se para al soltar
    return true;
}
```

Y en el update de la entidad: `y += velocidad * direccion * delta;` (si direccion=0, no se mueve)

### Comprobar tecla mantenida en render (alternativa)
```java
if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += velocidad * delta;
```

### Touch / click
```java
@Override
public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    Vector3 v3 = new Vector3(screenX, screenY, 0);
    camera.unproject(v3);   // convierte coords de pantalla a coords del mundo
    // ahora v3.x, v3.y son coordenadas del mundo
    for (MiEntidad e : entidades) {
        if (e.isHit(v3.x, v3.y)) {
            e.destruido = true;
            break;          // solo una entidad por click
        }
    }
    return true;
}
```

TECLAS ÚTILES: Input.Keys.UP/DOWN/LEFT/RIGHT, SPACE, ESCAPE, R, F, N, D, A, Z, NUM_0 a NUM_9.

---

## [INDICE-CAMARA] CÁMARA, UNPROJECT, COORDENADAS

CONCEPTO CLAVE: Hay DOS sistemas de coordenadas:
- COORDENADAS DE PANTALLA: píxeles reales de la ventana. Origen ARRIBA-IZQUIERDA. Y crece hacia abajo.
- COORDENADAS DEL MUNDO: las que tú defines (Mundo.ANCHO x Mundo.ALTO). Origen ABAJO-IZQUIERDA. Y crece hacia arriba.

Cuando tocas la pantalla, libGDX te da coords de PANTALLA. Para saber qué entidad tocaste necesitas convertirlas a coords del MUNDO con `unproject`:

```java
Vector3 v3 = new Vector3(screenX, screenY, 0);
camera.unproject(v3);
// v3.x, v3.y ahora en coordenadas del mundo
```

POR QUÉ Vector3 y no Vector2: unproject necesita la Z para los cálculos de proyección (aunque en 2D sea 0).

Configuración de cámara (en Main):
```java
camera.setToOrtho(false, Mundo.ANCHO, Mundo.ALTO + Mundo.TOP_BAR);
```
- `false` = Y hacia arriba (lo normal en juegos).
- El alto incluye TOP_BAR para dejar sitio a la barra de HUD arriba.

---

## [INDICE-SPAWN] SPAWN DE ENTIDADES

### Spawn por TIMER (cada X segundos)
```java
float spawnTimer = 2f;   // campo

// en render:
spawnTimer -= delta;
if (spawnTimer <= 0) {
    entidades.add(new MiEntidad());
    spawnTimer = MathUtils.random(0.5f, 2f);  // resetear (fijo o aleatorio)
}
```

### Spawn PROBABILÍSTICO (X% de probabilidad por frame) — como en Semáforos
```java
if (MathUtils.randomBoolean(0.01f)) {   // 1% por frame
    entidades.add(new MiEntidad());
}
```

### Spawn con LÍMITE (no más de N en pantalla) — como las balas del Dedo
```java
if (entidades.size < Mundo.MAX_ENTIDADES) {
    entidades.add(new MiEntidad());
}
```

### Spawn con CONTADOR TOTAL (solo lanzar N en toda la partida) — como los Globos
```java
int lanzados = 0;   // campo
if (spawnTimer <= 0 && lanzados < Mundo.TOTAL) {
    entidades.add(new MiEntidad());
    lanzados++;
    spawnTimer = ...;
}
```

CUÁNDO USAR CADA UNO:
- Timer: enemigos que aparecen a ritmo regular.
- Probabilístico: aparición más orgánica/aleatoria (tráfico).
- Límite: balas, proyectiles (un "cargador").
- Contador total: cuando el juego acaba al agotar N objetivos.

---

## [INDICE-COLISIONES] TIPOS DE COLISIÓN

### Rect-Rect (la más común)
```java
if (a.hitbox.overlaps(b.hitbox)) {
    // colisión
}
```
`overlaps` es de la clase Rectangle de libGDX.

### Click/Touch sobre Rectangle
```java
if (hitbox.contains(tx, ty)) {  // tx,ty del unproject
    // tocado
}
```

### Click sobre ÓVALO/ELIPSE (ej: globo dibujado con sr.ellipse)
```java
boolean isHit(float tx, float ty) {
    float cx = x + ancho/2;     // centro del óvalo
    float cy = y + alto/2;
    float dx = (tx - cx) / (ancho/2);
    float dy = (ty - cy) / (alto/2);
    return dx*dx + dy*dy <= 1f;   // dentro de la elipse
}
```

### Click sobre CÍRCULO
```java
boolean isHit(float tx, float ty) {
    float cx = x + radio, cy = y + radio;
    float dx = tx - cx, dy = ty - cy;
    return dx*dx + dy*dy <= radio*radio;
}
```

### Colisión CÍRCULO-RECT (manual, con clamp)
```java
float closestX = MathUtils.clamp(cx, rect.x, rect.x + rect.width);
float closestY = MathUtils.clamp(cy, rect.y, rect.y + rect.height);
float dx = cx - closestX, dy = cy - closestY;
boolean hit = dx*dx + dy*dy <= radio*radio;
```

### Con Intersector (libGDX) — para hitboxes complejas
```java
if (Intersector.overlaps(circulo, rectangulo)) { ... }  // Circle y Rectangle
```

### Colisión "solo entre formas iguales" (camuflaje, ej: Cazador)
```java
if (bala.hitbox.overlaps(sombra.hitbox)) {
    if (bala.forma == sombra.forma) {   // solo destruye si coinciden
        sombra.destruida = true;
    }
    bala.destruida = true;
}
```

---

## [INDICE-ARRAYS] GESTIÓN DE ARRAYS

Usar `com.badlogic.gdx.utils.Array` (NO ArrayList de Java):
```java
Array<MiEntidad> entidades = new Array<>();
entidades.add(e);
entidades.removeIndex(i);
entidades.removeValue(e, true);  // true = comparar por identidad
entidades.size;                  // tamaño (NO size())
entidades.get(i);
entidades.isEmpty();
entidades.clear();
```

### REGLA CRÍTICA: ELIMINAR MIENTRAS ITERAS
Recorrer SIEMPRE de atrás hacia delante con índice:
```java
for (int i = entidades.size - 1; i >= 0; i--) {
    MiEntidad e = entidades.get(i);
    if (e.destruido) {
        entidades.removeIndex(i);
    }
}
```

POR QUÉ:
- `for-each` (for (E e : array)) → lanza excepción al eliminar (ConcurrentModificationException).
- `for` de 0 a size → al eliminar i, el siguiente baja a la posición i y te lo SALTAS.
- `for` de size-1 a 0 → al eliminar i, los anteriores NO se mueven. CORRECTO.

### Doble bucle (balas vs enemigos)
```java
for (int i = balas.size - 1; i >= 0; i--) {
    Bala b = balas.get(i);
    for (int j = sombras.size - 1; j >= 0; j--) {
        Sombra s = sombras.get(j);
        if (b.hitbox.overlaps(s.hitbox)) {
            if (b.forma == s.forma) s.destruida = true;
            b.destruida = true;
            break;   // sal del bucle interno
        }
    }
}
// luego limpia ambos arrays en bucles separados
```

CUIDADO con doble removeIndex en el mismo bucle: si un objeto cumple dos condiciones (ej: destruido Y fuera de pantalla) y haces removeIndex dos veces, peta. Usa `if / else if` o `continue` tras el primer remove.

---

## [INDICE-HUD] BARRAS DE INFORMACIÓN

### HUD arriba (barra superior) — como Globos, Coches
La cámara se configura con `ALTO + TOP_BAR` de alto, así la zona y > ALTO queda reservada:
```java
// En Main: camera.setToOrtho(false, ANCHO, ALTO + TOP_BAR);

// En render de PantallaJuego:
batch.begin();
font.draw(batch, "Tiempo: " + String.format("%.2f", stateTime), 10, Mundo.ALTO + Mundo.TOP_BAR - 10);
font.draw(batch, "Puntos: " + Mundo.puntos, 200, Mundo.ALTO + Mundo.TOP_BAR - 10);
font.draw(batch, "Vidas: " + vidas, 400, Mundo.ALTO + Mundo.TOP_BAR - 10);
batch.end();
```

### HUD abajo (barra inferior) — como el Dedo
```java
// Mundo.ALTO_BARRA = 80f — los personajes no bajan de esa Y
sr.begin(ShapeRenderer.ShapeType.Filled);
sr.setColor(Color.BROWN);
sr.rect(0, 0, Mundo.ANCHO, Mundo.ALTO_BARRA);
sr.end();

batch.begin();
font.draw(batch, "Tpo: " + (int)stateTime, 10, Mundo.ALTO_BARRA * 0.5f);
batch.end();
```

NOTA: font.draw(batch, texto, x, y) — la Y es la línea BASE del texto (parte de abajo de las letras), no la de arriba.

---

## [INDICE-RENDER] ORDEN DE DIBUJADO, sr vs batch

REGLA DE ORO: NUNCA mezclar sr.begin/end con batch.begin/end. Cada uno su bloque.

```java
// 1. Formas geométricas
sr.begin(ShapeRenderer.ShapeType.Filled);
//  ... sr.rect, sr.circle, sr.ellipse
sr.end();

// 2. (opcional) Formas con contorno
sr.begin(ShapeRenderer.ShapeType.Line);
//  ... sr.rect (solo borde)
sr.end();

// 3. Texto y texturas
batch.begin();
//  ... font.draw, batch.draw(textura, ...)
batch.end();
```

TIPOS de ShapeRenderer:
- `ShapeType.Filled` → formas rellenas.
- `ShapeType.Line` → solo el contorno.

MÉTODOS de ShapeRenderer:
- `sr.rect(x, y, ancho, alto)` — rectángulo (origen abajo-izq).
- `sr.circle(centroX, centroY, radio)` — círculo (OJO: centro, no esquina).
- `sr.ellipse(x, y, ancho, alto)` — elipse (origen abajo-izq, como rect).
- `sr.line(x1, y1, x2, y2)` — línea.
- `sr.setColor(color)` — antes de dibujar.

ERROR COMÚN: doble begin. Si llamas batch.begin() y dentro de un método llamas batch.begin() otra vez, PETA. Un solo begin/end por bloque.

---

## [INDICE-RECORDS] PREFERENCES

```java
Preferences prefs = Gdx.app.getPreferences("record.pref");  // el nombre debe ser SIEMPRE el mismo

// LEER
float rec = prefs.getFloat("record", 0f);       // 0f = valor por defecto
int recPuntos = prefs.getInteger("recordPuntos", 0);

// GUARDAR
prefs.putFloat("record", tiempo);
prefs.putInteger("recordPuntos", puntos);
prefs.flush();    // CRÍTICO: sin flush no se guarda en disco

// BORRAR todo
prefs.clear();
prefs.flush();

// BORRAR una clave concreta
prefs.remove("record");
prefs.flush();
```

### Record "menos tiempo mejor"
```java
float recAnterior = prefs.getFloat("record", Float.MAX_VALUE);  // MAX para que el primero siempre gane
if (tiempo < recAnterior) {
    prefs.putFloat("record", tiempo);
    prefs.flush();
}
```

### Record "más puntos mejor"
```java
int recAnterior = prefs.getInteger("record", 0);
if (puntos > recAnterior) {
    prefs.putInteger("record", puntos);
    prefs.flush();
}
```

### Record combinado (primero puntos, desempate por tiempo) — como diseñamos en Globos
```java
int recP = prefs.getInteger("recordPuntos", 0);
float recT = prefs.getFloat("recordTiempo", Float.MAX_VALUE);
if (puntos > recP || (puntos == recP && tiempo < recT)) {
    prefs.putInteger("recordPuntos", puntos);
    prefs.putFloat("recordTiempo", tiempo);
    prefs.flush();
}
```

### Record por MODO/DIFICULTAD — como Semáforos
```java
String clave = "record_" + modo;   // ej: "record_facil", "record_dificil"
float rec = prefs.getFloat(clave, Float.MAX_VALUE);
if (tiempo < rec) {
    prefs.putFloat(clave, tiempo);
    prefs.flush();
}
```

CUIDADO: el nombre del archivo ("record.pref") debe ser IDÉNTICO en todos los sitios donde uses Preferences. Si en una pantalla pones "record.pref" y en otra "records.pref" son archivos distintos.

CUIDADO: si lees el record una vez en el constructor y luego lo borras con R, la pantalla seguirá mostrando el viejo. O lo relees en render, o lo actualizas en el keyDown al borrar.

---

## [INDICE-ESTADOS] MÁQUINA DE ESTADOS E INVENCIBILIDAD

### Máquina de estados (sin while, todo en update con delta) — como el anzuelo del Pescador
```java
enum Estado { PARADO, BAJANDO, SUBIENDO }
Estado estado = Estado.PARADO;

void update(float delta) {
    switch (estado) {
        case BAJANDO:
            y -= velocidad * delta;
            if (y <= limiteAbajo) estado = Estado.SUBIENDO;
            break;
        case SUBIENDO:
            y += velocidad * delta;
            if (y >= limiteArriba) estado = Estado.PARADO;
            break;
        case PARADO:
            // no hace nada
            break;
    }
    hitbox.setPosition(x, y);
}
```
CLAVE: NUNCA usar while para animaciones/movimiento. Todo se hace incremental en update() frame a frame.

### Invencibilidad temporal con parpadeo — como el Jugador de Coches
```java
float tiempoInvencible = 0f;
boolean invencible = false;

void golpear() {
    if (invencible) return;        // si ya es invencible, no recibe daño
    vidas--;
    invencible = true;
    tiempoInvencible = 2f;          // 2 segundos
}

void update(float delta) {
    if (invencible) {
        tiempoInvencible -= delta;
        if (tiempoInvencible <= 0) invencible = false;
    }
}

void render(ShapeRenderer sr) {
    // parpadeo: no dibuja en frames alternos mientras es invencible
    if (invencible && ((int)(tiempoInvencible * 10) % 2 == 0)) return;
    // ... dibujar normal
}
```

### Tiempo de gracia (sin spawn un momento) — como al cruzar en Coches
```java
float tiempoGracia = 0f;

// al cruzar o al golpearse:
tiempoGracia = 0.5f;

// en render:
if (tiempoGracia > 0) tiempoGracia -= delta;
if (tiempoGracia <= 0) {
    // spawn normal
}
```

---

## [INDICE-HERENCIA] JERARQUÍA DE CLASES

Cuando hay varios tipos de enemigo con comportamiento distinto — como el Dedo (Circulo/Rectangulo) o el Cazador (SombraCuadrada/SombraCircular):

```java
// Clase abstracta base
public abstract class Enemigo {
    float x, y, ancho, alto, velocidad;
    Rectangle hitbox;
    boolean destruido = false;

    public Enemigo(...) {
        // inicializar campos comunes
        this.hitbox = new Rectangle(x, y, ancho, alto);
    }

    abstract void update(float delta);   // cada subclase lo implementa distinto
    abstract void render(ShapeRenderer sr);
}

// Subclase 1
public class Rectangulo extends Enemigo {
    public Rectangulo() { super(...); }
    @Override void update(float delta) { x -= velocidad * delta; hitbox.setPosition(x,y); }
    @Override void render(ShapeRenderer sr) { sr.rect(x, y, ancho, alto); }
}

// Subclase 2
public class Circulo extends Enemigo {
    public Circulo() { super(...); }
    @Override void update(float delta) { /* movimiento distinto */ }
    @Override void render(ShapeRenderer sr) { sr.circle(...); }
}
```

Crear uno aleatorio:
```java
enemigos.add(MathUtils.randomBoolean() ? new Rectangulo() : new Circulo());
```

CLAVE: lo común (x, y, hitbox, velocidad, destruido) va en la base. Lo que cambia (movimiento, dibujo) son métodos abstractos que cada subclase implementa.

CUÁNDO NO heredar: si el "jugador" es muy distinto de los enemigos (tiene input, vidas, etc.), mejor una clase separada que forzar herencia.

---

## [INDICE-FORMAS] DIBUJAR CON SHAPERENDERER

```java
sr.begin(ShapeRenderer.ShapeType.Filled);

sr.setColor(Color.RED);                  // color predefinido
sr.setColor(new Color(0.2f, 0.5f, 0.8f, 1f));  // RGBA personalizado (0..1)
sr.setColor(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f); // aleatorio

sr.rect(x, y, ancho, alto);              // rectángulo
sr.circle(centroX, centroY, radio);      // círculo (CENTRO, no esquina)
sr.ellipse(x, y, ancho, alto);           // elipse (esquina abajo-izq)
sr.line(x1, y1, x2, y2);                 // línea
sr.triangle(x1,y1, x2,y2, x3,y3);        // triángulo

sr.end();
```

COLORES PREDEFINIDOS: Color.WHITE, BLACK, RED, GREEN, BLUE, YELLOW, CYAN, MAGENTA, ORANGE, PINK, GRAY, DARK_GRAY, LIGHT_GRAY, BROWN, GOLD, NAVY, PURPLE, etc.

### Color aleatorio para una entidad (asignar UNA vez en constructor)
```java
Color color = new Color(MathUtils.random(), MathUtils.random(), MathUtils.random(), 1f);
```

### Truco "ventanilla direccional" (Coches) — indicar dirección con un detalle
```java
sr.setColor(color);
sr.rect(x, y, tam, tam);                 // cuerpo
sr.setColor(Color.DARK_GRAY);            // ventanilla
if (direccion == 1) sr.rect(ventX, y + tam - ventSize, ventSize, ventSize);  // arriba
else                sr.rect(ventX, y + tam*0.1f, ventSize, ventSize);        // abajo
```

### Truco "brillo" (Jugador destacado) — círculo grande blanco detrás + círculo color delante
```java
sr.setColor(Color.WHITE);
sr.circle(cx, cy, radio);          // brillo exterior
sr.setColor(Color.CYAN);
sr.circle(cx, cy, radio * 0.7f);   // interior
```

---

## [INDICE-DIFICULTAD] MODOS Y SELECTOR

### Constantes y métodos en Mundo
```java
public static int NUM_CARRILES = 10;
public static int OBJETIVO = 5;

public static void setNormal()  { NUM_CARRILES = 10; OBJETIVO = 5;  }
public static void setDificil() { NUM_CARRILES = 20; OBJETIVO = 10; }
```

### Selector con flechas y colores (en PantallaInicio)
```java
int dificultad = 0;
String[] nombres = {"NORMAL", "DIFICIL"};

// en render:
for (int i = 0; i < nombres.length; i++) {
    font.setColor(i == dificultad ? Color.YELLOW : Color.GRAY);
    font.draw(batch, nombres[i], 250 + i * 200, Mundo.MITAD_ALTO);
}
font.setColor(Color.WHITE);

// en keyDown:
case Input.Keys.LEFT:  dificultad = Math.max(0, dificultad - 1); break;
case Input.Keys.RIGHT: dificultad = Math.min(nombres.length - 1, dificultad + 1); break;
case Input.Keys.SPACE:
    if (dificultad == 0) Mundo.setNormal(); else Mundo.setDificil();
    game.setScreen(new PantallaJuego(game));
    break;
```

### Selector con teclas directas (estilo Semáforos: F/N/D)
```java
case Input.Keys.F: Mundo.setFacil();   game.setScreen(new PantallaJuego(game)); break;
case Input.Keys.N: Mundo.setNormal();  game.setScreen(new PantallaJuego(game)); break;
case Input.Keys.D: Mundo.setDificil(); game.setScreen(new PantallaJuego(game)); break;
```

---

## [INDICE-MATHUTILS] FUNCIONES ÚTILES

```java
MathUtils.random(min, max)        // float aleatorio entre min y max
MathUtils.random(int, int)        // int aleatorio (ambos inclusive)
MathUtils.random()                // float entre 0 y 1
MathUtils.randomBoolean()         // true/false al 50%
MathUtils.randomBoolean(0.1f)     // true al 10%
MathUtils.clamp(valor, min, max)  // limita un valor al rango [min, max]
MathUtils.round(float)            // redondea a int
```

Java estándar:
```java
Math.max(a, b)
Math.min(a, b)
Math.abs(x)
(int) x                           // truncar a entero
String.format("%.2f", x)          // 2 decimales
```

USOS TÍPICOS:
- Posición aleatoria: `MathUtils.random(0, Mundo.ANCHO - ancho)`
- Velocidad aleatoria: `MathUtils.random(100f, 300f)`
- Dirección aleatoria: `MathUtils.randomBoolean() ? 1 : -1`
- Limitar movimiento a la pantalla: `x = MathUtils.clamp(x, 0, Mundo.ANCHO - ancho)`
- Tirar moneda para tipo de enemigo: `MathUtils.randomBoolean() ? new A() : new B()`

---

## [INDICE-ERRORES] ERRORES COMUNES Y SOLUCIONES

| Síntoma | Causa | Solución |
|---|---|---|
| El click/touch no hace nada | show() sobreescrito vacío en PantallaJuego | Borra el show() o pon super.show() |
| El hitbox no coincide con lo dibujado | Falta hitbox.setPosition(x,y) en update | Añádelo al final de cada update |
| ConcurrentModificationException | Eliminas con for-each | Usa for (int i=size-1; i>=0; i--) |
| Doble removeIndex peta | Una entidad cumple 2 condiciones de borrado | usa if/else if o continue |
| Doble begin de batch o sr | begin() dentro de método llamado desde render | Un solo begin/end por bloque |
| setScreen se llama dos veces | terminarPartida ya hace setScreen + otro setScreen detrás | Quita el setScreen duplicado |
| El color parpadea | El color se genera en render cada frame | Asígnalo una vez en el constructor |
| La velocidad va más rápido en otros PC | No multiplicas por delta | x += vel * delta |
| Record no se actualiza al borrar | Se leyó una vez en constructor | Reléelo en keyDown al borrar, o en render |
| El record nunca mejora | Comparas contra 0 en "menos es mejor" | Usa Float.MAX_VALUE como valor por defecto |
| keyDown no compila | Pusiste Input.Keys como parámetro | Es int keycode, compara con Input.Keys.X |
| No se ve nada / pantalla en negro | Olvidaste batch.begin/end o sr.begin/end | Comprueba que abres y cierras |
| El juego no termina nunca | Falta condición cuando se agotan entidades | Comprueba lanzados>=TOTAL && array.isEmpty() |
| Vehículo aparece en y=0 siempre | Asignaste y después de crear el rect | Asigna x,y ANTES de new Rectangle |

---

## [INDICE-TRUCOS] TRUCOS DE LOS EJERCICIOS CONCRETOS

### GLOBOS (insectos) — click sobre entidades que suben
- Globos suben: `y += speed * delta`.
- Spawn por timer con contador total (lanzados < TOTAL).
- Click con unproject + isHit ovalado.
- Win al pinchar TOTAL, game over al escapar MAX.
- Eliminar al salir por arriba (y > ALTO) → escapado++.
- Caso especial fin: `lanzados >= TOTAL && globos.isEmpty()`.

### COCHES (frogger) — movimiento por casillas, esquivar
- Pantalla apaisada (ANCHO 800, ALTO 480).
- Jugador se mueve por casillas: `MathUtils.clamp(x + dir*tam, 0, ANCHO-tam)`.
- Coches en carriles, movimiento fluido vertical (suben o bajan).
- Tam casilla = ANCHO / NUM_CARRILES (para que los carriles llenen el ancho).
- Colisión rect-rect → pierde vida + invencibilidad + parpadeo.
- Cruzar (x >= ANCHO-tam) → punto + reset posición + tiempo de gracia.
- Carriles dibujados con franjas alternas de gris (i%2).
- Ventanilla indica dirección del coche.

### CAZADOR DE SOMBRAS (dedo + camuflaje) — disparar, formas
- Cazador en x=0, mueve vertical, dispara con SPACE.
- Cambia forma (CUADRADO/CIRCULO) con tecla → enum Forma.
- Balas hacia la derecha, límite MAX_BALAS en pantalla.
- Sombras vienen de la derecha, dos tipos (herencia: SombraCuadrada/Circular).
- Bala solo destruye sombra si forma coincide (b.forma == s.forma).
- Sombra que llega a x<0 sin destruir → pierde vida.
- Doble bucle balas-sombras, limpiar ambos arrays después.

### SEMÁFOROS (el examen real) — el más complejo
- Coches de izquierda a derecha, velocidad aleatoria.
- Semáforo cicla VERDE→AMARILLO→ROJO por timer (enum Estado).
- Click en coche → para/arranca (toggle estado).
- Click en vacío → pone piedra (entidad estática del jugador).
- Coche choca con piedra → coche desaparece.
- Coche sale por derecha en ROJO → pierdes. En verde/amarillo → bonus tiempo.
- Dificultad por segundos de juego (facil/normal/dificil), record por modo.
- Pantalla de pausa separada (P para pausar, V para volver).

---

## NOTAS FINALES PARA EL EXAMEN

1. Lo PRIMERO: cambia el nombre del paquete si partes de plantilla.
2. Lee el enunciado DOS veces y aplica el checklist.
3. Que compile y dibuje algo cuanto antes, luego añade lógica.
4. Si algo no funciona, activa Mundo.DEBUG = true para ver hitboxes.
5. No te obsesiones con centrar textos a pixel — el profe valora la lógica.
6. ShapeRenderer es válido, no necesitas texturas/sprites.
7. Si te bloqueas: mira esta referencia con Ctrl+F por el concepto.
8. Acuérdate de los 4 errores mortales: show() vacío, hitbox sin actualizar, eliminar arrays al derecho, doble begin.

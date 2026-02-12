# Chuleta LibGDX (examen) — Snippets + dónde mirar (tus proyectos)

> Objetivo: si te atascas en el examen, busca el patrón en el proyecto correcto y copia/adapta.
> Proyectos de referencia:
> - **Insectos** (SimulacroExamen.zip): pantallas + HUD arriba + unproject + hitbox + records por N
> - **Bloques**: grid + offset + selección doble + easter egg + records por modo + pantallas
> - **Naves/Dedo**: Mundo “gordo” + pool + balas circulares + colisión círculo-rect + HUD con iconos
> - **Pescador**: máquina de estados (anzuelo) + bloqueo input por estado + spawn por timer

---

## 0) Plantilla mínima de proyecto (Game + Screen)
**Mira:** Insectos `Main.java` + `PantallaInicio/PantallaJuego`

```java
public class Main extends Game {
  public SpriteBatch batch;
  public ShapeRenderer sr;
  public BitmapFont font;

  @Override public void create() {
    batch = new SpriteBatch();
    sr = new ShapeRenderer();
    font = new BitmapFont();
    setScreen(new PantallaInicio(this));
  }
}
```

**Qué copiar:**
- Recursos globales en `Main`
- `setScreen(new PantallaX(this))`

---

## 1) Cámara + unproject (TOQUES correctos)
**Mira:** Insectos `PantallaJuego.touchDown()`

```java
Vector3 v3 = new Vector3(screenX, screenY, 0);
camera.unproject(v3);
Vector2 touch = new Vector2(v3.x, v3.y);
```

**Uso típico:**
- Touch sobre sprite: `rect.contains(touch)`
- Touch sobre celda de grid: compara contra x/y de cada bloque

---

## 2) Hitbox Rectangle + “clic sobre sprite”
**Mira:** Insectos `Insecto.isHit()`

```java
Rectangle hitbox = new Rectangle(x, y, w, h);
boolean hit(Vector2 t) { return hitbox.contains(t); }
```

**Clave:** actualiza `hitbox.setPosition(x,y)` cada `update()`.

---

## 3) Movimiento con delta + rebote en bordes
**Mira:** Insectos `Insecto.update(delta)`

```java
x += speed * dirX * delta;
y += speed * dirY * delta;

if (x < 0) { x = 0; dirX = 1; }
if (x > W - w) { x = W - w; dirX = -1; }

if (y < 0) { y = 0; dirY = 1; }
if (y > H - h) { y = H - h; dirY = -1; }
```

---

## 4) Barra superior (HUD arriba) “fuera” del área de juego
**Mira:** Insectos `Pantalla` (camera alto = ALTO_MUNDO + ALTURA_MENU_SUPERIOR)
y `PantallaJuego` (línea separadora)

```java
camera.setToOrtho(false, ANCHO, ALTO + TOP_BAR);
sr.rect(0, ALTO, ANCHO, 1); // línea
font.draw(batch, "texto", 10, ALTO + TOP_BAR - 8);
```

---

## 5) Barra inferior (HUD abajo) dentro del mundo (línea + texto)
**Mira:** Naves `Mundo.ALTO_BARRA_INFO`

```java
static final float HUD = 40;
sr.line(0, HUD, ANCHO, HUD);
font.draw(batch, "Time: " + (int)stateTime, 10, 25);
```

---

## 6) stateTime (tiempo de partida)
**Mira:** Insectos / Naves / Bloques

```java
stateTime += delta;   // en render/update
```

---

## 7) Preferences: récord (por modo/configuración)
**Mira:** Insectos (record por numInsects) y Bloques (record por modo)

```java
Preferences p = Gdx.app.getPreferences("record.prefs");
float rec = p.getFloat("record" + n, 0);
if (mejora) { p.putFloat("record" + n, stateTime); p.flush(); }
```

**Borrar récords:**
```java
p.clear(); p.flush();
```

---

## 8) Pool de balas (evitar new() en runtime)
**Mira:** Naves `Mundo` + `Bala implements Poolable`

```java
Pool<Bala> pool = new Pool<Bala>() {
  protected Bala newObject() { return new Bala(); }
};

Bala b = pool.obtain();
b.init(...);
balas.add(b);

if (b.destruida) { balas.removeValue(b,true); pool.free(b); }
```

---

## 9) Balas como círculos (ShapeRenderer) + colisión círculo-rect
**Mira:** Naves (balas circulares + colisión)

**Dibujo bala:**
```java
sr.begin(ShapeRenderer.ShapeType.Filled);
sr.circle(cx, cy, r);
sr.end();
```

**Colisión círculo-rect (rápida):**
```java
float closestX = MathUtils.clamp(cx, rect.x, rect.x+rect.width);
float closestY = MathUtils.clamp(cy, rect.y, rect.y+rect.height);
float dx = cx - closestX, dy = cy - closestY;
boolean hit = dx*dx + dy*dy <= r*r;
```

---

## 10) Spawn por timer (enemigos/filas/etc.)
**Mira:** Naves (enemigo cada X) / Bloques (offset + filas)

```java
nextSpawnAt -= delta;
if (nextSpawnAt <= 0) {
  spawn();
  nextSpawnAt = MathUtils.random(min, max);
}
```

---

## 11) Grid + “posición lógica vs posición visual” (scroll/offset)
**Mira:** Bloques (verticalOffset + gridY)

```java
float blockSize = ANCHO / COLS;
float visualY = gridY * blockSize + verticalOffset;
verticalOffset += speed * delta;
if (verticalOffset >= blockSize) {
  verticalOffset -= blockSize;
  // sube filas y añade fila nueva
}
```

---

## 12) Selección doble (dos toques consecutivos) + reglas de KO
**Mira:** Bloques (selectedBlock)

```java
if (touched == null) gameOver();
else if (selected == null) selected = touched;
else if (selected == touched) {} // ignorar
else if (selected.value == touched.value) { remove both; selected=null; }
else gameOver();
```

---

## 13) Easter egg por tiempo + zona de pantalla (primer toque)
**Mira:** Bloques (firstTouchDone + stateTime + esquina sup derecha)

```java
if (!firstTouchDone) {
  if (stateTime <= 1f && screenX > 0.9*W && screenY < 0.1*H) modeColors=true;
  firstTouchDone = true;
}
```

---

## 14) Máquina de estados (anzuelo / ataque / animación)
**Mira:** Pescador (anzuelo)
**Idea:** NO while, todo en `update(delta)`

```java
enum Estado { PARADO, BAJANDO, SUBIENDO }
Estado estado;

void update(float delta){
  if (estado==BAJANDO) y -= v*delta;
  if (estado==SUBIENDO) y += v*delta;
  if (llegó) estado = PARADO;
}
```

---

## 15) Cambio de pantallas (Inicio/Juego/Fin)
**Mira:** Insectos / Naves / Bloques

```java
game.setScreen(new PantallaJuego(game));
// al terminar:
game.setScreen(new PantallaFin(game));
```

---

## 16) Debug hitboxes (por si no colisiona)
**Mira:** útil en todos
```java
sr.begin(ShapeRenderer.ShapeType.Line);
sr.rect(rect.x, rect.y, rect.width, rect.height);
sr.end();
```

---

## “¿Dónde miro?” (ultra rápido)
- **No me detecta bien el toque** → Insectos: `unproject` + `Rectangle.contains`
- **Quiero HUD arriba** → Insectos: cámara con alto extra + línea separadora
- **Quiero HUD abajo** → Naves: `ALTO_BARRA_INFO` + línea + font
- **Necesito pools** → Naves: `Pool<Bala>`
- **Necesito colisión círculo-rect** → Naves
- **Necesito scroll por filas / grid** → Bloques
- **Necesito selección doble / KO por fallo** → Bloques
- **Necesito estados (no bloquear con while)** → Pescador

---

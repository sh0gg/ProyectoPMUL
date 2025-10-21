package com.example.hanoi;


import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Torres game;
    private LinearLayout towerALayout, towerBLayout, towerCLayout;

    private Tower selectedTower = null;  // Torre de la cual seleccionamos pieza para mover

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instancia el juego
        game = new Torres();

        // Referencias a layouts de torres
        towerALayout = findViewById(R.id.towerA);
        towerBLayout = findViewById(R.id.towerB);
        towerCLayout = findViewById(R.id.towerC);

        // Mostrar estado inicial
        renderTowers();

        // Setup click listeners para mover piezas
        towerALayout.setOnClickListener(v -> onTowerClicked(game.towerA, towerALayout));
        towerBLayout.setOnClickListener(v -> onTowerClicked(game.towerB, towerBLayout));
        towerCLayout.setOnClickListener(v -> onTowerClicked(game.towerC, towerCLayout));
    }

    private void onTowerClicked(Tower tower, LinearLayout towerLayout) {
        if (selectedTower == null) {
            // Selecciona la torre de origen si tiene piezas
            if (!tower.isEmpty()) {
                selectedTower = tower;
                towerLayout.setBackgroundColor(Color.parseColor("#FFDD55")); // marca selección
            }
        } else {
            // Intentar mover pieza de selectedTower a esta torre clicada
            if (game.movePiece(selectedTower, tower)) {
                // Movimiento OK
            } else {
                // Movimiento inválido, podrías mostrar mensaje o animación
            }
            // Limpiar selección visual
            clearTowerHighlights();

            selectedTower = null;

            // Actualizar UI
            renderTowers();
        }
    }

    private void clearTowerHighlights() {
        towerALayout.setBackgroundColor(Color.parseColor("#CCCCCC"));
        towerBLayout.setBackgroundColor(Color.parseColor("#BBBBBB"));
        towerCLayout.setBackgroundColor(Color.parseColor("#AAAAAA"));
    }

    // Crear la vista para una pieza según su tamaño
    private View createPieceView(int size) {
        View piece = new View(this);

        int color;
        switch (size) {
            case 5: color = Color.RED; break;
            case 4: color = Color.BLUE; break;
            case 3: color = Color.GREEN; break;
            case 2: color = Color.YELLOW; break;
            case 1: color = 0xFFFFA500; break; // naranja
            default: color = Color.GRAY; break;
        }
        piece.setBackgroundColor(color);

        int widthInDp = 50 + size * 20;
        int heightInDp = 30;

        int widthPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, widthInDp, getResources().getDisplayMetrics());
        int heightPx = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, heightInDp, getResources().getDisplayMetrics());

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(widthPx, heightPx);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.setMargins(0, 5, 0, 5);

        piece.setLayoutParams(params);
        return piece;
    }

    private void renderTowers() {
        // Limpia y dibuja las piezas en cada torre
        towerALayout.removeAllViews();
        towerBLayout.removeAllViews();
        towerCLayout.removeAllViews();

        // Mostrar piezas de abajo hacia arriba (stack bottom to top)
        renderTower(game.towerA, towerALayout);
        renderTower(game.towerB, towerBLayout);
        renderTower(game.towerC, towerCLayout);
    }

    private void renderTower(Tower tower, LinearLayout towerLayout) {
        // Las torres son stacks: mostrar desde el fondo (index 0) hasta la cima (top)

        for (int i = 0; i < tower.size(); i++) {
            int pieceSize = tower.getStack().get(i);
            View pieceView = createPieceView(pieceSize);
            towerLayout.addView(pieceView);
        }
    }
}

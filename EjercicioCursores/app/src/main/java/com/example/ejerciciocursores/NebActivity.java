package com.example.ejerciciocursores;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class NebActivity extends AppCompatActivity
        implements FrgControles.OnTeclaListener {

    private TextView tvPos, tvLibro;

    // estantería en memoria: [fila][columna]
    private String[][] estanteria;
    // posición actual (índices 0-based)
    private int fila = 0, col = 0;

    private AsistenteBD helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_neb);

        tvPos = findViewById(R.id.tvPosicionNeb);
        tvLibro = findViewById(R.id.tvLibroNeb);

        helper = new AsistenteBD(this);

        cargarEstanteriaDesdeBD();

        FragmentManager fm = getSupportFragmentManager();
        FrgControles frg = (FrgControles) fm.findFragmentById(R.id.frgControlesNeb);
        if (frg != null) {
            frg.setMostrarEnter(false); // aqui no aparece
        }

        actualizarUI();
    }

    private void cargarEstanteriaDesdeBD() {
        Cursor c = helper.obtenerLibrosConPosicion();

        ArrayList<String> datosLibro = new ArrayList<>();
        ArrayList<Integer> xs = new ArrayList<>();
        ArrayList<Integer> ys = new ArrayList<>();

        int maxX = 0;
        int maxY = 0;

        if (c.moveToFirst()) {
            do {
                String titulo = c.getString(1);
                String autor = c.getString(2);
                int posX = c.getInt(3);
                int posY = c.getInt(4);

                datosLibro.add(titulo + " - " + autor);
                xs.add(posX);
                ys.add(posY);

                if (posX > maxX) maxX = posX;
                if (posY > maxY) maxY = posY;

            } while (c.moveToNext());
        }
        c.close();

        // Por si no hubiera libros
        if (maxX <= 0) maxX = 1;
        if (maxY <= 0) maxY = 1;

        // maxX y maxY son en base 1, la matriz es [0..maxX-1][0..maxY-1]
        estanteria = new String[maxX][maxY];

        for (int i = 0; i < datosLibro.size(); i++) {
            int x = xs.get(i) - 1;
            int y = ys.get(i) - 1;
            if (x >= 0 && x < maxX && y >= 0 && y < maxY) {
                estanteria[x][y] = datosLibro.get(i);
            }
        }

        fila = 0;
        col = 0;
    }

    @Override
    public void onTeclaPulsada(int tecla) {
        if (estanteria == null) return;

        int nf = fila;
        int nc = col;

        if (tecla == FrgControles.TECLA_ARRIBA) {
            nf--;
        } else if (tecla == FrgControles.TECLA_ABAJO) {
            nf++;
        } else if (tecla == FrgControles.TECLA_IZQ) {
            nc--;
        } else if (tecla == FrgControles.TECLA_DER) {
            nc++;
        } else if (tecla == FrgControles.TECLA_ENTER) {
            // ya de por si no aparece pero bueno, porsiaca
            return;
        }

        // comprobar si se sale de la estantería
        if (nf < 0 || nc < 0 ||
                nf >= estanteria.length ||
                nc >= estanteria[0].length) {

            Toast.makeText(this,
                    getString(R.string.toast_fuera),
                    Toast.LENGTH_SHORT).show();

        } else {
            fila = nf;
            col = nc;
            actualizarUI();
        }
    }

    private void actualizarUI() {
        tvPos.setText("(" + (fila + 1) + "," + (col + 1) + ")"); // para que coicidan las posiciones con la matriz

        String libro = estanteria[fila][col];
        if (libro == null) {
            tvLibro.setText(getString(R.string.texto_sin_libro));
        } else {
            tvLibro.setText(libro);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) helper.close();
    }
}

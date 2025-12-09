package com.example.ejerciciocursores;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class ErcJuegoActivity extends AppCompatActivity
        implements FrgControles.OnTeclaListener {

    private TextView tvNombreEscape;
    private TextView tvCaminoActual;

    private String nombreEscape;
    private String caminoCorrecto;
    private int maxErrores;

    private StringBuilder caminoJugador = new StringBuilder();

    private AsistenteBD helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erc_juego);

        tvNombreEscape = findViewById(R.id.tvNombreEscape);
        tvCaminoActual = findViewById(R.id.tvCaminoActualJuego);

        helper = new AsistenteBD(this);

        nombreEscape = getIntent().getStringExtra("NOMBRE_ESCAPE");
        caminoCorrecto = getIntent().getStringExtra("CAMINO_CORRECTO");
        maxErrores = getIntent().getIntExtra("MAX_ERRORES", 0);

        if (nombreEscape == null) nombreEscape = "";
        if (caminoCorrecto == null) caminoCorrecto = "";

        tvNombreEscape.setText(nombreEscape);
        tvCaminoActual.setText(getString(R.string.label_camino_introducido));
    }

    @Override
    public void onTeclaPulsada(int tecla) {
        switch (tecla) {
            case FrgControles.TECLA_ARRIBA:
                caminoJugador.append("↑");
                break;
            case FrgControles.TECLA_ABAJO:
                caminoJugador.append("↓");
                break;
            case FrgControles.TECLA_IZQ:
                caminoJugador.append("←");
                break;
            case FrgControles.TECLA_DER:
                caminoJugador.append("→");
                break;
            case FrgControles.TECLA_ENTER:
                comprobarCamino();
                return;
        }

        tvCaminoActual.setText(getString(R.string.label_camino_introducido)
                + " " + caminoJugador.toString());
    }

    private void comprobarCamino() {
        String jug = caminoJugador.toString();
        String cor = caminoCorrecto;

        int max = Math.max(cor.length(), jug.length());
        int errores = 0;

        for (int i = 0; i < max; i++) {
            char c1 = i < cor.length() ? cor.charAt(i) : '-';
            char c2 = i < jug.length() ? jug.charAt(i) : '-';
            if (c1 != c2) errores++;
        }

        boolean gana = errores <= maxErrores;

        if (gana) {
            Toast.makeText(this,
                    String.format(getString(R.string.toast_ganado),
                            errores, maxErrores),
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,
                    String.format(getString(R.string.toast_perdido),
                            errores, maxErrores),
                    Toast.LENGTH_LONG).show();
        }

        // actualizamos stats con ese nº de errores máximo
        helper.sumarPartida(maxErrores);

        // reseteamos para poder rejugar
        caminoJugador.setLength(0);
        tvCaminoActual.setText(getString(R.string.label_camino_introducido));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) helper.close();
    }
}

package com.cdm.cniguia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity sencilla que muestra la ALERTA CNI:
 *  - En qué campo se detectó
 *  - Qué patrón coincidió
 *  - Nivel de aviso
 *  - Texto completo
 *  - Número de veces que se ha detectado @ot.com en "Para"
 *
 * Aquí NO usamos BD ni ListView (versión ligera para examen).
 */
public class ActivityAlertaGuia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerta_guia);

        // Leemos los datos del Intent
        String campo      = getIntent().getStringExtra("campo");
        String patron     = getIntent().getStringExtra("patron");
        String texto      = getIntent().getStringExtra("texto");
        String nivel      = getIntent().getStringExtra("nivel");
        int contadorOT    = getIntent().getIntExtra("contadorOT", 0);

        // Referencias a controles
        TextView tvResumen = findViewById(R.id.tvResumenAlerta);
        Button bCerrar = findViewById(R.id.bCerrarAlerta);

        // Montamos un texto informativo (lo importante es mostrar que llegó todo)
        StringBuilder sb = new StringBuilder();
        sb.append("⚠ ALERTA CNI ⚠\n\n");
        sb.append("Campo: ").append(campo).append("\n");
        sb.append("Patrón: ").append(patron).append("\n");
        sb.append("Nivel: ").append(nivel).append("\n\n");
        sb.append("Texto completo:\n").append(texto).append("\n\n");
        sb.append("Correos a @ot.com en 'Para': ").append(contadorOT);

        tvResumen.setText(sb.toString());

        // Volver a la pantalla anterior
        bCerrar.setOnClickListener(v -> finish());
    }
}

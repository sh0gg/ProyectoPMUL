package com.cdm.ajustesguia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

/**
 * Activity principal de ejemplo.
 *
 * - Muestra el nombre del usuario almacenado en Ajustes.
 * - Tiene un botón que abre AjustesActivity.
 * - Cuando volvemos de AjustesActivity, podemos refrescar la info.
 */
public class MainActivityGuia extends AppCompatActivity {

    TextView tvResumenAjustes;
    Button bAbrirAjustes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guia);

        tvResumenAjustes = findViewById(R.id.tvResumenAjustes);
        bAbrirAjustes = findViewById(R.id.bAbrirAjustes);

        // Botón para ir a la pantalla de ajustes
        bAbrirAjustes.setOnClickListener(v -> {
            Intent intent = new Intent(this, AjustesActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Cada vez que volvemos a MainActivity actualizamos el resumen
        Ajustes ajustes = Ajustes.getInstance(this);

        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(ajustes.getNombre()).append("\n");
        sb.append("Correo: ").append(ajustes.getCorreo()).append("\n");

        int edad = ajustes.getEdad(-1);
        sb.append("Edad: ").append(edad == -1 ? "(no indicada)" : edad).append("\n");

        sb.append("Recibir publicidad: ").append(
                ajustes.getRecibirPublicidad() ? "Sí" : "No"
        );

        tvResumenAjustes.setText(sb.toString());
    }
}

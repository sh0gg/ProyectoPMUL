package com.example.examendbr;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SuscripcionActivity extends AppCompatActivity {

    private TextView tvNombreCanal, tvCuota, tvPrecio;
    private EditText etNumPantallas;
    private Button bContratar, bCancelar;

    private String nombreCanal;
    private int cuota;
    private int precioPorPantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suscripcion);

        tvNombreCanal = findViewById(R.id.tvNombreCanal);
        tvCuota = findViewById(R.id.tvCuota);
        tvPrecio = findViewById(R.id.tvPrecio);
        etNumPantallas = findViewById(R.id.etNumPantallas);
        bContratar = findViewById(R.id.bContratar);
        bCancelar = findViewById(R.id.bCancelar);

        Intent i = getIntent();
        nombreCanal = i.getStringExtra("NOMBRE_CANAL");
        cuota = i.getIntExtra("CUOTA", 0);
        precioPorPantalla = i.getIntExtra("PRECIO", 0);

        tvNombreCanal.setText("Canal: " + nombreCanal);
        tvCuota.setText("Cuota fija: " + cuota + "€");
        tvPrecio.setText("Precio por pantalla: " + precioPorPantalla + "€");

        bContratar.setOnClickListener(v -> contratar());
        bCancelar.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void contratar() {
        String txt = etNumPantallas.getText().toString().trim();
        if (TextUtils.isEmpty(txt)) {
            etNumPantallas.setError("Introduce un número");
            return;
        }
        int numPantallas = Integer.parseInt(txt);
        if (numPantallas <= 0) {
            etNumPantallas.setError("Debe ser mayor que 0");
            return;
        }

        Intent data = new Intent();
        data.putExtra("NOMBRE_CANAL", nombreCanal);
        data.putExtra("NUM_PANTALLAS", numPantallas);
        setResult(RESULT_OK, data);
        finish();
    }
}

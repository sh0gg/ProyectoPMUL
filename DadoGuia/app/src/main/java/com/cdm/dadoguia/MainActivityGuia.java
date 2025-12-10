package com.cdm.dadoguia;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivityGuia extends AppCompatActivity {

    // Campos donde el usuario introduce los parámetros
    EditText etNumDados, etNumCarasDados, etNumMaxTiradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guia);

        // Obtenemos referencias
        etNumDados      = findViewById(R.id.etNumDados);
        etNumCarasDados = findViewById(R.id.etNumCarasDados);
        etNumMaxTiradas = findViewById(R.id.etNumMaxTiradas);
        Button bStart   = findViewById(R.id.bStart);

        // Cuando pulsa INICIAR, cambiamos de Activity con los datos
        bStart.setOnClickListener(v -> {

            // Leemos los valores introducidos por el usuario
            int numDados      = Integer.parseInt(etNumDados.getText().toString());
            int numCarasDados = Integer.parseInt(etNumCarasDados.getText().toString());
            int numMaxTiradas = Integer.parseInt(etNumMaxTiradas.getText().toString());

            // Creamos intent a la segunda Activity
            Intent intent = new Intent(MainActivityGuia.this, JuegoActivityGuia.class);

            // Metemos los datos como extras para la partida
            intent.putExtra("numDados", numDados);
            intent.putExtra("numCarasDados", numCarasDados);
            intent.putExtra("numMaxTiradas", numMaxTiradas);

            // Lanzamos el juego
            startActivity(intent);
        });
    }
}

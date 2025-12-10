package com.example.fragmentodado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tvNumDados = findViewById(R.id.tvNumDados);
        TextView tvNumCarasDados = findViewById(R.id.tvNumCarasDados);
        TextView tvNumMaxTiradas = findViewById(R.id.tvNumMaxTiradas);
        EditText etNumDados = findViewById(R.id.etNumDados);
        EditText etNumCarasDados = findViewById(R.id.etNumCarasDados);
        EditText etNumMaxTiradas = findViewById(R.id.etNumMaxTiradas);
        Button bStart = findViewById(R.id.bStart);

        bStart.setOnClickListener(v -> {
            int numDados = Integer.parseInt(etNumDados.getText().toString());
            int numCarasDados = Integer.parseInt(etNumCarasDados.getText().toString());
            int numMaxTiradas = Integer.parseInt(etNumMaxTiradas.getText().toString());

            Intent intent = new Intent(MainActivity.this, JuegoActivity.class);
            intent.putExtra("numDados", numDados);
            intent.putExtra("numCarasDados", numCarasDados);
            intent.putExtra("numMaxTiradas", numMaxTiradas);
            startActivity(intent);
        });
    }
}

package com.example.fragmentodado;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView tvNumDados = findViewById(R.id.tvNumDados);
        TextView tvNumCarasDados = findViewById(R.id.tvNumCarasDados);
        EditText etNumDados = findViewById(R.id.etNumDados);
        EditText etNumCarasDados = findViewById(R.id.etNumCarasDados);
        Button bStart = findViewById(R.id.bStart);

        bStart.setOnClickListener(v -> {
            int numDados = Integer.parseInt(etNumDados.getText().toString());
            int numCarasDados = Integer.parseInt(etNumCarasDados.getText().toString());

            Intent intent = new Intent(this, JuegoActivity.class);
            intent.putExtra("numDados", numDados);
            intent.putExtra("numCarasDados", numCarasDados);
            startActivity(intent);
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
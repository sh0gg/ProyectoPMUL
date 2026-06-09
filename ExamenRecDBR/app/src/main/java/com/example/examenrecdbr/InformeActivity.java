package com.example.examenrecdbr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InformeActivity extends AppCompatActivity {

    String texto;
    TextView tvMensaje;
    Button bGuardar;
    Button bCancelar;
    Button bIgnorar;
    AsistenteBD asistenteBD;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informe);
        Intent intent = getIntent();
        texto = intent.getStringExtra("texto");
        id = intent.getIntExtra("id", 0);

        tvMensaje = findViewById(R.id.tvMensaje);
        tvMensaje.setText("Vas a apagar sin guardar los datos de tu ordenador. ¿Estás seguro?");
        bGuardar = findViewById(R.id.bGuardar);
        bGuardar.setOnClickListener(view -> guardar());
        bCancelar = findViewById(R.id.bCancelar);
        bCancelar.setOnClickListener(view -> cancelar());
        bIgnorar = findViewById(R.id.bIgnorar);
        bIgnorar.setOnClickListener(view -> ignorar());


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_informe);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void guardar() {
        asistenteBD.registrar(id, texto);
    }

    public void cancelar() {

    }

    public void ignorar() {


    }
}
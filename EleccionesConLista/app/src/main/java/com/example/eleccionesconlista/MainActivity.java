package com.example.eleccionesconlista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.eleccionesconlista.db.ConexionBD;
import com.example.eleccionesconlista.db.Utiles;

public class MainActivity extends AppCompatActivity {

    private ConexionBD conexionBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        conexionBD = new ConexionBD(this);
        conexionBD.abrir();

        EditText etNIF = findViewById(R.id.etNIF);
        EditText etPswd = findViewById(R.id.etPswd);
        Button bLogin = findViewById(R.id.bLogin);

        bLogin.setOnClickListener(v -> {
            String nif = etNIF.getText().toString().trim();
            String password = etPswd.getText().toString();

            if (nif.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!Utiles.NifOk(nif)) {
                Toast.makeText(this, "NIF inválido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (conexionBD.login(nif, password)) {
                // Login correcto y ahora redirigimos a la actividad de votación
                Toast.makeText(this, "Login correcto", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, ActivityVotacion.class);
                intent.putExtra("nif", nif);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}
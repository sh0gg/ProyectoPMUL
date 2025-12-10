package com.example.ejemplo; // mismo paquete

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements FrgEjemplo.OnFrgEjemplo {

    FrgEjemplo frgEjemplo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtenemos el fragmento estático del layout
        FragmentManager fm = getSupportFragmentManager();
        frgEjemplo = (FrgEjemplo) fm.findFragmentById(R.id.frgEjemplo);

        // Registramos el listener
        if (frgEjemplo != null) {
            frgEjemplo.setOnFrgEjemplo(this);
        }
    }

    // Implementación del callback del fragmento
    @Override
    public void onBotonPulsado(FrgEjemplo frg, int contador) {
        // Aquí decides qué hacer cuando el fragmento avisa
        Toast.makeText(this,
                "Botón pulsado. Contador = " + contador,
                Toast.LENGTH_SHORT).show();
    }
}

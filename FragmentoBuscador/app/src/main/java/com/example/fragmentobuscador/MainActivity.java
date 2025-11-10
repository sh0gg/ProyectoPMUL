package com.example.fragmentobuscador;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FragmentoFiltro.OnElementoSeleccionadoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // lista de ejemplo
        ArrayList<String> lista = new ArrayList<>();
        lista.add("Manzana");
        lista.add("Mandarina");
        lista.add("Melón");
        lista.add("Banana");
        lista.add("Sandía");
        lista.add("Pera");
        lista.add("Fresa");
        lista.add("Uva");

        FragmentManager gestor = getSupportFragmentManager();
        FragmentoFiltro frg = (FragmentoFiltro) gestor.findFragmentById(R.id.idFragmentoFiltro);

        if (frg != null) {
            frg.pasarDatos(lista, false);
        }
    }

    @Override
    public void onElementoSeleccionado(String elemento) {
        Toast.makeText(this, "Seleccionaste: " + elemento, Toast.LENGTH_SHORT).show();
    }
}

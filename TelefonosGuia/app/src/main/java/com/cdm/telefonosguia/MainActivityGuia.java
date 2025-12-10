package com.cdm.telefonosguia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityGuia extends AppCompatActivity
        implements FrgTelefonoGuia.OnFrgTelefonoGuia {

    // IDs de los fragmentos estáticos en el XML
    int ids[] = { R.id.frgT1, R.id.frgT2, R.id.frgT3, R.id.frgT4 };
    FrgTelefonoGuia[] listaFrgTelefonos;

    // Historial
    TextView tvHistorial;
    StringBuilder sbHistorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guia);

        // Historial
        tvHistorial = findViewById(R.id.tvHistorial);
        sbHistorial = new StringBuilder();
        addLineaHistorial("Aplicación iniciada");

        FragmentManager fm = getSupportFragmentManager();
        listaFrgTelefonos = new FrgTelefonoGuia[ids.length];

        int i = 0;
        for (int id : ids) {
            FrgTelefonoGuia frgTelefono = (FrgTelefonoGuia) fm.findFragmentById(id);
            listaFrgTelefonos[i] = frgTelefono;

            // Creamos el modelo con número (1, 2, 3, 4…)
            TelefonoGuia tel = new TelefonoGuia(i + 1);

            // Registramos Activity + modelo en cada fragment
            frgTelefono.setOnFrgTelefonoGuia(tel, this);

            i++;
        }
    }

    // ==========
    // CALLBACKS DEL FRAGMENTO
    // ==========

    @Override
    public boolean llamar(TelefonoGuia telefonoOrigen, int numeroDestino) {
        boolean destinoDisponible = true;

        FrgTelefonoGuia frgTelefonoDestino;

        // Si el destino está entre los teléfonos de la app
        if ((frgTelefonoDestino = getFrgTelefono(numeroDestino)) != null) {
            destinoDisponible = frgTelefonoDestino.llaman(telefonoOrigen);
        }

        String mensaje = telefonoOrigen.getNumero() + " >>> " + numeroDestino;
        mensaje += " >>> " + getString(destinoDisponible
                ? R.string.aceptaLlamada
                : R.string.comunicando);

        // Mostrar en Toast
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        // Añadir al historial
        addLineaHistorial(mensaje);

        return destinoDisponible;
    }

    @Override
    public void colgar(TelefonoGuia telefonoOrigen, int numeroDestino) {
        FrgTelefonoGuia frgTelefono;

        // Si el destino está en la app, actualizamos su fragmento también
        if ((frgTelefono = getFrgTelefono(numeroDestino)) != null) {
            frgTelefono.cuelgan();
        }

        String mensaje = telefonoOrigen.getNumero() + " corta a " + numeroDestino;

        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        addLineaHistorial(mensaje);
    }

    // ==========
    // OBTENER FRAGMENTO POR NÚMERO
    // ==========
    private FrgTelefonoGuia getFrgTelefono(int numero) {
        if (numero < 1 || numero > ids.length) return null;
        return listaFrgTelefonos[numero - 1];
    }

    // ==========
    // HISTORIAL
    // ==========
    private void addLineaHistorial(String linea) {
        sbHistorial.append(linea).append("\n");
        tvHistorial.setText(sbHistorial.toString());
    }
}

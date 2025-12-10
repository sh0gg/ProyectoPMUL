package com.cdm.botonlimitadoguia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityGuia extends AppCompatActivity
        implements FrgBotonLimitadoGuia.OnFrgBotonLimitado {

    FrgBotonLimitadoGuia frg;
    TextView tvEstado;
    int limite = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guia);

        tvEstado = findViewById(R.id.tvEstado);

        FragmentManager fm = getSupportFragmentManager();
        frg = (FrgBotonLimitadoGuia) fm.findFragmentById(R.id.frgBoton);

        frg.setOnFrgBotonLimitado(this, limite);
    }

    @Override
    public boolean onClick(FrgBotonLimitadoGuia frg, int numClics) {
        tvEstado.setText("Clic nº " + numClics);
        return true;
    }

    @Override
    public void ultimoClick(FrgBotonLimitadoGuia frg, int numClics) {
        tvEstado.setText("Límite alcanzado (" + numClics + ")");
        Toast.makeText(this, "Máximo de clics!", Toast.LENGTH_SHORT).show();
    }
}

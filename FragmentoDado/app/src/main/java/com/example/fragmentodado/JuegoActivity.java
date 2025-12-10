package com.example.fragmentodado;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class JuegoActivity extends AppCompatActivity implements FrgDado.OnRachaListener, FrgDado.OnTirarListener {
    LinearLayout llContenedorFrgs;
    FragmentManager fm;
    private int[] tiradasPorDado;
    private final int MAX_TIRADAS = 3;
    private int numMaxTiradas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego);
        llContenedorFrgs = findViewById(R.id.ll);
        fm = getSupportFragmentManager();

        int numDados = getIntent().getIntExtra("numDados", 1);
        int numCaras = getIntent().getIntExtra("numCarasDados", 6);
        numMaxTiradas = getIntent().getIntExtra("numMaxTiradas", MAX_TIRADAS);

        tiradasPorDado = new int[numDados];

        FragmentTransaction ft = fm.beginTransaction();
        for (int i = 0; i < numDados; i++) {
            FrgDado frgDado = FrgDado.newInstance(i, numCaras);
            FragmentContainerView fcv = new FragmentContainerView(this);
            fcv.setId(View.generateViewId());
            llContenedorFrgs.addView(fcv);
            ft.add(fcv.getId(), frgDado);
        }
        ft.commit();
    }

    @Override
    public void onRacha(int numDado, int resultado) {
        Toast.makeText(this,
                "Dado " + (numDado + 1) + " hizo racha con el número " + resultado,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTirar(int numDado, Button btnDado) {
        tiradasPorDado[numDado]++;
        if (tiradasPorDado[numDado] >= numMaxTiradas) {
            btnDado.setEnabled(false);
        }
    }
}

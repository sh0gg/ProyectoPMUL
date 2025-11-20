package com.example.fragmentodado;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class JuegoActivity extends AppCompatActivity implements FrgDado.OnRachaListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(new View(this));

        int numDados = getIntent().getIntExtra("numDados", 1);
        int numCaras = getIntent().getIntExtra("numCarasDados", 6);

        for (int i = 0; i < numDados; i++) {
            FrgDado frg = FrgDado.newInstance(i, numCaras);
            getSupportFragmentManager().beginTransaction()
                    .add(android.R.id.content, frg, "dado" + i)
                    .commit();
        }
    }

    @Override
    public void onRacha(int numDado, int resultado) {
        Toast.makeText(this,
                "Dado " + (numDado + 1) + " hizo racha con el número " + resultado,
                Toast.LENGTH_SHORT).show();
    }
}

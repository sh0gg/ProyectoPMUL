package com.example.examenrecdbr;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements FrgOrdenador.onFrgOrdenadorEClick, FrgOrdenador.onFrgOrdenadorOnOffClick, FrgOrdenador.onFrgOrdenadorHClick {

    FragmentManager fm;
    LinearLayout llContenedorFrgs;

    private final int NUM_PCS= 3;

    private List<Ordenador> ordenadores;
    private List<FrgOrdenador> fragmentos;

    private ListView lvRegistros;

    private AsistenteBD asistenteBD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ordenadores = new ArrayList<>();
        for (int i = 0; i < NUM_PCS; i++) {
            ordenadores.add(new Ordenador(i + 1));
        }

        llContenedorFrgs = findViewById(R.id.ll);

        lvRegistros = findViewById(R.id.lvRegistros);

        asistenteBD = new AsistenteBD(this);
        asistenteBD.getWritableDatabase().close();

        FragmentManager fm = getSupportFragmentManager();

        FragmentTransaction ft = fm.beginTransaction();

        FragmentContainerView fcv = new FragmentContainerView(this);
        fcv.setId(View.generateViewId());

        for (Ordenador o : ordenadores) {
            FrgOrdenador frg = FrgOrdenador.newInstance(o);
            fragmentos.add(frg);
            ft.add(fcv.getId(), frg);
            frg.setEnviarListener(o, this);
            frg.setOnOffListener(o, this);
            frg.setHibernarListener(o, this);
        }

        ft.commit();

    }

    @Override
    public void onEnviar(FrgOrdenador frg, String texto) {
        asistenteBD.registrar(frg.getOrdenador().getID(), texto);
    }

    @Override
    public void onHibernar(FrgOrdenador frg, String texto) {
        if (frg.getOrdenador().getStatus().equals("off")) {
            return;
        } else {
            frg.getOrdenador().cambiarEstado("hibernar");
            if (!frg.getOrdenador().getStatus().equals("hibernando"))
                frg.setRegistroAux(texto);
        }
    }

    @Override
    public void onOnOff(FrgOrdenador frg) {
        // leemos estado
        String status = frg.getOrdenador().getStatus();

        if (status.equals("hibernando")){
            frg.getOrdenador().cambiarEstado("onoff");
            frg.setEtProcesador(frg,frg.getRegistroAux());
        } else if (status.equals("off")) {
            frg.getOrdenador().cambiarEstado("onoff");
        } else if (status.equals("on")) {
            if (!frg.etProcesador.getText().toString().isEmpty()) {
                frg.setRegistroAux(frg.etProcesador.getText().toString());
                Intent intent = new Intent(this, InformeActivity.class);
                intent.putExtra("texto", frg.getRegistroAux());
                intent.putExtra("id", frg.getOrdenador().getID());
                startActivity(intent);
            }
            frg.getOrdenador().cambiarEstado("onoff");
        }
    }
}
package com.example.eleccionesconlista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.eleccionesconlista.adapter.CandidatoAdapter;
import com.example.eleccionesconlista.db.ConexionBD;
import com.example.eleccionesconlista.modelo.Candidato;
import com.example.eleccionesconlista.modelo.Usuario;

import java.util.ArrayList;
import java.util.List;

public class ActivityVotacion extends AppCompatActivity {

    private ListView listViewCandidatos;
    private ArrayList<Candidato> candidatos;
    private CandidatoAdapter adapter;
    private ConexionBD conexionBD;
    private Button btnVotar;
    private String nif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_votacion);

        listViewCandidatos = findViewById(R.id.listViewCandidatos);
        btnVotar = findViewById(R.id.btnVotar);

        conexionBD = new ConexionBD(this);
        conexionBD.abrir();

        nif = getIntent().getStringExtra("nif");

        Usuario user = conexionBD.getUsuarioPorNif(nif);

        // Si el usuario ya ha votado, redirigir a los resultados.
        if (user != null && user.getHaVotado() == 1) {
            Toast.makeText(this, "Ya has votado. Mostrando resultados.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(ActivityVotacion.this, ResultadosActivity.class);
            startActivity(intent);
            finish();
            return;
        } 
        
        // Si el usuario no ha votado, mostrar la lista de candidatos.
        candidatos = conexionBD.getCandidatos();
        adapter = new CandidatoAdapter(this, candidatos, conexionBD);
        listViewCandidatos.setAdapter(adapter);

        btnVotar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Candidato> selectedCandidatos = adapter.getSelectedCandidatos();
                if (selectedCandidatos.size() == 3) {
                    long[] selectedCandidatoIds = new long[selectedCandidatos.size()];
                    for (int i = 0; i < selectedCandidatos.size(); i++) {
                        selectedCandidatoIds[i] = selectedCandidatos.get(i).getCodCandidato();
                    }
                    conexionBD.votar(selectedCandidatoIds, nif);
                    Intent intent = new Intent(ActivityVotacion.this, ResultadosActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ActivityVotacion.this, "Debes seleccionar 3 candidatos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (conexionBD != null) {
            conexionBD.cerrar();
        }
    }
}

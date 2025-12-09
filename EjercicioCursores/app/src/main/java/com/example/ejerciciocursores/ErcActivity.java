package com.example.ejerciciocursores;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

import java.util.ArrayList;

public class ErcActivity extends AppCompatActivity {

    private ListView lvEscapes;
    private EditText etMaxErrores;
    private TextView tvPartidas;
    private Button bJugar;

    private AsistenteBD helper;

    private ArrayList<String> nombres = new ArrayList<>();
    private ArrayList<String> caminos = new ArrayList<>();
    private int escapeSeleccionado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erc);

        helper = new AsistenteBD(this);

        lvEscapes = findViewById(R.id.lvEscapes);
        etMaxErrores = findViewById(R.id.etMaxErroresErc);
        tvPartidas = findViewById(R.id.tvPartidasConfigErc);
        bJugar = findViewById(R.id.bJugarErcPartida);

        cargarEscapes();

        lvEscapes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    android.view.View view, int position, long id) {
                escapeSeleccionado = position;
            }
        });

        etMaxErrores.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable editable) {
                String txt = editable.toString().trim();
                if (txt.isEmpty()) {
                    tvPartidas.setText(getString(R.string.label_partidas_config));
                } else {
                    try {
                        int errores = Integer.parseInt(txt);
                        int partidas = helper.obtenerPartidasConfig(errores);
                        String base = getString(R.string.label_partidas_config);
                        int pos = base.indexOf(":");
                        if (pos != -1) base = base.substring(0, pos);
                        tvPartidas.setText(base + ": " + partidas);
                    } catch (NumberFormatException e) {
                        tvPartidas.setText(getString(R.string.label_partidas_config));
                    }
                }
            }
        });

        bJugar.setOnClickListener(v -> {
            if (escapeSeleccionado == -1) {
                Toast.makeText(ErcActivity.this,
                        getString(R.string.toast_sin_escape),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String txt = etMaxErrores.getText().toString().trim();
            if (txt.isEmpty()) {
                Toast.makeText(ErcActivity.this,
                        getString(R.string.error_max_no_valido),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            int max;
            try {
                max = Integer.parseInt(txt);
            } catch (NumberFormatException e) {
                Toast.makeText(ErcActivity.this,
                        getString(R.string.error_max_no_valido),
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String nombre = nombres.get(escapeSeleccionado);
            String camino = caminos.get(escapeSeleccionado);

            Intent i = new Intent(ErcActivity.this, ErcJuegoActivity.class);
            i.putExtra("MAX_ERRORES", max);
            i.putExtra("NOMBRE_ESCAPE", nombre);
            i.putExtra("CAMINO_CORRECTO", camino);
            startActivity(i);
        });
    }

    private void cargarEscapes() {
        Cursor c = helper.obtenerEscapes();
        nombres.clear();
        caminos.clear();
        if (c.moveToFirst()) {
            do {
                nombres.add(c.getString(1)); // nombre
                caminos.add(c.getString(2)); // camino
            } while (c.moveToNext());
        }
        c.close();

        ArrayAdapter<String> ad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                nombres
        );
        lvEscapes.setAdapter(ad);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (helper != null) helper.close();
    }
}

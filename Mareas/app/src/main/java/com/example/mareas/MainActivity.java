package com.example.mareas;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    Spinner spPuertos;
    TextView tvPleamares, tvBajamares;

    private final String baseUrl = "https://servizos.meteogalicia.gal/mgrss/predicion/mareas/jsonMareas.action?idPorto=";

    private final String[] PUERTOS = {"Marín", "Vigo", "A Coruña", "Vilagarcía", "Ferrol"};
    private final int[] PUERTO_IDS = {15060, 15070, 15030, 15050, 15010};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spPuertos = findViewById(R.id.spPuertos);
        tvPleamares = findViewById(R.id.tvPleamares);
        tvBajamares = findViewById(R.id.tvBajamares);

        // Cargar datos en Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, PUERTOS
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPuertos.setAdapter(adapter);

        // Seleccionar por defecto Marín
        spPuertos.setSelection(0);

        // Evento al seleccionar puerto
        spPuertos.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                int idPorto = PUERTO_IDS[position];
                cargarMareas(idPorto);
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });
    }

    private void cargarMareas(int idPorto) {
        String url = baseUrl + idPorto;

        new Thread(() -> {
            String json = HTTP.getUrlContents(url);

            runOnUiThread(() -> parsearMareas(json));
        }).start();
    }

    private void parsearMareas(String strJSONMareas) {
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<Mareas>() {}.getType();
            Mareas mareas = gson.fromJson(strJSONMareas, type);

            List<Marea> lista = mareas.getListaMareas();

            // Filtrar pleamares y bajamares
            List<Marea> pleamares = lista.stream()
                    .filter(m -> m.getTipoMarea().equalsIgnoreCase("Pleamar"))
                    .collect(Collectors.toList());

            List<Marea> bajamares = lista.stream()
                    .filter(m -> m.getTipoMarea().equalsIgnoreCase("Baixamar"))
                    .collect(Collectors.toList());

            // Mostrar
            tvPleamares.setText("Pleamares:\n" + formatMareas(pleamares));
            tvBajamares.setText("Bajamares:\n" + formatMareas(bajamares));

        } catch (Exception e) {
            Log.e("Error", "Error al parsear JSON", e);
        }
    }

    private String formatMareas(List<Marea> mareas) {
        StringBuilder sb = new StringBuilder();

        for (Marea m : mareas) {
            sb.append(m.getHora().toString())
                    .append("  altura: ")
                    .append(m.getAltura())
                    .append(" m\n");
        }
        return sb.toString();
    }
}

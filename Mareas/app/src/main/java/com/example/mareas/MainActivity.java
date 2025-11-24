package com.example.mareas;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String url = "https://servizos.meteogalicia.gal/mgrss/predicion/mareas/jsonMareas.action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(() -> {
            String strJSONMareas = HTTP.getUrlContents(url);
            parsearMareas(strJSONMareas);
        }).start();
    }

    private void parsearMareas(String strJSONMareas) {
        try {
            Gson gson = new Gson();
            Type tipoMareas = new TypeToken<Mareas>() {
            }.getType();
            Mareas mareas = gson.fromJson(strJSONMareas, tipoMareas);

            List<Marea> listaMareas = mareas.getListaMareas();

        } catch (Exception e) {
            Log.e("Error", "Error al parsear JSON", e);
        }
    }
}

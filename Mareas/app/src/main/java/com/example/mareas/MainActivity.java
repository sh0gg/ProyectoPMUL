package com.example.mareas;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView tvURL;
    EditText etURL;

    //private final String url = "https://www.w3schools.com/xml/note.xml";
    //private final String url = "https://www.duolingo.com/2017-06-30/users/";
    private final String url = "https://servizos.meteogalicia.gal/mgrss/predicion/mareas/jsonMareas.action?idPorto=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvURL = findViewById(R.id.tvURL);
        etURL = findViewById(R.id.etURL);
        etURL.setText(url);

        new Thread(() -> {
            String strJSONMareas = HTTP.getUrlContents(url);
            tvURL.setText(strJSONMareas);
        }).start();
    }
}

//    private void parsearMareas(String strJSONMareas) {
//        try {
//            Gson gson = new Gson();
//            Type tipoMareas = new TypeToken<Mareas>() {
//            }.getType();
//            Mareas mareas = gson.fromJson(strJSONMareas, tipoMareas);
//
//            List<Marea> listaMareas = mareas.getListaMareas();
//
//        } catch (Exception e) {
//            Log.e("Error", "Error al parsear JSON", e);
//        }
//    }
//}

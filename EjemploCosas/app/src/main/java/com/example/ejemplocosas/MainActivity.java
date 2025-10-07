package com.example.ejemplocosas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private String[] NOMBRE_CLIENTES = {"Cliente 1", "Cliente 2", "Cliente 3", "Cliente 4"};

    private ListView lvClientes;
    private TextView tvTitulo;

    // launcher de la segunda actividad
    ActivityResultLauncher<Intent> resultLauncher;


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

        lvClientes = findViewById(R.id.lvClientes);
        tvTitulo = findViewById(R.id.tvTitulo);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, NOMBRE_CLIENTES);

        lvClientes.setAdapter(adapter);

        // registra lo que devuelve el intent a la segunda activity
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String respuesta = result.getData().getStringExtra("RESPUESTA");
                        // y lo pone en el titulo de esta
                        String textino = getString(R.string.hola) + " " + respuesta;
                        tvTitulo.setText(textino);
                    }
                }
        );

        // cambiar tvTitulo al clickar en la lista

        lvClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // cambia el texto del titulo

                String text = getString(R.string.hola) + " " + NOMBRE_CLIENTES[position];
                tvTitulo.setText(text);

                // y pasa el texto a la segunda actividad

                Intent intent = new Intent(MainActivity.this, SegundaActivity.class);
                intent.putExtra("texto", text);
                resultLauncher.launch(intent);

            }
        });






    }
}
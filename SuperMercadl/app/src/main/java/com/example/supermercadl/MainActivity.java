package com.example.supermercadl;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tvTicket;

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


        // DATOS
        Map<Producto, Integer> carrito = new HashMap<>();

        List<Producto> productos = new ArrayList<>();
        productos.add(new Producto(1, "Koka-Cola", 2.5));
        productos.add(new Producto(2, "Rosquillas Pepe", 2.99));
        productos.add(new Producto(3, "Pan de molde mohoso", 1.50));
        productos.add(new Producto(4, "Media cuña de queso", 7.45));
        productos.add(new Producto(5, "Extracto de vainilla", 9.89));

        // Poblamos el adaptador
        ArrayAdapter<Producto> productoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, productos);
        productoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // INTERFAZ
        TextView tvTitulo = findViewById(R.id.tvTitulo); // no se usa

        // TODO: Poblar spinner - HECHO!!!
        // Asignamos el adaptador al spinner
        Spinner spProductos = findViewById(R.id.spProductos);
        spProductos.setAdapter(productoAdapter);

        EditText etNumeroArticulos = findViewById(R.id.etNumeroArticulos);
        tvTicket = findViewById(R.id.tvTicket);
        empezarTicket(); // ver metodo, es para poner la cabecera de tabla en el tvTicket
        Button bAdd = findViewById(R.id.bAdd);
        Button bSend = findViewById(R.id.bSend);
        Button bCancel = findViewById(R.id.bCancel);


        bAdd.setOnClickListener(v -> {
            Producto producto = (Producto) spProductos.getSelectedItem();
            int numeroArticulos = Integer.parseInt(etNumeroArticulos.getText().toString());
            carrito.put(producto, numeroArticulos);
            String linea = "\n " + numeroArticulos + " | " + producto.getNombre() + " | " + producto.getPrecio() + "€ | " + producto.getPrecio() * numeroArticulos + "€";
            tvTicket.append(linea);
        });

        bSend.setOnClickListener(v -> {
            // TODO: Mandar el mapa a una nueva activity mediante intent y limpiar (carrito y tvTicket)

            // limpiar
            carrito.clear();
            empezarTicket();
        });

        bCancel.setOnClickListener(v -> {
            carrito.clear();
            empezarTicket();
        });


    }

    private void empezarTicket() {
        tvTicket.setText("");
        String cabecera = "Cantidad | Producto | Precio | Total";
        tvTicket.setText(cabecera);
    }
}
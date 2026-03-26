package com.example.supermercadl;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ListView lvTicket;
    private List<String> lineasTicket;
    private ArrayAdapter<String> adapterTicket;

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

        lvTicket = findViewById(R.id.lvTicket);
        lineasTicket = new ArrayList<>();
        adapterTicket = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lineasTicket);
        lvTicket.setAdapter(adapterTicket);

        // empezarTicket(); ver metodo, es para poner la cabecera de tabla en el tvTicket -- YA NO EXISTE EL METODO, CAMBIE A LV
        Button bAdd = findViewById(R.id.bAdd);
        Button bSend = findViewById(R.id.bSend);
        Button bCancel = findViewById(R.id.bCancel);

        bAdd.setOnClickListener(v -> {
            String cantidadStr = etNumeroArticulos.getText().toString();
            if (!cantidadStr.matches("[1-9]+")) {
                Toast.makeText(this, "Escribe un número entero (solo dígitos), mayor que cero", Toast.LENGTH_SHORT).show();
                return;
            }

            int numeroArticulos = Integer.parseInt(cantidadStr);

            Producto producto = (Producto) spProductos.getSelectedItem();

            if (carrito.containsKey(producto)) {
                int cantidadAnterior = carrito.get(producto);
                carrito.put(producto, cantidadAnterior + numeroArticulos);
            } else {
                carrito.put(producto, numeroArticulos);
            }

            double subTotal = producto.getPrecio() * numeroArticulos;
            String linea = numeroArticulos + " x " + producto.getNombre() + " | Subtotal: " + subTotal + "€";
            lineasTicket.add(linea);
            adapterTicket.notifyDataSetChanged();

            etNumeroArticulos.setText(""); // limpiar campo
        });

        bSend.setOnClickListener(v -> {
            // TODO: Mandar el mapa a una nueva activity mediante intent y limpiar (carrito y tvTicket)
            if (carrito.isEmpty()) {
                Toast.makeText(this, "El carrito está vacío", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, ResumenActivity.class);
            intent.putExtra("carritoCompra", (Serializable) carrito);
            startActivity(intent);

            // limpiar
            carrito.clear();
            lineasTicket.clear();
            adapterTicket.notifyDataSetChanged();
        });

        bCancel.setOnClickListener(v -> {
            carrito.clear();
            lineasTicket.clear();
            adapterTicket.notifyDataSetChanged();
        });


    }

}
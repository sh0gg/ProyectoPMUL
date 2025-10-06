package com.example.mantenimientodeclientes;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ClienteActivity extends AppCompatActivity {

    private EditText etNombre, etApellidos, etNIF, etLatitud, etLongitud;
    private Spinner spinnerProvincia;
    private CheckBox cbVIP;
    private Button btnVerMapa;
    private double latitud = 0.0;
    private double longitud = 0.0;
    private AsistenteBD asistenteBD;
    private int codCliente = -1;  // Por defecto, nuevo cliente

    private ArrayList<Integer> codigosProvincias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        TextView tvTituloCliente = findViewById(R.id.tvTituloCliente);
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etNIF = findViewById(R.id.etNIF);
        etLatitud = findViewById(R.id.etLatitud);
        etLongitud = findViewById(R.id.etLongitud);
        spinnerProvincia = findViewById(R.id.spinnerProvincia);
        cbVIP = findViewById(R.id.cbVIP);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        btnVerMapa = findViewById(R.id.btnVerMapa);

        asistenteBD = new AsistenteBD(this);

        cargarProvincias();

        // Ver si viene un codCliente para editar
        codCliente = getIntent().getIntExtra("codCliente", -1);

        if (codCliente == -1) {
            // Crear nuevo cliente
            tvTituloCliente.setText("Crear Cliente");
            btnVerMapa.setEnabled(false); // por defecto deshabilitado
        } else {
            // Editar cliente existente
            tvTituloCliente.setText("Editar Cliente");
            cargarDatosCliente(codCliente);
        }

        btnVerMapa.setOnClickListener(v -> {
            if (latitud != 0 && longitud != 0) {
                String uri = String.format("geo:%f,%f?q=%f,%f(%s)", latitud, longitud, latitud, longitud, etNombre.getText().toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setPackage("com.google.android.apps.maps"); // Opcional: abrir directamente Google Maps
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "No hay aplicación de mapas instalada", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnGuardar.setOnClickListener(v -> guardarCliente());
    }

    private void cargarProvincias() {
        ArrayList<String> listaProvincias = new ArrayList<>();
        codigosProvincias = new ArrayList<>();

        Cursor cursor = asistenteBD.getReadableDatabase()
                .rawQuery("SELECT codProvincia, nombre FROM provincias", null);
        if (cursor.moveToFirst()) {
            do {
                codigosProvincias.add(cursor.getInt(0));
                listaProvincias.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, listaProvincias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvincia.setAdapter(adapter);
    }

    private void cargarDatosCliente(int codCliente) {
        Cursor cursor = asistenteBD.getReadableDatabase().rawQuery(
                "SELECT nombre, apellidos, NIF, codProvincia, VIP, latitud, longitud FROM clientes WHERE codCliente = ?",
                new String[]{String.valueOf(codCliente)});
        if (cursor.moveToFirst()) {
            etNombre.setText(cursor.getString(0));
            etApellidos.setText(cursor.getString(1));
            etNIF.setText(cursor.getString(2));
            int provinciaIndex = codigosProvincias.indexOf(cursor.getInt(3));
            if (provinciaIndex >= 0) spinnerProvincia.setSelection(provinciaIndex);
            cbVIP.setChecked(cursor.getInt(4) == 1);
            latitud = cursor.getDouble(5);
            longitud = cursor.getDouble(6);
            etLatitud.setText(String.valueOf(latitud));
            etLongitud.setText(String.valueOf(longitud));
            btnVerMapa.setEnabled(latitud != 0.0 && longitud != 0.0);
        }
        cursor.close();
    }

    private void guardarCliente() {
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String nif = etNIF.getText().toString().trim();
        int provinciaPos = spinnerProvincia.getSelectedItemPosition();
        int codProvincia = (provinciaPos >= 0) ? codigosProvincias.get(provinciaPos) : -1;
        int vip = cbVIP.isChecked() ? 1 : 0;
        double latitud, longitud;

        try {
            latitud = Double.parseDouble(etLatitud.getText().toString().trim());
            longitud = Double.parseDouble(etLongitud.getText().toString().trim());
        } catch (NumberFormatException e) {
            // No pasa nada, dejamos 0 si está vacío o mal
            latitud = 0;
            longitud = 0;
        }

        if (nombre.isEmpty() || apellidos.isEmpty() || nif.isEmpty() || codProvincia == -1) {
            Toast.makeText(this, "Por favor, rellena todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put("nombre", nombre);
        cv.put("apellidos", apellidos);
        cv.put("NIF", nif);
        cv.put("codProvincia", codProvincia);
        cv.put("VIP", vip);
        cv.put("latitud", latitud);
        cv.put("longitud", longitud);

        SQLiteDatabase db = asistenteBD.getWritableDatabase();

        if (codCliente == -1) {
            // Insertar nuevo cliente
            long id = db.insert("clientes", null, cv);
            if (id > 0) {
                Toast.makeText(this, "Cliente creado", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error al crear cliente", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Actualizar cliente existente
            int filas = db.update("clientes", cv, "codCliente = ?", new String[]{String.valueOf(codCliente)});
            if (filas > 0) {
                Toast.makeText(this, "Cliente actualizado", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Error al actualizar cliente", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

package com.example.mantenimientodeclientes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private AsistenteBD asistenteBD;

    private ArrayList<String> listaClientes;   // Para mostrar en la lista
    private ArrayList<Integer> codClientes;    // Para saber codCliente al hacer clic

    private ArrayAdapter<String> adapter;
    private ListView lvClientes;

    private final ActivityResultLauncher<Intent> clienteActivityLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    cargarClientes();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvClientes = findViewById(R.id.lvClientes);
        FloatingActionButton btnAddCliente = findViewById(R.id.btnAddCliente);
        asistenteBD = new AsistenteBD(this);

        listaClientes = new ArrayList<>();
        codClientes = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item_cliente, listaClientes);
        lvClientes.setAdapter(adapter);

        cargarClientes();

        btnAddCliente.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClienteActivity.class);
            clienteActivityLauncher.launch(intent);
        });

        lvClientes.setOnItemClickListener((parent, view, position, id) -> {
            int codCliente = codClientes.get(position);
            Intent intent = new Intent(MainActivity.this, ClienteActivity.class);
            intent.putExtra("codCliente", codCliente);
            clienteActivityLauncher.launch(intent);
        });

        // --- Implementación de borrado múltiple ---
        lvClientes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        lvClientes.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            private final ArrayList<Integer> clientesSeleccionados = new ArrayList<>();

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                int codCliente = codClientes.get(position);
                if (checked) {
                    clientesSeleccionados.add(codCliente);
                } else {
                    clientesSeleccionados.remove(Integer.valueOf(codCliente));
                }
                mode.setTitle(clientesSeleccionados.size() + " seleccionados");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_contextual, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                if (item.getItemId() == R.id.item_borrar) {
                    if (!clientesSeleccionados.isEmpty()) {
                        int borrados = asistenteBD.borrarClientes(clientesSeleccionados);
                        Toast.makeText(MainActivity.this, "Se han borrado " + borrados + " clientes", Toast.LENGTH_SHORT).show();
                        cargarClientes(); // Recargar la lista
                    }
                    mode.finish(); // Cierra el modo de selección
                    return true;
                }
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                clientesSeleccionados.clear();
            }
        });
    }

    private void cargarClientes() {
        listaClientes.clear();
        codClientes.clear();

        Cursor cursor = asistenteBD.getReadableDatabase()
                .rawQuery("SELECT codCliente, nombre, apellidos FROM clientes ORDER BY nombre", null);
        if (cursor.moveToFirst()) {
            do {
                int codCliente = cursor.getInt(0);
                String nombreCompleto = cursor.getString(1) + " " + cursor.getString(2);
                codClientes.add(codCliente);
                listaClientes.add(nombreCompleto);
            } while (cursor.moveToNext());
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }
}

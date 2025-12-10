package com.cdm.spinnerbdguia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity de ejemplo:
 *
 *  - Tiene dos fragmentos de tipo FrgSpinnerGuia:
 *      • frgMarcas   → muestra marcas de la BD
 *      • frgModelos  → muestra modelos de la marca seleccionada
 *
 *  - Cuando se selecciona una MARCA, se recarga el Spinner de MODELOS
 *    consultando la BD con AsistenteBD.
 */
public class MainActivityGuia extends AppCompatActivity
        implements FrgSpinnerGuia.OnFrgSpinnerGuiaListener {

    FrgSpinnerGuia frgMarcas, frgModelos;
    AsistenteBD asistenteBD;

    List<Marca> listaMarcas = new ArrayList<>();
    List<Modelo> listaModelos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_guia);

        asistenteBD = new AsistenteBD(this);

        FragmentManager fm = getSupportFragmentManager();
        frgMarcas = (FrgSpinnerGuia) fm.findFragmentById(R.id.frgMarcas);
        frgModelos = (FrgSpinnerGuia) fm.findFragmentById(R.id.frgModelos);

        // Títulos
        frgMarcas.setTitulo("Marca");
        frgModelos.setTitulo("Modelo");

        // Cargamos las MARCAS desde la BD y las pasamos al primer spinner
        listaMarcas = asistenteBD.getMarcas();
        List<String> nombresMarcas = new ArrayList<>();
        for (Marca m : listaMarcas) {
            nombresMarcas.add(m.nombre);
        }
        frgMarcas.setDatos(nombresMarcas);

        // Inicialmente, cargamos modelos de la primera marca (si existe)
        if (!listaMarcas.isEmpty()) {
            actualizarModelosDeMarca(listaMarcas.get(0).id);
        }
    }

    // =====================================================
    //   CALLBACK DE LOS FRAGMENTOS SPINNER
    // =====================================================
    @Override
    public void onItemSelected(FrgSpinnerGuia frg, int position, String texto) {

        if (frg == frgMarcas) {
            // Se ha seleccionado una MARCA → recargar modelos
            Marca marcaSeleccionada = listaMarcas.get(position);
            actualizarModelosDeMarca(marcaSeleccionada.id);
        }
        // Si quisieras, podrías hacer algo cuando se selecciona un modelo
        // en el segundo Spinner (frgModelos), comprobando: if (frg == frgModelos) { ... }
    }

    // =====================================================
    //   CARGAR MODELOS EN EL SEGUNDO SPINNER
    // =====================================================
    private void actualizarModelosDeMarca(int idMarca) {
        listaModelos = asistenteBD.getModelosPorMarca(idMarca);

        List<String> nombresModelos = new ArrayList<>();
        for (Modelo m : listaModelos) {
            nombresModelos.add(m.nombre);
        }

        frgModelos.setDatos(nombresModelos);
        frgModelos.setSeleccion(0);
    }
}

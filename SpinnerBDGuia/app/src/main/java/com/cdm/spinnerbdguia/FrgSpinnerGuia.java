package com.cdm.spinnerbdguia;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento GENÉRICO que contiene un Spinner.
 *
 * - La Activity le pasa una lista de Strings con setDatos(...)
 * - Cuando el usuario selecciona un elemento, llama a un listener.
 * - Lleva una etiqueta (TextView) para mostrar "Marca", "Modelo", etc.
 */
public class FrgSpinnerGuia extends Fragment {

    // Interfaz de comunicación con la Activity
    public interface OnFrgSpinnerGuiaListener {
        void onItemSelected(FrgSpinnerGuia frg, int position, String texto);
    }

    private OnFrgSpinnerGuiaListener listener;

    private TextView tvTitulo;
    private Spinner spinner;

    private String titulo = "";
    private final List<String> lista = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    // ============= ENGANCHE DEL LISTENER =============
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof OnFrgSpinnerGuiaListener)) {
            throw new RuntimeException("La Activity debe implementar OnFrgSpinnerGuiaListener");
        }
        listener = (OnFrgSpinnerGuiaListener) context;
    }

    // ============= INFLAR VISTA =============
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frg_spinner_guia, container, false);

        tvTitulo = v.findViewById(R.id.tvTituloSpinner);
        spinner = v.findViewById(R.id.spinner);

        tvTitulo.setText(titulo);

        // Adapter sencillo para lista de Strings
        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                lista
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // Listener de selección del Spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent,
                                       View view, int position, long id) {
                if (listener != null && position >= 0 && position < lista.size()) {
                    listener.onItemSelected(FrgSpinnerGuia.this,
                            position,
                            lista.get(position));
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        return v;
    }

    // ============= MÉTODOS PÚBLICOS =============

    /**
     * Cambia el título (texto encima del Spinner).
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
        if (tvTitulo != null) tvTitulo.setText(titulo);
    }

    /**
     * Rellena el Spinner con una nueva lista de opciones.
     */
    public void setDatos(List<String> datos) {
        lista.clear();
        lista.addAll(datos);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    /**
     * Permite seleccionar una posición desde la Activity (opcional).
     */
    public void setSeleccion(int pos) {
        if (spinner != null && pos >= 0 && pos < lista.size()) {
            spinner.setSelection(pos);
        }
    }
}

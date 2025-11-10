package com.example.fragmentobuscador;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FragmentoFiltro extends Fragment {

    private OnElementoSeleccionadoListener listener;
    private ArrayList<String> listaOriginal;
    private boolean sensibleMayusculas;
    private ArrayAdapter<String> adaptador;
    private List<String> listaFiltrada = new ArrayList<>();


    // para pasar los datos al fragmento
    public void pasarDatos(ArrayList<String> lista, boolean sensibleMayusculas) {
        this.listaOriginal = lista;
        this.sensibleMayusculas = sensibleMayusculas;
        listaFiltrada.clear();
        listaFiltrada.addAll(listaOriginal);
        if (adaptador != null) {
            adaptador.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnElementoSeleccionadoListener) {
            listener = (OnElementoSeleccionadoListener) context;
        } else {
            throw new ClassCastException(context.toString() + " debe implementar OnElementoSeleccionadoListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflador, @Nullable ViewGroup contenedor, @Nullable Bundle estadoGuardado) {

        View vista = inflador.inflate(R.layout.fragmento_filtro, contenedor, false);

        EditText cajaBusqueda = vista.findViewById(R.id.cajaBusqueda);
        ListView listaVista = vista.findViewById(R.id.listaVista);

        if (listaOriginal == null) {
            listaOriginal = new ArrayList<>();
        }
        if (adaptador == null) {
            adaptador = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, listaFiltrada);
            listaVista.setAdapter(adaptador);
        }

        cajaBusqueda.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarLista(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        listaVista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listener != null) {
                    listener.onElementoSeleccionado(listaFiltrada.get(position));
                }
            }
        });

        return vista;
    }

    private void filtrarLista(String texto) {
        listaFiltrada.clear();

        if (!sensibleMayusculas) texto = texto.toLowerCase();

        if (texto.isEmpty()) {
            listaFiltrada.addAll(listaOriginal);
        } else {
            List<String> empiezaPor = new ArrayList<>();
            List<String> contieneOrden = new ArrayList<>();
            List<String> contieneLetras = new ArrayList<>();

            for (String item : listaOriginal) {
                String comparar = sensibleMayusculas ? item : item.toLowerCase();

                if (comparar.startsWith(texto)) {
                    empiezaPor.add(item);
                } else if (comparar.contains(texto)) {
                    contieneOrden.add(item);
                } else if (contieneTodasLetras(comparar, texto)) {
                    contieneLetras.add(item);
                }
            }

            listaFiltrada.addAll(empiezaPor);
            listaFiltrada.addAll(contieneOrden);
            listaFiltrada.addAll(contieneLetras);
        }

        adaptador.notifyDataSetChanged();
    }

    private boolean contieneTodasLetras(String texto, String patron) {
        for (char c : patron.toCharArray()) {
            if (texto.indexOf(c) == -1) return false;
        }
        return true;
    }

    public interface OnElementoSeleccionadoListener {
        void onElementoSeleccionado(String elemento);
    }
}

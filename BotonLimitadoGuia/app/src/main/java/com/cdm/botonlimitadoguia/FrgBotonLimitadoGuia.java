package com.cdm.botonlimitadoguia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FrgBotonLimitadoGuia extends Fragment {

    // ============================
    // 1) INTERFAZ DE COMUNICACIÓN
    // ============================
    public interface OnFrgBotonLimitado {
        // Se llama en cada clic válido
        boolean onClick(FrgBotonLimitadoGuia frg, int numClics);

        // Se llama cuando llega al límite máximo
        void ultimoClick(FrgBotonLimitadoGuia frg, int numClics);
    }

    // ============================
    // 2) CAMPOS
    // ============================
    private OnFrgBotonLimitado listener;

    private Button boton;
    private int numClics = 0;
    private int maxClics = 5; // valor por defecto

    // ============================
    // 3) SETTER DEL LISTENER
    // ============================
    public void setOnFrgBotonLimitado(OnFrgBotonLimitado listener, int maxClics) {
        this.listener = listener;
        this.maxClics = maxClics;
    }

    // ============================
    // 4) INFLAR VISTA
    // ============================
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frg_boton_limitado_guia, container, false);

        boton = v.findViewById(R.id.botonLimitado);

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulsacion();
            }
        });

        return v;
    }

    // ============================
    // 5) LÓGICA DEL BOTÓN
    // ============================
    private void pulsacion() {

        if (listener == null) return;

        numClics++;

        boolean aceptado = listener.onClick(this, numClics);

        if (!aceptado) {
            // Si la Activity no acepta el clic, lo deshacemos
            numClics--;
            return;
        }

        // Si llegamos al último clic permitido
        if (numClics >= maxClics) {
            listener.ultimoClick(this, numClics);
            boton.setEnabled(false);
        }
    }

    public void reset() {
        numClics = 0;
        boton.setEnabled(true);
    }
}

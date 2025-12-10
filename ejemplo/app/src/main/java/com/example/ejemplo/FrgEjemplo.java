package com.example.ejemplo;  // cambia el paquete según tu proyecto

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FrgEjemplo extends Fragment {

    // ==========
    // 1) INTERFAZ DE COMUNICACIÓN CON LA ACTIVITY
    // ==========

    public interface OnFrgEjemplo {
        // Se llama cuando se pulsa el botón del fragmento
        // Puedes añadir más parámetros según necesites
        void onBotonPulsado(FrgEjemplo frg, int contador);
    }

    // ==========
    // 2) CAMPOS
    // ==========

    private OnFrgEjemplo listener = null;

    private Button boton;
    private TextView tvContador;

    // Estado interno del fragmento
    private int contador = 0;

    // ==========
    // 3) MÉTODO PARA REGISTRAR EL LISTENER Y OTROS DATOS
    // ==========

    // Aquí podrías pasar también parámetros de configuración
    public void setOnFrgEjemplo(OnFrgEjemplo listener) {
        this.listener = listener;
    }

    // ==========
    // 4) CICLO DE VIDA DEL FRAGMENTO
    // ==========

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflamos el layout del fragmento
        View layout = inflater.inflate(R.layout.frg_ejemplo, container, false);

        // Referencias a los controles
        boton = layout.findViewById(R.id.botonFrgEjemplo);
        tvContador = layout.findViewById(R.id.tvContador);

        // Estado inicial
        actualizarTextoContador();

        // Asignamos el onClick del botón
        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botonPulsado();
            }
        });

        return layout;
    }

    // ==========
    // 5) LÓGICA INTERNA + AVISO A LA ACTIVITY
    // ==========

    private void botonPulsado() {
        contador++;
        actualizarTextoContador();

        // Avisamos a la Activity si hay listener
        if (listener != null) {
            listener.onBotonPulsado(this, contador);
        }
    }

    private void actualizarTextoContador() {
        if (tvContador != null) {
            tvContador.setText("Contador: " + contador);
        }
    }

    // (Opcional) Método público para resetear desde la Activity
    public void reset() {
        contador = 0;
        actualizarTextoContador();
    }
}

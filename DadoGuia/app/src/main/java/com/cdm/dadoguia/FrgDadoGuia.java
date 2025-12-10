package com.cdm.dadoguia;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class FrgDadoGuia extends Fragment {

    // Claves para los argumentos que llegan por newInstance()
    private static final String ARG_ID = "idDado";
    private static final String ARG_CARAS = "numCaras";

    // Estado interno del fragmento
    private int idDado;          // identificador único (0,1,2,...)
    private int numCaras;        // nº de caras del dado
    private int ultimoResultado = -1;  // para detectar racha
    private int numTiradas = 0;        // cuántas tiradas lleva este dado

    private Button btnDado;      // botón que actúa como dado
    private final Random rand = new Random();

    // Lista de listeners hacia la Activity
    private OnRachaListener rachaListener;
    private OnTirarListener tirarListener;

    // ============================================================
    //    FACTORY METHOD newInstance()
    //    Esta es la forma correcta de pasar parámetros a un Fragment
    // ============================================================
    public static FrgDadoGuia newInstance(int idDado, int numCaras) {
        FrgDadoGuia frg = new FrgDadoGuia();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, idDado);
        args.putInt(ARG_CARAS, numCaras);
        frg.setArguments(args);
        return frg;
    }

    // ============================================================
    //    INTERFACES PARA COMUNICACIÓN CON LA ACTIVITY
    // ============================================================
    public interface OnRachaListener {
        // Se llama cuando dos tiradas salen iguales
        void onRacha(FrgDadoGuia frg, int numDado, int resultado);
    }

    public interface OnTirarListener {
        // Se llama en cada tirada
        void onTirar(FrgDadoGuia frg, int numDado, int resultado, int numTirada);
    }

    // ============================================================
    //    CICLO DE VIDA — onAttach()
    //    Aquí obtenemos las interfaces implementadas en la Activity
    // ============================================================
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // La Activity DEBE implementar estas interfaces (si no, error)
        if (context instanceof OnRachaListener) {
            rachaListener = (OnRachaListener) context;
        } else {
            throw new RuntimeException("La Activity debe implementar OnRachaListener");
        }

        if (context instanceof OnTirarListener) {
            tirarListener = (OnTirarListener) context;
        } else {
            throw new RuntimeException("La Activity debe implementar OnTirarListener");
        }
    }

    // ============================================================
    //    onCreateView() — Inflamos la vista y configuramos el botón
    // ============================================================
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Recuperamos argumentos enviados por newInstance()
        if (getArguments() != null) {
            idDado = getArguments().getInt(ARG_ID, 0);
            numCaras = getArguments().getInt(ARG_CARAS, 6);
        } else {
            idDado = 0;
            numCaras = 6;
        }

        View layout = inflater.inflate(R.layout.frg_dado_guia, container, false);

        // Nuestro botón-dado
        btnDado = layout.findViewById(R.id.btnDado);
        btnDado.setText("X");

        // Acción al pulsar el dado
        btnDado.setOnClickListener(v -> tirar());

        return layout;
    }

    // ============================================================
    //    MÉTODO tirar() — Lógica principal del dado
    // ============================================================
    private void tirar() {

        // Generamos resultado aleatorio entre 1 y numCaras
        int nuevo = rand.nextInt(numCaras) + 1;

        // Si hay racha (dos tiradas iguales)
        if (nuevo == ultimoResultado && ultimoResultado != -1 && rachaListener != null) {
            rachaListener.onRacha(this, idDado, nuevo);
        }

        ultimoResultado = nuevo;
        numTiradas++;

        // Mostramos el valor en el botón
        btnDado.setText(String.valueOf(nuevo));

        // Avisamos a la Activity
        if (tirarListener != null) {
            tirarListener.onTirar(this, idDado, nuevo, numTiradas);
        }
    }

    // ============================================================
    //    bloquear() — Lo llama la Activity cuando se alcanza el límite
    // ============================================================
    public void bloquear() {
        if (btnDado != null) {
            btnDado.setEnabled(false); // ya no se puede tirar más
        }
    }
}

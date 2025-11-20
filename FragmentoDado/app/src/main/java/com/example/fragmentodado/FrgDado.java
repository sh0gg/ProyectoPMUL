package com.example.fragmentodado;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Random;

public class FrgDado extends Fragment {

    private static final String ARG_CARAS = "numCaras";
    private static final String ARG_ID = "idDado";

    private int numCaras;
    private int idDado;
    private int ultimoResultado = -1;

    private Button btnDado;
    private final Random rand = new Random();

    private OnRachaListener rachaListener;

    public static FrgDado newInstance(int id, int caras) {
        FrgDado frg = new FrgDado();
        Bundle b = new Bundle();
        b.putInt(ARG_ID, id);
        b.putInt(ARG_CARAS, caras);
        frg.setArguments(b);
        return frg;
    }

    public interface OnRachaListener {
        void onRacha(int numDado, int resultado);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnRachaListener) {
            rachaListener = (OnRachaListener) context;
        } else {
            throw new RuntimeException("La Activity debe implementar OnRachaListener");
        }
    }

    @Override
    public View onCreateView(@NonNull android.view.LayoutInflater inflater,
                             @Nullable android.view.ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        numCaras = getArguments() != null ? getArguments().getInt(ARG_CARAS, 6) : 6;
        idDado = getArguments() != null ? getArguments().getInt(ARG_ID, 0) : 0;

        btnDado = new Button(getContext());
        btnDado.setText("X");
        btnDado.setTextSize(24);

        btnDado.setOnClickListener(v -> {
            int nuevo = rand.nextInt(numCaras) + 1;
            if (nuevo == ultimoResultado && ultimoResultado != -1 && rachaListener != null) {
                rachaListener.onRacha(idDado, nuevo);
            }
            ultimoResultado = nuevo;
            btnDado.setText(String.valueOf(nuevo));
        });

        return btnDado;
    }
}

package com.example.ejerciciocursores;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class FrgControles extends Fragment {

    public static final int TECLA_ARRIBA = 1;
    public static final int TECLA_ABAJO = 2;
    public static final int TECLA_IZQ = 3;
    public static final int TECLA_DER = 4;
    public static final int TECLA_ENTER = 5;

    public interface OnTeclaListener {
        void onTeclaPulsada(int tecla);
    }

    private OnTeclaListener escuchador;
    private Button bEnter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity act = (Activity) context;
        try {
            escuchador = (OnTeclaListener) act;
        } catch (ClassCastException e) {
            throw new ClassCastException(act.toString() + " debe implementar OnTeclaListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_controles, container, false);

        Button bArriba = v.findViewById(R.id.bArriba);
        Button bAbajo = v.findViewById(R.id.bAbajo);
        Button bIzq = v.findViewById(R.id.bIzquierda);
        Button bDer = v.findViewById(R.id.bDerecha);
        bEnter = v.findViewById(R.id.bEnter);

        View.OnClickListener ocl = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tecla = 0;
                int id = view.getId();
                if (id == R.id.bArriba) tecla = TECLA_ARRIBA;
                else if (id == R.id.bAbajo) tecla = TECLA_ABAJO;
                else if (id == R.id.bIzquierda) tecla = TECLA_IZQ;
                else if (id == R.id.bDerecha) tecla = TECLA_DER;
                else if (id == R.id.bEnter) tecla = TECLA_ENTER;

                if (escuchador != null && tecla != 0) {
                    escuchador.onTeclaPulsada(tecla);
                }
            }
        };

        bArriba.setOnClickListener(ocl);
        bAbajo.setOnClickListener(ocl);
        bIzq.setOnClickListener(ocl);
        bDer.setOnClickListener(ocl);
        bEnter.setOnClickListener(ocl);

        return v;
    }

    // cutrecito pero funciona para ocultar el ENTER en NEB
    public void setMostrarEnter(boolean mostrar) {
        if (bEnter != null) {
            bEnter.setVisibility(mostrar ? View.VISIBLE : View.INVISIBLE);
        }
    }
}

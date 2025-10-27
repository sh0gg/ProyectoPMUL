package com.example.ejemplofragmentos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FrgBotonLimitado extends Fragment {
    private int maxClicks,numClics;
    // interfaz para el escuchador del onclick del boton
    private OnFrgBLClickListener listener;

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }


    interface OnFrgBLClickListener {
        boolean onClickBotonLimitado();
    }

    private Button boton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflas el layout, a partir del contexto que es la activity en la que se encuentra
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.frg_boton_limitado, null);

        // guardamos la referencia del boton
        boton = layout.findViewById(R.id.boton);

        boton.setOnClickListener(view -> Clic());

        return layout;
    }

    // Llama al codigo que implemente de la interfaz
    private void Clic() {

        if(numClics<=maxClicks){
            if (listener.onClickBotonLimitado()){
                numClics++;
            } else {
                numClics--;
            }
        }
    }
    public void setText(String texto) {
        boton.setText(texto);
    }

    public void setOnFrgBotonLimitadoListener(OnFrgBLClickListener listener) {
        this.listener = listener;

    }
}

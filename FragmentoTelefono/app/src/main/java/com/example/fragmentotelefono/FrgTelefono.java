package com.example.fragmentotelefono;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class FrgTelefono extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflas el layout, a partir del contexto que es la activity en la que se encuentra
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_telefono, null);

        // guardamos la referencia del boton
        boton = layout.findViewById(R.id.boton);

        boton.setOnClickListener(view -> Clic());

        return layout;
    }
}

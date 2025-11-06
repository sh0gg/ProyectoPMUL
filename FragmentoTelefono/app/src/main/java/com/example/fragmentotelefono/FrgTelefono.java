package com.example.fragmentotelefono;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class FrgTelefono extends Fragment {

    TextView tvID;
    TextView etNumero;
    ImageView ivBoton;
    int idOrigen = 0;
    int idDestino = 0;
    int idActual = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // inflas el layout, a partir del contexto que es la activity en la que se encuentra
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_telefono, null);

        // guardamos los elementos del fragmento
        tvID = layout.findViewById(R.id.tvID);
        etNumero = layout.findViewById(R.id.etNumero);
        ivBoton = layout.findViewById(R.id.ivBoton);

        // asignamos sus listeners
        ivBoton.setOnClickListener(view -> hacerClick());

        return layout;
    }

    private void hacerClick() {
        if (idOrigen == 0 || idDestino == 0) { // es decir, si no hay llamada activa, empieza una
            idOrigen = Integer.parseInt(tvID.getText().toString());
            idDestino = Integer.parseInt(etNumero.getText().toString());
            if (idOrigen == idDestino) {
                Toast.makeText(getActivity(), "No se puede llamar a sí mismo", Toast.LENGTH_SHORT).show();
            } else {
                llamar(idOrigen, idDestino);
            }
        } else { // si hay una llamada activa, termina la actual
            idActual = Integer.parseInt(tvID.getText().toString());
            terminarLlamada(idActual);
        }
    }

    private void llamar(int idOrigen, int idDestino) {
        if (getActivity() instanceof MainActivity) {
            Centralita centralita = ((MainActivity) getActivity()).getCentralita();
            boolean llamadaExitosa = centralita.llamar(idOrigen, idDestino);

            if (llamadaExitosa) {
                ivBoton.setImageResource(android.R.drawable.sym_call_outgoing);
                tvID.setText(idOrigen + " > " + idDestino);
            } else {
                ivBoton.setImageResource(android.R.drawable.sym_call_missed);
                tvID.setText("XXX");
            }
        }
    }


    private void terminarLlamada(int idActual) {

        if (getActivity() instanceof MainActivity) {
            Centralita centralita = ((MainActivity) getActivity()).getCentralita();
            centralita.terminarLlamada(idActual);


            ivBoton.setImageResource(android.R.drawable.ic_menu_call);
            tvID.setText(idActual + " - Libre");
        }
    }
}

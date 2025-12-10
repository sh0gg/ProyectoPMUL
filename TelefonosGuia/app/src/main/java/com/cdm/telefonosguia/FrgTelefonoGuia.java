package com.cdm.telefonosguia;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FrgTelefonoGuia extends Fragment {

    private TelefonoGuia telefono;
    private OnFrgTelefonoGuia listener = null;

    private TextView tvNumeroPropio;
    private EditText etNumeroDestino;
    private Button bLlamarColgar;

    // ==========
    // INTERFAZ PARA LA ACTIVITY
    // ==========
    public interface OnFrgTelefonoGuia {
        boolean llamar(TelefonoGuia telefonoOrigen, int numTelefonoDestino);
        void colgar(TelefonoGuia telefonoOrigen, int numTelefonoDestino);
    }

    // ==========
    // ENLACE DESDE LA ACTIVITY
    // ==========
    public void setOnFrgTelefonoGuia(TelefonoGuia telefono, OnFrgTelefonoGuia listener) {
        this.telefono = telefono;
        this.listener = listener;
        actualizarUI();
    }

    // ==========
    // CICLO DE VIDA DEL FRAGMENT
    // ==========
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frg_telefono_guia, container, false);

        tvNumeroPropio = v.findViewById(R.id.tvNumeroPropio);
        etNumeroDestino = v.findViewById(R.id.etNumeroDestino);
        bLlamarColgar = v.findViewById(R.id.bLlamarColgar);

        bLlamarColgar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null && telefono != null) {
                    if (telefono.estoyHablando())
                        Colgar();
                    else
                        Llamar();
                }
            }
        });

        return v;
    }

    // ==========
    // LÓGICA DE LLAMAR / COLGAR
    // ==========
    private void Llamar() {
        int numeroDestino;
        try {
            numeroDestino = Integer.parseInt(etNumeroDestino.getText().toString());
        }
        catch (NumberFormatException x) {
            etNumeroDestino.setError(getString(R.string.num_telefono_incorrecto));
            return;
        }

        if (numeroDestino == telefono.getNumero()) {
            etNumeroDestino.setError(getString(R.string.num_telefono_incorrecto));
            return;
        }

        // Avisamos a la Activity: si devuelve true, empieza la llamada
        if (listener.llamar(telefono, numeroDestino)) {
            setEmpiezaLlamada(numeroDestino, true);
        }
    }

    private void Colgar() {
        setEstoyLibre();
        listener.colgar(telefono, telefono.getNumeroDestino());
    }

    // Llaman DESDE OTRO teléfono
    public boolean llaman(TelefonoGuia telefonoQueLlama) {
        if (telefono.estoyHablando()) return false;
        setEmpiezaLlamada(telefonoQueLlama.getNumero(), false);
        return true;
    }

    // El otro cuelga
    public void cuelgan() {
        if (telefono.estoyHablando()) setEstoyLibre();
    }

    private void setEmpiezaLlamada(int numeroDestino, boolean inicioYo) {
        telefono.setEmpiezaLlamada(numeroDestino, inicioYo);
        actualizarUI();
    }

    private void setEstoyLibre() {
        telefono.setEstoyLibre();
        actualizarUI();
    }

    private void actualizarUI() {
        if (tvNumeroPropio == null || etNumeroDestino == null || bLlamarColgar == null || telefono == null)
            return;

        tvNumeroPropio.setText(telefono.toString());
        etNumeroDestino.setText("");
        etNumeroDestino.setEnabled(!telefono.estoyHablando());

        // Iconos de llamada / colgar (igual estilo que el original)
        bLlamarColgar.setBackgroundResource(
                telefono.estoyHablando()
                        ? android.R.drawable.sym_call_incoming
                        : android.R.drawable.ic_menu_call
        );
    }
}

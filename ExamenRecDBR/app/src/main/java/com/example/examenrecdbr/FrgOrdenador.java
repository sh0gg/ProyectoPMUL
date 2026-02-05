package com.example.examenrecdbr;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class FrgOrdenador extends Fragment {

    private Ordenador ordenador;
    private onFrgOrdenadorEClick enviarListener;
    private onFrgOrdenadorOnOffClick onOffListener;
    private onFrgOrdenadorHClick hibernarListener;
    private TextView tvID;
    private TextView tvStatus;
    EditText etProcesador;
    private Button bEnviar;
    private Button bOnOff;
    private Button bHibernar;
    private String registroAux;

    public static FrgOrdenador newInstance(Ordenador ordenador) {
        FrgOrdenador frg = new FrgOrdenador();
        frg.setOrdenador(ordenador);
        return frg;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onFrgOrdenadorEClick) {
            enviarListener = (onFrgOrdenadorEClick) context;
        } else {
            throw new RuntimeException("Tienes que implementar onFrgOrdenadorEClick");
        }
        if (context instanceof onFrgOrdenadorOnOffClick) {
            onOffListener = (onFrgOrdenadorOnOffClick) context;
        } else {
            throw new RuntimeException("Tienes que implementar onFrgOrdenadorOnOffClick");
        }
        if (context instanceof onFrgOrdenadorHClick) {
            hibernarListener = (onFrgOrdenadorHClick) context;
        } else {
            throw new RuntimeException("Tienes que implementar onFrgOrdenadorHClick");
        }
    }

    public void setEnviarListener(Ordenador ordenador, onFrgOrdenadorEClick listener) {
        this.ordenador = ordenador;
        this.enviarListener = listener;
    }

    public void setOnOffListener(Ordenador ordenador, onFrgOrdenadorOnOffClick listener) {
        this.ordenador = ordenador;
        this.onOffListener = listener;
    }

    public void setHibernarListener(Ordenador ordenador, onFrgOrdenadorHClick listener) {
        this.ordenador = ordenador;
        this.hibernarListener = listener;
    }

    public void setEtProcesador(FrgOrdenador frg, String texto) {
        frg.etProcesador.setText(texto);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.frg_ordenador, container, false);

        tvID = v.findViewById(R.id.tvID);
        tvStatus = v.findViewById(R.id.tvStatus);
        etProcesador = v.findViewById(R.id.etProcesador);
        bEnviar = v.findViewById(R.id.bEnviar);
        bOnOff = v.findViewById(R.id.bOnOff);
        bHibernar = v.findViewById(R.id.bHibernar);

        int idOrdenador = ordenador != null ? ordenador.getID() : 0;
        tvID.setText(String.valueOf(idOrdenador));
        String status = ordenador != null ? ordenador.getStatus(): "null";
        tvStatus.setText(status);

        bEnviar.setOnClickListener(view -> enviar());
        bOnOff.setOnClickListener(view -> onOff());
        bHibernar.setOnClickListener(view -> hibernar());

        return v;
    }

    public void enviar() {
        if (enviarListener != null) {
            if (!etProcesador.getText().toString().isEmpty()) {
                enviarListener.onEnviar(this, etProcesador.getText().toString());
            }
        }
    }

    public void onOff() {
        if (onOffListener != null) {
            onOffListener.onOnOff(this);
        }
    }

    public void hibernar() {
        if (hibernarListener != null) {
            hibernarListener.onHibernar(this, etProcesador.getText().toString());
        }
    }

    public Ordenador getOrdenador() {
        return ordenador;
    }

    public String getRegistroAux() {
        return registroAux;
    }

    public void setRegistroAux(String registroAux) {
        this.registroAux = registroAux;
    }

    public void setOrdenador(Ordenador ordenador) {
        this.ordenador = ordenador;
    }

    public interface onFrgOrdenadorEClick {
        void onEnviar(FrgOrdenador frg, String texto);
    }

    public interface onFrgOrdenadorOnOffClick {
        // No hace falta pasar nada más, si está encendido pues lo apaga y viceversa
        void onOnOff(FrgOrdenador frg);
    }

    public interface onFrgOrdenadorHClick {
        void onHibernar(FrgOrdenador frg, String texto);
    }
}


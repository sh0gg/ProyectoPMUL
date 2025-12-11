package com.example.examendbr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FrgTelevision extends Fragment {
    private Televisor televisor;
    public interface onFrgTelevisionListener {
        void onItemSelected(FrgTelevision frg, int position, String texto);
    }
    private onFrgTelevisionListener listener;

    interface onFrgTelevisionBClick {
        void onSuscrip(FrgTelevision frg, Canal canal);
    }

    private onFrgTelevisionBClick suscripcionListener;
    private final List<Canal> canalesDisponibles = new ArrayList<>();
    private List<String> canalesDisponiblesStr = new ArrayList<>();

    private TextView tvIDTele;
    private Spinner spCanal;
    private TextView tvEstatus;
    private Button bSuscribirse;
    private TextView tvContrato;
    private ArrayAdapter<String> adapter;


    public void setOnFrgTele(Televisor televisor, onFrgTelevisionListener listener) {
        this.televisor = televisor;
        this.listener = listener;
    }

    public void setOnFrgTeleSus(Televisor televisor, onFrgTelevisionBClick suscripcionListener) {
        this.televisor = televisor;
        this.suscripcionListener = suscripcionListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        for (Canal c : canalesDisponibles) {
            canalesDisponiblesStr.add(c.getNombre());
        }

        View v = inflater.inflate(R.layout.frg_television,container,false);
        tvIDTele = v.findViewById(R.id.tvIDTele);
        spCanal = v.findViewById(R.id.spCanal);
        tvEstatus = v.findViewById(R.id.tvEstatus);
        bSuscribirse = v.findViewById(R.id.bSuscribirse);
        tvContrato = v.findViewById(R.id.tvContrato);
        bSuscribirse.setVisibility(View.INVISIBLE);
        tvContrato.setVisibility(View.INVISIBLE);

        tvIDTele.setText("Televisor" + ""); //falta añadir el id de cada televisor
        tvEstatus.setText("Todavia no estas viendo nada");
        bSuscribirse.setText("Suscribirse al canal");

        bSuscribirse.setOnClickListener(view -> suscribirse((Canal) spCanal.getSelectedItem()));
        tvContrato = v.findViewById(R.id.tvContrato);

        adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                canalesDisponiblesStr
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCanal.setAdapter(adapter);

        spCanal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (listener != null && position >= 0 && position < canalesDisponibles.size()) {
                    listener.onItemSelected(FrgTelevision.this, position, canalesDisponiblesStr.get(position));
                    tvContrato.setVisibility(View.VISIBLE);
                    bSuscribirse.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return v;
    }

    private void suscribirse(Canal nuevaSuscripcion) {
        suscripcionListener.onSuscrip(this,nuevaSuscripcion);
    }

    public void setDatos(List<String> datos) {
        canalesDisponiblesStr.clear();
        canalesDisponiblesStr.addAll(datos);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    public void setSeleccion(int pos) {
        if (spCanal != null && pos >= 0 && pos < canalesDisponiblesStr.size()) {
            spCanal.setSelection(pos);
        }
    }

    public Televisor getTelevisor() {
        return televisor;
    }

    public onFrgTelevisionListener getListener() {
        return listener;
    }

    public onFrgTelevisionBClick getSuscripcionListener() {
        return suscripcionListener;
    }

    public List<Canal> getCanalesDisponibles() {
        return canalesDisponibles;
    }

    public List<String> getCanalesDisponiblesStr() {
        return canalesDisponiblesStr;
    }

    public TextView getTvIDTele() {
        return tvIDTele;
    }

    public Spinner getSpCanal() {
        return spCanal;
    }

    public TextView getTvEstatus() {
        return tvEstatus;
    }

    public Button getbSuscribirse() {
        return bSuscribirse;
    }

    public TextView getTvContrato() {
        return tvContrato;
    }

    public ArrayAdapter<String> getAdapter() {
        return adapter;
    }
}

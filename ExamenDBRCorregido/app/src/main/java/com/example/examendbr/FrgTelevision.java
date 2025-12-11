package com.example.examendbr;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

public class FrgTelevision extends Fragment {

    public interface onFrgTelevisionListener {
        void onItemSelected(FrgTelevision frg, int position, String texto);
    }

    public interface onFrgTelevisionBClick {
        void onSuscrip(FrgTelevision frg, Canal canal);
    }

    private Televisor televisor;
    private onFrgTelevisionListener listener;
    private onFrgTelevisionBClick suscripcionListener;

    private final List<Canal> canalesDisponibles = new ArrayList<>();
    private final List<String> canalesDisponiblesStr = new ArrayList<>();

    private ImageView imgTelevision;
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

    public void setCanales(List<Canal> canales) {
        canalesDisponibles.clear();
        canalesDisponibles.addAll(canales);
        canalesDisponiblesStr.clear();
        for (Canal c : canalesDisponibles) {
            canalesDisponiblesStr.add(c.getNombre());
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frg_television, container, false);

        imgTelevision = v.findViewById(R.id.imgTelevision);
        tvIDTele = v.findViewById(R.id.tvIDTele);
        spCanal = v.findViewById(R.id.spCanal);
        tvEstatus = v.findViewById(R.id.tvEstatus);
        bSuscribirse = v.findViewById(R.id.bSuscribirse);
        tvContrato = v.findViewById(R.id.tvContrato);

        int idTele = (televisor != null) ? televisor.getId() : 0;
        tvIDTele.setText("Televisor " + idTele);
        tvEstatus.setText("Todavía no estás viendo nada");
        bSuscribirse.setText("Suscribirse al canal");
        bSuscribirse.setVisibility(View.INVISIBLE);
        tvContrato.setVisibility(View.INVISIBLE);

        bSuscribirse.setOnClickListener(view -> suscribirse());

        adapter = new ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                canalesDisponiblesStr
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View fila = super.getView(position, convertView, parent);
                pintarCanal(position, fila);
                return fila;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View fila = super.getDropDownView(position, convertView, parent);
                pintarCanal(position, fila);
                return fila;
            }

            private void pintarCanal(int position, View fila) {
                TextView tv = fila.findViewById(android.R.id.text1);
                if (position >= 0 && position < canalesDisponibles.size() && televisor != null) {
                    Canal c = canalesDisponibles.get(position);
                    int color;
                    if ("PUBLICO".equals(c.getDisponibilidad())) {
                        color = Color.GREEN;
                    } else if (televisor.getBar().getSuscritos().contains(c)) {
                        color = Color.YELLOW;
                    } else {
                        color = Color.RED;
                    }
                    tv.setTextColor(color);
                }
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCanal.setAdapter(adapter);

        spCanal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (listener != null && position >= 0 && position < canalesDisponibles.size()) {
                    listener.onItemSelected(FrgTelevision.this, position, canalesDisponiblesStr.get(position));
                    tvContrato.setVisibility(View.VISIBLE);

                    Canal canal = canalesDisponibles.get(position);
                    if ("PAGO".equals(canal.getDisponibilidad())) {
                        bSuscribirse.setVisibility(View.VISIBLE);
                    } else {
                        bSuscribirse.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return v;
    }

    private void suscribirse() {
        if (suscripcionListener != null && spCanal != null) {
            int pos = spCanal.getSelectedItemPosition();
            if (pos >= 0 && pos < canalesDisponibles.size()) {
                Canal canal = canalesDisponibles.get(pos);
                suscripcionListener.onSuscrip(this, canal);
            }
        }
    }

    public void actualizarDesdeTelevisor() {
        if (televisor != null) {
            tvEstatus.setText(televisor.getEstatus());
            tvContrato.setText(televisor.getDetalles());
        }
    }

    public Televisor getTelevisor() {
        return televisor;
    }

    public List<Canal> getCanalesDisponibles() {
        return canalesDisponibles;
    }

    public ImageView getImgTelevision() {
        return imgTelevision;
    }

    public Button getbSuscribirse() {
        return bSuscribirse;
    }

    public TextView getTvContrato() {
        return tvContrato;
    }
}

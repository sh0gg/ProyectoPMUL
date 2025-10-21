package com.example.eleccionesconlista.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eleccionesconlista.R;
import com.example.eleccionesconlista.db.ConexionBD;
import com.example.eleccionesconlista.db.Utiles;
import com.example.eleccionesconlista.modelo.Candidato;
import com.example.eleccionesconlista.modelo.Partido;

import java.util.ArrayList;
import java.util.List;

public class CandidatoAdapter extends ArrayAdapter<Candidato> {

// Llamamos a conexionBD para tratar con los datos de los candidatos

    private ConexionBD conexionBD;
    private List<Candidato> selectedCandidatos = new ArrayList<>();

    public CandidatoAdapter(Context context, List<Candidato> candidatos, ConexionBD conexionBD) {
        super(context, 0, candidatos);
        this.conexionBD = conexionBD;
    }

// Los datos que metemos en la vista y en caso de que algo falte pasa lo siguiente:

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Candidato candidato = getItem(position);
        // la primera vez lo hace vacio
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_candidato, parent, false);
        }

        ImageView imgCandidato = convertView.findViewById(R.id.imgCandidato);
        TextView txtNombre = convertView.findViewById(R.id.txtNombre);
        ImageView imgLogoPartido = convertView.findViewById(R.id.imgLogoPartido);
        TextView txtPartido = convertView.findViewById(R.id.txtPartido);
        CheckBox chkSeleccion = convertView.findViewById(R.id.chkSeleccion);


        // empieza a llenar el adaptador con sus datos
        if (candidato != null) {
            txtNombre.setText(candidato.getNombre() + " " + candidato.getApellidos());

            Partido partido = conexionBD.getPartido(candidato.getCodPartido());

            // en caso de no encontrar partido, pone el color rojo para el nombre y coge una imagen de placeholder
            if (partido != null) {
                txtPartido.setText(partido.getNombre());
                txtPartido.setTextColor(Color.parseColor("#" + partido.getColor()));

                int logoId = Utiles.getDrawableIdByName(getContext(), partido.getLogo());
                if (logoId != 0) {
                    imgLogoPartido.setImageResource(logoId);
                } else {
                    imgLogoPartido.setImageResource(R.drawable.ic_partido);
                }
            } else {
                txtPartido.setText("Partido no encontrado");
                txtPartido.setTextColor(Color.RED);
                imgLogoPartido.setImageResource(R.drawable.ic_partido);
            }

            // lo mismo si no encuentra la foto del candidato, le pone una de placeholder
            int fotoId = Utiles.getDrawableIdByName(getContext(), "cc_" + candidato.getFoto());
            if (fotoId != 0) {
                imgCandidato.setImageResource(fotoId);
            } else {
                imgCandidato.setImageResource(R.drawable.ic_person);
            }
        }

        // para seleccionar a quien votamos decidí usar checks y a continuación bloqueamos que se
        // seleccionen mas de tres antes de enviar el voto

        chkSeleccion.setOnCheckedChangeListener(null);
        chkSeleccion.setChecked(selectedCandidatos.contains(candidato));

        chkSeleccion.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (selectedCandidatos.size() < 3) {
                    selectedCandidatos.add(candidato);
                } else {
                    buttonView.setChecked(false);
                    Toast.makeText(getContext(), "Solo puedes seleccionar 3 candidatos", Toast.LENGTH_SHORT).show();
                }
            } else {
                selectedCandidatos.remove(candidato);
            }
        });

        return convertView;
    }

    // este metodo devuelve en una lista los candidatos seleccionados en la lista
    public List<Candidato> getSelectedCandidatos() {
        return selectedCandidatos;
    }
}

package com.example.edittextcni;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import java.util.List;



public class FrgEditTextCNI extends Fragment {
    private EditText etCNI;
    private List<String> listaPalabrasMalas;
    public onFragEtCNI listener = null;
    public interface onFragEtCNI {
        boolean onTextoEncontrado(String texto);
    }

    public void setOnFrgEtCNI(onFragEtCNI listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // inflas el layout, a partir del contexto que es la activity en la que se encuentra
        View layout = LayoutInflater.from(getActivity()).inflate(R.layout.frg_etcni, null);

        etCNI = layout.findViewById(R.id.etCNI);
        return layout;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        etCNI.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });
    }


}

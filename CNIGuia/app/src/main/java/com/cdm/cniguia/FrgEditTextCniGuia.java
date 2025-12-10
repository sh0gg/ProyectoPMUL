package com.cdm.cniguia;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

/**
 * Fragmento CNI genérico:
 *  - Encapsula un EditText.
 *  - Tiene una lista de "reglas" (patrón + nivel + avisado).
 *  - Cada vez que cambia el texto, busca coincidencias.
 *  - AVISA a la Activity solo la primera vez que una regla se cumple.
 */
public class FrgEditTextCniGuia extends Fragment {

    // ====== ENUM NIVEL DE AVISO ======
    public enum NivelAviso { VERDE, AMARILLO, ROJO }

    // ====== INTERFAZ PARA AVISAR A LA ACTIVITY ======
    public interface OnTokenDetectadoListener {
        /**
         * @param frg           fragmento que avisa (Para/Asunto/Cuerpo)
         * @param campo         nombre lógico ("Para", "Asunto" o "Cuerpo")
         * @param patronRegla   patrón que ha coincidido (palabra o regex)
         * @param textoCompleto texto completo del EditText en ese momento
         * @param nivel         nivel de gravedad
         */
        void onTokenDetectado(FrgEditTextCniGuia frg,
                              String campo,
                              String patronRegla,
                              String textoCompleto,
                              NivelAviso nivel);
    }

    private OnTokenDetectadoListener listener;

    private EditText et;
    private String nombreCampo = "";  // Para / Asunto / Cuerpo

    // ====== CLASE INTERNA REGLA ======
    private static class Regla {
        String patron;       // palabra o regex
        NivelAviso nivel;
        boolean avisado;     // true cuando ya hemos avisado una vez

        Regla(String patron, NivelAviso nivel) {
            this.patron = patron;
            this.nivel = nivel;
            this.avisado = false;
        }
    }

    private final ArrayList<Regla> reglas = new ArrayList<>();

    // =====================================================
    //  CONFIGURACIÓN DESDE LA ACTIVITY
    // =====================================================

    /**
     * Configura el fragmento CNI.
     *
     * @param nombreCampo nombre lógico (se usará en el aviso)
     * @param patrones    array de palabras/regex
     * @param niveles     array con el mismo tamaño que patrones
     */
    public void configurar(String nombreCampo,
                           String[] patrones,
                           NivelAviso[] niveles) {

        this.nombreCampo = nombreCampo;
        reglas.clear();

        for (int i = 0; i < patrones.length; i++) {
            reglas.add(new Regla(patrones[i], niveles[i]));
        }

        // Si ya tenemos el EditText inflado, actualizamos el hint
        if (et != null) {
            et.setHint(nombreCampo);
        }
    }

    // =====================================================
    //  CICLO DE VIDA DEL FRAGMENTO
    // =====================================================

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // La Activity que contiene el fragmento debe implementar la interfaz
        if (!(context instanceof OnTokenDetectadoListener)) {
            throw new RuntimeException("La Activity debe implementar OnTokenDetectadoListener");
        }
        listener = (OnTokenDetectadoListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.frg_edittext_cni_guia, container, false);
        et = v.findViewById(R.id.etCni);

        if (!nombreCampo.isEmpty()) {
            et.setHint(nombreCampo);
        }

        // TextWatcher: cada vez que cambia el texto, comprobamos las reglas
        et.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                detectarTokens(s.toString());
            }
        });

        return v;
    }

    // =====================================================
    //  LÓGICA DE DETECCIÓN
    // =====================================================

    private void detectarTokens(String texto) {
        if (reglas.isEmpty() || listener == null) return;

        for (Regla r : reglas) {

            // si ya avisamos una vez por esa regla, no volvemos a hacerlo
            if (r.avisado) continue;

            // (?i) → case-insensitive | .*patron.* → en cualquier parte del texto
            if (texto.matches("(?i).*" + r.patron + ".*")) {
                r.avisado = true;

                listener.onTokenDetectado(
                        this,
                        nombreCampo,
                        r.patron,
                        texto,
                        r.nivel
                );
            }
        }
    }

    // Método opcional por si quieres "volver a escuchar" en el futuro
    public void resetAvisos() {
        for (Regla r : reglas) r.avisado = false;
    }
}

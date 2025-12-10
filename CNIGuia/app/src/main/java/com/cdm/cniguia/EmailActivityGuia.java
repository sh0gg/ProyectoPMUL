package com.cdm.cniguia;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

/**
 * Activity principal "simulador de correo":
 *
 * 3 campos (fragmentos):
 *   - Para   → detectar correos a @ot.com (ROJO)      (+ lleva contador)
 *   - Asunto → detectar "ascensor" (AMARILLO) y "fuego" (ROJO)
 *   - Cuerpo → detectar IBAN y NIF (ROJO / AMARILLO)
 *
 * Cuando se detecta un token, se abre ActivityAlertaGuia
 * mostrando la información.
 */
public class EmailActivityGuia extends AppCompatActivity
        implements FrgEditTextCniGuia.OnTokenDetectadoListener {

    FrgEditTextCniGuia frgPara, frgAsunto, frgCuerpo;
    int contadorOT = 0;  // nº de veces que aparece un mail a @ot.com en "Para"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_guia);

        FragmentManager fm = getSupportFragmentManager();

        // Obtenemos los 3 fragmentos estáticos declarados en el XML
        frgPara   = (FrgEditTextCniGuia) fm.findFragmentById(R.id.frgPara);
        frgAsunto = (FrgEditTextCniGuia) fm.findFragmentById(R.id.frgAsunto);
        frgCuerpo = (FrgEditTextCniGuia) fm.findFragmentById(R.id.frgCuerpo);

        // ==================================================
        //   CONFIGURACIÓN DE REGLAS PARA CADA FRAGMENTO
        // ==================================================

        // -------- PARA → correos a @ot.com (muy peligroso = ROJO)
        frgPara.configurar(
                "Para",
                new String[]{"@ot\\.com"},
                new FrgEditTextCniGuia.NivelAviso[]{
                        FrgEditTextCniGuia.NivelAviso.ROJO
                }
        );

        // -------- ASUNTO → "ascensor" (AMARILLO) y "fuego" (ROJO)
        frgAsunto.configurar(
                "Asunto",
                new String[]{"ascensor", "fuego"},
                new FrgEditTextCniGuia.NivelAviso[]{
                        FrgEditTextCniGuia.NivelAviso.AMARILLO,
                        FrgEditTextCniGuia.NivelAviso.ROJO
                }
        );

        // -------- CUERPO → IBAN + NIF (regex)
        frgCuerpo.configurar(
                "Cuerpo",
                new String[]{
                        "ES\\d{2}-?\\d{4}-?\\d{4}-?\\d{4}-?\\d{4}-?\\d{4}", // IBAN sencillo
                        "\\d{8}[A-Za-z]"                                   // NIF sencillo
                },
                new FrgEditTextCniGuia.NivelAviso[]{
                        FrgEditTextCniGuia.NivelAviso.ROJO,
                        FrgEditTextCniGuia.NivelAviso.AMARILLO
                }
        );
    }

    // ==================================================
    //   CALLBACK: algún fragmento ha detectado un token
    // ==================================================
    @Override
    public void onTokenDetectado(FrgEditTextCniGuia frg,
                                 String campo,
                                 String patron,
                                 String textoCompleto,
                                 FrgEditTextCniGuia.NivelAviso nivel) {

        // Si viene del campo "Para" y es @ot.com, incrementamos contador
        if ("Para".equals(campo) && "@ot\\.com".equals(patron)) {
            contadorOT++;
        }

        // Lanzamos ActivityAlertaGuia pasando toda la info por Intent
        Intent intent = new Intent(this, ActivityAlertaGuia.class);
        intent.putExtra("campo", campo);
        intent.putExtra("patron", patron);
        intent.putExtra("texto", textoCompleto);
        intent.putExtra("nivel", nivel.name());
        intent.putExtra("contadorOT", contadorOT);
        startActivity(intent);
    }
}

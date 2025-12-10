package com.cdm.dadoguia;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.gridlayout.widget.GridLayout;

public class JuegoActivityGuia extends AppCompatActivity
        // Implementamos los listeners del fragmento (dos callbacks)
        implements FrgDadoGuia.OnRachaListener, FrgDadoGuia.OnTirarListener {

    // GridLayout donde colocamos los fragmentos dinámicamente
    private GridLayout gridDados;

    // Necesario para manejar fragmentos dinámicos
    private FragmentManager fm;

    // Arrays paralelos para llevar control de tiradas y referencias a los fragmentos
    private int[] tiradasPorDado;      // nº tiradas por cada dado
    private FrgDadoGuia[] listaDados;  // referencias para bloquearlos

    private int numMaxTiradas;                  // límite de tiradas por dado
    private static final int MAX_TIRADAS_DEFECTO = 3;

    // --- HISTORIAL ---
    private TextView tvHistorial;               // TextView donde mostramos el historial
    private StringBuilder sbHistorial = new StringBuilder(); // acumulamos mensajes aquí

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_juego_guia);

        // Obtenemos referencias de UI
        gridDados = findViewById(R.id.gridDados);
        tvHistorial = findViewById(R.id.tvHistorial);

        fm = getSupportFragmentManager();

        // Recibimos los parámetros enviados desde MainActivityGuia
        int numDados     = getIntent().getIntExtra("numDados", 1);
        int numCaras     = getIntent().getIntExtra("numCarasDados", 6);
        numMaxTiradas    = getIntent().getIntExtra("numMaxTiradas", MAX_TIRADAS_DEFECTO);

        // Mensaje inicial al historial
        addLineaHistorial("Iniciando partida con " +
                numDados + " dados, " +
                numCaras + " caras y máximo " +
                numMaxTiradas + " tiradas.");

        // Reservamos arrays según los dados pedidos
        tiradasPorDado = new int[numDados];
        listaDados     = new FrgDadoGuia[numDados];

        // Decidimos cuántas columnas tendrá el GridLayout
        // Máximo 3 columnas para que no quede demasiado apretado
        int columnas = Math.min(3, numDados);
        if (columnas < 1) columnas = 1;
        gridDados.setColumnCount(columnas);

        // Transacción para añadir todos los fragmentos dinámicamente
        FragmentTransaction ft = fm.beginTransaction();

        for (int i = 0; i < numDados; i++) {

            // Creamos el fragmento con newInstance (patrón recomendado)
            FrgDadoGuia frgDado = FrgDadoGuia.newInstance(i, numCaras);
            listaDados[i] = frgDado;   // guardamos referencia

            // Creamos un contenedor dinámico para cada fragmento
            FragmentContainerView fcv = new FragmentContainerView(this);
            fcv.setId(View.generateViewId());    // ID único generado en tiempo de ejecución

            // Añadirlo visualmente al GridLayout
            gridDados.addView(fcv);

            // Añadir el fragmento dentro del contenedor dinámico
            ft.add(fcv.getId(), frgDado);

            addLineaHistorial("Creado Dado " + (i + 1));
        }

        // Commit obligatorio para que aparezcan en pantalla
        ft.commit();
    }

    // ============================================================
    //    CALLBACKS RECIBIDOS DESDE CADA FRAGMENTO (EVENTOS)
    // ============================================================

    @Override
    public void onRacha(FrgDadoGuia frg, int numDado, int resultado) {
        // Mensaje de racha (dos tiradas iguales seguidas)
        String msg = "¡¡Racha!! • Dado " + (numDado + 1) +
                " volvió a sacar " + resultado;

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        addLineaHistorial(msg);
    }

    @Override
    public void onTirar(FrgDadoGuia frg, int numDado, int resultado, int numTirada) {
        // Guardamos nº tirada (útil para saber cuándo bloquearlo)
        tiradasPorDado[numDado] = numTirada;

        addLineaHistorial("Dado " + (numDado + 1) +
                " → Tirada " + numTirada +
                " → Resultado: " + resultado);

        // Si se alcanzó el límite, bloqueamos el botón del fragmento
        if (numTirada >= numMaxTiradas) {
            frg.bloquear();
            addLineaHistorial("Dado " + (numDado + 1) + " BLOQUEADO (sin tiradas restantes)");
        }
    }

    // ============================================================
    //    MÉTODO PARA ESCRIBIR EN EL HISTORIAL
    // ============================================================
    private void addLineaHistorial(String linea) {
        sbHistorial.append(linea).append("\n");
        tvHistorial.setText(sbHistorial.toString());
    }
}

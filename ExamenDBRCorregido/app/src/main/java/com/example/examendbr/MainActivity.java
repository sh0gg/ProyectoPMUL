package com.example.examendbr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements FrgTelevision.onFrgTelevisionListener,
        FrgTelevision.onFrgTelevisionBClick {

    private static final int REQ_SUSCRIPCION = 1;

    private Bar bar;
    private Televisor tele1, tele2, tele3;
    private TextView tvBar;
    private ListView lvCanalesPago;
    private Button bReset;

    private AsistenteBD asistenteBD;
    private List<Canal> listaCanales;

    // para recordar qué TV pidió la suscripción
    private FrgTelevision frgPendiente;
    private Canal canalPendiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvBar = findViewById(R.id.tvBar);
        lvCanalesPago = findViewById(R.id.lvCanalesPago);
        bReset = findViewById(R.id.bReset);

        asistenteBD = new AsistenteBD(this);
        asistenteBD.getWritableDatabase().close();

        // usamos la lista creada en AsistenteBD (para el examen es suficiente)
        listaCanales = AsistenteBD.getListaCanales();

        List<Canal> canalesSuscritos = new ArrayList<>();
        List<Integer> numSuscripciones = new ArrayList<>();

        for (Canal c : listaCanales) {
            if ("MotoGP".equals(c.getNombre())) {
                canalesSuscritos.add(c);
                numSuscripciones.add(2);
            } else if ("F1".equals(c.getNombre())) {
                canalesSuscritos.add(c);
                numSuscripciones.add(1);
            } else if ("Caza y Pesca".equals(c.getNombre())) {
                canalesSuscritos.add(c);
                numSuscripciones.add(3);
            }
        }

        String nombreDelBar = asistenteBD.getNombreBar(1);
        bar = new Bar(nombreDelBar, 3, canalesSuscritos, numSuscripciones);
        tvBar.setText(bar.getNombre());

        tele1 = new Televisor(bar, 1);
        tele2 = new Televisor(bar, 2);
        tele3 = new Televisor(bar, 3);

        FragmentManager fm = getSupportFragmentManager();

        FrgTelevision frg1 = (FrgTelevision) fm.findFragmentById(R.id.frgTele1);
        FrgTelevision frg2 = (FrgTelevision) fm.findFragmentById(R.id.frgTele2);
        FrgTelevision frg3 = (FrgTelevision) fm.findFragmentById(R.id.frgTele3);

        if (frg1 != null) {
            frg1.setOnFrgTele(tele1, this);
            frg1.setOnFrgTeleSus(tele1, this);
            frg1.setCanales(listaCanales);
            frg1.actualizarDesdeTelevisor();
        }
        if (frg2 != null) {
            frg2.setOnFrgTele(tele2, this);
            frg2.setOnFrgTeleSus(tele2, this);
            frg2.setCanales(listaCanales);
            frg2.actualizarDesdeTelevisor();
        }
        if (frg3 != null) {
            frg3.setOnFrgTele(tele3, this);
            frg3.setOnFrgTeleSus(tele3, this);
            frg3.setCanales(listaCanales);
            frg3.actualizarDesdeTelevisor();
        }

        actualizarListaCanalesPago();

        bReset.setOnClickListener(v -> resetApp());
    }

    private void actualizarListaCanalesPago() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < bar.getSuscritos().size(); i++) {
            Canal c = bar.getSuscritos().get(i);
            int numPant = bar.getSuscripciones().get(i);
            int precio = c.getCuota() + c.getPrecioPorPantalla() * numPant;
            lineas.add(c.getNombre() + " - " + numPant + " pantallas - " + precio + "€");
        }
        ArrayAdapter<String> ad = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, lineas);
        lvCanalesPago.setAdapter(ad);
    }

    private void resetApp() {
        bar.resetEnUso();
        tele1.reset();
        tele2.reset();
        tele3.reset();

        FragmentManager fm = getSupportFragmentManager();
        FrgTelevision frg1 = (FrgTelevision) fm.findFragmentById(R.id.frgTele1);
        FrgTelevision frg2 = (FrgTelevision) fm.findFragmentById(R.id.frgTele2);
        FrgTelevision frg3 = (FrgTelevision) fm.findFragmentById(R.id.frgTele3);

        if (frg1 != null) {
            frg1.actualizarDesdeTelevisor();
            frg1.getImgTelevision().setImageResource(R.drawable.error);
        }
        if (frg2 != null) {
            frg2.actualizarDesdeTelevisor();
            frg2.getImgTelevision().setImageResource(R.drawable.error);
        }
        if (frg3 != null) {
            frg3.actualizarDesdeTelevisor();
            frg3.getImgTelevision().setImageResource(R.drawable.error);
        }

        actualizarListaCanalesPago();
    }

    @Override
    public void onItemSelected(FrgTelevision frg, int position, String texto) {
        List<Canal> canalesDisp = frg.getCanalesDisponibles();
        if (position >= 0 && position < canalesDisp.size()) {
            Canal nuevoCanal = canalesDisp.get(position);
            frg.getTelevisor().cambiarCanal(nuevoCanal);
            frg.actualizarDesdeTelevisor();

            int idImg = AsistenteBD.getDrawableIdByName(this, nuevoCanal.getImagen());
            if (idImg == 0) {
                idImg = R.drawable.error;
            }
            frg.getImgTelevision().setImageResource(idImg);
        }
    }

    @Override
    public void onSuscrip(FrgTelevision frg, Canal canal) {
        // Si NO estoy suscrito, abro Activity de Suscripción
        if (!bar.getSuscritos().contains(canal)) {
            frgPendiente = frg;
            canalPendiente = canal;

            Intent i = new Intent(this, SuscripcionActivity.class);
            i.putExtra("NOMBRE_CANAL", canal.getNombre());
            i.putExtra("CUOTA", canal.getCuota());
            i.putExtra("PRECIO", canal.getPrecioPorPantalla());
            startActivityForResult(i, REQ_SUSCRIPCION);
        } else {
            // Ya estoy suscrito: ampliamos una pantalla más (opcional del enunciado)
            bar.ampliarSuscripcion(canal);
            frg.getTelevisor().cambiarCanal(canal);
            frg.actualizarDesdeTelevisor();
            actualizarListaCanalesPago();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_SUSCRIPCION && resultCode == RESULT_OK && data != null) {
            String nombre = data.getStringExtra("NOMBRE_CANAL");
            int numPantallas = data.getIntExtra("NUM_PANTALLAS", 1);

            Canal canal = null;
            for (Canal c : listaCanales) {
                if (c.getNombre().equals(nombre)) {
                    canal = c;
                    break;
                }
            }

            if (canal != null) {
                bar.addSuscripcion(canal, numPantallas);

                if (frgPendiente != null && frgPendiente.getTelevisor() != null) {
                    frgPendiente.getTelevisor().cambiarCanal(canal);
                    frgPendiente.actualizarDesdeTelevisor();

                    int idImg = AsistenteBD.getDrawableIdByName(this, canal.getImagen());
                    if (idImg == 0) idImg = R.drawable.error;
                    frgPendiente.getImgTelevision().setImageResource(idImg);
                }

                actualizarListaCanalesPago();
            }
        }

        frgPendiente = null;
        canalPendiente = null;
    }
}

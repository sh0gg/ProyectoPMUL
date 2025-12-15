package com.example.examendbr;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FrgTelevision.onFrgTelevisionListener, FrgTelevision.onFrgTelevisionBClick {

    Bar bar;

    AsistenteBD aBD;

    TextView tvBar;
    FrgTelevision frgTele1;
    FrgTelevision frgTele2;
    FrgTelevision frgTele3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        aBD = new AsistenteBD(this);
        SQLiteDatabase db = aBD.getReadableDatabase();

        Cursor c = db.rawQuery("SELECT nombre, numTeles FROM bar", null);
        int colIndexNombre = c.getColumnIndex("nombre");
        int colIndexNumTeles = c.getColumnIndex("numTeles");
        String nombre = c.getString(colIndexNombre);
        int numTeles = c.getInt(colIndexNumTeles);
        c.close();

        bar = new Bar(nombre, numTeles);

        c = db.rawQuery("SELECT id, nombre, disponibilidad, imagen, cuota, precioPorPantalla FROM canales", null);
        int colIndexId = c.getColumnIndex("id");
        int colIndexNombre = c.getColumnIndex("nombre");
        int colIndex
        int colIndex
        int colIndex
        int colIndex


        c = db.rawQuery("SELECT nombre, numTeles FROM barCanales", null);
        int colIndexNombre = c.getColumnIndex("nombre");
        int colIndexNumTeles = c.getColumnIndex("numTeles");
        String nombre = c.getString(colIndexNombre);
        int numTeles = c.getInt(colIndexNumTeles);
        c.close();

        bar.addSuscripcion(, );

        tvBar = findViewById(R.id.tvBar);
        String str = AsistenteBD.getNombreBar(1);
        tvBar.setText(str);

        FragmentManager fm = getSupportFragmentManager();
        frgTele1 = (FrgTelevision) fm.findFragmentById(R.id.frgTele1);
        frgTele2 = (FrgTelevision) fm.findFragmentById(R.id.frgTele2);
        frgTele3 = (FrgTelevision) fm.findFragmentById(R.id.frgTele3);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onItemSelected(FrgTelevision frg, int position, String texto) {
        List<Canal> canalesDisp = frg.getCanalesDisponibles();
        Canal nuevoCanal = canalesDisp.get(position);
        frg.getTelevisor().cambiarCanal(nuevoCanal);
    }

    @Override
    public void onSuscrip(FrgTelevision frg, Canal canal) {

    }
}
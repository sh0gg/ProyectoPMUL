package com.example.examendbr;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements FrgTelevision.onFrgTelevisionListener,FrgTelevision.onFrgTelevisionBClick {

    TextView tvBar;
    FrgTelevision frgTele1;
    FrgTelevision frgTele2;
    FrgTelevision frgTele3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

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
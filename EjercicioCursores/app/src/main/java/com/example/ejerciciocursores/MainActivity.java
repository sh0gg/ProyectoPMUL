package com.example.ejerciciocursores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button bIrErc, bIrNeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bIrErc = findViewById(R.id.bIrErc);
        bIrNeb = findViewById(R.id.bIrNeb);

        bIrErc.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ErcActivity.class);
            startActivity(i);
        });

        bIrNeb.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, NebActivity.class);
            startActivity(i);
        });
    }
}

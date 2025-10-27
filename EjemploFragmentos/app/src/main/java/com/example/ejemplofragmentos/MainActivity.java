package com.example.ejemplofragmentos;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends AppCompatActivity {

    private static final int MAX_INTENTOS_LOGIN = 3;
    TextView tvLogin;
    EditText etUsuario;
    EditText etContra;
    FrgBotonLimitado bEntrar;

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

        tvLogin = findViewById(R.id.tvLogin);
        etUsuario = findViewById(R.id.etUsuario);
        etContra = findViewById(R.id.etContra);
        //necesitas el FGM para poder inicializarlo
        FragmentManager fgm = getSupportFragmentManager();
        // y tienes que castearlo a la clase correspondiente
        bEntrar = (FrgBotonLimitado) fgm.findFragmentById(R.id.bEntrar);
        // seteas el maximo de clicks
        bEntrar.setMaxClicks(MAX_INTENTOS_LOGIN);
        bEntrar.setOnFrgBotonLimitadoListener(new FrgBotonLimitado.OnFrgBLClickListener() {
            @Override
            public boolean onClickBotonLimitado() {
                return Entrar();
            }
        });


    }

    private boolean Entrar() {
        String usuario = etUsuario.getText().toString().trim();
        String password = etContra.getText().toString().trim();
        if (usuario.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (usuario.equals("admin") && password.equals("admin")) {
                Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
    }
}
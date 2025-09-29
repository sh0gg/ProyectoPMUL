package com.example.eva;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

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

        EditText etNombre = findViewById(R.id.etNombre);
        TextView tvMensaje = findViewById(R.id.tvMensaje);

        //animacion
        Animation floatAnimation = AnimationUtils.loadAnimation(this, R.anim.float_animation);
        tvMensaje.startAnimation(floatAnimation);

        Button bClick = findViewById(R.id.bClick);
        etNombre.setHint("Escribe tu nombre");

        // CON ESTO HACES QUE EL ENTER FUNCIONE COMO EL BOTON!!
        etNombre.setOnEditorActionListener((textView, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)) {

                bClick.performClick();
                return true;
            }
            return false;
        });


        bClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etNombre.getText().toString();

                // vamos a poner bien el nombre.
                if (name != null && name.length() > 0) {
                    name = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase();
                }

                if (!name.isEmpty()) {
                    String coloredName = "<font color='#ff8080'>" + name + "</font>";
                    tvMensaje.setText(HtmlCompat.fromHtml("Hola " + coloredName, HtmlCompat.FROM_HTML_MODE_LEGACY));
                } else {
                    tvMensaje.setText("Hola, desconocido...");
                }
            }
        });

    }

}
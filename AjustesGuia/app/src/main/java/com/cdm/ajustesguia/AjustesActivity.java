package com.cdm.ajustesguia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Activity de AJUSTES.
 *
 * - Muestra los valores actuales del Singleton Ajustes.
 * - Permite modificarlos y guardarlos.
 * - Valida los datos a través de Ajustes.set(...)
 */
public class AjustesActivity extends AppCompatActivity {

    Ajustes ajustes; // referencia a la instancia única

    EditText etNombre, etCorreo, etEdad;
    CheckBox cbRecibirPublicidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes); // layout similar al de los apuntes

        // Referencias a la UI
        etNombre = findViewById(R.id.nombre);
        etCorreo = findViewById(R.id.correo);
        etEdad = findViewById(R.id.edad);
        cbRecibirPublicidad = findViewById(R.id.recibirPublicidad);

        // Botón guardar
        findViewById(R.id.bGuardar).setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Guardar();
            }
        });

        // Obtenemos la instancia única de Ajustes
        ajustes = Ajustes.getInstance(this);

        // Rellenamos la UI con los valores actuales
        etNombre.setText(ajustes.getNombre());
        etCorreo.setText(ajustes.getCorreo());

        int edad = ajustes.getEdad(-1);
        etEdad.setText(edad == -1 ? "" : Integer.toString(edad));

        cbRecibirPublicidad.setChecked(ajustes.getRecibirPublicidad());
    }

    /**
     * Lee lo que hay en los EditText/CheckBox y lo intenta guardar en Ajustes.
     * Si la validación falla, muestra un Toast de error.
     * Si todo OK, cierra la Activity (finish()).
     */
    private void Guardar() {

        String nombre = etNombre.getText().toString();
        String correo = etCorreo.getText().toString();
        String edad = etEdad.getText().toString();
        boolean recibirPublicidad = cbRecibirPublicidad.isChecked();

        // Pedimos a Ajustes que valide y guarde
        if (ajustes.set(nombre, correo, edad, recibirPublicidad)) {
            // Todo OK → cerramos ajustes y volvemos
            finish();
        } else {
            // Algún campo es incorrecto
            Toast.makeText(this,
                    getString(R.string.errorGuardarAjustes),
                    Toast.LENGTH_LONG).show();
        }
    }
}

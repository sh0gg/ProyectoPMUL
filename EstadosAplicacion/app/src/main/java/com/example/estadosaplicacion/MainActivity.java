package com.example.estadosaplicacion;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
public class MainActivity extends Activity {
    TextView tv;
    StringBuilder sb;
    private void addLinea(String linea) {
        sb.append(linea);
        sb.append("\n");
        tv.setText(sb.toString());
        Log.i("CicloDeVida",linea);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tv=new TextView(this);
        if (savedInstanceState != null) {
            String textoGuardado = savedInstanceState.getString("textoGuardado");
            if (textoGuardado != null) {
                sb = new StringBuilder(textoGuardado);
            }
        } else {
            sb=new StringBuilder("");
        }
        tv.setText(sb.toString());
        setContentView(tv);
    }

    @Override protected void onStart() { super.onStart(); addLinea("onStart"); }
    @Override protected void onRestart() { super.onRestart(); addLinea("onRestart"); }
    @Override protected void onResume() { super.onResume(); addLinea("onResume"); }
    @Override protected void onPause() { super.onPause(); addLinea("onPause"); }
    @Override protected void onStop() { super.onStop(); addLinea("onStop"); }
    @Override protected void onDestroy() { super.onDestroy(); addLinea("onDestroy"); }
    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("textoGuardado", sb.toString());
    }
}
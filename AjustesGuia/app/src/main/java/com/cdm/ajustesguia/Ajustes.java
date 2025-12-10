package com.cdm.ajustesguia;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase de AJUSTES de la app.
 *
 * - Implementa el patrón SINGLETON: sólo existe una instancia en toda la app
 * - Envuelve un archivo de SharedPreferences
 * - Ofrece getters para leer los ajustes y un set(...) para modificarlos + validación
 *
 * Campos:
 *   nombre (obligatorio, no vacío)
 *   correo (opcional, formato válido si no está vacío)
 *   edad (opcional, entero positivo si no está vacío)
 *   recibirPublicidad (boolean)
 */
public class Ajustes {

    // ====== CAMPOS EN MEMORIA (REFLEJO DE LO GUARDADO EN PREFS) ======
    private String nombre;
    private String correo;
    private String edad; // se guarda como String para validarla
    private boolean recibirPublicidad;

    // ====== SharedPreferences ======
    private SharedPreferences sp;

    // Claves que se usan en el archivo XML de preferencias
    private final String CLAVE_NOMBRE = "nombre";
    private final String CLAVE_CORREO = "correo";
    private final String CLAVE_EDAD = "edad";
    private final String CLAVE_RECIBIR_PUBLICIDAD = "recibirPublicidad";

    // ============================================================
    // GETTERS
    // ============================================================

    public String getNombre() { return nombre; }

    public String getCorreo() { return correo; }

    /**
     * Devuelve la edad como int.
     * Si no se puede convertir o está vacía, devuelve valorSiNoHayEdad.
     */
    public int getEdad(int valorSiNoHayEdad) {
        int edadInt;
        try {
            edadInt = Integer.parseInt(this.edad);
        } catch (NumberFormatException e) {
            return valorSiNoHayEdad;
        }
        return edadInt;
    }

    public boolean getRecibirPublicidad() { return recibirPublicidad; }

    // ============================================================
    // SET (modifica todos los ajustes de golpe)
    //  - Valida los datos
    //  - Si todo está bien, actualiza memoria + SharedPreferences
    //  - Devuelve true si OK, false si algún dato es incorrecto
    // ============================================================
    public boolean set(String nombre, String correo, String edad, boolean recibirPublicidad) {

        // Nombre obligatorio
        if (nombre.trim().equals("")) return false;

        // Correo: si no está vacío, debe tener formato válido
        if (!correo.equals("")) {
            if (!esCorreoValido(correo)) return false;
        }

        // Edad: si no está vacía, debe ser entero positivo
        if (!edad.equals("")) {
            if (!esEnteroPositivo(edad)) return false;
        }

        // Si todo OK, actualizamos memoria
        this.nombre = nombre;
        this.correo = correo;
        this.edad = edad;
        this.recibirPublicidad = recibirPublicidad;

        // Y guardamos en el archivo XML de preferencias
        sp.edit()
                .putString(CLAVE_NOMBRE, nombre)
                .putString(CLAVE_CORREO, correo)
                .putString(CLAVE_EDAD, edad)
                .putBoolean(CLAVE_RECIBIR_PUBLICIDAD, recibirPublicidad)
                .apply(); // apply() = asíncrono, commit() = síncrono

        return true;
    }

    // ============================================================
    //  IMPLEMENTACIÓN DEL PATRÓN SINGLETON
    // ============================================================

    // Referencia estática a la instancia única
    private static Ajustes instancia = null;

    /**
     * Devuelve la instancia única de Ajustes.
     * Si aún no existe, la crea leyendo de SharedPreferences usando la Activity.
     *
     * Se pasa una Activity porque necesitamos un CONTEXTO para acceder a getPreferences(...)
     */
    public static Ajustes getInstance(Activity activity) {
        if (instancia == null) {
            instancia = new Ajustes(activity);
        }
        return instancia;
    }

    /**
     * Constructor PRIVADO: solo se puede crear desde getInstance().
     * Carga los valores desde el archivo de preferencias.
     */
    private Ajustes(Activity activity) {
        // getPreferences() usa un único archivo de prefs asociado a esta Activity (MODE_PRIVATE)
        sp = activity.getPreferences(Context.MODE_PRIVATE);

        // Leemos los valores existentes, o valores por defecto si aún no hay nada
        nombre = sp.getString(CLAVE_NOMBRE, "");
        correo = sp.getString(CLAVE_CORREO, "");
        edad = sp.getString(CLAVE_EDAD, "");
        recibirPublicidad = sp.getBoolean(CLAVE_RECIBIR_PUBLICIDAD, false);
    }

    // ============================================================
    // MÉTODOS ESTÁTICOS DE VALIDACIÓN
    // ============================================================

    /**
     * Comprueba si un correo tiene un formato razonable:
     * - usuario con letras, números, puntos o guiones
     * - @
     * - uno o más dominios separados por puntos
     */
    static boolean esCorreoValido(String correo) {
        // Expresión regular simplificada, suficiente para el examen
        return correo.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]{2,}");
    }

    /**
     * Devuelve true si la cadena es un entero positivo.
     */
    static boolean esEnteroPositivo(String numero) {
        return numero.matches("\\d+");
    }
}

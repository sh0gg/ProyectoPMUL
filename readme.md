\# GUIA PMUL – FRAGMENTOS, LISTENERS, SPINNERS, BD, DETECTORES



============================================================

1\. COMO CREAR UN FRAGMENTO ESTANDAR

============================================================



public class MiFragmento extends Fragment {



&nbsp;   private TextView tv;



&nbsp;   @Nullable

&nbsp;   @Override

&nbsp;   public View onCreateView(LayoutInflater inflater,

&nbsp;                            ViewGroup container,

&nbsp;                            Bundle savedInstanceState) {



&nbsp;       View v = inflater.inflate(R.layout.frg\_mi\_fragmento, container, false);



&nbsp;       tv = v.findViewById(R.id.textView);

&nbsp;       return v;

&nbsp;   }

}



Notas:

\- Siempre usar la vista inflada v.findViewById, no findViewById directo.

\- Toda la lógica visual va DENTRO del fragmento.





============================================================

2\. PONER VARIOS FRAGMENTOS EN UNA ACTIVITY

============================================================



(a) ESTATICOS (XML):



<fragment

&nbsp;   android:id="@+id/frg1"

&nbsp;   android:name="com.cdm.MiFragmento"

&nbsp;   android:layout\_width="match\_parent"

&nbsp;   android:layout\_height="wrap\_content" />



En la Activity:

MiFragmento frg1 = (MiFragmento) getSupportFragmentManager()

&nbsp;       .findFragmentById(R.id.frg1);





(b) DINAMICOS (Java):



FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

ft.add(R.id.contenedor, new MiFragmento());

ft.commit();





============================================================

3\. COMUNICACION FRAGMENTO → ACTIVITY (LISTENER)

============================================================



Paso 1: Define la interfaz dentro del fragmento:



public interface OnMiEventoListener {

&nbsp;   void onEvento(String dato);

}



Paso 2: Engancha la Activity en onAttach():



private OnMiEventoListener listener;



@Override

public void onAttach(Context ctx) {

&nbsp;   super.onAttach(ctx);

&nbsp;   if (!(ctx instanceof OnMiEventoListener))

&nbsp;       throw new RuntimeException("La Activity debe implementar OnMiEventoListener");

&nbsp;   listener = (OnMiEventoListener) ctx;

}



Paso 3: Llamar al listener cuando ocurra algo:



listener.onEvento("Hola Activity");



Paso 4: La Activity implementa la interfaz:



public class MainActivity extends AppCompatActivity

&nbsp;       implements MiFragmento.OnMiEventoListener {



&nbsp;   @Override

&nbsp;   public void onEvento(String dato) {

&nbsp;       // responder al fragmento

&nbsp;   }

}



IMPORTANTE:

\- Este patrón se usa en TODOS los ejercicios: CNI, Spinners, Dado, etc.





============================================================

4\. COMUNICACION ENTRE FRAGMENTOS

============================================================



Los fragmentos NO se hablan directamente.

Patrón obligatorio:



FrgA → Activity → FrgB



Ejemplo:

En Activity:



@Override

public void onEvento(String dato) {

&nbsp;   frgB.actualizar(dato);

}



En FrgB:



public void actualizar(String texto) {

&nbsp;   tv.setText(texto);

}





============================================================

5\. FRAGMENTO GENERICO DE SPINNER

============================================================



public class FrgSpinnerGuia extends Fragment {



&nbsp;   public interface OnFrgSpinnerGuiaListener {

&nbsp;       void onItemSelected(FrgSpinnerGuia frg, int pos, String texto);

&nbsp;   }



&nbsp;   private OnFrgSpinnerGuiaListener listener;

&nbsp;   private Spinner spinner;

&nbsp;   private ArrayAdapter<String> adapter;

&nbsp;   private List<String> lista = new ArrayList<>();



&nbsp;   @Override

&nbsp;   public void onAttach(Context ctx) {

&nbsp;       super.onAttach(ctx);

&nbsp;       listener = (OnFrgSpinnerGuiaListener) ctx;

&nbsp;   }



&nbsp;   @Override

&nbsp;   public View onCreateView(LayoutInflater inf, ViewGroup c, Bundle b) {

&nbsp;       View v = inf.inflate(R.layout.frg\_spinner\_guia, c, false);

&nbsp;       spinner = v.findViewById(R.id.spinner);



&nbsp;       adapter = new ArrayAdapter<>(getContext(),

&nbsp;               android.R.layout.simple\_spinner\_item,

&nbsp;               lista);



&nbsp;       spinner.setAdapter(adapter);



&nbsp;       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

&nbsp;           @Override public void onItemSelected(AdapterView<?> p, View v, int pos, long id) {

&nbsp;               listener.onItemSelected(FrgSpinnerGuia.this, pos, lista.get(pos));

&nbsp;           }

&nbsp;           @Override public void onNothingSelected(AdapterView<?> p) {}

&nbsp;       });



&nbsp;       return v;

&nbsp;   }



&nbsp;   public void setDatos(List<String> datos) {

&nbsp;       lista.clear();

&nbsp;       lista.addAll(datos);

&nbsp;       if (adapter != null) adapter.notifyDataSetChanged();

&nbsp;   }

}



Puntos clave:

\- Spinner dentro del fragmento.

\- La Activity recibe item seleccionado via listener.

\- Activity puede actualizar OTRO fragmento.





============================================================

6\. BD SQLITE MINIMA

============================================================



public class AsistenteBD extends SQLiteOpenHelper {



&nbsp;   public AsistenteBD(Context c) { super(c, "bd.db", null, 1); }



&nbsp;   @Override

&nbsp;   public void onCreate(SQLiteDatabase db) {

&nbsp;       db.execSQL("CREATE TABLE marcas(\_id INTEGER PRIMARY KEY, nombre TEXT)");

&nbsp;       db.execSQL("CREATE TABLE modelos(\_id INTEGER PRIMARY KEY, nombre TEXT, idMarca INTEGER)");



&nbsp;       db.execSQL("INSERT INTO marcas(nombre) VALUES('Toyota'), ('Ford')");

&nbsp;       db.execSQL("INSERT INTO modelos(nombre,idMarca) VALUES('Corolla',1), ('Yaris',1), ('Focus',2)");

&nbsp;   }



&nbsp;   public List<String> getMarcas() {

&nbsp;       ArrayList<String> res = new ArrayList<>();

&nbsp;       Cursor c = getReadableDatabase().rawQuery("SELECT nombre FROM marcas", null);

&nbsp;       while (c.moveToNext()) res.add(c.getString(0));

&nbsp;       return res;

&nbsp;   }



&nbsp;   public List<String> getModelos(String marca) {

&nbsp;       ArrayList<String> res = new ArrayList<>();

&nbsp;       Cursor c = getReadableDatabase().rawQuery(

&nbsp;           "SELECT mo.nombre FROM modelos mo JOIN marcas ma ON mo.idMarca=ma.\_id WHERE ma.nombre=?",

&nbsp;           new String\[]{marca});

&nbsp;       while (c.moveToNext()) res.add(c.getString(0));

&nbsp;       return res;

&nbsp;   }

}





============================================================

7\. EJEMPLO: DOS SPINNERS CONECTADOS (MARCA → MODELO)

============================================================



En Activity:



public class MainActivityGuia extends AppCompatActivity

&nbsp;       implements FrgSpinnerGuia.OnFrgSpinnerGuiaListener {



&nbsp;   FrgSpinnerGuia frgMarcas, frgModelos;

&nbsp;   AsistenteBD bd;



&nbsp;   @Override

&nbsp;   protected void onCreate(Bundle b) {

&nbsp;       super.onCreate(b);

&nbsp;       setContentView(R.layout.activity\_main\_guia);



&nbsp;       bd = new AsistenteBD(this);



&nbsp;       frgMarcas = (FrgSpinnerGuia) getSupportFragmentManager().findFragmentById(R.id.frgMarcas);

&nbsp;       frgModelos = (FrgSpinnerGuia) getSupportFragmentManager().findFragmentById(R.id.frgModelos);



&nbsp;       frgMarcas.setDatos(bd.getMarcas());

&nbsp;   }



&nbsp;   @Override

&nbsp;   public void onItemSelected(FrgSpinnerGuia frg, int pos, String texto) {

&nbsp;       if (frg == frgMarcas) {

&nbsp;           frgModelos.setDatos(bd.getModelos(texto));

&nbsp;       }

&nbsp;   }

}



Resumen:

\- El primer spinner es MARCA.

\- El segundo spinner depende del primero.

\- Comunicación via listener.





============================================================

8\. FRAGMENTO DETECTOR CNI (CON TEXTWATCHER)

============================================================



TextWatcher obligatorio para detectar mientras se escribe.



et.addTextChangedListener(new TextWatcher() {

&nbsp;   @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}

&nbsp;   @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}



&nbsp;   @Override

&nbsp;   public void afterTextChanged(Editable s) {

&nbsp;       detectarTokens(s.toString());

&nbsp;   }

});



detección:



if (texto.matches("(?i).\*" + patron + ".\*") \&\& !regla.avisado) {

&nbsp;   regla.avisado = true;

&nbsp;   listener.onTokenDetectado(this, campo, patron, texto, nivel);

}



Notas:

\- (?i) → ignorar mayúsculas/minúsculas

\- .\*patron.\* → aparece en cualquier punto

\- regla.avisado evita que dispare continuamente





============================================================

9\. LANZAR OTRA ACTIVITY CON DATOS (INTENT)

============================================================



Enviar:

Intent intent = new Intent(this, ActivityAlertaGuia.class);

intent.putExtra("campo", campo);

intent.putExtra("texto", texto);

startActivity(intent);



Recibir:

String campo = getIntent().getStringExtra("campo");





============================================================

10\. COSAS QUE SIEMPRE CAEN EN EL EXAMEN

============================================================



\- Un fragmento NO usa findViewById() de la Activity: usa la vista inflada.

\- onAttach() para enganchar el listener.

\- Interfaz dentro del fragmento.

\- Activity implementa la interfaz → comunicación fragmento→activity.

\- Activity coordina varios fragmentos (A afecta a B).

\- BD SQLite con onCreate() y consultas rawQuery().

\- Spinner + ArrayAdapter.

\- TextWatcher en fragmento CNI.

\- Intent para cambiar de Activity pasando datos.




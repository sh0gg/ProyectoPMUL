package com.example.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements CasillaAdapter.Listener {

    private static final int ANCHO = 9;
    private static final int ALTO = 9;
    private static final int MINAS = 10;

    private static final String EXTRA_NEW_GAME = "extra_new_game";

    private Tablero tablero;
    private CasillaAdapter adapter;
    private RecyclerView rv;
    private TextView tvTimer;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private long baseElapsedMs = 0L;
    private long startRealtimeMs = 0L;
    private boolean timerRunning = false;

    private final Runnable tick = new Runnable() {
        @Override public void run() {
            if (!timerRunning) return;
            tvTimer.setText(formatMs(getElapsedMs()));
            handler.postDelayed(this, 50);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        tvTimer = findViewById(R.id.tvTimer);
        rv = findViewById(R.id.recycler);

        final int baseL = rv.getPaddingLeft();
        final int baseT = rv.getPaddingTop();
        final int baseR = rv.getPaddingRight();
        final int baseB = rv.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(rv, (v, insets) -> {
            Insets sys = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(baseL + sys.left, baseT + sys.top, baseR + sys.right, baseB + sys.bottom);
            return insets;
        });
        ViewCompat.requestApplyInsets(rv);

        boolean forceNew = getIntent() != null && getIntent().getBooleanExtra(EXTRA_NEW_GAME, false);

        if (savedInstanceState == null || forceNew) {
            nuevoJuego();
        } else {
            int ancho = savedInstanceState.getInt("ancho", ANCHO);
            int alto = savedInstanceState.getInt("alto", ALTO);
            int minasTotal = savedInstanceState.getInt("minasTotal", MINAS);

            tablero = new Tablero(ancho, alto, minasTotal);

            boolean[] arrMinas = savedInstanceState.getBooleanArray("arrMinas");
            boolean[] arrRevelada = savedInstanceState.getBooleanArray("arrRevelada");
            boolean[] arrBandera = savedInstanceState.getBooleanArray("arrBandera");
            int[] arrAdy = savedInstanceState.getIntArray("arrAdy");

            if (arrMinas != null && arrRevelada != null && arrBandera != null && arrAdy != null) {
                unflatten(arrMinas, tablero.minas, tablero.ancho);
                unflatten(arrRevelada, tablero.revelada, tablero.ancho);
                unflatten(arrBandera, tablero.conBandera, tablero.ancho);
                unflatten(arrAdy, tablero.adyacentes, tablero.ancho);
            }

            tablero.gameOver = savedInstanceState.getBoolean("gameOver", false);
            tablero.victoria = savedInstanceState.getBoolean("victoria", false);

            baseElapsedMs = savedInstanceState.getLong("elapsedMs", 0L);
            timerRunning = savedInstanceState.getBoolean("timerRunning", false);
            if (timerRunning && !tablero.gameOver) {
                startRealtimeMs = SystemClock.elapsedRealtime();
                handler.post(tick);
            }
            tvTimer.setText(formatMs(getElapsedMs()));

            adapter = new CasillaAdapter(this, tablero, this);
            rv.setLayoutManager(new GridLayoutManager(this, tablero.ancho));
            rv.setAdapter(adapter);

            rv.post(() -> {
                int totalWidth = rv.getWidth() - rv.getPaddingLeft() - rv.getPaddingRight();
                int cell = totalWidth / tablero.ancho;
                adapter.setCellSize(cell);
            });
        }
    }

    private void nuevoJuego() {
        stopTimer();
        baseElapsedMs = 0L;
        tvTimer.setText(formatMs(0L));

        tablero = new Tablero(ANCHO, ALTO, MINAS);
        adapter = new CasillaAdapter(this, tablero, this);
        rv.setLayoutManager(new GridLayoutManager(this, tablero.ancho));
        rv.setAdapter(adapter);

        rv.post(() -> {
            int totalWidth = rv.getWidth() - rv.getPaddingLeft() - rv.getPaddingRight();
            int cell = totalWidth / tablero.ancho;
            adapter.setCellSize(cell);
        });
    }

    private void ensureTimerStarted() {
        if (timerRunning) return;
        timerRunning = true;
        startRealtimeMs = SystemClock.elapsedRealtime();
        handler.removeCallbacks(tick);
        handler.post(tick);
    }

    private void stopTimer() {
        timerRunning = false;
        handler.removeCallbacks(tick);
    }

    private long getElapsedMs() {
        if (!timerRunning) return baseElapsedMs;
        return baseElapsedMs + (SystemClock.elapsedRealtime() - startRealtimeMs);
    }

    @Override
    public void onCavar(int x, int y) {
        if (tablero.gameOver) return;
        ensureTimerStarted();
        tablero.revelar(x, y);
        adapter.notifyDataSetChanged();
        if (tablero.gameOver) onFinishGame();
    }

    @Override
    public void onBandera(int x, int y) {
        if (tablero.gameOver) return;
        ensureTimerStarted();
        tablero.alternarBandera(x, y);
        adapter.notifyItemChanged(y * tablero.ancho + x);
        if (tablero.gameOver) onFinishGame();
    }

    private void onFinishGame() {
        baseElapsedMs = getElapsedMs();
        stopTimer();

        if (tablero.victoria) {
            RecordsStore.addRecord(this, baseElapsedMs);
        }

        Intent i = new Intent(this, ResultsActivity.class);
        i.putExtra(ResultsActivity.EXTRA_WON, tablero.victoria);
        i.putExtra(ResultsActivity.EXTRA_TIME_MS, baseElapsedMs);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private static String formatMs(long ms) {
        if (ms < 0) ms = 0;
        long totalCs = ms / 10;
        long cs = totalCs % 100;
        long totalS = totalCs / 100;
        long s = totalS % 60;
        long m = totalS / 60;

        String mm = (m < 10) ? ("0" + m) : String.valueOf(m);
        String ss = (s < 10) ? ("0" + s) : String.valueOf(s);
        String cc = (cs < 10) ? ("0" + cs) : String.valueOf(cs);
        return mm + ":" + ss + "." + cc;
    }

    private static boolean[] flatten(boolean[][] src) {
        int h = src.length, w = src[0].length;
        boolean[] out = new boolean[w * h];
        int i = 0;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                out[i++] = src[y][x];
        return out;
    }

    private static int[] flatten(int[][] src) {
        int h = src.length, w = src[0].length;
        int[] out = new int[w * h];
        int i = 0;
        for (int y = 0; y < h; y++)
            for (int x = 0; x < w; x++)
                out[i++] = src[y][x];
        return out;
    }

    private static void unflatten(boolean[] src, boolean[][] dst, int w) {
        for (int i = 0; i < src.length; i++) {
            int y = i / w;
            int x = i % w;
            dst[y][x] = src[i];
        }
    }

    private static void unflatten(int[] src, int[][] dst, int w) {
        for (int i = 0; i < src.length; i++) {
            int y = i / w;
            int x = i % w;
            dst[y][x] = src[i];
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tablero == null) return;

        if (timerRunning) {
            baseElapsedMs = getElapsedMs();
            startRealtimeMs = SystemClock.elapsedRealtime();
        }

        outState.putInt("ancho", tablero.ancho);
        outState.putInt("alto", tablero.alto);
        outState.putInt("minasTotal", tablero.numeroDeMinas);
        outState.putBoolean("gameOver", tablero.gameOver);
        outState.putBoolean("victoria", tablero.victoria);

        outState.putLong("elapsedMs", baseElapsedMs);
        outState.putBoolean("timerRunning", timerRunning);

        outState.putBooleanArray("arrMinas", flatten(tablero.minas));
        outState.putBooleanArray("arrRevelada", flatten(tablero.revelada));
        outState.putBooleanArray("arrBandera", flatten(tablero.conBandera));
        outState.putIntArray("arrAdy", flatten(tablero.adyacentes));
    }

    public static Intent newGameIntent(AppCompatActivity a) {
        Intent i = new Intent(a, MainActivity.class);
        i.putExtra(EXTRA_NEW_GAME, true);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return i;
    }
}

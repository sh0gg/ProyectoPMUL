package com.example.minesweeper;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity {

    public static final String EXTRA_WON = "extra_won";
    public static final String EXTRA_TIME_MS = "extra_time_ms";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        boolean won = getIntent() != null && getIntent().getBooleanExtra(EXTRA_WON, false);
        long timeMs = getIntent() != null ? getIntent().getLongExtra(EXTRA_TIME_MS, 0L) : 0L;

        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvSubtitle = findViewById(R.id.tvSubtitle);
        TextView tvRecords = findViewById(R.id.tvRecords);
        Button btnRestart = findViewById(R.id.btnRestart);
        Button btnPlayAgain = findViewById(R.id.btnPlayAgain);

        tvTitle.setText(won ? "¡Victoria!" : "Game Over");
        tvSubtitle.setText(won ? ("Tiempo: " + formatMs(timeMs)) : "Has perdido");

        List<RecordsStore.Record> records = RecordsStore.getRecords(this);
        tvRecords.setText(buildRecordsText(records));

        btnRestart.setOnClickListener(v -> {
            startActivity(MainActivity.newGameIntent(this));
            finish();
        });

        btnPlayAgain.setOnClickListener(v -> {
            startActivity(MainActivity.newGameIntent(this));
            finish();
        });
    }

    private String buildRecordsText(List<RecordsStore.Record> records) {
        if (records == null || records.isEmpty()) return "Mejores tiempos:\n(ninguno todavía)";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        StringBuilder sb = new StringBuilder();
        sb.append("Mejores tiempos:\n");
        int n = Math.min(10, records.size());
        for (int i = 0; i < n; i++) {
            RecordsStore.Record r = records.get(i);
            sb.append(i + 1).append(". ")
                    .append(formatMs(r.timeMs))
                    .append("  —  ")
                    .append(sdf.format(new Date(r.timestampMs)))
                    .append("\n");
        }
        return sb.toString().trim();
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
}

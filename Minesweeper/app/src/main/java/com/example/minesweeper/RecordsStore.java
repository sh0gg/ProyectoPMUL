package com.example.minesweeper;

import android.content.Context;
import android.content.SharedPreferences;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class RecordsStore {

    private static final String PREF = "minesweeper_prefs";
    private static final String KEY = "records_v1";
    private static final int MAX = 10;

    public static final class Record {
        public final long timeMs;
        public final long timestampMs;
        public Record(long timeMs, long timestampMs) {
            this.timeMs = timeMs;
            this.timestampMs = timestampMs;
        }
    }

    private RecordsStore() {}

    public static void addRecord(Context ctx, long timeMs) {
        List<Record> list = getRecords(ctx);
        list.add(new Record(timeMs, System.currentTimeMillis()));
        Collections.sort(list, Comparator.comparingLong(r -> r.timeMs));
        if (list.size() > MAX) list = list.subList(0, MAX);
        save(ctx, list);
    }

    public static List<Record> getRecords(Context ctx) {
        SharedPreferences sp = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
        String raw = sp.getString(KEY, "[]");
        List<Record> out = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(raw);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                out.add(new Record(o.getLong("t"), o.getLong("d")));
            }
        } catch (Exception ignored) {}
        Collections.sort(out, Comparator.comparingLong(r -> r.timeMs));
        if (out.size() > MAX) out = out.subList(0, MAX);
        return out;
    }

    private static void save(Context ctx, List<Record> list) {
        JSONArray arr = new JSONArray();
        try {
            for (Record r : list) {
                JSONObject o = new JSONObject();
                o.put("t", r.timeMs);
                o.put("d", r.timestampMs);
                arr.put(o);
            }
        } catch (Exception ignored) {}
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY, arr.toString())
                .apply();
    }
}

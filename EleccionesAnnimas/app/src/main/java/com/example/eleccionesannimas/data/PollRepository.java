package com.example.eleccionesannimas.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.UUID;

import com.example.eleccionesannimas.model.Poll;
import com.example.eleccionesannimas.model.PollOption;

/**
 * @noinspection ALL
 */
public class PollRepository {
    private static final String PREFS = "polls_prefs";
    private static final String KEY_POLLS = "polls_json";
    private static PollRepository INSTANCE;
    private final SharedPreferences prefs;
    private final ArrayList<Poll> cache = new ArrayList<>();
    private Poll current;

    private PollRepository(Context ctx) {
        prefs = ctx.getApplicationContext().getSharedPreferences(PREFS,
                Context.MODE_PRIVATE);
        load();
    }

    public static PollRepository get(Context ctx) {
        if (INSTANCE == null) INSTANCE = new PollRepository(ctx);
        return INSTANCE;
    }

    public ArrayList<Poll> getAll() {
        return new ArrayList<>(cache);
    }

    public Poll getCurrent() {
        return current;
    }

    public void setCurrent(Poll p) {
        current = p;
    }

    public void create(String title, int expected, ArrayList<String>
            optionTexts) {
        Poll p = new Poll(UUID.randomUUID().toString(), title, expected);
        for (String t : optionTexts) {
            if (t != null && !t.trim().isEmpty())
                p.getOptions().add(new PollOption(t.trim()));
        }
        cache.add(0, p);
        save();
    }

    public void update() {
        save();
    }

    public void delete(String id) {
        for (int i = 0; i < cache.size(); i++) {
            if (cache.get(i).getId().equals(id)) {
                cache.remove(i);
                break;
            }
        }
        if (current != null && current.getId().equals(id)) current = null;
        save();
    }

    private void save() {
        JSONArray arr = new JSONArray();
        try {
            for (Poll p : cache) arr.put(p.toJson());
            prefs.edit().putString(KEY_POLLS, arr.toString()).apply();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void load() {
        cache.clear();
        String json = prefs.getString(KEY_POLLS, "[]");
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++)
                cache.add(Poll.fromJson(arr.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
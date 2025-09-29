package com.example.eleccionesannimas.model;

import org.json.JSONException;
import org.json.JSONObject;

public class PollOption {
    private final String text;
    private int votes;

    public PollOption(String text) {
        this.text = text;
        this.votes = 0;
    }

    public String getText() {
        return text;
    }

    public int getVotes() {
        return votes;
    }

    public void increment() {
        votes++;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("text", text);
        o.put("votes", votes);
        return o;
    }

    public static PollOption fromJson(JSONObject o) throws JSONException {
        PollOption po = new PollOption(o.getString("text"));
        po.votes = o.optInt("votes", 0);
        return po;
    }
}

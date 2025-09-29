package com.example.eleccionesannimas.model;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
public class Poll {
    private String id;
    private String title;
    private int expectedVoters;
    private ArrayList<PollOption> options = new ArrayList<>();
    private int abstentions = 0;
    public Poll(String id, String title, int expectedVoters) {
        this.id = id;
        this.title = title;
        this.expectedVoters = expectedVoters;
    }
    public String getId() { return id; }
    public String getTitle() { return title; }
    public int getExpectedVoters() { return expectedVoters; }
    public ArrayList<PollOption> getOptions() { return options; }
    public int getAbstentions() { return abstentions; }
    public void abstain() { abstentions++; }
    public int getTotalVotes() {
        int sum = abstentions;
        for (PollOption o : options) sum += o.getVotes();
        return sum;
    }
    public JSONObject toJson() throws JSONException {
        JSONObject o = new JSONObject();
        o.put("id", id);
        o.put("title", title);
        o.put("expectedVoters", expectedVoters);
        o.put("abstentions", abstentions);
        JSONArray arr = new JSONArray();
        for (PollOption p : options) arr.put(p.toJson());
        o.put("options", arr);
        return o;
    }
    public static Poll fromJson(JSONObject o) throws JSONException {
        Poll p = new Poll(
                o.getString("id"),
                o.getString("title"),
                o.optInt("expectedVoters", 0)
        );
        p.abstentions = o.optInt("abstentions", 0);
        JSONArray arr = o.optJSONArray("options");
        if (arr != null) for (int i = 0; i < arr.length(); i++)
            p.options.add(PollOption.fromJson(arr.getJSONObject(i)));
        return p;
    }
}
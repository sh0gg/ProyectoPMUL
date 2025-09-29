package com.example.eleccionesannimas;
import android.os.Bundle;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.eleccionesannimas.data.PollRepository;
import com.example.eleccionesannimas.model.Poll;
import com.example.eleccionesannimas.model.PollOption;
public class ResultadoActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultado);
        PollRepository repo = PollRepository.get(this);
        Poll poll = repo.getCurrent();
        if (poll == null) { finish(); return; }
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvSummary = findViewById(R.id.tvSummary);
        RecyclerView rv = findViewById(R.id.rvResults);
        Button btnBack = findViewById(R.id.btnBackHome);
        tvTitle.setText(poll.getTitle());
        String summary = getString(R.string.fmt_results_summary,
                poll.getTotalVotes(), poll.getExpectedVoters(),
                poll.getAbstentions());
        tvSummary.setText(summary);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new ResultsAdapter(poll));
        btnBack.setOnClickListener(v -> finish());
    }
    static class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.VH>
    {
        private final Poll poll;
        ResultsAdapter(Poll poll) { this.poll = poll; }
        @Override public VH onCreateViewHolder(android.view.ViewGroup parent,
                                               int viewType) {
            android.view.View v =
                    android.view.LayoutInflater.from(parent.getContext())
                            .inflate(android.R.layout.simple_list_item_2, parent,
                                    false);
            return new VH(v);
        }
        @Override public void onBindViewHolder(VH h, int pos) {
            PollOption opt = poll.getOptions().get(pos);
            int total = Math.max(1, poll.getTotalVotes());
            int pct = Math.round(100f * opt.getVotes() / total);
            h.t1.setText(opt.getText());

            h.t2.setText(h.itemView.getContext().getString(R.string.fmt_votes_pct,
                    opt.getVotes(), pct));
        }
        @Override public int getItemCount() { return poll.getOptions().size(); }
        static class VH extends RecyclerView.ViewHolder {
            TextView t1, t2;
            VH(android.view.View v) { super(v); t1 =
                    v.findViewById(android.R.id.text1); t2 = v.findViewById(android.R.id.text2); }
        }
    }
}
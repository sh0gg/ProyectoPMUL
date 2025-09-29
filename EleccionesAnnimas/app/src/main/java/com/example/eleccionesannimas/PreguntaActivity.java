package com.example.eleccionesannimas;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eleccionesannimas.data.PollRepository;
import com.example.eleccionesannimas.model.Poll;
import com.example.eleccionesannimas.model.PollOption;

public class PreguntaActivity extends AppCompatActivity implements
        VoteOptionsAdapter.Listener {
    private PollRepository repo;
    private Poll poll;
    private TextView tvCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregunta);
        repo = PollRepository.get(this);
        poll = repo.getCurrent();
        if (poll == null) {
            finish();
            return;
        }
        TextView tvTitle = findViewById(R.id.tvTitle);
        tvCounter = findViewById(R.id.tvCounter);
        RecyclerView rvOptions = findViewById(R.id.rvOptions);
        Button btnAbstain = findViewById(R.id.btnAbstain);
        Button btnShowResults = findViewById(R.id.btnShowResults);
        tvTitle.setText(poll.getTitle());
        updateCounter();
        int span = (getResources().getConfiguration().orientation ==
                Configuration.ORIENTATION_LANDSCAPE) ? 4 : 2;
        rvOptions.setLayoutManager(new GridLayoutManager(this, span));
        VoteOptionsAdapter adapter = new VoteOptionsAdapter(this);
        rvOptions.setAdapter(adapter);
        adapter.submit(poll.getOptions());
        btnAbstain.setOnClickListener(v -> {
            poll.abstain();
            repo.update();
            afterVote();
        });
        btnShowResults.setOnClickListener(v -> goResults());
    }

    @Override
    public void onVote(PollOption option) {
        option.increment();
        repo.update();
        afterVote();
    }

    private void afterVote() {
        updateCounter();
        if (poll.getTotalVotes() >= poll.getExpectedVoters()) goResults();
    }

    private void updateCounter() {
        tvCounter.setText(getString(R.string.fmt_counter, poll.getTotalVotes(),
                poll.getExpectedVoters()));
    }

    private void goResults() {
        startActivity(new Intent(this, ResultadoActivity.class));
        finish();
    }
}
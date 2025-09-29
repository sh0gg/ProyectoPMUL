package com.example.eleccionesannimas;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.eleccionesannimas.data.PollRepository;
import com.example.eleccionesannimas.model.Poll;

public class MainActivity extends AppCompatActivity {
    private EditText etTitle, etVoters;
    private OptionEditAdapter optionEditAdapter;
    private PollListAdapter pollListAdapter;
    private PollRepository repo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        repo = PollRepository.get(this);
        etTitle = findViewById(R.id.etTitle);
        etVoters = findViewById(R.id.etVoters);
        RecyclerView rvOptionEdit = findViewById(R.id.rvOptionEdit);
        RecyclerView rvPolls = findViewById(R.id.rvPolls);
        Button btnAddOption = findViewById(R.id.btnAddOption);
        Button btnCreate = findViewById(R.id.btnCreate);
        Button btnStartLatest = findViewById(R.id.btnStartLatest);
        optionEditAdapter = new OptionEditAdapter();
        rvOptionEdit.setLayoutManager(new LinearLayoutManager(this));
        rvOptionEdit.setAdapter(optionEditAdapter);
        btnAddOption.setOnClickListener(v -> optionEditAdapter.addEmpty());
        pollListAdapter = new PollListAdapter(new PollListAdapter.Listener() {
            @Override
            public void onStart(Poll p) {
                startPoll(p);
            }

            @Override
            public void onDelete(Poll p) {
                repo.delete(p.getId());
                refreshPolls();
            }

            @Override
            public void onEdit(Poll p) {
                etTitle.setText(p.getTitle());
                etVoters.setText(String.valueOf(p.getExpectedVoters()));
                optionEditAdapter.setFromPoll(p);
                Toast.makeText(MainActivity.this, R.string.msg_loaded_for_edit,
                        Toast.LENGTH_SHORT).show();
            }
        });
        rvPolls.setLayoutManager(new LinearLayoutManager(this));
        rvPolls.setAdapter(pollListAdapter);
        refreshPolls();
        btnCreate.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String votersStr = etVoters.getText().toString().trim();
            ArrayList<String> optionTexts = optionEditAdapter.getTexts();
            if (TextUtils.isEmpty(title)) {
                etTitle.setError(getString(R.string.err_required));
                return;
            }
            if (TextUtils.isEmpty(votersStr)) {
                etVoters.setError(getString(R.string.err_required));
                return;
            }
            int voters = 0;
            try {
                voters = Integer.parseInt(votersStr);
            } catch (Exception
                    ignored) {
            }
            if (voters < 1) {
                etVoters.setError(getString(R.string.err_voters));
                return;
            }
            int nonEmpty = 0;
            for (String t : optionTexts)
                if (t != null && !t.trim().isEmpty())
                    nonEmpty++;
            if (nonEmpty < 2) {
                Toast.makeText(this, R.string.err_min_options,
                        Toast.LENGTH_SHORT).show();
                return;
            }
            repo.create(title, voters, optionTexts);
            optionEditAdapter.clearAll();
            etTitle.setText("");
            etVoters.setText("");
            refreshPolls();
            Toast.makeText(this, R.string.msg_created,
                    Toast.LENGTH_SHORT).show();
        });
        btnStartLatest.setOnClickListener(v -> {
            if (repo.getAll().isEmpty()) {
                Toast.makeText(this, R.string.err_no_polls,
                        Toast.LENGTH_SHORT).show();
            } else {
                startPoll(repo.getAll().get(0));
            }
        });
    }

    private void refreshPolls() {
        pollListAdapter.submit(repo.getAll());
    }

    private void startPoll(Poll p) {
        repo.setCurrent(p);
        startActivity(new Intent(this, PreguntaActivity.class));
    }
}

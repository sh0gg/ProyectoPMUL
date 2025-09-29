package com.example.eleccionesannimas;

import android.view.*;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.example.eleccionesannimas.model.Poll;

public class PollListAdapter extends RecyclerView.Adapter<PollListAdapter.VH> {
    public interface Listener {
        void onStart(Poll p);

        void onDelete(Poll p);

        void onEdit(Poll p);
    }

    private final Listener listener;
    private final ArrayList<Poll> items = new ArrayList<>();

    public PollListAdapter(Listener l) {
        this.listener = l;
    }

    public void submit(ArrayList<Poll> data) {
        items.clear();
        items.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent,
                                 int viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_poll_title,
                        parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Poll p = items.get(pos);
        h.tvTitle.setText(p.getTitle());
        h.tvMeta.setText(h.itemView.getContext().getString(R.string.fmt_meta,
                p.getExpectedVoters()));
        h.btnStart.setOnClickListener(v -> listener.onStart(p));
        h.btnEdit.setOnClickListener(v -> listener.onEdit(p));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(p));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvTitle;
        final TextView tvMeta;
        final ImageButton btnStart;
        final ImageButton btnEdit;
        final ImageButton btnDelete;

        VH(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvPollTitle);
            tvMeta = v.findViewById(R.id.tvPollMeta);
            btnStart = v.findViewById(R.id.btnStartPoll);
            btnEdit = v.findViewById(R.id.btnEditPoll);
            btnDelete = v.findViewById(R.id.btnDeletePoll);
        }
    }
}

package com.example.eleccionesannimas;
import android.content.res.ColorStateList;
import android.view.*;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Random;
import com.example.eleccionesannimas.model.PollOption;
public class VoteOptionsAdapter extends
        RecyclerView.Adapter<VoteOptionsAdapter.VH> {
    public interface Listener { void onVote(PollOption option); }
    private final Listener listener;
    private final ArrayList<PollOption> items = new ArrayList<>();
    private final Random rnd = new Random();
    public VoteOptionsAdapter(Listener l) { this.listener = l; }
    public void submit(java.util.List<PollOption> data) { items.clear();
        items.addAll(data); notifyDataSetChanged(); }
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_vote,
                        parent, false);
        return new VH(v);
    }
    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        PollOption opt = items.get(pos);
        h.btn.setText(opt.getText());
        int[] palette =
                h.itemView.getResources().getIntArray(R.array.option_palette);
        int color = palette[rnd.nextInt(palette.length)];
        h.btn.setBackgroundTintList(ColorStateList.valueOf(color));
        h.btn.setOnClickListener(v -> listener.onVote(opt));
    }
    @Override public int getItemCount() { return items.size(); }
    static class VH extends RecyclerView.ViewHolder {
        Button btn;
        VH(View v) { super(v); btn = v.findViewById(R.id.btnVoteOption); }
    }
}
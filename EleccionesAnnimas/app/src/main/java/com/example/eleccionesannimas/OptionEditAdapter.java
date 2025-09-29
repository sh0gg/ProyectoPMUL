package com.example.eleccionesannimas;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import com.example.eleccionesannimas.model.Poll;
import com.example.eleccionesannimas.R;
public class OptionEditAdapter extends
        RecyclerView.Adapter<OptionEditAdapter.VH> {
    private final ArrayList<String> items = new ArrayList<>();
    public void addEmpty() { items.add("");
        notifyItemInserted(items.size()-1); }
    public void clearAll() { items.clear(); notifyDataSetChanged(); }
    public ArrayList<String> getTexts() { return new ArrayList<>(items); }
    public void setFromPoll(Poll p) {
        items.clear();
        for (int i = 0; i < p.getOptions().size(); i++)
            items.add(p.getOptions().get(i).getText());
        notifyDataSetChanged();
    }
    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent,
                                                    int viewType) {
        View v =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_option_edit,
                        parent, false);
        return new VH(v);
    }
    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        h.bind(items.get(pos), s -> items.set(h.getBindingAdapterPosition(),
                s));
    }
    @Override public int getItemCount() { return items.size(); }
    static class VH extends RecyclerView.ViewHolder {
        private final EditText et;
        private TextWatcher watcher;
        VH(View v) { super(v); et = v.findViewById(R.id.etOptionText); }
        void bind(String text, OnChange cb) {
            if (watcher != null) et.removeTextChangedListener(watcher);
            et.setText(text);
            watcher = new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int st,
                                                        int c, int a) {}
                @Override public void onTextChanged(CharSequence s, int st, int
                        b, int c) {}
                @Override public void afterTextChanged(Editable s)
                { cb.onChange(s.toString()); }
            };
            et.addTextChangedListener(watcher);
        }
        interface OnChange { void onChange(String s); }
    }
}

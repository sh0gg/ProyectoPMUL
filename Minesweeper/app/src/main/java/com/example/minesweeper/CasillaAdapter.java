package com.example.minesweeper;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CasillaAdapter extends RecyclerView.Adapter<CasillaAdapter.VH> {

    public interface Listener {
        void onCavar(int x, int y);
        void onBandera(int x, int y);
    }

    private final Context ctx;
    private final Tablero tablero;
    private final Listener listener;

    private int cellSizePx = -1;

    public CasillaAdapter(Context ctx, Tablero tablero, Listener listener) {
        this.ctx = ctx;
        this.tablero = tablero;
        this.listener = listener;
    }

    public void setCellSize(int px) {
        this.cellSizePx = px;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_casilla, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        if (cellSizePx > 0) {
            ViewGroup.LayoutParams lp = h.tv.getLayoutParams();
            lp.width = cellSizePx;
            lp.height = cellSizePx;
            h.tv.setLayoutParams(lp);
        }

        int x = position % tablero.ancho;
        int y = position / tablero.ancho;

        boolean revelada = tablero.revelada[y][x];
        boolean bandera = tablero.conBandera[y][x];
        boolean mina = tablero.minas[y][x];
        int n = tablero.adyacentes[y][x];

        h.tv.setTypeface(null, Typeface.BOLD);
        h.tv.setTextSize(16);

        if (revelada) {
            h.tv.setBackgroundResource(R.drawable.cell_bg_revealed);
            if (mina) {
                h.tv.setText("💣");
                h.tv.setTextColor(0xFF000000);
            } else if (n == 0) {
                h.tv.setText("");
            } else {
                h.tv.setText(String.valueOf(n));
                switch (n) {
                    case 1: h.tv.setTextColor(0xFF1976D2); break;
                    case 2: h.tv.setTextColor(0xFF388E3C); break;
                    case 3: h.tv.setTextColor(0xFFD32F2F); break;
                    case 4: h.tv.setTextColor(0xFF7B1FA2); break;
                    case 5: h.tv.setTextColor(0xFF5D4037); break;
                    case 6: h.tv.setTextColor(0xFF00796B); break;
                    case 7: h.tv.setTextColor(0xFF000000); break;
                    case 8: h.tv.setTextColor(0xFF616161); break;
                }
            }
        } else {
            h.tv.setBackgroundColor(0xFF8E8E8E);
            if (bandera) {
                h.tv.setText("🚩");
            } else {
                h.tv.setText("");
            }
            h.tv.setTextColor(0xFF000000);
        }

        h.itemView.setOnClickListener(v -> listener.onCavar(x, y));
        h.itemView.setOnLongClickListener(v -> {
            listener.onBandera(x, y);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return tablero.ancho * tablero.alto;
    }

    public static class VH extends RecyclerView.ViewHolder {
        final TextView tv;
        public VH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tvCell);
        }
    }
}

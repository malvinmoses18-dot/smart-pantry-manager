package com.example.smartpantry.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartpantry.R;
import com.example.smartpantry.data.PantryItem;
import com.example.smartpantry.util.Format;

import java.util.ArrayList;
import java.util.List;

/** Custom RecyclerView adapter that shows one pantry item per card. */
public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    /** The Activity implements this to react to taps. */
    public interface Listener {
        void onItemClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private static final long ONE_DAY = 24L * 60 * 60 * 1000;
    private static final long SOON_WINDOW = 3 * ONE_DAY;
    private static final int EXPIRING_COLOR = 0xFFD32F2F;

    private final List<PantryItem> items = new ArrayList<>();
    private final Listener listener;
    private boolean highlightExpiring = true;

    public PantryAdapter(Listener listener) {
        this.listener = listener;
    }

    public void setItems(List<PantryItem> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void setHighlightExpiring(boolean highlight) {
        if (highlightExpiring != highlight) {
            highlightExpiring = highlight;
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PantryItem item = items.get(position);

        holder.name.setText(item.name);
        holder.quantity.setText(Format.quantity(item.quantity) + " " + item.unit);

        if (item.expiryDate == null) {
            holder.expiry.setVisibility(View.GONE);
        } else {
            holder.expiry.setVisibility(View.VISIBLE);
            // The stored date is midnight at the start of the expiry day, so add a day.
            long remaining = item.expiryDate + ONE_DAY - System.currentTimeMillis();
            String date = Format.date(item.expiryDate);
            if (highlightExpiring && remaining < 0) {
                holder.expiry.setText("Expired " + date);
                holder.expiry.setTextColor(EXPIRING_COLOR);
            } else if (highlightExpiring && remaining <= SOON_WINDOW) {
                holder.expiry.setText("Expires soon: " + date);
                holder.expiry.setTextColor(EXPIRING_COLOR);
            } else {
                holder.expiry.setText("Expires " + date);
                holder.expiry.setTextColor(holder.defaultExpiryColor);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
        holder.delete.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView quantity;
        final TextView expiry;
        final ImageButton delete;
        final int defaultExpiryColor;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.text_name);
            quantity = itemView.findViewById(R.id.text_quantity);
            expiry = itemView.findViewById(R.id.text_expiry);
            delete = itemView.findViewById(R.id.button_delete);
            defaultExpiryColor = expiry.getCurrentTextColor();
        }
    }
}

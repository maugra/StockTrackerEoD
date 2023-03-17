package com.example.stocktrackereod;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktrackereod.position.Position;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

//adapter according to  https://guides.codepath.com/android/using-the-recyclerview#creating-the-recyclerview-adapter
public class PositionsAdapter extends
        RecyclerView.Adapter<PositionsAdapter.ViewHolder> {

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = inflater.inflate(R.layout.positions_item, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Position position = positions.get(i);
        TextView symbolTextView = holder.symbolTextView;
        symbolTextView.setText(position.getSymbol());
        TextView differentialTextView = holder.differentialTextView;
        @SuppressLint("DefaultLocale") String differentialText = String.format("%.2f", position.getDifferential()) + "%";
        differentialTextView.setText(differentialText);

    }

    @Override
    public int getItemCount() {
        return positions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView symbolTextView;
        public TextView differentialTextView;
        public Button removeButton;

        @SuppressLint("NotifyDataSetChanged")
        public ViewHolder(View itemView) {
            super(itemView);
            symbolTextView = itemView.findViewById(R.id.symbol_text_view);
            differentialTextView = itemView.findViewById(R.id.differential_text_view);
            removeButton = itemView.findViewById(R.id.remove_position_button);
            removeButton.setOnClickListener(removeButton -> {
                Optional<Position> position = positions.stream().filter(position1 -> position1.getSymbol() == symbolTextView.getText()).findFirst();
                position.ifPresent(positions::remove);
                notifyDataSetChanged();
            });
        }
    }

    private final List<Position> positions;

    // Pass in the position array into the constructor
    public PositionsAdapter(List<Position> positionsSet) {
        this.positions = positionsSet;
    }
}

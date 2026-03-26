package com.example.findhobbies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.Hobby;

import java.util.List;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder> {

    private List<Hobby> hobbies;
    private OnHobbyRemoveListener removeListener;

    // Interface pour notifier la suppression d'un hobby
    public interface OnHobbyRemoveListener {
        void onHobbyRemoved(Hobby hobby);
    }

    public HobbyAdapter(List<Hobby> hobbies, OnHobbyRemoveListener removeListener) {
        this.hobbies = hobbies;
        this.removeListener = removeListener;
    }

    @NonNull
    @Override
    public HobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hobby, parent, false);
        return new HobbyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HobbyViewHolder holder, int position) {
        Hobby hobby = hobbies.get(position);
        holder.tvName.setText(hobby.getName());
        holder.cbHobby.setChecked(hobby.isSelected());


        switch (hobby.getName()) {
            case "Musique": holder.ivHobby.setImageResource(R.drawable.music); break;
            case "Sport": holder.ivHobby.setImageResource(R.drawable.sport); break;

            case "Voyage": holder.ivHobby.setImageResource(R.drawable.voyage); break;
            case "Cuisine": holder.ivHobby.setImageResource(R.drawable.cooking); break;
            case "Dessin": holder.ivHobby.setImageResource(R.drawable.art); break;
            case "Programmation": holder.ivHobby.setImageResource(R.drawable.tech); break;


            case "Photographie": holder.ivHobby.setImageResource(R.drawable.photography); break;
            default: holder.ivHobby.setImageResource(R.drawable.default_ic); break;
        }

        // Click sur la checkbox
        holder.cbHobby.setOnClickListener(v -> {
            boolean isChecked = holder.cbHobby.isChecked();
            hobby.setSelected(isChecked);

            if (!isChecked && removeListener != null) {
                removeListener.onHobbyRemoved(hobby);
            }
        });

        // Click sur l'item pour toggle
        holder.itemView.setOnClickListener(v -> {
            boolean newState = !holder.cbHobby.isChecked();
            holder.cbHobby.setChecked(newState);
            hobby.setSelected(newState);

            if (!newState && removeListener != null) {
                removeListener.onHobbyRemoved(hobby);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hobbies.size();
    }

    static class HobbyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivHobby;
        TextView tvName;
        CheckBox cbHobby;

        public HobbyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivHobby = itemView.findViewById(R.id.ivHobby);
            tvName = itemView.findViewById(R.id.tvHobbyName);
            cbHobby = itemView.findViewById(R.id.cbHobby);
        }
    }
}

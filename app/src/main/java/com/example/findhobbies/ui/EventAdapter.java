package com.example.findhobbies.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.entity.Event;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> events;
    private OnEventClickListener listener;

    public interface OnEventClickListener {
        void onEventClick(Event event);
        void onJoinEventClick(Event event);
        void onShowMapClick(Event event);
    }

    public EventAdapter(List<Event> events) {
        this.events = events;
    }

    public void setOnEventClickListener(OnEventClickListener listener) {
        this.listener = listener;
    }

    public void updateList(List<Event> newEvents) {
        this.events = newEvents;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_card, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onEventClick(event);
        });

        holder.btnJoinEvent.setOnClickListener(v -> {
            if (listener != null) listener.onJoinEventClick(event);
        });

        holder.btnShowMap.setOnClickListener(v -> {
            if (listener != null) listener.onShowMapClick(event);
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    static class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEventName, tvEventLocation, tvEventDate, tvParticipants, tvCategory, tvPrice;
        private Button btnJoinEvent, btnShowMap;
        private ImageView ivEventIcon;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            tvEventDate = itemView.findViewById(R.id.tvEventDate);
            tvParticipants = itemView.findViewById(R.id.tvParticipants);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnJoinEvent = itemView.findViewById(R.id.btnJoinEvent);
            btnShowMap = itemView.findViewById(R.id.btnShowMap);
            ivEventIcon = itemView.findViewById(R.id.ivEventIcon);
        }

        public void bind(Event event) {
            tvEventName.setText(event.getName());
            tvEventLocation.setText(event.getLocation());
            tvEventDate.setText(formatDate(event.getDate()));
            tvParticipants.setText("50+");
            tvCategory.setText(event.getCategory());
            tvPrice.setText("Gratuit");
            setEventIcon(event.getName());
        }

        private String formatDate(String date) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());
                Date parsedDate = inputFormat.parse(date);
                return outputFormat.format(parsedDate);
            } catch (Exception e) {
                return date;
            }
        }

        private void setEventIcon(String eventName) {
            int iconRes = R.drawable.ic_event;
            String name = eventName.toLowerCase();
            if (name.contains("concert") || name.contains("jazz")) iconRes = R.drawable.ic_music;
            else if (name.contains("marathon")) iconRes = R.drawable.ic_sport;
            else if (name.contains("exposition") || name.contains("art")) iconRes = R.drawable.ic_art;
            else if (name.contains("festival") || name.contains("cinéma")) iconRes = R.drawable.ic_movie;
            else if (name.contains("conférence")) iconRes = R.drawable.ic_education;
            ivEventIcon.setImageResource(iconRes);
        }
    }
}

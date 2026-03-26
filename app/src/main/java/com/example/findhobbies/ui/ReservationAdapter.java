package com.example.findhobbies.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.findhobbies.R;
import com.example.findhobbies.db.HobbyDatabase;
import com.example.findhobbies.entity.Event;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class ReservationAdapter extends RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder> {

    private final List<EventRegistrationWithName> reservations;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
    private final OnCancelClickListener cancelClickListener;
    private final HobbyDatabase db;

    // Interface pour le clic sur le bouton Annuler
    public interface OnCancelClickListener {
        void onCancelClick(EventRegistrationWithName item, int position);
    }

    public OnCancelClickListener getCancelClickListener() {
        return cancelClickListener;
    }

    public ReservationAdapter(List<EventRegistrationWithName> reservations,
                              OnCancelClickListener listener,
                              HobbyDatabase db) {
        this.reservations = reservations;
        this.cancelClickListener = listener;
        this.db = db;

        // Tri automatique par date de l'événement (les plus proches en haut)
        Collections.sort(this.reservations, new Comparator<EventRegistrationWithName>() {
            @Override
            public int compare(EventRegistrationWithName o1, EventRegistrationWithName o2) {
                Event e1 = db.eventDao().getEventById(o1.registration.getEventId());
                Event e2 = db.eventDao().getEventById(o2.registration.getEventId());
                if (e1 == null || e1.getDate() == null) return 1;
                if (e2 == null || e2.getDate() == null) return -1;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    long t1 = sdf.parse(e1.getDate()).getTime();
                    long t2 = sdf.parse(e2.getDate()).getTime();
                    return Long.compare(t1, t2);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                    return 0;
                }
            }
        });
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        EventRegistrationWithName item = reservations.get(position);

        holder.tvEventName.setText(item.eventName != null ? item.eventName : "Événement");
        holder.tvFullName.setText(item.registration.getFirstName() + " " + item.registration.getLastName());
        holder.tvAge.setText(item.registration.getAge() != null ? "Âge: " + item.registration.getAge() : "");
        holder.tvInterests.setText(item.registration.getInterests() != null ? "Intérêts: " + item.registration.getInterests() : "");
        holder.tvDate.setText(dateFormat.format(item.registration.getCreatedAt()));

        // Calcul des jours restants
        Event event = db.eventDao().getEventById(item.registration.getEventId());
        if (event != null && event.getDate() != null) {
            holder.tvDaysLeft.setText(getDaysLeft(event.getDate()));
        } else {
            holder.tvDaysLeft.setText("");
        }

        // Clic sur Annuler
        holder.btnCancel.setOnClickListener(v -> {
            if (cancelClickListener != null) {
                cancelClickListener.onCancelClick(item, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservations.size();
    }

    // ViewHolder
    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvFullName, tvAge, tvInterests, tvDate, tvDaysLeft;
        Button btnCancel;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tvEventName);
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvInterests = itemView.findViewById(R.id.tvInterests);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvDaysLeft = itemView.findViewById(R.id.tvDaysLeft);
            btnCancel = itemView.findViewById(R.id.btnCancelReservation);
        }
    }

    // Méthode pour calculer les jours restants
    private String getDaysLeft(String eventDateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            long eventTime = sdf.parse(eventDateString).getTime();
            long now = System.currentTimeMillis();
            long diff = eventTime - now;
            long days = diff / (1000 * 60 * 60 * 24);

            if (days > 1) return days + " jours restants";
            else if (days == 1) return "1 jour restant";
            else if (days == 0) return "Aujourd'hui";
            else return "Événement passé";
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}

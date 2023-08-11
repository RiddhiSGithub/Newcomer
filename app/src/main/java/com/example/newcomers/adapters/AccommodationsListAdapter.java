package com.example.newcomers.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newcomers.beans.Accommodation;
import com.example.newcomers.databinding.ItemAccommodationBinding;
import com.example.newcomers.interfaces.AccommodationItemListeners;

import java.util.ArrayList;

public class AccommodationsListAdapter extends RecyclerView.Adapter<AccommodationsListAdapter.MyViewHolder> {

    ArrayList<Accommodation> accommodations = new ArrayList<>();
    AccommodationItemListeners listeners;

    public AccommodationsListAdapter(AccommodationItemListeners listeners) {
        this.listeners = listeners;
    }

    // --- set accommodations list
    public void setList(ArrayList<Accommodation> accommodations) {
        this.accommodations = accommodations;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAccommodationBinding binding = ItemAccommodationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(accommodations.get(position));
    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final ItemAccommodationBinding binding;

        MyViewHolder(@NonNull ItemAccommodationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        // --- bind data with item view
        void bind(Accommodation accommodation) {
            binding.txtTitle.setText(accommodation.getPropertyType());
            binding.txtSubTitle.setText(accommodation.getCity() + ", " + accommodation.getProvince());
            binding.txtOtherDetails.setText(accommodation.getPostalCode());
            binding.txtRent.setText("$" + accommodation.getRent());

            // --- set item click listener
            binding.getRoot().setOnClickListener(v -> {
                listeners.onItemClick(accommodation);
            });
        }
    }
}

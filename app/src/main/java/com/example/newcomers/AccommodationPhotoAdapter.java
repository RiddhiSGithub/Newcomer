package com.example.newcomers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.newcomers.databinding.ItemPhotoBigBinding;

import java.util.ArrayList;

public class AccommodationPhotoAdapter extends RecyclerView.Adapter<AccommodationPhotoAdapter.MyViewHolder> {

    ArrayList<String> images = new ArrayList<>();

    AccommodationPhotoAdapter(ArrayList<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPhotoBigBinding binding = ItemPhotoBigBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String imgUrl = images.get(position);
        holder.bind(imgUrl);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final ItemPhotoBigBinding binding;

        MyViewHolder(ItemPhotoBigBinding itemPhotoBigBinding) {
            super(itemPhotoBigBinding.getRoot());
            binding = itemPhotoBigBinding;
        }

        void bind(String imgUrl) {
            Glide.with(binding.getRoot().getContext()).load(imgUrl).into(binding.imageView);
        }
    }
}

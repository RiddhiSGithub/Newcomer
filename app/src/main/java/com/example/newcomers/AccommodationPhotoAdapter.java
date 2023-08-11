package com.example.newcomers;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.bumptech.glide.Glide;
import com.example.newcomers.databinding.ItemPhotoBigBinding;
import com.example.newcomers.databinding.ItemPhotoBinding;

import java.util.ArrayList;
import java.util.List;

public class AccommodationPhotoAdapter extends RecyclerView.Adapter<AccommodationPhotoAdapter.MyViewHolder> {

    ArrayList<String> imageURLs = new ArrayList<>();
    ArrayList<Uri> imageUris = new ArrayList<>();
    String type; // it can be "postPage" or "detailsPage" as this same adapter is being used at both pages

    AccommodationPhotoAdapter(String type) {
        this.type = type;
    }

    <T> void setList(List<T> images) {
        if (images == null || images.isEmpty()) {
            imageUris = null;
            imageURLs = null;
        } else if (images.get(0) instanceof String) {
            imageURLs = (ArrayList<String>) images;
            imageUris = null;
        } else if (images.get(0) instanceof Uri) {
            imageUris = (ArrayList<Uri>) images;
            imageURLs = null;
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (this.type == "postPage") {
            ItemPhotoBinding binding = ItemPhotoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(binding, null);
        } else {
            ItemPhotoBigBinding binding = ItemPhotoBigBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new MyViewHolder(null, binding);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ArrayList<?> images = type == "postPage" ? imageUris : imageURLs;
        holder.bind(images.get(position));
    }

    @Override
    public int getItemCount() {
        return type == "postPage" ? imageUris.size() : imageURLs.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        final ItemPhotoBigBinding photoBigBinding;
        final ItemPhotoBinding photoBinding;

        MyViewHolder(@Nullable ItemPhotoBinding itemPhotoBinding, @Nullable ItemPhotoBigBinding itemPhotoBigBinding) {
            super((itemPhotoBinding != null ? itemPhotoBinding : itemPhotoBigBinding).getRoot());
            photoBinding = itemPhotoBinding;
            photoBigBinding = itemPhotoBigBinding;
        }

        <T> void bind(T imgData) {
            ViewBinding binding = photoBinding != null ? photoBinding : photoBigBinding;
            ImageView imageView = photoBinding != null ? photoBinding.imageView : photoBigBinding.imageView;

            if (imgData instanceof Uri || imgData instanceof String)
                Glide.with(binding.getRoot().getContext()).load(imgData).into(imageView);
        }
    }
}

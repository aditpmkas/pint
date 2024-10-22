package com.example.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewHolder> {

    private int[] imageResources; // Original image resources
    private String[] texts; // Original texts
    private List<Integer> filteredImageResources; // Filtered list of image resources
    private List<String> filteredTexts;

    // Constructor to pass image resources and texts to the adapter
    public ViewPagerAdapter(int[] imageResources, String[] texts) {
        this.imageResources = imageResources;
        this.texts = texts;
        this.filteredImageResources = new ArrayList<>();
        this.filteredTexts = new ArrayList<>();
        for (int image : imageResources) {
            filteredImageResources.add(image);
        }
        for (String text : texts) {
            filteredTexts.add(text);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each page (page_with_image.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_with_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Set the image resource to the ImageView in the page
        holder.imageView.setImageResource(imageResources[position]);
        // Set the corresponding text to the TextView
        holder.textView.setText(texts[position]);
    }

    @Override
    public int getItemCount() {
        return imageResources.length;
    }

    public void filter(String query) {
        filteredImageResources.clear();
        filteredTexts.clear();

        if (query.isEmpty()) {
            for (int image : imageResources) {
                filteredImageResources.add(image);
            }
            for (String text : texts) {
                filteredTexts.add(text);
            }
        } else {
            for (int i = 0; i < texts.length; i++) {
                if (texts[i].toLowerCase().contains(query.toLowerCase())) {
                    filteredImageResources.add(imageResources[i]);
                    filteredTexts.add(texts[i]);
                }
            }
        }
        notifyDataSetChanged(); // Refresh the adapter
    }
    // ViewHolder class to bind ImageView and TextView
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pageImage);
            textView = itemView.findViewById(R.id.pageText);
        }
    }
}

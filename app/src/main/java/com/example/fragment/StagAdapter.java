package com.example.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StagAdapter extends RecyclerView.Adapter<StagAdapter.ViewHolder> {
    private List<Photos> mList;  // Using List<Photos>
    Context context;

    // Constructor to receive List<Photos>
    public StagAdapter(Context context, List<Photos> mList) {
        this.context = context;
        this.mList = mList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        VideoView videoView; // Add VideoView

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);
            videoView = itemView.findViewById(R.id.video_view); // Initialize VideoView
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stag_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photos photo = mList.get(position);

        // Handle image or video
        if (photo.getMediaType() == Photos.TYPE_IMAGE) {
            holder.imageView.setImageResource(photo.getMediaResource());
            holder.imageView.setVisibility(View.VISIBLE); // Show ImageView
        } else if (photo.getMediaType() == Photos.TYPE_VIDEO) {
            holder.videoView.setVideoURI(Uri.parse("android.resource://" + context.getPackageName() + "/" + photo.getMediaResource()));
            holder.videoView.start();                     // Start video playback
            holder.videoView.setVisibility(View.VISIBLE);  // Show VideoView
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent;
            if (photo.getMediaType() == Photos.TYPE_VIDEO) {
                intent = new Intent(context, DetailVideoActivity.class);
                intent.putExtra("media_resource", photo.getMediaResource());
            } else {
                intent = new Intent(context, DetailImageActivity.class);
                intent.putExtra("image_resource", photo.getMediaResource());
                intent.putExtra("keyword", photo.getKeyword());
            }
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
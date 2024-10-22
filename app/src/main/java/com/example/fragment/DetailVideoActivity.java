package com.example.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailVideoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_video); // Ensure this is the correct layout file

        VideoView videoView = findViewById(R.id.video_view);
        int videoResourceId = getIntent().getIntExtra("media_resource", 0);

        // Prepare the video URI
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + videoResourceId);
        videoView.setVideoURI(videoUri);

        // Set up MediaController for play/pause
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        // Start the video
        videoView.start();
    }
}
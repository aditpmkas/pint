package com.example.fragment;

import android.os.Bundle;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Set up EdgeToEdge (if applicable)
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        // Create the list of photos
        List<Photos> mList = new ArrayList<>();
        mList.add(new Photos(R.drawable.image1, "flower"));
        mList.add(new Photos(R.drawable.image2, "cat"));
        mList.add(new Photos(R.drawable.image3, "moon"));
        mList.add(new Photos(R.drawable.image4, "meme"));
        mList.add(new Photos(R.drawable.image5, "cat"));
        mList.add(new Photos(R.drawable.image6, "cat"));
        mList.add(new Photos(R.drawable.image7, "cat"));
        mList.add(new Photos(R.drawable.image8, "moon"));
        mList.add(new Photos(R.drawable.image9, "flower"));
        mList.add(new Photos(R.drawable.image10, "flower"));

        // Set up StaggeredGridLayoutManager
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        // Set the item decoration for spacing
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));

        // Initialize and set the adapter
        StagAdapter stagAdapter = new StagAdapter(getContext(), mList);
        recyclerView.setAdapter(stagAdapter);

        // Initialize BottomNavigationVie

        return view; // Return the view to display the UI
    }
}

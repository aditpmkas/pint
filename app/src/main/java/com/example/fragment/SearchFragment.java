package com.example.fragment;

import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SearchFragment extends Fragment {
    private ViewPager2 viewPager2; // First ViewPager2 (Vertical)
    private ViewPager2 horizontalViewPager2; // Second ViewPager2 (Horizontal)
    private ViewPagerAdapter verticalAdapter; // Adapter for vertical ViewPager2
    private SearchView searchView;

    // Image resources and texts for both ViewPager2
    private int[] verticalImages = {R.drawable.imagesearch1, R.drawable.imagesearch2, R.drawable.imagesearch3}; // Top ViewPager Images
    private String[] verticalTexts = {"The Beautiful Rose Flower", "The Aesthetic Full Moon", "The Beautiful Flower"}; // Corresponding texts

    private int[] horizontalImages = {R.drawable.imagesearch1, R.drawable.imagesearch2}; // Bottom ViewPager Images
    private String[] horizontalTexts = {"The Beautiful Flower", "The Beutiful Moon"}; // Corresponding texts

    private TextView extraContent; // Declare TextView

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        // Initialize the UI components
        viewPager2 = view.findViewById(R.id.viewPager);
        searchView = view.findViewById(R.id.searchView);
        horizontalViewPager2 = view.findViewById(R.id.horizontalViewPager);

        // Set up the adapter for the vertical ViewPager2
        verticalAdapter = new ViewPagerAdapter(verticalImages, verticalTexts);
        viewPager2.setAdapter(verticalAdapter);

        // Set up the SearchView listener
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Handle search query submission
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Handle search text changes
                return false;
            }
        });

        // Set up the horizontal ViewPager2 (Horizontal)
        ViewPagerAdapter horizontalAdapter = new ViewPagerAdapter(horizontalImages, horizontalTexts);
        horizontalViewPager2.setAdapter(horizontalAdapter);
        horizontalViewPager2.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL); // Make it scroll horizontally

        return view; // Return the inflated view
    }
}

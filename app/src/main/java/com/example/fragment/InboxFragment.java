package com.example.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class InboxFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        // Step 1: Initialize RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.inboxRecyclerView); // Use view to find RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use getContext()

        // Step 2: Create dummy data for the RecyclerView
        List<InboxMessage> inboxMessages = new ArrayList<>();
        inboxMessages.add(new InboxMessage("Jessica", "How was your day?", R.drawable.pict1));
        inboxMessages.add(new InboxMessage("John", "Are we meeting today?", R.drawable.pict2));
        inboxMessages.add(new InboxMessage("Emily", "Don't forget the meeting.", R.drawable.pict3));
        inboxMessages.add(new InboxMessage("Michael", "Happy birthday!", R.drawable.pict4));
        inboxMessages.add(new InboxMessage("Sarah", "Let's catch up soon.", R.drawable.pict5));

        // Log the number of messages
        Log.d("InboxFragment", "Number of messages: " + inboxMessages.size());

        // Step 3: Initialize and set the adapter
        InboxAdapter adapter = new InboxAdapter(getContext(), inboxMessages); // Use getContext()
        recyclerView.setAdapter(adapter);  // Attach the adapter

        return view; // Return the inflated view
    }
}

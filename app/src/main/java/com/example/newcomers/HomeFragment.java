package com.example.newcomers;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomers.adapters.AccommodationsListAdapter;
import com.example.newcomers.beans.Accommodation;
import com.example.newcomers.databinding.FragmentHomeBinding;
import com.example.newcomers.databinding.FragmentRideListBinding;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    
    FragmentHomeBinding homeBinding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Accommodation> accommodations;


    public HomeFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_home, container, false);
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = homeBinding.getRoot();
        init();
        return view;

    }

    private void init() {
        db.collection("accommodations").limit(3).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            for (QueryDocumentSnapshot document : task.getResult()) {
                Accommodation accommodation = document.toObject(Accommodation.class);
                accommodations.add(accommodation);
            }
            Log.i("--------", "init: get list size:"+accommodations.size());

        }).addOnFailureListener(e -> {
            Log.w("Error", "Error getting accommodations.", e);
        });
    }

    void loadAccommodations() {
        // --- load accommodations list

    }
}
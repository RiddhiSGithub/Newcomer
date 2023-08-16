package com.example.newcomers;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomers.adapters.AccommodationsListAdapter;
import com.example.newcomers.beans.Accommodation;
import com.example.newcomers.beans.Order;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.databinding.FragmentHomeBinding;
import com.example.newcomers.databinding.FragmentRideListBinding;
import com.example.newcomers.interfaces.AccommodationItemListeners;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private static final String TAG = "__HomeFragment__";
    FragmentHomeBinding homeBinding;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayList<Accommodation> acclist = new ArrayList<>();
    private ArrayList<Trip> list = new ArrayList<>();


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
        //init();


        MaterialToolbar actionBar = getActionBar();
        actionBar.getMenu().clear(); // clear previous menu items


        initACC();
        reloadData();
        return view;

    }

    private void initACC() {
        db.collection("accommodations")
                .limit(3)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            acclist.clear();
                            for (QueryDocumentSnapshot doc: task.getResult()) {
                                Log.d(TAG, "onComplete: docs["+doc.toString()+"]");
                                Accommodation acc = doc.toObject(Accommodation.class);
                                acclist.add(acc);
                            }
                            Log.d(TAG, "onComplete ----**: "+acclist.size());
                            showAccItems();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e.getMessage()+"----"+e.toString());
                    }
                });



    }

    private void showAccItems() {
        AccommodationsListAdapter ala = new AccommodationsListAdapter(new AccommodationItemListeners() {
            @Override
            public void onItemClick(Accommodation accommodation) {
                Log.d(TAG, "onItemClick: item clicked");
            }
        });
        Log.d(TAG, "set list: "+ acclist.size());
        ala.setList(acclist);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        homeBinding.rvAccommodations.setLayoutManager(layoutManager);
        homeBinding.rvAccommodations.setAdapter(ala);
        ala.setList(acclist);
    }

    private void init() {
        db.collection("accommodations").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            for (QueryDocumentSnapshot document : task.getResult()) {
                Accommodation accommodation = document.toObject(Accommodation.class);
                //accommodations.add(accommodation);
            }

        }).addOnFailureListener(e -> {
            Log.w("Error", "Error getting accommodations.", e);
        });
    }

    private void reloadData() {
        String userId = "XXXX-OOOO";
        FirebaseFirestore.getInstance().collection("Trips")
                .limit(4)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Trip trip = document.toObject(Trip.class);
                                trip.setId(document.getId());
                                Log.d(TAG, trip.toString());
                                list.add(trip);
                            }
                            //bindAdapter();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    void loadAccommodations() {
        // --- load accommodations list

    }


    // --- helper method to find actionbar view
    MaterialToolbar getActionBar() {
        MaterialToolbar actionBar = null;
        ViewGroup viewGroup = getActivity().findViewById(R.id.actionBar);
        if (viewGroup != null)
            actionBar = (MaterialToolbar) viewGroup.findViewById(R.id.materialToolbar);
        return actionBar;
    }
}
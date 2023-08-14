package com.example.newcomers.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.example.newcomers.R;
import com.example.newcomers.activities.AddRideActivity;
import com.example.newcomers.activities.RideOrderListActivity;
import com.example.newcomers.adapters.RideListAdapter;
import com.example.newcomers.adapters.TripListAdapter;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.databinding.FragmentRideListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class RideListFragment extends Fragment implements Toolbar.OnMenuItemClickListener {
    private static String TAG="RideListFragment";
    FragmentRideListBinding rideListBinding;
    private TripListAdapter listAdapter;
    private List<Trip> eList = new ArrayList<>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public RideListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ride_list, container, false);
        rideListBinding = FragmentRideListBinding.inflate(inflater, container, false);
        View view = rideListBinding.getRoot();
        initRideListFragment();
        return view;
    }

    /**
     * Called when the fragment is visible to the user and actively running.
     * This is generally
     * tied to {@link Activity#onResume() Activity.onResume} of the containing
     * Activity's lifecycle.
     */
    @Override
    public void onResume() {
        super.onResume();
        this.getTripList();
    }

    // --- helper method to find actionbar view
    MaterialToolbar getActionBar() {
        MaterialToolbar actionBar = null;
        ViewGroup viewGroup = getActivity().findViewById(R.id.actionBar);
        if (viewGroup != null)
            actionBar = (MaterialToolbar) viewGroup.findViewById(R.id.materialToolbar);
        return actionBar;
    }

    /**
     * init Ride List Fragment
     */
    private void initRideListFragment() {
        // --- set navigation menu options
        MaterialToolbar actionBar = getActionBar();
        actionBar.getMenu().clear(); // clear previous menu items
        if (actionBar != null) {
            actionBar.inflateMenu(R.menu.rideaction_menu);
            actionBar.setOnMenuItemClickListener(this);
        }
        //getTripList();
    }

    /**
     * get Trip List From Firebase
     */
    private void getTripList() {
        //db.clearPersistence();
        Source source = Source.SERVER;
        db.collection("Trips")
                .get(source)
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            eList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Trip trip = document.toObject(Trip.class);
                                trip.setId(document.getId());
                                Log.d(TAG, trip.toString());
                                eList.add(trip);
                            }
                            bindAdapter();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void bindAdapter() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rideListBinding.rcView.setLayoutManager(layoutManager);
        listAdapter = new TripListAdapter(eList, getContext());
        rideListBinding.rcView.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();

//        RideListAdapter rideListAdapter = new RideListAdapter(this.getActivity(),eList);
//        rideListBinding.listView.setAdapter(rideListAdapter);
//        rideListAdapter.notifyDataSetChanged();
    }

    /**
     * This method will be invoked when a menu item is clicked if the item itself did
     * not already handle the event.
     *
     * @param item {@link MenuItem} that was clicked
     * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if(item.getItemId() == R.id.ridePlus)
        {
            Intent intent = new Intent(this.getActivity(), AddRideActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.rideOrder) {
            Intent intent = new Intent(this.getActivity(), RideOrderListActivity.class);
            startActivity(intent);
        }
        return false;
    }
}
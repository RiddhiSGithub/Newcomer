package com.example.newcomers.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.example.newcomers.R;
import com.example.newcomers.adapters.RideOrderListAdapter;
import com.example.newcomers.beans.Order;
import com.example.newcomers.beans.Trip;
import com.example.newcomers.databinding.ActivityRideOrderListBinding;
import com.example.newcomers.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;

/**
 * Ride Order List Activity
 * @Author: Meng
 * @Date: Aug 14 2023
 */
public class RideOrderListActivity extends AppCompatActivity {
    private static final String TAG = "RideOrderListActivity";
    ActivityRideOrderListBinding listBinding;
    List<Order> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listBinding = ActivityRideOrderListBinding.inflate(getLayoutInflater());
        View view = listBinding.getRoot();
        initRideOrderList();
        setContentView(view);
    }

    /**
     * init Ride Order List
     */
    private void initRideOrderList() {
        // --- set back button on toolbar
        listBinding.rideOrderActionBar.materialToolbar.setNavigationIcon(R.drawable.baseline_keyboard_backspace_24);
        listBinding.rideOrderActionBar.materialToolbar.setNavigationOnClickListener(view -> finish());
        listBinding.rideOrderActionBar.materialToolbar.setTitle(R.string.my_order_list);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        this.reloadData();
    }

    /**
     * reload Data
     */
    private void reloadData() {
        String userId = Utils.getCurrentUserID();
        FirebaseFirestore.getInstance().collection("Orders")
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .whereEqualTo("orderUserID",userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            list.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                Order order = document.toObject(Order.class);
                                order.setId(document.getId());
                                Log.d(TAG, order.toString());
                                list.add(order);
                            }
                            bindAdapter();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

    }

    /**
     * bind Adapter
     */
    private void bindAdapter() {
        ListAdapter adapter = new RideOrderListAdapter(list);
        listBinding.lvRideOrder.setAdapter(adapter);
    }
}
package com.example.newcomers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomers.adapters.AccommodationsListAdapter;
import com.example.newcomers.beans.Accommodation;
import com.example.newcomers.databinding.FragmentAccommodationsListBinding;
import com.example.newcomers.interfaces.AccommodationItemListeners;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationsList extends Fragment implements Toolbar.OnMenuItemClickListener, AccommodationItemListeners {

    FragmentAccommodationsListBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Accommodation> accommodations = new ArrayList<>();
    ArrayList<Accommodation> filteredAccommodations = new ArrayList<>();
    AccommodationsListAdapter accommodationsListAdapter;
    final int REQUEST_CODE_POST_ACCOMMODATION = 3244;

    public AccommodationsList() {
    }

    public static AccommodationsList newInstance() {
        return new AccommodationsList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccommodationsListBinding.inflate(inflater, container, false);

        initViews();
        initData();

        return binding.getRoot();
    }

    // --- helper method to find actionbar view
    MaterialToolbar getActionBar() {
        MaterialToolbar actionBar = null;
        ViewGroup viewGroup = getActivity().findViewById(R.id.actionBar);
        if (viewGroup != null)
            actionBar = (MaterialToolbar) viewGroup.findViewById(R.id.materialToolbar);
        return actionBar;
    }

    // --- initialize UI elements
    void initViews() {
        // --- set navigation menu options
        MaterialToolbar actionBar = getActionBar();
        actionBar.getMenu().clear(); // clear previous menu items
        if (actionBar != null) {
            actionBar.inflateMenu(R.menu.accommodations_menu);
            actionBar.setOnMenuItemClickListener(this);
        }

        // --- setup recycler view
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        accommodationsListAdapter = new AccommodationsListAdapter(this);
        binding.rvAccommodations.setLayoutManager(layoutManager);
        binding.rvAccommodations.setAdapter(accommodationsListAdapter);

        // --- set listener on search field change
        binding.edtPostalCode.addTextChangedListener(postalCodeFilterChange);
    }

    // --- postal code filter change listener
    TextWatcher postalCodeFilterChange = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            applyFilters();
        }
    };

    @Override
    public void onItemClick(Accommodation accommodation) {
        Intent intent = new Intent(getActivity(), AccommodationDetailsActivity.class);
        intent.putExtra("accommodation", accommodation);
        startActivity(intent);
    }

    // --- apply filters on accommodations list
    void applyFilters() {
        String filterByPostalCode = binding.edtPostalCode.getText().toString();
        filteredAccommodations.clear();
        for (Accommodation accommodation : accommodations) {
            if (filterByPostalCode.isEmpty() || accommodation.getPostalCode().toLowerCase().contains(filterByPostalCode.toLowerCase()))
                filteredAccommodations.add(accommodation);
        }

        // --- update recycler view list
        accommodationsListAdapter.setList(filteredAccommodations);

        // --- refresh list related views (eg. loader, list, empty view)
        refreshListViews();
    }

    // --- refresh list related views (eg. loader, list, empty view)
    void refreshListViews() {
        binding.layoutListLoader.setVisibility(View.GONE);

        // --- hide/show empty view
        if (filteredAccommodations.isEmpty()) binding.layoutEmptyList.setVisibility(View.VISIBLE);
        else binding.layoutEmptyList.setVisibility(View.GONE);

        // --- hide/show list view
        if (filteredAccommodations.isEmpty()) binding.rvAccommodations.setVisibility(View.GONE);
        else binding.rvAccommodations.setVisibility(View.VISIBLE);
    }

    // --- load accommodations list
    void loadAccommodations() {
        // --- load accommodations list
        db.collection("accommodations").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) return;
            for (QueryDocumentSnapshot document : task.getResult()) {
                Accommodation accommodation = document.toObject(Accommodation.class);
                accommodations.add(accommodation);
            }

            // --- apply filters
            applyFilters();
        }).addOnFailureListener(e -> {
            Log.w("Error", "Error getting accommodations.", e);
        });
    }

    // --- initialize data (eg. load data from server on UI)
    void initData() {
        // --- load accommodations list
        loadAccommodations();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.postAccommodation) {
            Intent intent = new Intent(getActivity(), PostAccommodation.class);
            startActivityForResult(intent, REQUEST_CODE_POST_ACCOMMODATION);
            return true;
        }

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_POST_ACCOMMODATION && resultCode == Activity.RESULT_OK) {
            accommodations.clear();
            loadAccommodations(); // --- refresh list
        }
    }
}
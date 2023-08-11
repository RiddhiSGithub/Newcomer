package com.example.newcomers;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomers.databinding.FragmentAccommodationsListBinding;
import com.google.android.material.appbar.MaterialToolbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationsList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationsList extends Fragment implements Toolbar.OnMenuItemClickListener {

    FragmentAccommodationsListBinding binding;

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
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.postAccommodation) {
            Intent intent = new Intent(getActivity(), PostAccommodation.class);
            startActivity(intent);
            return true;
        }

        return false;
    }
}
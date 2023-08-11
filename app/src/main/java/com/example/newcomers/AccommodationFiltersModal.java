package com.example.newcomers;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.newcomers.beans.AccommodationFilters;
import com.example.newcomers.databinding.FragmentAccommodationFiltersModalBinding;
import com.example.newcomers.interfaces.AccommodationListeners;
import com.example.newcomers.utils.Utils;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFiltersModal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFiltersModal extends DialogFragment implements View.OnClickListener {

    FragmentAccommodationFiltersModalBinding binding;
    Calendar startDate;
    AccommodationListeners accommodationListeners;

    public AccommodationFiltersModal() {
    }

    public static AccommodationFiltersModal newInstance(@Nullable AccommodationFilters filters) {
        AccommodationFiltersModal accommodationFiltersModal = new AccommodationFiltersModal();

        if (filters != null) {
            Bundle args = new Bundle();
            args.putSerializable("filters", filters);
            accommodationFiltersModal.setArguments(args);
        }

        return accommodationFiltersModal;
    }

    void setListener(AccommodationListeners accommodationListeners) {
        this.accommodationListeners = accommodationListeners;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationFiltersModalBinding.inflate(inflater, container, false);

        initViews();
        initData();

        return binding.getRoot();
    }

    // --- initialize UI elements
    void initViews() {
        // --- set click listeners
        binding.btnClose.setOnClickListener(this);
        binding.edtStartDate.setOnClickListener(this);
        binding.btnApplyFilters.setOnClickListener(this);
        binding.btnResetFilters.setOnClickListener(this);
    }

    // --- initialize data
    void initData() {
        // --- set default values
        Bundle arguments = getArguments();
        if (arguments == null) return;
        Serializable filtersData = arguments.getSerializable("filters");
        if (filtersData == null) return;
        AccommodationFilters filters = (AccommodationFilters) filtersData;

        if (filters.startDate != null) {
            startDate = Calendar.getInstance(TimeZone.getTimeZone("utc"));
            startDate.setTimeInMillis(filters.startDate);
            binding.edtStartDate.setText(Utils.formatDate(startDate));
        }

        if (filters.noOfBedrooms != null)
            binding.edtBedrooms.setText(filters.noOfBedrooms.toString());

        if (filters.noOfBathrooms != null)
            binding.edtBathrooms.setText(filters.noOfBathrooms.toString());

        if (filters.minRent != null && filters.maxRent != null)
            binding.rangeSliderRent.setValues(filters.minRent.floatValue(), filters.maxRent.floatValue());
    }

    @Override
    public void onClick(View view) {
        // --- close modal btn click handler
        if (view == binding.btnClose) {
            dismiss();
            return;
        }

        // --- start date input field click handler
        if (view == binding.edtStartDate) {
            Utils.showMaterialDatePickerDialog(getParentFragmentManager(), System.currentTimeMillis(), startDate != null ? startDate.getTimeInMillis() : 0, new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeZone(TimeZone.getTimeZone("utc"));
                    calendar.setTimeInMillis((Long) selection);
                    startDate = calendar;

                    binding.edtStartDate.setText(Utils.formatDate(startDate));
                }
            });
            return;
        }

        // --- reset filters btn click handler
        if (view == binding.btnResetFilters) {
            startDate = null;
            binding.edtStartDate.setText("");
            binding.edtBedrooms.setText("");
            binding.edtBathrooms.setText("");
            binding.rangeSliderRent.setValues(binding.rangeSliderRent.getValueFrom(), binding.rangeSliderRent.getValueTo());
            return;
        }

        // --- apply filters btn click handler
        if (view == binding.btnApplyFilters) {
            Long startDate = this.startDate != null ? this.startDate.getTimeInMillis() : null;
            Integer noOfBedrooms = Utils.toInteger(binding.edtBedrooms.getText().toString());
            Double noOfBathrooms = Utils.toDouble(binding.edtBathrooms.getText().toString());
            List<Float> rentValues = binding.rangeSliderRent.getValues();

            AccommodationFilters accommodationFilters = new AccommodationFilters(startDate, noOfBedrooms, noOfBathrooms, rentValues.get(0), rentValues.get(1));
            accommodationListeners.onFiltersUpdated(accommodationFilters);
            dismiss();
        }
    }
}
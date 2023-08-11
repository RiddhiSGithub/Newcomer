package com.example.newcomers.beans;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class AccommodationFilters implements Serializable {
    public Long startDate;
    public Integer noOfBedrooms;
    public Double noOfBathrooms;
    public Float minRent;
    public Float maxRent;

    public AccommodationFilters() {
    }

    public AccommodationFilters(Long startDate, Integer noOfBedrooms, Double noOfBathrooms, Float minRent, Float maxRent) {
        this.startDate = startDate;
        this.noOfBedrooms = noOfBedrooms;
        this.noOfBathrooms = noOfBathrooms;
        this.minRent = minRent;
        this.maxRent = maxRent;
    }
}

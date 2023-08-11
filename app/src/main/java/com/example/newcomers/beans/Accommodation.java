package com.example.newcomers.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class Accommodation implements Serializable {
    String propertyType;
    String address;
    String city;
    String province;
    String postalCode;
    Double rent;
    long startDate;
    int noOfBedrooms;
    Double noOfBathrooms;
    ArrayList<String> amenities;
    String contactNo;
    String otherDetails;
    ArrayList<String> imageURLs;

    public Accommodation() {
    }

    public Accommodation(String propertyType, String address, String city, String province, String postalCode, Double rent, long startDate, int noOfBedrooms, Double noOfBathrooms, ArrayList<String> amenities, String contactNo, String otherDetails, ArrayList<String> imageURLs) {
        this.propertyType = propertyType;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.rent = rent;
        this.startDate = startDate;
        this.noOfBedrooms = noOfBedrooms;
        this.noOfBathrooms = noOfBathrooms;
        this.amenities = amenities;
        this.contactNo = contactNo;
        this.otherDetails = otherDetails;
        this.imageURLs = imageURLs;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getProvince() {
        return province;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public Double getRent() {
        return rent;
    }

    public long getStartDate() {
        return startDate;
    }

    public int getNoOfBedrooms() {
        return noOfBedrooms;
    }

    public Double getNoOfBathrooms() {
        return noOfBathrooms;
    }

    public ArrayList<String> getAmenities() {
        return amenities;
    }

    public String getContactNo() {
        return contactNo;
    }

    public String getOtherDetails() {
        return otherDetails;
    }

    public ArrayList<String> getImageURLs() {
        return imageURLs;
    }
}

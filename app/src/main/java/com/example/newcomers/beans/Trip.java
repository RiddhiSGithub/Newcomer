package com.example.newcomers.beans;

public class Trip {
   public String from;
   public String destination;
   public String carModel;
   public String carColor;
   public String licencePlate;
   public int seatRemain;
   public String description;

   public String userID;

   public Trip() {
   }

   public Trip(String from, String destination, String carModel, String carColor, String licencePlate, int seatRemain, String description, String userID) {
      this.from = from;
      this.destination = destination;
      this.carModel = carModel;
      this.carColor = carColor;
      this.licencePlate = licencePlate;
      this.seatRemain = seatRemain;
      this.description = description;
      this.userID = userID;
   }

   @Override
   public String toString() {
      return "Trip{" +
              "from='" + from + '\'' +
              ", destination='" + destination + '\'' +
              ", carModel='" + carModel + '\'' +
              ", carColor='" + carColor + '\'' +
              ", licencePlate='" + licencePlate + '\'' +
              ", seatRemain=" + seatRemain +
              ", description='" + description + '\'' +
              ", userID='" + userID + '\'' +
              '}';
   }

   public String getUserID() {
      return userID;
   }

   public void setUserID(String userID) {
      this.userID = userID;
   }

   public String getFrom() {
      return from;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public String getDestination() {
      return destination;
   }

   public void setDestination(String destination) {
      this.destination = destination;
   }

   public String getCarModel() {
      return carModel;
   }

   public void setCarModel(String carModel) {
      this.carModel = carModel;
   }

   public String getCarColor() {
      return carColor;
   }

   public void setCarColor(String carColor) {
      this.carColor = carColor;
   }

   public String getLicencePlate() {
      return licencePlate;
   }

   public void setLicencePlate(String licencePlate) {
      this.licencePlate = licencePlate;
   }

   public int getSeatRemain() {
      return seatRemain;
   }

   public void setSeatRemain(int seatRemain) {
      this.seatRemain = seatRemain;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }
}

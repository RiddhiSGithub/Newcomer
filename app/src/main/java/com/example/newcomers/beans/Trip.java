package com.example.newcomers.beans;

import java.io.Serializable;

/**
 * Trip Bean for DB
 * @Author: Meng
 * @Date Aug 10 2023
 */
public class Trip implements Serializable {
   public String id;
   public String fromId;
   public String from;
   public double fromLat, fromLng;
   public double destLat, destLng;
   public String destinationId;
   public String destination;
   public String carModel;
   public String carColor;
   public String licencePlate;
   public int seatTotal;
   public int seatTaken;
   public String description;

   public String userID;

   public Trip() {
   }

   public Trip(String id, String fromId, String from, double fromLat, double fromLng, double destLat, double destLng, String destinationId, String destination, String carModel, String carColor, String licencePlate, int seatTotal, int seatTaken, String description, String userID) {
      this.id = id;
      this.fromId = fromId;
      this.from = from;
      this.fromLat = fromLat;
      this.fromLng = fromLng;
      this.destLat = destLat;
      this.destLng = destLng;
      this.destinationId = destinationId;
      this.destination = destination;
      this.carModel = carModel;
      this.carColor = carColor;
      this.licencePlate = licencePlate;
      this.seatTotal = seatTotal;
      this.seatTaken = seatTaken;
      this.description = description;
      this.userID = userID;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getFromId() {
      return fromId;
   }

   public void setFromId(String fromId) {
      this.fromId = fromId;
   }

   public String getFrom() {
      return from;
   }

   public void setFrom(String from) {
      this.from = from;
   }

   public double getFromLat() {
      return fromLat;
   }

   public void setFromLat(double fromLat) {
      this.fromLat = fromLat;
   }

   public double getFromLng() {
      return fromLng;
   }

   public void setFromLng(double fromLng) {
      this.fromLng = fromLng;
   }

   public double getDestLat() {
      return destLat;
   }

   public void setDestLat(double destLat) {
      this.destLat = destLat;
   }

   public double getDestLng() {
      return destLng;
   }

   public void setDestLng(double destLng) {
      this.destLng = destLng;
   }

   public String getDestinationId() {
      return destinationId;
   }

   public void setDestinationId(String destinationId) {
      this.destinationId = destinationId;
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

   public int getSeatTotal() {
      return seatTotal;
   }

   public void setSeatTotal(int seatTotal) {
      this.seatTotal = seatTotal;
   }

   public int getSeatTaken() {
      return seatTaken;
   }

   public void setSeatTaken(int seatTaken) {
      this.seatTaken = seatTaken;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getUserID() {
      return userID;
   }

   public void setUserID(String userID) {
      this.userID = userID;
   }

   @Override
   public String toString() {
      return "Trip{" +
              "id='" + id + '\'' +
              ", fromId='" + fromId + '\'' +
              ", from='" + from + '\'' +
              ", fromLat=" + fromLat +
              ", fromLng=" + fromLng +
              ", destLat=" + destLat +
              ", destLng=" + destLng +
              ", destinationId='" + destinationId + '\'' +
              ", destination='" + destination + '\'' +
              ", carModel='" + carModel + '\'' +
              ", carColor='" + carColor + '\'' +
              ", licencePlate='" + licencePlate + '\'' +
              ", seatTotal=" + seatTotal +
              ", seatTaken=" + seatTaken +
              ", description='" + description + '\'' +
              ", userID='" + userID + '\'' +
              '}';
   }
}

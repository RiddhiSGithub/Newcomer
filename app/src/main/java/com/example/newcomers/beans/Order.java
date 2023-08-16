package com.example.newcomers.beans;

import java.io.Serializable;

/**
 * Order Bean for DB
 * @Author: Meng
 * @Date Aug 14 2023
 */
public class Order implements Serializable {
   public String id;
   public Trip orderTrip;
   public String orderUserID;
   public String orderDate;
   public String flightNumber;

   public Order() {
   }

   public Order(String id, Trip orderTrip, String orderUserID, String orderDate, String flightNumber) {
      this.id = id;
      this.orderTrip = orderTrip;
      this.orderUserID = orderUserID;
      this.orderDate = orderDate;
      this.flightNumber = flightNumber;
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public Trip getOrderTrip() {
      return orderTrip;
   }

   public void setOrderTrip(Trip orderTrip) {
      this.orderTrip = orderTrip;
   }

   public String getOrderUserID() {
      return orderUserID;
   }

   public void setOrderUserID(String orderUserID) {
      this.orderUserID = orderUserID;
   }

   public String getOrderDate() {
      return orderDate;
   }

   public void setOrderDate(String orderDate) {
      this.orderDate = orderDate;
   }

   public String getFlightNumber() {
      return flightNumber;
   }

   public void setFlightNumber(String flightNumber) {
      this.flightNumber = flightNumber;
   }

   @Override
   public String toString() {
      return "Order{" +
              "id='" + id + '\'' +
              ", orderTrip=" + orderTrip +
              ", orderUserID='" + orderUserID + '\'' +
              ", orderDate='" + orderDate + '\'' +
              ", flightNumber='" + flightNumber + '\'' +
              '}';
   }
}

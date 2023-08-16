package com.example.newcomers.beans;

import java.io.Serializable;

/**
 * User bean
 * Author: Meng
 *
 */
public class User implements Serializable {
   private static int latestId = 0;

   private int id;
   private String username;

   public String getStreetAddress() {
      return streetAddress;
   }

   public void setStreetAddress(String streetAddress) {
      this.streetAddress = streetAddress;
   }

   private String streetAddress;

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   private String city;

   public String getProvince() {
      return province;
   }

   public void setProvince(String province) {
      this.province = province;
   }

   private String province;

   public String getPostalCode() {
      return postalCode;
   }

   public void setPostalCode(String postalCode) {
      this.postalCode = postalCode;
   }

   private String postalCode;
   private String phoneNumber;

   public User() {
      id = ++latestId;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", phoneNumber='" + phoneNumber + '\'' +
              ", Street Address='" + streetAddress + '\'' +
              ", city='" + city + '\'' +
              ", province='" + province + '\'' +
              ", postal code='" + postalCode + '\'' +
              '}';
   }
}

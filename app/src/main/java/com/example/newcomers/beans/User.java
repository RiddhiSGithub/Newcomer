package com.example.newcomers.beans;

import java.io.Serializable;

/**
 * User bean
 * Author:Meng
 */
public class User implements Serializable {
   private int id;
   public String username, emailId, password;
   public int phoneNumber;

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

   public String getEmailId() {
      return emailId;
   }

   public void setEmailId(String emailId) {
      this.emailId = emailId;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public int getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(int phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   @Override
   public String toString() {
      return "User{" +
              "id=" + id +
              ", username='" + username + '\'' +
              ", emailId='" + emailId + '\'' +
              ", phoneNumber='" + phoneNumber + '\'' +
              ", password='" + password + '\'' +
              '}';
   }
}

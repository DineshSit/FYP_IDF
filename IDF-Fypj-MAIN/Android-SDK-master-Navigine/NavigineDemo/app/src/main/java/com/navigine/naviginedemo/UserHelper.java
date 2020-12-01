package com.navigine.naviginedemo;

public class UserHelper {
    String userName,phoneNumber,Location,ShowLocation,Email;

    public UserHelper(String userName, String phoneNumber, String location, String showLocation,String Email) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
       this.Location=location;
       this.ShowLocation=showLocation;
       this.Email=Email;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getShowLocation() {
        return ShowLocation;
    }

    public void setShowLocation(String showLocation) {
        ShowLocation = showLocation;
    }
}

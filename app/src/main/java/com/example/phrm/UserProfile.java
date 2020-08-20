package com.example.phrm;

public class UserProfile {
    public String first_name;
    public String last_name;
    public String email;
    public String mobile;

    public UserProfile(){

    }
    public UserProfile(String first_name, String last_name, String email, String mobile) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_name() {
        return first_name;
    }
}

package com.example.shoppingapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.Serializable;

public class User implements Serializable {
    private  String email;
    private  String profilePicUrl;

    public  String getEmail() {
        return email;
    }

    public  String getProfilePicUrl() {
        return profilePicUrl;
    }

    public  void setEmail(String e) {
        this.email = e;
    }

    public   void setProfilePicUrl(String propic) {
        this.profilePicUrl = propic;
    }

}

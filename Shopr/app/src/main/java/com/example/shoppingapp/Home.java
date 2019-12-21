package com.example.shoppingapp;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
// Runs on execution of app, to determine if firebase user is still active (not logged out)
public class Home extends Application {
    @Override
    public void onCreate(){
        super.onCreate();

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //If current log in user exists, then go to User's Page!
        if(firebaseUser!=null){
            Intent goToAfterLoginIntent=new Intent(Home.this,AfterLoginMainPage.class);
            goToAfterLoginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goToAfterLoginIntent);
        }
        else if(firebaseUser==null){
            Intent goToAfterLoginIntent=new Intent(Home.this,UserLogin.class);
            goToAfterLoginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(goToAfterLoginIntent);
        }

    }
}

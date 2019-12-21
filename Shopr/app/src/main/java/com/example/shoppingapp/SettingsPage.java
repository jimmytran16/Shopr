package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;

public class SettingsPage extends AppCompatActivity {
    private TextView settings_username;
    private ImageView profileImageView;
    private ImageButton takeAPic;
    private String TAG = "UploadItemactivity";
    private String EMAIL_ADDRESS="";
    private String IMAGE_URL="";
    private User user;
    private UserLogin USER_REFERENCE;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);
        BottomNavigationView bottomNavigationView = findViewById(R.id.settings_bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.settingsItem_settings);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.homeItem_settings:
                        Intent thisIntent =new Intent(SettingsPage.this,AfterLoginMainPage.class);
                        startActivity(thisIntent);
                        break;
                    case R.id.signOutItem_settings:
                        USER_REFERENCE = new UserLogin();
                        USER_REFERENCE.userSignOut();
                        Intent intent = new Intent(SettingsPage.this, UserLogin.class);
                        //clear stack of current user's login
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
            }
        });
        //get instance of firebase and current user email
        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            user = (User) extras.getSerializable("userInfo");
            IMAGE_URL = user.getProfilePicUrl();
            EMAIL_ADDRESS = user.getEmail();
        }




        profileImageView = (ImageView)findViewById(R.id.setting_profile_pic);
        settings_username = (TextView)findViewById(R.id.setting_username);
        takeAPic= (ImageButton) findViewById(R.id.setting_takepic);

        settings_username.setText(EMAIL_ADDRESS);
        Picasso.get()
                .load(IMAGE_URL)
                .fit()
                .centerCrop()
                .tag(getApplicationContext())
                .into(profileImageView);


        takeAPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SettingsPage.this,TakeProfilePicture.class));
            }
        });


    }



}

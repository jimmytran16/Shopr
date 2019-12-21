package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.shoppingapp.ui.main.SectionsPagerAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserBuySell extends AppCompatActivity {
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    UserLogin USER_REFERENCE;
    private final String TAG = "UserBuySell";
    @Override
    protected void onCreate(Bundle SavedInstanceBundle){
        super.onCreate(SavedInstanceBundle);
        setContentView(R.layout.activity_user_buy_sell);

        USER_REFERENCE = new UserLogin();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.buysellItem);

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()){
                    case R.id.homeItem:
                        startActivity(new Intent(UserBuySell.this,AfterLoginMainPage.class));
                    case R.id.settingsItem:
                        //user's setting's page
                        break;
                    case R.id.buysellItem:
                        break;
                    case R.id.signOutItem:
                        USER_REFERENCE.userSignOut();
                        Intent intent = new Intent(UserBuySell.this,UserLogin.class);

                        //clear stack of current user's login
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
            }
        });


    }
}

package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;


import android.widget.GridView;
import android.widget.ImageButton;

import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AfterLoginMainPage extends AppCompatActivity {
    private List<Upload> uploadList;
    private List<Upload> sellingList;
    private List<Upload> wishListArrayList;
    private User userProfileUploadKeeper=null;
    private GridView gridView;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FloatingActionButton addFloatingAction;
    private ImageButton refreshBtn, addItemBtn;
    private DatabaseReference DATABASE;
    private UserLogin USER_REFERENCE;
    private final String TAG = "AfterLoginMainPage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login_main_page);
        USER_REFERENCE = new UserLogin();

        refreshBtn = (ImageButton) findViewById(R.id.refreshBtn);

        //Get bottom nagivation bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.homeItem);
        addItemBtn = (ImageButton) findViewById(R.id.addItemBtn);

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AfterLoginMainPage.this, UploadItemActivity.class));
            }
        });

        bottomNavigationView.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.settingsItem:
                        Intent thisIntent =new Intent(AfterLoginMainPage.this,SettingsPage.class) ;
                        thisIntent.putExtra("userInfo",userProfileUploadKeeper);
                        startActivity(thisIntent);
                        break;
                    case R.id.buysellItem:
                        Intent buySellIntent = new Intent(AfterLoginMainPage.this, UserBuySell.class);
                        buySellIntent.putExtra("sellingList", (Serializable) sellingList);
                        buySellIntent.putExtra("wishlist", (Serializable) wishListArrayList);
                        Log.d("sellingsLISTTTT", "" + sellingList+" "+wishListArrayList);
                        startActivity(buySellIntent);
                        break;
                    case R.id.signOutItem:
                        USER_REFERENCE.userSignOut();
                        Intent intent = new Intent(AfterLoginMainPage.this, UserLogin.class);
                        //clear stack of current user's login
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        break;
                }
            }
        });

        gridView = (GridView) findViewById(R.id.itemGridLayout);


        uploadList = new ArrayList<>();

        DATABASE = FirebaseDatabase.getInstance().getReference("uploads");
        DATABASE.addValueEventListener(new ValueEventListener() {
            //Get Data fromm DATABASE and sets it in the Upload class , and added to List
            //List is used to pass to the adapter to generate the grid view
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "postSnapshot: " + postSnapshot);
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                makeWishList();
                getUserSettingInformation();


                Log.d(TAG,"SELLIGNGS "+ uploadList);
                sellingList = new ArrayList<Upload>(getSellings(uploadList));
                Log.d(TAG,"SELLIGNGS "+ getSellings(uploadList));
                ItemAdapter adapter = new ItemAdapter(AfterLoginMainPage.this, uploadList);
                gridView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AfterLoginMainPage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


        //go to item detail view after picture is clicked
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                Intent goToDetail_intent = new Intent(AfterLoginMainPage.this, ItemDescription.class);
                goToDetail_intent.putExtra("position", position);
                goToDetail_intent.putExtra("uploadList", (Serializable) uploadList);

                startActivity(goToDetail_intent);
            }
        });


        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshPage();
            }
        });

    }

    //Find out user's sellings function ,
    public ArrayList<Upload> getSellings(List<Upload> UPLOADS_LIST) {
        ArrayList<Upload> sellers = new ArrayList<>();
        for (Upload items : UPLOADS_LIST) {
            if (items.getUser().equals(mAuth.getCurrentUser().getEmail())) {
                sellers.add(items);
            }
        }
        return sellers;
    }

    //Function to refresh page
    public void refreshPage() {
        finish();
        startActivity(getIntent());
    }

    public void makeWishList() {

        DATABASE = FirebaseDatabase.getInstance().getReference("wishlist");
        DATABASE.addValueEventListener(new ValueEventListener() {
            //Get Data fromm DATABASE and sets it in the Upload class , and added to List
            //List is used to pass to the adapter to generate the grid view
            Upload wishList = new Upload();
            boolean wishCheck = false;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                wishListArrayList = new ArrayList<>();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "postSnapshot: " + postSnapshot);
                    wishList = postSnapshot.getValue(Upload.class);

                    String split[] = wishList.getPhone().split(" ");
                    Log.d(TAG,"Wish List splits: "+split[1]);
                    Log.d(TAG,"Wish List phone: "+wishList.getPhone());
                    Log.d(TAG,"Wish List currentUserEmail: "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    //compares the email of current user to the wishlist item reference
                    if(FirebaseAuth.getInstance().getCurrentUser().getEmail().equals(split[1])){
                        wishListArrayList.add(postSnapshot.getValue(Upload.class));
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AfterLoginMainPage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });


    }
    //function to get User profile info
    public void getUserSettingInformation(){
        DATABASE = FirebaseDatabase.getInstance().getReference("profile");
        DATABASE.addValueEventListener(new ValueEventListener() {
            //Get Data fromm DATABASE and sets it in the Upload class , and added to List
            //List is used to pass to the adapter to generate the grid view
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG, "postSnapshot: " + postSnapshot);
                    User userProfileUpload = postSnapshot.getValue(User.class);
                    if (userProfileUpload.getEmail().equals(mAuth.getCurrentUser().getEmail())) {
                        userProfileUploadKeeper = userProfileUpload;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AfterLoginMainPage.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}

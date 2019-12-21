package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SellerItemDescription extends AppCompatActivity {
    TextView description_text,description_item_name,description_price_tag;
    ImageView description_item_image;
    Button deleteButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_item);
        Bundle extras = getIntent().getExtras();
        //get position, path (either 'uploads', or 'wishlist'),and get the list(either wishlist or sell) from intent
        int position = extras.getInt("position");
        String PATH_TO_DATABASE = extras.getString("PATH");
        ArrayList<Upload> uploadsList =(ArrayList<Upload>) extras.getSerializable("buy/sell");
        deleteButton = (Button)findViewById(R.id.seller_delete_button);
        description_text = (TextView)findViewById(R.id.seller_item_des);
        description_item_name= (TextView)findViewById(R.id.seller_item_name);
        description_price_tag= (TextView)findViewById(R.id.seller_item_price);

        description_item_image = (ImageView)findViewById(R.id.seller_item_image);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database =FirebaseDatabase.getInstance();
                //calls query from DB to get reference to folder to delete item
                database.getReference(PATH_TO_DATABASE).orderByChild("mImageUrl").equalTo(uploadsList.get(position).getmImageUrl()).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            //if path exist, remove item
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child : dataSnapshot.getChildren()) {
                                    child.getRef().removeValue();
                                    Log.d("SellerItemDescription", "SUCCESS DELETE");
                                    startActivity(new Intent(SellerItemDescription.this, AfterLoginMainPage.class));
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.w("SellerItemDescription", "getUser:onCancelled", databaseError.toException());
                            }
                        });
            }
        });


        //load price, itemname, and description
        description_price_tag.setText("Price: $"+uploadsList.get(position).getPrice());
        description_item_name.setText("Item: "+uploadsList.get(position).getName());
        description_text.setText("Description: "+uploadsList.get(position).getDescription());

        //load image into the imageview, by getting url
        Picasso.get()
                .load(uploadsList.get(position).getmImageUrl())
                .fit()
                .centerCrop()
                .tag(getApplicationContext())
                .into(description_item_image);

    }
}

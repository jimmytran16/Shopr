package com.example.shoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ItemDescription extends AppCompatActivity {
    TextView description_text,description_item_name,description_price_tag,des_city,des_phone,des_email;
    ImageView description_item_image;
    private DatabaseReference DATABASE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);
        Bundle extras = getIntent().getExtras();
            int position = extras.getInt("position");
            ArrayList<Upload> uploadsList =(ArrayList<Upload>) extras.getSerializable("uploadList");

        description_text = (TextView)findViewById(R.id.des_item_des);
        description_item_name= (TextView)findViewById(R.id.des_item_name);
        description_price_tag= (TextView)findViewById(R.id.des_price_tag);
        des_email= (TextView)findViewById(R.id.des_item_email);
        des_phone= (TextView)findViewById(R.id.des_item_phone);
        des_city= (TextView)findViewById(R.id.des_item_city);
        description_item_image = (ImageView)findViewById(R.id.des_item_image);


        //load price, itemname, and description
        description_price_tag.setText("Price : $"+uploadsList.get(position).getPrice());
        description_item_name.setText("Item: "+uploadsList.get(position).getName());
        description_text.setText("Description: "+uploadsList.get(position).getDescription());
        des_email.setText("Seller's Email: "+uploadsList.get(position).getUser());
        des_phone.setText("Seller's Phone: "+uploadsList.get(position).getPhone());
        des_city.setText("Seller's Location: "+uploadsList.get(position).getCity());

        //load image into the imageview, by getting url
        Picasso.get()
                .load(uploadsList.get(position).getmImageUrl())
                .fit()
                .centerCrop()
                .tag(getApplicationContext())
                .into(description_item_image);


        Button BuyButton = (Button)findViewById(R.id.BuyButton);
        BuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("wishlist");
                String uploadId = databaseReference.push().getKey();
                Upload wishList = new Upload();
                wishList = uploadsList.get(position);
                String sellerPhone = uploadsList.get(position).getPhone();
                wishList.setPhone(sellerPhone+" "+FirebaseAuth.getInstance().getCurrentUser().getEmail());
                databaseReference.child(uploadId).setValue(wishList);
                startActivity(new Intent(getApplicationContext(),AfterLoginMainPage.class));
            }
        });





    }
}

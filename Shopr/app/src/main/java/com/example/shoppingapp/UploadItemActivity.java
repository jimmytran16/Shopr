package com.example.shoppingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UploadItemActivity extends AppCompatActivity {
   private EditText itemPriceText, itemNameText,itemDescription,itemCity,itemPhone;
   private Button uploadBtn,chooseFileBtn;
   private ImageView fileChosenImage;
   private String TAG = "UploadItemactivity";

   private static final int PICK_IMAGE_REQUEST = 1;

   private Uri ImageUri;

   private DatabaseReference DATABASE;
   private StorageReference STORAGE;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_item);

        fileChosenImage = (ImageView)findViewById(R.id.fileImageView);

        itemPhone = (EditText)findViewById(R.id.itemPhoneNumber);
        itemCity = (EditText)findViewById(R.id.itemCity);
        itemPriceText = (EditText)findViewById(R.id.itemPriceText);
        itemNameText = (EditText)findViewById(R.id.itemNameText);
        itemDescription = (EditText)findViewById(R.id.itemDescription);

        uploadBtn = (Button)findViewById(R.id.upload_button);
        chooseFileBtn= (Button)findViewById(R.id.choose_button);

        STORAGE = FirebaseStorage.getInstance().getReference("uploads");

        chooseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooseer();
            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }
    //function to open the images folder in phone
    public void openFileChooseer(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    //function to render the photo into the imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data !=null && data.getData()!=null){
            //get url of storage
            ImageUri = data.getData();
//           Picasso.with(this).load(ImageUri).into(ImageUri);
            Log.d("UploadItemAct","IMAGE URL - "+ImageUri);
            Picasso.get().load(ImageUri).into(fileChosenImage);
        }
    }
    //getting exntetion from our image file
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    //function to upload the item
    private void uploadFile(){
        if(ImageUri!=null){

        StorageReference fileReference = STORAGE.child(System.currentTimeMillis()+
                "."+getFileExtension(ImageUri));
        fileReference.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(UploadItemActivity.this,"Successfully UPLOAD",Toast.LENGTH_SHORT).show();
                        fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            //Saves the url into the storage then saves into the database
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                DATABASE = FirebaseDatabase.getInstance().getReference("uploads");
                                Upload upload = new Upload(itemNameText.getText().toString().trim(),itemPriceText.getText().toString().trim(),itemDescription.getText().toString().trim(),uri.toString(),user.getEmail(),itemCity.getText().toString(),itemPhone.getText().toString());
                                String uploadId = DATABASE.push().getKey();
                                DATABASE.child(uploadId).setValue(upload);
                                startActivity(new Intent(UploadItemActivity.this,AfterLoginMainPage.class));
                                Log.d(TAG, "URI ADDRESSE= "+uri);
                                Log.d(TAG, "URI_TO_STRING= "+uri.toString());
                            }
                        });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(UploadItemActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                    }
                });
        }else{
            Toast.makeText(this,"No file selected",Toast.LENGTH_SHORT).show();
        }
    }
}

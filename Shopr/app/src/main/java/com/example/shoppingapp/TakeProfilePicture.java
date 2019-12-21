package com.example.shoppingapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class TakeProfilePicture extends AppCompatActivity {
    String TAG = "TakeProfilePicture";
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView fileChosenImage;
    private DatabaseReference DATABASE;
    private StorageReference STORAGE;
    private Button uploadProfilePicBtn, chooseProfilePicBtn;
    Uri ImageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_profile_pic);

        chooseProfilePicBtn = (Button)findViewById(R.id.choose_profile_button);
        uploadProfilePicBtn= (Button)findViewById(R.id.comfirm_profile_button);

        STORAGE = FirebaseStorage.getInstance().getReference("profile");

        fileChosenImage = (ImageView)findViewById(R.id.user_profile_pic);

        chooseProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooseer();
            }
        });
        uploadProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }

    //function to render the photo into the imageview
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data !=null && data.getData()!=null){
            //get url of storage
            ImageUri = data.getData();
            Log.d(TAG,"IMAGE URL - "+ImageUri);
            Picasso.get().load(ImageUri).into(fileChosenImage);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    public void openFileChooseer(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    private void uploadFile(){
        if(ImageUri!=null){

            StorageReference fileReference = STORAGE.child(System.currentTimeMillis()+
                    "."+getFileExtension(ImageUri));
            fileReference.putFile(ImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(TakeProfilePicture.this,"Successfully UPLOAD",Toast.LENGTH_SHORT).show();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                //Saves the url into the storage then saves into the database
                                @Override
                                public void onSuccess(Uri uri) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    DATABASE = FirebaseDatabase.getInstance().getReference("profile");
                                    User userInfo = new User();
                                    userInfo.setEmail(user.getEmail());
                                    userInfo.setProfilePicUrl(uri.toString());
                                    String uploadId = DATABASE.push().getKey();
                                    DATABASE.child(uploadId).setValue(userInfo);
                                    Intent intent =new Intent(TakeProfilePicture.this,SettingsPage.class);
                                    //pass user profile info to the intent
                                    intent.putExtra("userInfo",userInfo);
                                    startActivity(intent);
                                    Log.d(TAG, "URI ADDRESSE= "+uri);
                                    Log.d(TAG, "URI_TO_STRING= "+uri.toString());
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TakeProfilePicture.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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


package com.example.shoppingapp;
//////////////////////// FINISH WISH LIST,

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {
   //Initialize login and registration button to redirect to page
    private final String TAG = "MainActivity";
    private Button btn_LoginPage, btn_RegisterPage, btnGoogleLogIn;
    private Intent intent;
    private GoogleSignInClient googleSignInClient;
    //Get instnace of firebase authhorization
    final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //function to detect INTERNET connection
    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_LoginPage = (Button)findViewById(R.id.btn_loginpage);
        btn_RegisterPage = (Button)findViewById(R.id.btn_registrationpage);
        btnGoogleLogIn = (Button)findViewById(R.id.signinwithGoogleBtn);

        //Check if device is connected to the internet
        if (isNetworkConnected()== true){
            Toast toast = Toast.makeText(getApplicationContext(),
                    "INTERNET CONNECTED",
                    Toast.LENGTH_LONG);
            toast.show();
        }
        else{
            Toast toast = Toast.makeText(getApplicationContext(),
                    "NO INTERNET ACCESS",
                    Toast.LENGTH_LONG);

            toast.show();
        }

        //click listener for google login, user login, register
        btnGoogleLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Google plug in
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();
                googleSignInClient = GoogleSignIn.getClient(MainActivity.this,gso);
                signIntoGoogle();

            }
        });
        //direct to login page
        btn_LoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this,UserLogin.class);
                startActivity(intent);
            }
        });
        //direct to Register page
        btn_RegisterPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this,RegisterUser.class));
            }
        });
    }


    void signIntoGoogle(){
        Intent signIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signIntent,123);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 123) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }
    //authenticate google account function
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d("GoogleSignUP","firebaseAuthWithGoogle: "+account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this,task ->{
                    //if google log in success
                    if(task.isSuccessful()){
                        FirebaseUser user = mAuth.getCurrentUser();
                        Log.d("GoogleSignUp","SUCCESS");
                        user.sendEmailVerification();
                        Intent intent = new Intent(MainActivity.this,AfterLoginMainPage.class);
                        intent.putExtra("type","google");
                        startActivity(intent);
                        //if not success
                    }else{
                        Log.d("GoogleSignUp","task is not success");
                        startActivity(new Intent(MainActivity.this,UserLogin.class));
                    }
                });
    }

    //get google client instance
    public void signGoogleClientOut(){
        if(googleSignInClient!=null) {
            googleSignInClient.signOut().addOnCompleteListener(MainActivity.this,
                    new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
        }
    }

}

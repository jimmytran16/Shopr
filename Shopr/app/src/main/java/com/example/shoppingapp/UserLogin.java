package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserLogin extends AppCompatActivity {
    EditText user_name, user_password;
    ProgressBar progressBar;
    Button signInBtn;
    String USER_NAME, USER_PASSWORD;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private final String TAG = "UserLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signInBtn = (Button)findViewById(R.id.userLoginBtn);
        progressBar = (ProgressBar)findViewById(R.id.login_progressBar);
        progressBar.setVisibility(View.GONE);
        user_name= (EditText)findViewById(R.id.user_name);
        user_password = (EditText)findViewById(R.id.user_pass);
        user_name.setText("testing@gmail.com");

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USER_NAME = user_name.getText().toString();
                USER_PASSWORD= user_password.getText().toString();
                userSignIn(USER_NAME,USER_PASSWORD);
            }
        });
    }
    //Function to sign in authentication
    private void userSignIn(String email, String password){
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "UserLogin-SUCCESS");
                            user = mAuth.getCurrentUser();
                            Toast.makeText(UserLogin.this, "Log in success!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UserLogin.this,AfterLoginMainPage.class));

                            //IF IT IS SUCCESS THEN WE WILL PASSING INTENT
                        } else {
                            // If sign in fails, display a message to the user.
//                            updateUI(null);
                            Log.w(TAG, "UserLogin-:failure", task.getException());
                            Toast.makeText(UserLogin.this, "The username and password you entered did not match our records. Please try again.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    //function for google/firebase user to log out
    public void userSignOut(){

        MainActivity GOOGLE_REFERENCE = new MainActivity();
        GOOGLE_REFERENCE.signGoogleClientOut();
        mAuth.signOut();
    }
}

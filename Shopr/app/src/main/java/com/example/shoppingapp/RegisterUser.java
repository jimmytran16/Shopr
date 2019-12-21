package com.example.shoppingapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterUser extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText user_email,user_pass;
    Button registerBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance(); //get the instance of the firebase database
        registerBtn = (Button)findViewById(R.id.userSignUp);
        user_email= (EditText)findViewById(R.id.register_email);
        user_pass= (EditText)findViewById(R.id.register_password);

        user_email.setText("jimmytran1620@gmail.com");
        user_pass.setText("");

        //register
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String USER_EMAIL= user_email.getText().toString();
                String USER_PASS= user_pass.getText().toString();
                signUp(USER_EMAIL,USER_PASS);
            }
        });

    }
    private void signUp(String email, String password){

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            startActivity(new Intent(RegisterUser.this,TakeProfilePicture.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("tag", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterUser.this, "Email already exist!.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

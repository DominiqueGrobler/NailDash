package com.naomi.nail_dash;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    //declare all necessary fields/classes
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    EditText email, password;
    String Password, Username;
    RegUser user = new RegUser();
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initiate all your elements
        email = (EditText) findViewById(R.id.txtEmailL);
        password = (EditText) findViewById(R.id.txtPasswordLog);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            goHome();
        }
    }
    //redirect to the desired page when the button is clicked
    public void goRegister(View view) {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }
    //redirect to the desired page when the button is clicked
    public void goHome() {
        Intent intent = new Intent(this,Home.class);
        startActivity(intent);
    }

    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)

    //check if the user is in the database
    public void LoginUser(View view) {
        String encPassword = Base64.encodeToString(password.getText().toString().getBytes(), Base64.DEFAULT);
        mAuth.signInWithEmailAndPassword(email.getText().toString(), encPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(Login.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            goHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }



    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    //clear all fields
    public void clear() {
        email.getText().clear();
        password.getText().clear();
    }

}
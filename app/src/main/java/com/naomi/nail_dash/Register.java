package com.naomi.nail_dash;

import static android.content.ContentValues.TAG;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    EditText username, password, email, conPassword;

    private String userID;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //get settings from database
        //code attribution
        //this method was taken from a previous project
        //Naomi Groenewald
        //(Groenewald, 2022)
        // initiate all your elements
        username = (EditText) findViewById(R.id.txtNameReg);
        email = (EditText) findViewById(R.id.txtEmail);
        password = (EditText) findViewById(R.id.txtPasswordReg);
        conPassword = (EditText) findViewById(R.id.txtConPasswordReg);

        mAuth = FirebaseAuth.getInstance();

    }
    // get the database reference then Add User To Firebase
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
//    public void RegisterUser(View view) {
//        if (username.getText().toString().length() > 0 && password.getText().toString().length() > 0 &&
//                conPassword.getText().toString().length() > 0 && email.getText().toString().length() > 0) {
//
//            if (password.getText().toString().length() > 6 && conPassword.getText().toString().length() > 6) {
//
//                if (password.getText().toString().equals(conPassword.getText().toString())) {
//                    //check email already exist or not.
//                    mAuth.fetchSignInMethodsForEmail(email.getText().toString())
//                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
//
//                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();
//
//                                    if (isNewUser) {
//                                        addUser();
//                                    } else {
//                                        Toast.makeText(Register.this, "Email already exists",
//                                                Toast.LENGTH_SHORT).show();
//                                    }
//
//                                }
//                            });
//
//                } else {
//                    Toast.makeText(Register.this, "The password and confirm password must match",
//                            Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(Register.this, "The password and confirm password must have more than 6 characters",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        } else {
//            Toast.makeText(Register.this, "All fields must be filled ",
//                    Toast.LENGTH_SHORT).show();
//        }
//
//    }
    public void RegisterUser(View view) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference( "Users/");

        //(stackoverflow, 2016)
        if(username.getText().toString().length()>0 && password.getText().toString().length()>0 &&
                conPassword.getText().toString().length()>0 && email.getText().toString().length()>0 )
        {
            if(password.getText().toString().equals(conPassword.getText().toString()))
//                if(Encrypt.md5(password.getText().toString()).equals(Encrypt.md5(conPassword.getText().toString())))
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm");
                builder.setMessage("Are you sure you want to add this User?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       //check email already exist or not.
                    mAuth.fetchSignInMethodsForEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                    boolean isNewUser = task.getResult().getSignInMethods().isEmpty();

                                    if (isNewUser) {
                                        addUser(userRef);
                                    } else {
                                        Toast.makeText(Register.this, "Email already exists",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

            }
            else
            {
                Toast.makeText(Register.this, "Password is not the same as Confirm password",
                        Toast.LENGTH_LONG).show();

            }


        }
        else
        {
            Toast.makeText(Register.this, "Make sure all fields are filled in",
                    Toast.LENGTH_LONG).show();
        }


    }
    //redirect to the desired page when the button is clicked
    public void goLogin(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    //methode to add user to the database
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    private  void addUser(DatabaseReference userRef)
    {
        String encPassword = Base64.encodeToString(password.getText().toString().getBytes(), Base64.DEFAULT);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), encPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            RegUser u = new RegUser("" + username.getText().toString()
                                    ,"" + encPassword,
                                    "" + email.getText().toString());

                            // pass the data through the constructor into the database
                            userRef.child(user.getUid()).setValue(u);
                            Toast.makeText(Register.this, "New user added.",
                                    Toast.LENGTH_SHORT).show();
                            clear();
                            login();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void clear() {
        email.setText("");
        password.setText("");
        conPassword.setText("");
        username.setText("");
    }

    public void login() {
        Intent intent = new Intent(this, TestMap2.class);
        startActivity(intent);
    }
}
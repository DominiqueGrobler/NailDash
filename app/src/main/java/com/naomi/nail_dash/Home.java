package com.naomi.nail_dash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
    //redirect to the desired page when the button is clicked
    public void goLocation(View view) {
        Intent intent = new Intent(this,TestMap2.class);
        startActivity(intent);
    }

    public void goLogout(View view) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this,Coverpage.class);
        startActivity(intent);
    }

    public void goSettings(View view) {
        Intent intent = new Intent(this, Settings.class);
        startActivity(intent);
    }
        public void goFavorite(View view) {
            Intent intent = new Intent(this,Favorites.class);
            startActivity(intent);
        }
    }


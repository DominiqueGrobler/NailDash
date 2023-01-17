package com.naomi.nail_dash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class Coverpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);
    }
    public void RegPage(View view)
    {
        Intent intent = new Intent(this,Register.class);
        startActivity(intent);
    }
    public void LogPage(View view)
    {

        Intent intent = new Intent(this,Login.class);
        startActivity(intent);
    }
}
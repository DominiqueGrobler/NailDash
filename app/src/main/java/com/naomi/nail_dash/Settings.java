package com.naomi.nail_dash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Settings extends AppCompatActivity {
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    RegUser user = new RegUser();
    Settings_Details set = new Settings_Details();
    private Spinner spinner,spinnerL, spinnerM;

    private static final String[] paths1 = { "Select Measurement","Metric", "Imperial"};
    private static final String[] paths2 = { "Select Travel Mode","Driving", "Walking", "Biking"};
    private static final String[] paths3 = { "Select Map Type","Satellite", "Terrain", "Hybrid"};
    private String uid;

    private TextView dropdown_items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //connect to the correct database

        //get settings from database
        //code attribution
        //this method was taken from a previous project
        //Naomi Groenewald
        //(Groenewald, 2022)
        spinner = (Spinner)findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Settings.this,
                R.layout.dropdown_items,paths1);

        adapter.setDropDownViewResource(R.layout.drodown_list);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(itemSelectedListener);

        spinnerL = (Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Settings.this,
                R.layout.dropdown_items,paths2);

        adapter2.setDropDownViewResource(R.layout.drodown_list);
        spinnerL.setAdapter(adapter2);
        spinnerL.setOnItemSelectedListener(itemSelectedListener2);

        spinnerM = (Spinner)findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Settings.this,
                R.layout.dropdown_items,paths3);

        adapter3.setDropDownViewResource(R.layout.drodown_list);
        spinnerM.setAdapter(adapter3);
        spinnerM.setOnItemSelectedListener(itemSelectedListener3);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            uid = user.getUid();
        }

    }
    public void goHome(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    //Add settings to the database
    public void EditDatabaseM(DatabaseReference userRef) {
        // call the constructor in the book class to pass the data through
        Settings_Details i = new Settings_Details("metric");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
        }

    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    public void EditDatabaseI(DatabaseReference userRef) {
        // call the constructor in the book class to pass the data through
        Settings_Details i = new Settings_Details("imperial");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l ) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Type the path to the data you want to work with
            DatabaseReference set = database.getReference("Users/"+ uid +  "/settings/mapUnits/");
            DatabaseReference setL = database.getReference("Users/"+ uid +  "/settings/Landmarks/");
            switch (i) {
                case 0:

                    break;
                case 1:
                    EditDatabaseM(set);
                    break;
                case 2:
                    EditDatabaseI(set);
                    break;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };


    public void EditDatabaseD(DatabaseReference userRef) {
        // call the constructor in the book class to pass the data through
        SettingMode i = new SettingMode("driving");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }

    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    public void EditDatabaseW(DatabaseReference userRef) {
        // call the constructor in the book class to pass the data through
        SettingMode i = new SettingMode("walking");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }

    public void EditDatabaseB(DatabaseReference userRef) {
        // call the constructor in the book class to pass the data through
        SettingMode i = new SettingMode("biking");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }
    private AdapterView.OnItemSelectedListener itemSelectedListener2 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l ) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Type the path to the data you want to work with

            DatabaseReference setL = database.getReference("Users/"+ uid +  "/settings/Transport/");
            switch (i) {
                case 0:

                    break;
                case 1:
                    EditDatabaseD(setL);
                    break;
                case 2:
                    EditDatabaseW(setL);
                    break;
                case 3:
                    EditDatabaseB(setL);
                    break;

            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };

    public void EditDatabaseSat(DatabaseReference userRef) {
        SettingsMap i = new SettingsMap("satellite");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }

    public void EditDatabaseTer(DatabaseReference userRef) {

        // call the constructor in the book class to pass the data through
        SettingsMap i = new SettingsMap("terrain");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }

    public void EditDatabaseHyb(DatabaseReference userRef) {

        // call the constructor in the book class to pass the data through
        SettingsMap i = new SettingsMap("hybrid");

        // pass the data through the constructor into the database
        userRef.setValue(i);

        Toast.makeText(Settings.this, "Edited Settings Successfully!", Toast.LENGTH_LONG).show();
    }
    private AdapterView.OnItemSelectedListener itemSelectedListener3 = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l ) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Type the path to the data you want to work with
            DatabaseReference set = database.getReference("Users/"+ uid +  "/settings/Map/");
            switch (i) {
                case 0:

                    break;
                case 1:
                    EditDatabaseSat(set);
                    break;
                case 2:
                    EditDatabaseTer(set);
                    break;
                case 3:
                    EditDatabaseHyb(set);
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg0) {
            // TODO Auto-generated method stub

        }
    };
}
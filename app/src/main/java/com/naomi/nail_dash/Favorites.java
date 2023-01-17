package com.naomi.nail_dash;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Favorites extends AppCompatActivity {
    //get settings from database
    //code attribution
    //this method was taken from a previous project
    //Naomi Groenewald
    //(Groenewald, 2022)
    private ListView listItems;
    private ArrayList<String> items_list;
    private ArrayAdapter<String> items_adapter;
    private ValueEventListener favListener;
    private String Latitude, name;
    String uid, lat, lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        //get settings from database
        //code attribution
        //this method was taken from a previous project
        //Naomi Groenewald
        //(Groenewald, 2022)
        listItems = (ListView) findViewById(R.id.lvOutput);
        FetchList ();
        items_list = new ArrayList<String>();
        items_adapter = new ArrayAdapter<String>(this, R.layout.list_color, items_list);
        listItems.setAdapter(items_adapter);

        listItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {

                String data=(String) parent.getItemAtPosition(position);
                List<String> favList = Arrays.asList(data.split(" , "));

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference fav = database.getReference().child("Users/" +
                        user.getUid() +
                        "/favourites/" + favList.get(1) +"/latlng/" );

                DatabaseReference fave = database.getReference().child("Users/" +
                        user.getUid() +
                        "/favourites/" + favList.get(1) +"/" );
                Intent intent = new Intent(getBaseContext(), TestMap2.class);
                fav.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                DataSnapshot data = task.getResult();
                                 lat = data.child("latitude").getValue().toString();
                                 lng = data.child("longitude").getValue().toString();


                                intent.putExtra("Latitude", lat);
                                intent.putExtra("Longitude", lng);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: Retrieval Unsuccessful.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

                fave.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if(task.isSuccessful()) {
                            if(task.getResult().exists()) {
                                DataSnapshot data = task.getResult();
                                name = data.child("favName").getValue().toString();


                                intent.putExtra("Name", name);
                                startActivity(intent);
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error: Retrieval Unsuccessful.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
//
//
            }
        });
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
    private void FetchList() {

        // connect to the database and select the correct path to the desired data
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference faveData = database.getReference().child("Users/" +
                user.getUid() +
                "/favourites/");

        favListener= faveData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot items : snapshot.getChildren()) {
                    items_list.add(items.child("favName").getValue().toString() +" , "+ items.child("favAddress").getValue().toString());

                }

                items_adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               
            }
        });
    }
}
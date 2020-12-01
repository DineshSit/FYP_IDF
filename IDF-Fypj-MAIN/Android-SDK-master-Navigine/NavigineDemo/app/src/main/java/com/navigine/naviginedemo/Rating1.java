package com.navigine.naviginedemo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navigine.naviginedemo.VenueClass;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.firebase.database.DatabaseReference;


public class Rating1 extends AppCompatActivity {
    SharedPreferences spref;
    EditText FeedMessage;
    Button SendFeed;
    String rating1=""; //String rating1="",rating2="", rating3="";
    RatingBar ratingbar1;  //RatingBar ratingbar1,ratingbar2,ratingbar3;
    DatabaseReference ref;
    Feedback feedback; // get,set class
    ListView listView;
    FirebaseDatabase database;
    String setLocation1;
    static final String TAG = "NAVIGINE.Demo";
    long maxid=0;

    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating1); // link java to xml

        //user variable
        listView = (ListView) findViewById(R.id.ListViewLoc) ;
        FeedMessage = (EditText) findViewById(R.id.FeedMessage);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar1);
        SendFeed = (Button) findViewById(R.id.sendFeed);

        //display list of venues from db on listview
        SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
        final String phoneNumber = sp.getString("phoneNumber","");
        final String location = sp.getString("location","");

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Venues");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    list.add(snapshot1.getValue().toString());
            }
            adapter.notifyDataSetChanged();
        }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,  long id) {
                ref = database.getReference("Reviews");
                ref.child(location).child("location").setValue(setLocation1);
                setLocation1=adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"Location "+adapter.getItem(position)+" has been selected!",Toast.LENGTH_SHORT).show();
            }
        });

        //
        DatabaseReference reff1;
        reff1=FirebaseDatabase.getInstance().getReference().child("Reviews");
        // auto increment feedback count
        reff1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    maxid=(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
                //insert data to fb
                //feedback = new Feedback();
                //ref = FirebaseDatabase.getInstance().getReference().child("Feedback");
                SendFeed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //rating1 = String.valueOf(ratingbar1.getRating());
                        //feedback.setRreviews(FeedMessage.getText().toString().trim());
                        //feedback.setRrating(rating1);
                        //feedback.setRlocation(loc);
                        //ref.push().setValue(feedback);
                        float rate = ratingbar1.getRating();
                        String fb = FeedMessage.getText().toString().trim();

                        //check user input
                        if (adapter.isEmpty()){
                            Toast.makeText(Rating1.this,"Please select a location",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(fb)){
                            FeedMessage.setError("Please enter feedback");
                            return;
                        }
                        if (ratingbar1.getRating()==0.0){
                            Toast.makeText(Rating1.this, "Please enter rating", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        database = FirebaseDatabase.getInstance();
                        ref = database.getReference("Reviews");

                        ReviewClass reviewclass = new ReviewClass(setLocation1, rate, fb);
                        //ref.child(setLocation1).setValue(reviewclass); // set as key
                        ref.child(String.valueOf(maxid+1)).setValue(reviewclass);

                        spref = getSharedPreferences("MyUserProfile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spref.edit();
                        editor.putString("location", setLocation1);
                        editor.putFloat("rating", rate);
                        editor.putString("feedback", fb);
                        editor.commit();

                        //ref.child(String.valueOf(maxid+1)).setValue(reviewclass);
                        Toast.makeText(Rating1.this, "Review submitted!", Toast.LENGTH_LONG).show();
                    }
                });
    }








    //list = new ArrayList<>();
    //arrayAdapter = new ArrayAdapter<>(this,R.layout.)
    // display list of location from DB in spinner
    //reff1 = FirebaseDatabase.getInstance().getReference("Venues");
    //arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, ArrayList);
    //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //spinner.setAdapter(arrayAdapter);
    //spinner = findViewById(R.id.spinnerLoc);
    //listView = (ListView) findViewById(R.id.ListViewLoc) ;




}
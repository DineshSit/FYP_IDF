package com.navigine.naviginedemo;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.navigine.naviginedemo.VenueClass;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class VenuePage extends Activity {
    private Location mLocation = null;
    private NavigationThread mNavigation = null;
    private int mCurrentSubLocationIndex = 0;
//    String[] VenueList  = new String[] { "L306","L307","L308","L309","L310","L311","L312","L313","L314","L335","L339","L340","FYPJ Enterance","Blk L Floor 3 Enterance","L435","L437","L432","L4 LIFT","L4 LIFT 2","L4 STAIRS", "L424","L4 eNTRANCE","L431","L434","L439","IT HELP DESK","L425","L438","L430","L436","L4 FEMALE TOILET","L4 MALE TOILET"};
    List<String> VenueList = new ArrayList<String>();
    ArrayList<VenueClass> VenueList1 = new ArrayList<VenueClass>();
    ListView simpleList;
    private static final String TAG = "NAVIGINE.Demo";
    SharedPreferences settings;

    Button rbtn;

    ////





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNavigation     = NavigineSDK.getNavigation();
        Categories();
        //VenueItem();
        setContentView(R.layout.customized_list);

        simpleList = findViewById(R.id.listVenue);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView, VenueList);
        simpleList.setAdapter(arrayAdapter);


        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                String cat = (String) parent.getItemAtPosition(position);
                //Toast.makeText(VenuePage.this, clickedFile, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("cat",cat);
                startActivity(intent);

            }



        });

        rbtn = findViewById(R.id.want_toreview);
        rbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(),Rating1.class));
                startActivity(new Intent(getApplicationContext(),reviews.class));
                //startActivity(new Intent(getApplicationContext(),WriteReview.class));
            }
        });


    }
    public void Form_Fav(View v)
    {
        Intent intent = new Intent(this, VenueClass.class);
        intent.putExtra("List","Favourite");
        startActivity(intent);
    }

    public void Form_Star(View v)
    {
        Intent intent = new Intent(this, VenueClass.class);
        intent.putExtra("List","Starred");
        startActivity(intent);
    }
    public void Form_Wanttogo(View v)
    {
        Intent intent = new Intent(this, VenueClass.class);
        intent.putExtra("List","Want_To_Go");
        startActivity(intent);
    }

    public void reviewMethod(View v)
    {
        //Intent intent = new Intent(this, Rating1.class);
        Intent intent = new Intent(this, reviews.class);
        //Intent intent = new Intent(this, WriteReview.class);
        startActivity(intent);
    }


    public void Categories() {
        mLocation = mNavigation.getLocation();

        settings=this.getSharedPreferences("FloorPref",0);
        int newfloor = settings.getInt("Floor",0);
        mCurrentSubLocationIndex = newfloor;

        SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);

        for (int i = 0; i < subLoc.getVenues().size(); i++) {
            Venue ven = subLoc.getVenues().get(i);
            if (ven.getCategory().getName().equals("Office")){
                continue;
            }
            else{
                VenueList.add(ven.getCategory().getName());
            }


        }
        Set<String> NewVenue = new HashSet<>(VenueList);
        VenueList.clear();
        VenueList.addAll(NewVenue);


    }

    public void back(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

/*
    public void VenueItem(){
        String VenueLists = null;
        Boolean fav = false;



        VenueList1.add(
                new VenueClass("L306", false)
        );


        VenueList1.add(
                new VenueClass("L307", false)
        );


        VenueList1.add(
                new VenueClass("L308", false)
        );


        VenueList1.add(
                new VenueClass("L309", false)
        );


        VenueList1.add(
                new VenueClass("L310", false)
        );


        VenueList1.add(
                new VenueClass("L311", false)
        );


        VenueList1.add(
                new VenueClass("L312", false)
        );


        VenueList1.add(
                new VenueClass("L313", false)
        );


        VenueList1.add(
                new VenueClass("L314", false)
        );


        VenueList1.add(
                new VenueClass("L335", false)
        );


        VenueList1.add(
                new VenueClass("L339", false)
        );


        VenueList1.add(
                new VenueClass("L340", false)
        );

        Log.d(TAG, "venues List: " + VenueList1);
    }


    public void onDefaultToggleClick(View View) {
        Toast.makeText(this, "DefaultToggle", Toast.LENGTH_SHORT).show();
    }

    public void onCustomToggleClick(View view){
        Toast.makeText(this, "CustomToggle", Toast.LENGTH_SHORT).show();
    }

*/
}


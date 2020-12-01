package com.navigine.naviginedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class shortcut extends Activity {

    private Location mLocation = null;
    private NavigationThread mNavigation = null;
    private int mCurrentSubLocationIndex = 0;
    private Spinner LVenue1 = null;
    private EditText shortcut1 = null;
    public String name = null;
    public String filename ="Shortcut";
    public String destination = null;
   // public String text1 = null;
   private MyDbAdapter mydb ;

    SharedPreferences settings;

    List<String> VenueList = new ArrayList<String>();

    private static final String TAG = "NAVIGINE.Demo";


    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigation     = NavigineSDK.getNavigation();
        VenueLists();
        setContentView(R.layout.shortcut);




        shortcut1 = (EditText) findViewById(R.id.shortcut1);
        LVenue1 = (Spinner) findViewById(R.id.spinner1);
        mydb = new MyDbAdapter(this);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        LVenue1.setAdapter(adapter1);










        LVenue1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                destination = VenueList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });


    }
    public void VenueLists() {
        mLocation = mNavigation.getLocation();

        settings=this.getSharedPreferences("FloorPref",0);
        int newfloor = settings.getInt("Floor",0);
        mCurrentSubLocationIndex = newfloor;
        
        SubLocation subLoc = mLocation.getSubLocations().get(mCurrentSubLocationIndex);

        for ( int i = 0; i < subLoc.getVenues().size(); i++) {

            Venue ven = subLoc.getVenues().get(i);
            VenueList.add(ven.getName());

            Log.d(TAG, "venues: " + ven.getName());
        }

        Set<String> NewVenue = new HashSet<>(VenueList);
        VenueList.clear();
        VenueList.addAll(NewVenue);
        VenueList.add(0,"---Select---");



    }
    /*
    public void Search(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("Position1", text1);
        intent.putExtra("Position2", text2);
        startActivity(intent);

    }*/

    public void Back(View v)
    {
        Intent intent = new Intent(this, SearchPage.class);

        startActivity(intent);
    }

    public void Add(View v)
    {
        name = shortcut1.getText().toString();



        if (name == "" || destination == "---Select---") {
            Toast.makeText(shortcut.this, "Please enter the right informantion", Toast.LENGTH_SHORT).show();
        }
        else{



            //Internal Storage
            FileOutputStream fos;
            try {
                fos = openFileOutput(name, Context.MODE_PRIVATE);
                fos.write(destination.getBytes());
                fos.close();

                Toast.makeText(
                        shortcut.this,
                        name + " saved",
                        Toast.LENGTH_LONG).show();

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            //mydb.insertShortcut(name,destination);


            Intent intent = new Intent(this, SearchPage.class);

            startActivity(intent);
        }

        /*
        intent.putExtra("name", name);
        intent.putExtra("Position", destination);

         */

    }



}

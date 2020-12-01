package com.navigine.naviginedemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.*;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.firebase.database.snapshot.Index;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.NavigineSDK;
import com.navigine.naviginesdk.SubLocation;
import com.navigine.naviginesdk.Venue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class SearchPage extends Activity {
    MyDbAdapter mydb;

    private Location mLocation = null;
    private NavigationThread mNavigation = null;

    SharedPreferences settings;



//    SharedPreferences sharedPreferences = getSharedPreferences("MyUserProfile",Context.MODE_PRIVATE);
//
//    SharedPreferences sharedPreferences =getApplicationContext().getSharedPreferences("FloorPref", Context.MODE_PRIVATE);
//    Integer FloorNum= sharedPreferences.getInt("Floor",0);



    private int mCurrentSubLocationIndex;

    private Spinner LVenue1 = null;
    private Spinner LVenue2 = null;
    public String text1 = null;
    public String text2 = null;
    public String filename ="History";
    public String name = null;
    public String destination = null;

    public String name2 = null;
    public String destination2 = null;

    List<String> VenueList = new ArrayList<String>();
    List<String> Shortcut_list = new ArrayList<>();
    String [] Trying;
    List<String> Shortcut_Venue = new ArrayList<String>();
    List<String> testing = new ArrayList<String>();
    ListView simpleList;

    int MyFloor;

    //ArrayList array_list = mydb.getAllCotacts();

    private static final String TAG = "NAVIGINE.Demo";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        settings=this.getSharedPreferences("floorpreferences", MODE_PRIVATE);
//        String floornumber = settings.getString("Floor","");
        //        TextView debug = findViewById(R.id.searchdebug);
//        debug.setText(floornumber);

        mNavigation     = NavigineSDK.getNavigation();
        VenueLists();



//        TextView debug2 = findViewById(R.id.debuglocate);
//        debug2.setText(floornumber);


//        mCurrentSubLocationIndex = 1;
//        TextView locationIndex = findViewById(R.id.navigation__current_floor_label);
//        MyFloor = Integer.parseInt(locationIndex.toString());

        setContentView(R.layout.search);
        LVenue1 = (Spinner) findViewById(R.id.spinner1);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        LVenue1.setAdapter(adapter1);


        LVenue2 = (Spinner) findViewById(R.id.spinner2);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList);
// Specify the layout to use when the list of choices appears
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        LVenue2.setAdapter(adapter2);







        //mydb = new MyDbAdapter(this);


        ShortcutLists();

        registerForContextMenu(simpleList);


        /*
        if (name !=null && destination != null){
            Shortcut_list.add(name + " : " + destination);
            name = null;
            destination = null;
        }*/










        LVenue1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text1 = VenueList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });


        LVenue2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                text2 = VenueList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                // TODO Auto-generated method stub
                String clickedFile = (String) parent.getItemAtPosition(position);
                //Toast.makeText(SearchPage.this, clickedFile, Toast.LENGTH_SHORT).show();

                FileInputStream fis;

                try {
                    fis = openFileInput(clickedFile);
                    byte[] input = new byte[fis.available()];
                    while (fis.read(input) != -1) {}
                    text1= new String(input);
                    Toast.makeText(SearchPage.this, text1, Toast.LENGTH_SHORT).show();

                    text2 = "---Select---";
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Position1", text1);
                    intent.putExtra("Position2", text2);
                    startActivity(intent);



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }



        });






    }




    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.searchmenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String file = null;
        switch (item.getItemId()) {
            case R.id.delete:

                file = Shortcut_Venue.get(info.position);
                deleteFile(file);
                Shortcut_Venue.remove(file);
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView,Shortcut_Venue);
                simpleList = findViewById(R.id.listVenue);
                simpleList.setAdapter(arrayAdapter);
                arrayAdapter.notifyDataSetChanged();
                return true;
            case R.id.edit:
                file = Shortcut_Venue.get(info.position);

                FileInputStream fis;

                try {
                    fis = openFileInput(file);
                    byte[] input = new byte[fis.available()];
                    while (fis.read(input) != -1) {}
                    text1= new String(input);
                    //Toast.makeText(SearchPage.this, text1, Toast.LENGTH_SHORT).show();

                    //text2 = "---Select---";
                    Intent intent = new Intent(getApplicationContext(), shortcut_edit.class);
                    intent.putExtra("Position1", text1);
                    intent.putExtra("name", file);
                    startActivity(intent);



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            default:
                return super.onContextItemSelected(item);
        }
    }
    /*
    public void Listview(){


        Log.d(TAG, "Lists: " + Shortcut_list);
    }*/




    public void VenueLists() {
        mLocation = mNavigation.getLocation();

        // findviewbyid wont work because it's current page has no such id.

//        TextView myfloor = ;
//        String MyFloor = myfloor.toString();
//        //Code to change the floor level
//        if (MyFloor == "3"){
//            mCurrentSubLocationIndex = 1;
//        }

//        CHANGE YOUR FLOOR LEVEL HERE!
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

    public void ShortcutLists(){

        Trying = getApplicationContext().fileList();
        Shortcut_list = Arrays.asList(getApplicationContext().fileList());
        for ( int i = 0; i < Shortcut_list.size(); i++){
            Log.d(TAG, "Files: " + Shortcut_list.get(i)  );
            if (Shortcut_list.get(i).equals("Navigine") || Shortcut_list.get(i).equals("libnavigate-jni.so")||Shortcut_list.get(i).equals("History") || Shortcut_list.get(i).equals("Favourite") || Shortcut_list.get(i).equals("Starred")||Shortcut_list.get(i).equals("Want_To_Go")){
                continue;
            }
            else {
                Shortcut_Venue.add(Shortcut_list.get(i));
            }

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView,Shortcut_Venue);
        simpleList = findViewById(R.id.listVenue);
        simpleList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();

    }
    public void simpleListView(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView,Shortcut_Venue);
        simpleList = findViewById(R.id.listVenue);
        simpleList.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }


    public void Search(View v)
    {
        if (text1 == "---Select---" && text2 != "---Select---"){
            Toast.makeText(SearchPage.this, "Please choose your first destination", Toast.LENGTH_SHORT).show();
        }
        else if (text1 == "---Select---" && text2 == "---Select---"){
            Toast.makeText(SearchPage.this, "Please choose your destinations", Toast.LENGTH_SHORT).show();
        }
        else if (text1 != "---Select---" && text2 == "---Select---"){
            //Internal Storage
            FileOutputStream fos;
            try {
                String input = text1 + " , " ;
                fos = openFileOutput(filename, Context.MODE_APPEND);
                fos.write(input.getBytes());
                fos.close();



            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("Position1", text1);
            intent.putExtra("Position2", text2);
            startActivity(intent);
        }
        else{
            //Internal Storage
            FileOutputStream fos;
            try {
                String input = text1 + " , "  + text2 + " , ";
                fos = openFileOutput(filename, Context.MODE_APPEND);
                fos.write(input.getBytes());
                fos.close();



            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("Position1", text1);
            intent.putExtra("Position2", text2);
            startActivity(intent);
        }


    }

    public void Back(View v)
    {
      Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

    public void History(View v)
    {
        Intent intent = new Intent(this, search_history.class);

        startActivity(intent);
    }
    public void Shortcut(View v)
    {
        Intent intent = new Intent(this, shortcut.class);

        startActivity(intent);
    }


}

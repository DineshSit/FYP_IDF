package com.navigine.naviginedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.navigine.naviginesdk.Location;
import com.navigine.naviginesdk.NavigationThread;
import com.navigine.naviginesdk.RouteEvent;
import com.navigine.naviginesdk.RoutePath;

import java.lang.reflect.TypeVariable;
import java.net.NoRouteToHostException;
import java.util.ArrayList;
import java.util.List;

public class WriteReview extends AppCompatActivity {
    TextView mShareLocation,mEmail,mPhoneNumber,mUserName,mTest,mgotoFindMeetup,mDebug;
    SharedPreferences sp;
    FirebaseDatabase dB;
    DatabaseReference reference;
    //search variable
    NavigationThread mNavigation = null;
    Spinner LVenue1 = null;
    List<String> VenueList = new ArrayList<String>();
    Location mLocation = null;
    int mCurrentSubLocationIndex = 0;
    static final String TAG = "NAVIGINE.Demo";
    Button mSetting,mBtnShareLocation,mRecommendLocation;
    String setLocation;

    //search variables


    ArrayList<String>stringArrayList=new ArrayList<>();
    ArrayAdapter<String>adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        //user variable
        mTest=findViewById(R.id.tvTest);
        //mgotoFindMeetup=findViewById(R.id.tvGoToFindMeetUp);
        mDebug=findViewById(R.id.tvDebug);
        ListView Lv=findViewById(R.id.ListView);
        //mSetting=findViewById(R.id.tvSetting);
        //mBtnShareLocation=findViewById(R.id.btnShareLocation);

        mRecommendLocation=findViewById(R.id.btnRecommend);
//search
        SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
        final String phoneNumber=sp.getString("phoneNumber","");
        dB=FirebaseDatabase.getInstance();
        reference= dB.getReference("Venues");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    stringArrayList.add(snapshot1.getValue().toString());
                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,stringArrayList);
        Lv.setAdapter(adapter);
        Lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                reference=dB.getReference("users");
                reference.child(phoneNumber).child("location").setValue(setLocation);
                Toast.makeText(getApplicationContext(),"Location "+adapter.getItem(position)+" has been shared!",Toast.LENGTH_SHORT).show();
                mDebug.setText("Meet Location: "+adapter.getItem(position));
                setLocation=adapter.getItem(position);

            }
        });

        //mgotoFindMeetup.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), FindMeetup.class));
            //}
        //});
        //mSetting.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //startActivity(new Intent(getApplicationContext(), UserProfile.class));
            //}
        //});

        //mBtnShareLocation.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {
                //reference=dB.getReference("users");
                //reference.child(phoneNumber).child("location").setValue(setLocation);
            }
        //});
        //mRecommendLocation.setOnClickListener(new View.OnClickListener() {
            //@Override
            //public void onClick(View v) {


            //}
        //});

















        //Search


/*

        mNavigation     = NavigineSDK.getNavigation();
        VenueLists();

        setContentView(R.layout.search);
        LVenue1 = (Spinner) findViewById(R.id.spinner1);

// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, VenueList);
// Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        LVenue1.setAdapter(adapter1);

        //venue method











   /*     mLocation=findViewById(R.id.tvLocation);

        String Location= NavigineSDK.getLastKnownLocation().toString();
        String Location1=com.navigine.naviginesdk.Location.class.getName();
         String Location2=NavigineSDK.getNavigation().getLocationName();//closest
       String L3= com.navigine.naviginesdk.Venue.class.getName();

   //     mLocation.setText(L3); */
/*
    }
    public void VenueLists() {
        mLocation = mNavigation.getLocation();
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

*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();

        menuInflater.inflate(R.menu.menu_search,menu);
        MenuItem menuItem=menu.findItem(R.id.search_view);
        SearchView searchView=(SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Filter list
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}

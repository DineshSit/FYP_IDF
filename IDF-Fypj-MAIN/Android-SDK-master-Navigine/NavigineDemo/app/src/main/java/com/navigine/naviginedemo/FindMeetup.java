package com.navigine.naviginedemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FindMeetup extends AppCompatActivity {
    TextView mTest,mCreateMeetup;
    EditText mSearchUser;
    Button mBtnGo,mHeadToMeetUp,mMyMeetUp;
    FirebaseDatabase dB;

    DatabaseReference reference;
    SharedPreferences sp;

    //History




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_meetup);
        mBtnGo=findViewById(R.id.btnGoFindMeetup);
        mHeadToMeetUp=findViewById(R.id.btnHeadToMeetup);
        mSearchUser=findViewById(R.id.txtSearch);
        mCreateMeetup=findViewById(R.id.tvCreateMeetUp);
        mMyMeetUp=findViewById(R.id.btnGoToMyMeetups);
        mTest=findViewById(R.id.tvTest);
        mHeadToMeetUp.setVisibility(View.GONE);
        sp=getSharedPreferences("MyUserProfile",MODE_PRIVATE);

        String[]search_History=getResources().getStringArray(R.array.search_History);
//goto create meetup
        mCreateMeetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindLocation.class));
            }
        });


//Button to Find meetup location
        mBtnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String FindUser=mSearchUser.getText().toString().trim();
                dB= FirebaseDatabase.getInstance();
                reference=dB.getReference("users").child(FindUser).child("location");
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        final String Location;

                        if(snapshot.exists())
                        {              Location=snapshot.getValue().toString();
                        //Display meet up location for user
                            mTest.setText(FindUser+" will Meeting you at "+Location);
                            //Set go to meetup to true
                            mHeadToMeetUp.setVisibility(View.VISIBLE);
                            mHeadToMeetUp.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                 public void onClick(View v) {
                                      SharedPreferences.Editor editor=sp.edit();
                                   editor.putString("QrData",Location);
                              editor.commit();
                                      if (Location.equals("Notset") || Location == null
                                      ) {

                                          Toast.makeText(getApplicationContext(),"Friend has not set meetup location",Toast.LENGTH_SHORT).show();
                                      }
                                      else {
                                          startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                      }
                                  }
                            });
                        }
                        else
                        {
                            Toast.makeText(FindMeetup.this,"User does not exist",Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });

mMyMeetUp.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(getApplicationContext(), MyMeetUps.class));
    }
});




    }






private void loadHistory(TextView textView)
{String[]SearchHistory=getResources().getStringArray(R.array.search_History);
int arraysize=SearchHistory.length;
    for(int i=0;i<arraysize;i++)
    {
        textView.append(SearchHistory[i]);
        textView.append("\n");
    }
}










}
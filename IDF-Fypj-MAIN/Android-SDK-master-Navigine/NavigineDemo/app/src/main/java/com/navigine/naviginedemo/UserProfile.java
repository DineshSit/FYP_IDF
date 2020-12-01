package com.navigine.naviginedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class UserProfile extends AppCompatActivity {
    TextView mShareLocation,mEmail,mPhoneNumber,mUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        //intialize user profile variable

        mShareLocation=findViewById(R.id.tvSharingLocation);
        mEmail=findViewById(R.id.tvEmail);
        mPhoneNumber=findViewById(R.id.tvPhoneNumber);
        mUserName=findViewById(R.id.tvUserName);

        //  Get  User Profile
        SharedPreferences sp=getApplicationContext().getSharedPreferences("MyUserProfile", Context.MODE_PRIVATE);
        String name=sp.getString("username","");
        final String phoneNumber=sp.getString("phoneNumber","");
        String location=sp.getString("location","");
        String ShowLocation=sp.getString("showLocation","");
        String email=sp.getString("email","");
        //Display User Profile
        mUserName.setText(name);
        mPhoneNumber.setText(phoneNumber);
        mShareLocation.setText(ShowLocation);
        mEmail.setText(email);

    }
}
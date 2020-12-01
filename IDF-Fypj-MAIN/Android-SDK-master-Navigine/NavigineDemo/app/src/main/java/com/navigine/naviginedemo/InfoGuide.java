package com.navigine.naviginedemo;

import android.Manifest;
import android.app.*;
import android.content.*;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.*;
import android.se.omapi.Session;
import android.view.*;
import android.view.View.*;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.*;
import android.util.*;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import java.io.*;
import java.lang.*;
import java.util.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.navigine.naviginesdk.*;

public class InfoGuide extends Activity implements View.OnClickListener {

    ViewFlipper viewFlipper;
    Button next;
    Button previous;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        viewFlipper = (ViewFlipper)findViewById(R.id.viewflipper);
        next = findViewById(R.id.nextpicture);
        previous = findViewById(R.id.previouspicture);

        next.setOnClickListener(this);
        previous.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == next) {
            viewFlipper.showNext();
        }
        else if (v == previous) {
            viewFlipper.showPrevious();
        }
    }

    public void back(View v)
    {
        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);
    }

}

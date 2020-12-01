package com.navigine.naviginedemo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class reviews extends AppCompatActivity {
    Button browse, post, azure, browseP;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);

        browse = findViewById(R.id.btnBrowseReview);
        post = findViewById(R.id.btnPostReview);
        azure = findViewById(R.id.addPhotoBTN);
        browseP = findViewById(R.id.insertpicBTN);

        // direct to post a review
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Rating1.class));
            }
        });

        // direct to browse reviews
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DisplayReview.class));
            }
        });

        azure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PicActivity.class));
            }
        });

        browseP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), class309.class));
            }
        });
    }

    public void Form_postR(View v){
        Intent intent = new Intent(this, Rating1.class);
        startActivity(intent);
    }

    public void Form_browseR(View v){
        Intent intent = new Intent(this, DisplayReview.class);
        startActivity(intent);
    }

    public void Form_azureR(View v){
        Intent intent = new Intent(this, PicActivity.class);
        startActivity(intent);
    }

    public void Form_browseP(View v){
        Intent intent = new Intent(this, class309.class);
        startActivity(intent);
    }
}

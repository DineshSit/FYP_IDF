package com.navigine.naviginedemo;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseLongArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.firebase.database.DatabaseReference;


public class Rating1 extends AppCompatActivity {
    private static final String APP_TAG = "" ;
    SharedPreferences spref;
    EditText FeedMessage;
    Button SendFeed;
    String rating1=""; //String rating1="",rating2="", rating3="";
    RatingBar ratingbar1;  //RatingBar ratingbar1,ratingbar2,ratingbar3;
    DatabaseReference ref;
    Feedback feedback; // get,set class
    ListView listView;
    Spinner spinner;
    FirebaseDatabase database;
    String setLocation1;
    static final String TAG = "NAVIGINE.Demo";
    long maxid=0;
    private static final int SELECT_IMAGE = 100;
    private static final int REQUEST_IMAGE_CAPTURE = 101;


    ArrayList<String> list = new ArrayList<>();
    ArrayAdapter<String> adapter;

    ImageView ivImage;
    private Uri imageUri;
    //Integer REQUEST_CAMERA=1, SELECT_FILE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating1); // link java to xml
        // iMAGE
        ivImage = (ImageView) findViewById(R.id.imageView9);

       Button  pic = (Button) findViewById(R.id.button2);
       pic.setOnClickListener(new View.OnClickListener(){
           @Override
           public  void  onClick(View view){
               SelectImage();
           }
       });
        //user variable
        listView = (ListView) findViewById(R.id.ListViewLoc) ;
        FeedMessage = (EditText) findViewById(R.id.FeedMessage);
        ratingbar1 = (RatingBar) findViewById(R.id.ratingBar1);
        SendFeed = (Button) findViewById(R.id.sendFeed);
        //spinner = (Spinner) findViewById(R.id.spinnerLoc);

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
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                        String picture = imageUri.toString();

                        ReviewClass reviewclass = new ReviewClass(setLocation1, rate, fb);
                       // ref.child(setLocation1).setValue(reviewclass); // set as key
                        ref.child(String.valueOf(maxid+1)).setValue(reviewclass);

                        spref = getSharedPreferences("MyUserProfile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = spref.edit();
                        editor.putString("location", setLocation1);
                        editor.putFloat("rating", rate);
                        editor.putString("feedback", fb);
                        editor.commit();
                        UploadImage();


                        //ref.child(String.valueOf(maxid+1)).setValue(reviewclass);
                        Toast.makeText(Rating1.this, "Review submitted!", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), DisplayReview.class));
                    }
                });
    }

    private void UploadImage()
    {
        try {
            final InputStream imageStream = getContentResolver().openInputStream(this.imageUri);
            final int imageLength = imageStream.available();

            final Handler handler = new Handler();

            Thread th = new Thread(new Runnable() {
                public void run() {

                    try {

                        final String imageName = ImageManager.UploadImage(imageStream, imageLength);

                        handler.post(new Runnable() {

                            public void run() {
                                Toast.makeText(Rating1.this, "Image Uploaded Successfully. Name = " + imageName, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    catch(Exception ex) {
                        final String exceptionMessage = ex.getMessage();
                        handler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(Rating1.this, exceptionMessage, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }});
            th.start();
        }
        catch(Exception ex) {

            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void  SelectImage(){
        final CharSequence[] items = {"Camera","Gallery", "Cancel "};

        AlertDialog.Builder builder = new AlertDialog.Builder(Rating1.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if(items[i].equals("Camera")){
                    askCameraPermissions();
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    if(intent.resolveActivity(getPackageManager())!=null)
                    {
                        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                    }

                } else if (items[i].equals("Gallery")){
//                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    intent.setType("images/*");
//                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_IMAGE);
                } else if (items[i].equals("Cancel")){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }



    private  void  askCameraPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
        };
    }

    @Override
    protected  void  onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {


           if (requestCode == REQUEST_IMAGE_CAPTURE ) {

               Bundle bundle = data.getExtras();
                Bitmap finalPic = (Bitmap) bundle.get("data");

                ivImage.setImageBitmap(finalPic);
                //ivImage.invalidate();

                // RESIZE BITMAP, see section below
                // Load the taken image into a preview




            } else if (requestCode == SELECT_IMAGE) {
//                Uri selectImageUri = data.getData();
//                ivImage.setImageURI(selectImageUri);
               this.imageUri = data.getData();
               this.ivImage.setImageURI(this.imageUri);
            }
        }
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
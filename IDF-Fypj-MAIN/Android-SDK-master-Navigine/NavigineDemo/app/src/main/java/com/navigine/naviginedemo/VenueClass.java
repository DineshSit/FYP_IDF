package com.navigine.naviginedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class VenueClass extends Activity {

    TextView Empty = null;
    String text1 = null;
    public String list = null;
    List<String> Venue_list = new ArrayList<>();
    List<String> file_list = new ArrayList<>();
    ListView simpleList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text1 = getIntent().getStringExtra("List");
        file_list = Arrays.asList(getApplicationContext().fileList());
        for ( int i = 0; i < file_list.size(); i++){

            if (file_list.get(i).equals(text1)){


                FileInputStream fis;

                try {
                    fis = openFileInput(text1);
                    byte[] input = new byte[fis.available()];
                    while (fis.read(input) != -1) {}
                    list= new String(input);
                    Venue_list = Arrays.asList(list.split(" , "));
                    if (Venue_list.isEmpty()){
                        setContentView(R.layout.empty_list);
                        Empty =findViewById(R.id.AddList);
                        registerForContextMenu(Empty);

                    }
                    else {
                        setContentView(R.layout.venue);


                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView,Venue_list);
                        simpleList = findViewById(R.id.listVenue);
                        simpleList.setAdapter(arrayAdapter);
                        registerForContextMenu(simpleList);
                    }
                    //Toast.makeText(search_history.this, text1, Toast.LENGTH_SHORT).show();





                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;



            }
            else {
                setContentView(R.layout.empty_list);
                Empty =findViewById(R.id.AddList);
                registerForContextMenu(Empty);
            }

        }









    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.listvenuemenu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String file = null;
        switch (item.getItemId()) {

            case R.id.delete:
                String ven = null;
                String text2 = null;
                ven = Venue_list.get(info.position);


                for(int i = 0; i < Venue_list.size(); ++i){
                    if (text2 == null){
                        if (Venue_list.get(i).equals(ven)){
                            continue;
                        }
                        else {
                            text2 = Venue_list.get(i) ;
                        }


                    }

                    else {
                        if (Venue_list.get(i).equals(ven)){
                            continue;
                        }
                        else {
                            text2 = text2 + " , "+Venue_list.get(i);
                        }

                    }
                }
                if (text2 == null){
                    setContentView(R.layout.empty_list);
                    Empty =findViewById(R.id.AddList);
                    registerForContextMenu(Empty);
                    deleteFile(text1);
                }
                else {
                    FileOutputStream fos;
                    try {

                        fos = openFileOutput(text1, Context.MODE_PRIVATE);
                        fos.write(text2.getBytes());
                        fos.close();


                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    FileInputStream fis;

                    try {
                        fis = openFileInput(text1);
                        byte[] input = new byte[fis.available()];
                        while (fis.read(input) != -1) {}
                        list= new String(input);

                        Venue_list = Arrays.asList(list.split(" , "));
                        if (Venue_list.isEmpty()){
                            setContentView(R.layout.empty_list);
                            Empty =findViewById(R.id.AddList);
                            registerForContextMenu(Empty);

                        }
                        else{
                            setContentView(R.layout.venue);
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView,Venue_list);
                            simpleList = findViewById(R.id.listVenue);
                            simpleList.setAdapter(arrayAdapter);
                            registerForContextMenu(simpleList);
                        }





                        //Toast.makeText(search_history.this, text1, Toast.LENGTH_SHORT).show();





                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }




                return true;




            default:
                return super.onContextItemSelected(item);
        }
    }

    public void add(View v)
    {
        Intent intent = new Intent(this, formVenue.class);
        intent.putExtra("List",text1);
        startActivity(intent);
    }
    public void back(View v)
    {

        Intent intent = new Intent(this, VenuePage.class);
        startActivity(intent);
    }

}


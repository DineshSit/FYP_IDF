package com.navigine.naviginedemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class search_history extends Activity {

    public String filename ="History";
    public String list = null;
    List<String> History_list = new ArrayList<>();
    ListView simpleList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_history);
        FileInputStream fis;

        try {
            fis = openFileInput(filename);
            byte[] input = new byte[fis.available()];
            while (fis.read(input) != -1) {}
            list= new String(input);
            //Toast.makeText(search_history.this, text1, Toast.LENGTH_SHORT).show();
            History_list = Arrays.asList(list.split(" , "));
            Collections.reverse(History_list);
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.search_listview, R.id.textView,History_list);
            simpleList = findViewById(R.id.listVenue2);
            simpleList.setAdapter(arrayAdapter);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

package com.navigine.naviginedemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class formVenue extends Activity {
    String text1 = null;

    List<String> venues = new ArrayList<String>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        text1 = getIntent().getStringExtra("List");
        setContentView(R.layout.venue_form);



    }
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.L308:
                if (checked)
                    venues.add("L308");
                else
                    venues.remove("L308");

                break;
            case R.id.L311:
                if (checked)
                    venues.add("L311");
                else
                    venues.remove("L311");
                break;

            case R.id.L312:
                if (checked)
                    venues.add("L312");
                else if (!checked)
                    venues.remove("L312");
                break;

            case R.id.L313:
                if (checked)
                    venues.add("L313");
                else
                    venues.remove("L313");
                break;
            case R.id.L314:
                if (checked)
                    venues.add("L314");
                else
                    venues.remove("L314");
                break;
            case R.id.L306:
                if (checked)
                    venues.add("L306");
                else
                    venues.remove("L306");
                break;
            case R.id.L310:
                if (checked)
                    venues.add("L310");
                else
                    venues.remove("L310");
                break;
            case R.id.L309:
                if (checked)
                    venues.add("L309");
                else
                    venues.remove("L309");
                break;
            case R.id.L335:
                if (checked)
                    venues.add("L335");
                else
                    venues.remove("L335");
                break;
            case R.id.L348:
                if (checked)
                    venues.add("L348");
                else
                    venues.remove("L348");
                break;
            case R.id.L330:
                if (checked)
                    venues.add("L330");
                else
                    venues.remove("L330");
                break;
            case R.id.FYP:
                if (checked)
                    venues.add("FYP Entrance/Exit");
                else
                    venues.remove("FYP Entrance/Exit");
                break;
            case R.id.BlkL:
                if (checked)
                    venues.add("Blk L - Level 3 Entrance/Exit");
                else
                    venues.remove("Blk L - Level 3 Entrance/Exit");

                break;

            case R.id.L435:
                if (checked)
                    venues.add("L435");
                else
                    venues.remove("L435");
                break;

            case R.id.L437:
                if (checked)
                    venues.add("L437");
                else
                    venues.remove("L437");
                break;

            case R.id.L432:
                if (checked)
                    venues.add("L432");
                else
                    venues.remove("L432");
                break;

            case R.id.L4Lift2:
                if (checked)
                    venues.add("L4 Lift 2");
                else
                    venues.remove("L4 Lift 2");
                break;

            case R.id.L4Lift1:
                if (checked)
                    venues.add("L4 Lift 1");
                else
                    venues.remove("L4 Lift 1");
                break;

            case R.id.L4Stairs:
                if (checked)
                    venues.add("L4 Stairs");
                else
                    venues.remove("L4 Stairs");
                break;

            case R.id.L424:
                if (checked)
                    venues.add("L424");
                else
                    venues.remove("L424");
                break;

            case R.id.L4Entrance:
                if (checked)
                    venues.add("L4 Entrance");
                else
                    venues.remove("L4 Entrance");
                break;

            case R.id.L431:
                if (checked)
                    venues.add("L431");
                else
                    venues.remove("L431");
                break;

            case R.id.L434:
                if (checked)
                    venues.add("L434");
                else
                    venues.remove("L434");
                break;

            case R.id.L439:
                if (checked)
                    venues.add("L439");
                else
                    venues.remove("L439");
                break;

            case R.id.ITHelpDesk:
                if (checked)
                    venues.add("IT Help Desk");
                else
                    venues.remove("IT Help Desk");
                break;

            case R.id.L425:
                if (checked)
                    venues.add("L425");
                else
                    venues.remove("L425");
                break;

            case R.id.L438:
                if (checked)
                    venues.add("L438");
                else
                    venues.remove("L438");
                break;

            case R.id.L430:
                if (checked)
                    venues.add("L430");
                else
                    venues.remove("L430");
                break;

            case R.id.L436:
                if (checked)
                    venues.add("L436");
                else
                    venues.remove("L436");
                break;

            case R.id.L4FemaleToilet:
                if (checked)
                    venues.add("L4 Female Toilet");
                else
                    venues.remove("L4 Female Toilet");
                break;

            case R.id.L4MaleToilet:
                if (checked)
                    venues.add("L4 Male Toilet");
                else
                    venues.remove("L4 Male Toilet");
                break;

        }
    }



    public void add(View v)
    {
        String text2 = null;

        if (venues.isEmpty()){
            Toast.makeText(formVenue.this, "Please choose at least 1 venue", Toast.LENGTH_SHORT).show();
        }

        else {
            for(int i = 0; i < venues.size(); ++i){
                if (text2 == null){
                    text2 = venues.get(i) ;
                }
                else{
                    text2 = text2 + " , " + venues.get(i);
                }

            }



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







            Intent intent = new Intent(this, VenuePage.class);
            startActivity(intent);

        }







    }
    public void back(View v)
    {

        Intent intent = new Intent(this, VenuePage.class);
        startActivity(intent);
    }


}

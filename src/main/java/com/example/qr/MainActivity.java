package com.example.myfirstapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends AppCompatActivity {
Button scan, change;
TextView textview1,section,section_name;
String filename = "section_id";
boolean fileflag = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        scan = (Button) findViewById(R.id.scan1);
        change = (Button) findViewById(R.id.change);
        section_name =(TextView) findViewById(R.id.section_text);
        //Check if share preferenc exisits
        File f = new File("/data/data/" + getPackageName() +  "/shared_prefs/" + filename + ".xml");
        if(f.exists())
        {
            fileflag = true;
        }

        if(fileflag == true)
        {
            SharedPreferences section_id_prefs = getSharedPreferences("section_id", 0);
            int sec_id = section_id_prefs.getInt("section_id_key", 0); //0 is the default value
            String sec_idstring = Integer.toString(sec_id);
            section_name.setText("Section Id: "+sec_idstring);
        }
        else{
            section_name.setText("Section Id is not set");
        }

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Title");

// Set up the input
                final EditText input = new EditText(MainActivity.this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

// Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       String section_id = input.getText().toString();
                       int section_idint = Integer.parseInt(section_id);
                       section_name.setText("Section Id: "+section_id);
                        // Shared Preference
                        if(fileflag == false) {
                            Context context = MainActivity.this;
                            SharedPreferences sharedPref = context.getSharedPreferences("section_id", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putInt("section_id_key", section_idint);
                            editor.commit();
                        }
                        else{
                            MainActivity.this.getSharedPreferences("section_id",0)
                                    .edit()
                                    .putInt("section_id_key", section_idint)
                                    .apply();
                        }

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();

            }
        });


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fileflag == true) {
                    SharedPreferences section_id_prefs1 = getSharedPreferences("section_id", 0);
                    int sec_id = section_id_prefs1.getInt("section_id_key", 0); //0 is the default value
                    String sec_idstring1 = Integer.toString(sec_id);
                    Intent rIntent = new Intent(MainActivity.this, ReaderActivity.class);
                    rIntent.putExtra("section_id", sec_idstring1);
                    startActivity(rIntent);
                }
                else{
                    Toast.makeText(MainActivity.this, "First fill the Section Id", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}

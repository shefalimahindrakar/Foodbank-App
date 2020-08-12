package com.example.myfirstapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class DataSendActivity extends AppCompatActivity {
    private Button reset;
    private TextView status;
    private String jsonstring;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_send);
        Bundle data = getIntent().getExtras();
        jsonstring = data.getString("key");
        String jsondata = backslashstring(jsonstring);
        status = (TextView) findViewById(R.id.status);
        status.setText("a");

    }

    protected String backslashstring(String jsonstring)
    {
       String jsondata = "";

       for(int i=0; i<jsonstring.length();i++)
       {
           if(jsonstring.charAt(i) == '"')
           {
               jsondata = jsondata + "\\";
               jsondata = jsondata + jsonstring.charAt(i);

           }
           else{
               jsondata = jsondata + jsonstring.charAt(i);
           }
       }
       return jsondata;
    }
}

package com.example.myfirstapplication;

import android.app.Activity;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn, reset_button;
    private TextView textview1, textview2;
    private String Barcodevalue, jsonresponse;
    private String section_id = "0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        reset_button = (Button) findViewById(R.id.reset_button);
        section_id = getIntent().getStringExtra("section_id");

        //textview1 = (TextView) findViewById(R.id.BarCode);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents()==null){
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                Barcodevalue = result.getContents();
                //Sends HTTP request

                Thread thread = new Thread(new Runnable(){
                    @Override
                    public void run(){
                        //code to do the HTTP request
                        OkHttpClient client = new OkHttpClient();
                        final String url = "http://shefalimahindrakar.pythonanywhere.com/api/fetch/"+Barcodevalue;
                        final Request request = new Request.Builder().url(url).build();
                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                if(response.isSuccessful())
                                {
                                    final String myresponse = response.body().string();
                                    jsonresponse = backslashstring(myresponse);
                                    //textview2.setText(jsonresponse);
                                    RequestBody body = RequestBody.create(MediaType.parse("application/json"), myresponse);
                                    String url2 = "http://shefalimahindrakar.pythonanywhere.com/resource/"+section_id;
                                    Request request1 = new Request.Builder()
                                            .url(url2)
                                            .post(body)
                                            .build();
                                    final OkHttpClient client1 = new OkHttpClient();
                                    client1.newCall(request1).enqueue(new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                            e.printStackTrace();
                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                                            if(response.isSuccessful())
                                            {
                                                scan_btn.setVisibility(View.INVISIBLE);
                                                reset_button.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {

                                                        RequestBody body1 = RequestBody.create(MediaType.parse("application/json"), myresponse);
                                                        String url3 = "http://shefalimahindrakar.pythonanywhere.com/resource/delete/"+section_id;
                                                        Request request2 = new Request.Builder()
                                                                .url(url3)
                                                                .post(body1)
                                                                .build();
                                                        final OkHttpClient client2 = new OkHttpClient();
                                                        client2.newCall(request2).enqueue(new Callback() {
                                                            @Override
                                                            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                                                                e.printStackTrace();
                                                            }

                                                            @Override
                                                            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                                //restartActivity(ReaderActivity.this);
                                                                Intent nIntent = new Intent(ReaderActivity.this, MainActivity.class);
                                                                startActivity(nIntent);
                                                            }
                                                        });

                                                    }
                                                });

                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }
                });
                thread.start();







            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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

    public static void restartActivity(Activity activity){
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }


}






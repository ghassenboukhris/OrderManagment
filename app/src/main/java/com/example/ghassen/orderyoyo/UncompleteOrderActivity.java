package com.example.ghassen.orderyoyo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class UncompleteOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uncomplete_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        TextView test=(TextView) findViewById(R.id.textView4);
         Intent intent = getIntent();
        String getcode = intent.getExtras().getString("CountryCode");
        String getname = intent.getExtras().getString("CountryName");
        test.setText(getcode);
        if (getname.equals("Denmark"))
        test.setText("OrderId:36576\nCustomerName:Frank\nRestaurant:Egedals Pizzeria & Steak House\nOpenHours:From 11.30 to 21.30\nOrderId:36568\nCustomerName:Peter\nRestaurant:Kokkeriet\nOpenHours:From 18.00 to 01.00\nOrderId:36562CustomerName:Thomas\nRestaurant:Kadeau Kobenhagen\nOpenHours:From 18.30 to 00.00") ;

         if (getname.equals("England"))
            test.setText("OrderId:24856\nCustomerName:Steven\nRestaurant:The Five Fields\nOpenHours:From 10.30 to 21.30\nOrderId:24842\nCustomerName:Jack\nRestaurant:The Quicken Tree\n\"OpenHours:From 12.00 to 15.00 and From 18.00 to 22.00\nOrderId:24866\nCustomerName:Frank\nRestaurant:The Ledbury\nOpenHours:From 12.00 to 21.45");
        Button changecountry=(Button) findViewById(R.id.changecountry);
        changecountry.setText(getname);
        changecountry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent gotorecheck = new Intent(UncompleteOrderActivity.this, CountryCheckActivity.class);
                startActivity(gotorecheck);


            }
        });


        new DoGetAccessToken().execute();

    }
    class DoGetAccessToken extends AsyncTask<Void,Void,Void> {
        @Override
        protected  Void doInBackground(Void...params)
        {
            try {
                String retname = "";
                String retcode = "";

                try {
                    InputStream inputStreamtoken = getApplicationContext().openFileInput("tokenss"+".txt");

                    if ( inputStreamtoken != null ) {
                        InputStreamReader inputStreamReader = new InputStreamReader(inputStreamtoken);
                        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                        String receiveString = "";
                        StringBuilder stringBuilder = new StringBuilder();

                        while ( (receiveString = bufferedReader.readLine()) != null ) {
                            stringBuilder.append(receiveString);
                        }

                        inputStreamtoken.close();
                        retname = stringBuilder.toString();
                    }
                }
                catch (FileNotFoundException e) {
                    Log.e("login activity", "File not found: " + e.toString());
                } catch (IOException e) {
                    Log.e("login activity", "Can not read file: " + e.toString());
                }

                    Intent intent = getIntent();
                    String easyPuzzlee = intent.getExtras().getString("CountryCode");
                    System.out.println(easyPuzzlee);
                    URL urlcountry=new URL("https://iwaitersupportapi.azurewebsites.net/api/order/"+easyPuzzlee);
                System.out.println(urlcountry);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlcountry.openConnection();
                    httpURLConnection.addRequestProperty("Authorization", "Bearer "+retname);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream is = httpURLConnection.getInputStream();
                    int byteCharacterr;
                    String resultt="";
                    while ((byteCharacterr=is.read())!= -1){
                        resultt +=(char) byteCharacterr;
                    }
              Intent gototest=new Intent(UncompleteOrderActivity.this,TestActivity.class);
                gototest.putExtra("getTEst",resultt);
                startActivity(gototest);



            }
            catch(IOException e)
            {
                // Error
            }
            return null;
        }
    }

}

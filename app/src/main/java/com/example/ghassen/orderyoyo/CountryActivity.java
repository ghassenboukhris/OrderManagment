package com.example.ghassen.orderyoyo;

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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CountryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_class);
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
        TextView country=(TextView) findViewById(R.id.textView2);
        Intent intent = getIntent();
        String easyPuzzle = intent.getExtras().getString("epuzzle");
        country.setText(easyPuzzle);
        Button getcountry=(Button) findViewById(R.id.button2);
        getcountry.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DoGetCountry().execute();


            }
        });
    }
    class DoGetCountry extends AsyncTask<Void,Void,Void> {
        @Override
        protected  Void doInBackground(Void...params)
        {
            Intent intent = getIntent();
            String easyPuzzle = intent.getExtras().getString("epuzzle");
TextView get=(TextView) findViewById(R.id.textView2);



            try {
                URL url=new URL("https://iwaitersupportapi.azurewebsites.net/api/countries");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.addRequestProperty("Authorization", "Bearer" + "<"+easyPuzzle+">");
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream is = httpURLConnection.getInputStream();
                int byteCharacter;
                String result="";
                while ((byteCharacter=is.read())!= -1){
                    result +=(char) byteCharacter;
                }
                Intent i = new Intent(CountryActivity.this, TestActivity.class);
                i.putExtra("epuzzle", result);
                startActivity(i);
            }
            catch(IOException e)
            {
                // Error
            }
            return null;
        }
    }

}

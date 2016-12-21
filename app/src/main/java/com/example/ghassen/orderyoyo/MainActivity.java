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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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

        Button login=(Button) findViewById(R.id.button);
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //new DoGetAccessToken().execute();
                Intent i1=new Intent(MainActivity.this,CountryCheckActivity.class);
                startActivity(i1);


            }
        });
       final  CheckBox save=(CheckBox)findViewById(R.id.checkBox);
        final  CheckBox notsave=(CheckBox)findViewById(R.id.checkBox2);
        save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {

                    notsave.setEnabled(false); // disable checkbox
                }
                else
                {notsave.setEnabled(true);

                }
            }
        });
        notsave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                if (isChecked) {

                    save.setEnabled(false); // disable checkbox
                }
                else
                {save.setEnabled(true);

                }
            }
        });

    }

    class DoGetAccessToken extends AsyncTask<Void,Void,Void> {
        @Override
        protected  Void doInBackground(Void...params)
        {
            HttpURLConnection connection;
            OutputStreamWriter request = null;
            String response = null;
            String parameters = "username=User1&password=a9874b0f-69aa-4b89-808b-55835c56b847&grant_type=password";
            URL url = null;
            try {  url = new URL("https://iwaitersupportapi.azurewebsites.net/api/token ");
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestMethod("POST");

                request = new OutputStreamWriter(connection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();

                int byteCharacter;
                String result="";
                while ((byteCharacter=isr.read())!= -1){
                    result +=(char) byteCharacter;
                }
                String easyPuzzle  = result;
                try {

                    JSONObject jObj = new JSONObject(result);
                    String aJsonString = jObj.getString("access_token");
                    try {
                        OutputStreamWriter outputStreamWritertoken = new OutputStreamWriter(getApplicationContext().openFileOutput("tokenss.txt", Context.MODE_PRIVATE));
                        outputStreamWritertoken.write(aJsonString);
                        outputStreamWritertoken.close();
                    }
                    catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                    URL urlcountry=new URL("https://iwaitersupportapi.azurewebsites.net/api/countries");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) urlcountry.openConnection();
                    httpURLConnection.addRequestProperty("Authorization", "Bearer "+aJsonString);
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();
                    InputStream is = httpURLConnection.getInputStream();
                    int byteCharacterr;
                    String resultt="";
                    while ((byteCharacterr=is.read())!= -1){
                        resultt +=(char) byteCharacterr;
                    }
                       System.out.println(resultt);
                    JsonFactory f = new JsonFactory();
                    ObjectMapper mapper = new ObjectMapper();
                    JsonParser jp = f.createJsonParser(resultt);
                    // advance stream to START_ARRAY first:
                    jp.nextToken();
                    String data="";
                    String datacode="";
                 int j=0;
                    // and then each time, advance to opening START_OBJECT
                    while (jp.nextToken() == JsonToken.START_OBJECT) {

                        Map<String,Object> userData = mapper.readValue(jp, Map.class);
                        data=userData.get("name").toString();
                        datacode=userData.get("code").toString();
                        Context context;
                        try {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("config" + String.valueOf(j)+".txt", Context.MODE_PRIVATE));
                            outputStreamWriter.write(data);
                            outputStreamWriter.close();

                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }
                        try {
                            OutputStreamWriter outputStreamCodeWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("configcode" + String.valueOf(j)+".txt", Context.MODE_PRIVATE));
                            outputStreamCodeWriter.write(datacode);
                            outputStreamCodeWriter.close();

                        }
                        catch (IOException e) {
                            Log.e("Exception", "File write failed: " + e.toString());
                        }
                        j++;
                    }
                    try {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getApplicationContext().openFileOutput("numberofcountries.txt", Context.MODE_PRIVATE));
                        outputStreamWriter.write(String.valueOf(j));
                        outputStreamWriter.close();
                    }
                    catch (IOException e) {
                        Log.e("Exception", "File write failed: " + e.toString());
                    }
                    Intent i = new Intent(MainActivity.this, CountryCheckActivity.class);
                    i.putExtra("Country",data);
                    startActivity(i);

                } catch (JSONException e) {
                    Log.e("MYAPP", "unexpected JSON exception", e);
                    // Do something to recover ... or kill the app.
                }


            }
            catch(IOException e)
            {
                // Error
            }
            return null;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

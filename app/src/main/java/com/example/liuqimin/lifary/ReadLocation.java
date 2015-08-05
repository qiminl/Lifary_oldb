package com.example.liuqimin.lifary;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by User on 2015/8/1.
 */
public class ReadLocation {

    Context con;
    Communication com;
    public ReadLocation(Context c, Communication cc){

        Log.d("Lifary", "ReadLocation 1");
        con = c;
        Log.d("Lifary", "ReadLocation 2");
        com = cc;
        Log.d("Lifary", "ReadLocation 3");
        checkConnection();

        Log.d("Lifary", "ReadLocation 4");
    }



    public void getLocation(String lat, String lon) {
        Log.d("Lab11", "buttonGetWeater");
        new ReadlocationJSONDataTask().execute("http://api.geonames.org/findNearbyPlaceNameJSON?lat=" +
                lat +
                "&lng=" +
                lon +
                "&username=demo");

    }


    public void checkConnection(){
        Log.d("Lifary", "checkConnection");

        ConnectivityManager connectMgr =
                (ConnectivityManager)con.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.d("Lifary", "checkConnection 2");
        NetworkInfo networkInfo = null;
        try {
            networkInfo = connectMgr.getActiveNetworkInfo();

        }catch (Exception e){
            Log.d("Lifary", "ERROR: " + e.getLocalizedMessage());
        }
        Log.d("Lifary", "checkConnection 3");

        if(networkInfo != null && networkInfo.isConnected()){
            //fetch data

            String networkType = networkInfo.getTypeName().toString();
        }
        else {
            //display error
            Log.d("Lifary", "CONNECTION ERROR");
        }
    }


    private String readJSONData(String myurl) throws IOException {

        Log.d("Lab11", "readJsondata");

        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 2500;

        URL url = new URL(myurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try {
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d("tag", "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
                conn.disconnect();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {

        Log.d("Lab11", "readIt");

        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    private class ReadlocationJSONDataTask extends AsyncTask<String, Void, String> {


        Exception exception = null;

        protected String doInBackground(String... urls) {
            Log.d("Lab11", "doInBackground");
            Log.d("Lab11", "location url = " + urls[0]);
            try{
                String JSONResult = readJSONData(urls[0]);
                //    Log.d("Lab11", "JSONResult = " + JSONResult);

                return JSONResult;
            }catch(IOException e){
                exception = e;
            }
            return null;
        }

        protected void onPostExecute(String result) {

            Log.d("Lab11", "location onPostExecute");
            //    Log.d("Lab11", "onPostExecute result = " + result);
            try {

                Log.d("Lab11", "jsonObject");
                JSONObject jsonObject = new JSONObject(result);
                Log.d("Lab11", "weatherObservationItems");

                JSONArray weatherObservationItems = new JSONArray(jsonObject.getString("geonames"));

                Log.d("Lab11", "try to get location name");
                JSONObject observObject = weatherObservationItems.getJSONObject(0);

                String locName = observObject.getString("name");
                Log.d("Lab11", "locName = " + locName);
                com.com(locName);

            } catch (Exception e) {
                Log.d("Lab11", "errorrrr" + e.getLocalizedMessage());
                if(e.getLocalizedMessage().equals("No value for geonames")){
                    try {
                        JSONObject statusMsg = new JSONObject(result);
                        Toast.makeText(con, ""+statusMsg.getString("status"), Toast.LENGTH_LONG).show();
                    }catch (Exception err){
                        Log.d("Lab11", "ERROR: " + err.getLocalizedMessage());
                    }
                }
            }
        }

    }


}

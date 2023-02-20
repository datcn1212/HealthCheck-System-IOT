package com.example.iotproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.widget.Toast;

import com.example.iotproject.model.User;
import com.example.iotproject.view.MainView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GetUserInfo extends AsyncTask<String, String, String> {
    MainView activity = MainView.getInstance();
    ProgressDialog progressDialog;
    int responseCode;
    public GetUserInfo(MainView activity) {
        this.activity = activity;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // display a progress dialog to show the user what is happening
        progressDialog = new ProgressDialog(activity);
        progressDialog.show();

    }

    @Override
    protected String doInBackground(String... strings) {

        // Fetch data from the API in the background.

        String result = "";
        URL url;
        HttpsURLConnection urlConnection = null;

        try {

            url = new URL("https://iot-health.onrender.com/user/information");
            // Set property for connection
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("token", User.USER_TOKEN);

            InputStream in = urlConnection.getInputStream();
            InputStreamReader isw = new InputStreamReader(in);


            int data = isw.read();

            while (data != -1) {
                result += (char) data;
                data = isw.read();
            }

            System.out.println("Received data is" + result);
            responseCode = urlConnection.getResponseCode();
            System.out.println(urlConnection.getResponseCode() + urlConnection.getResponseMessage());

            urlConnection.disconnect();
        } catch (Exception e) {
//            e.printStackTrace();
            System.out.println("Error is " + e.getMessage());
            try {
                InputStream errorStream = urlConnection.getErrorStream();
                InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
                int data = errorStreamReader.read();
                while (data != -1) {
                    result += (char) data;
                    data = errorStreamReader.read();
                }
            } catch (IOException io) {
                io.printStackTrace();
            }

        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("Result is " + s);
        if (responseCode >= 400) {
            Toast.makeText(activity, "Token inccorrect", Toast.LENGTH_LONG).show();
        } else {
            try {
                JSONObject userInfo = new JSONObject(s);

                User.getInstance().getData(userInfo);

                new GetDeviceList(activity).execute();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
            // show results
            progressDialog.hide();
    }
}
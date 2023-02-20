package com.example.iotproject.api;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.iotproject.model.Device;
import com.example.iotproject.model.User;
import com.example.iotproject.view.MainView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class GetDeviceList extends AsyncTask<String, String, String> {

    MainView activity;
    ProgressDialog progressDialog;
    int responseCode;

    public GetDeviceList(MainView activity) {
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

            url = new URL("https://iot-health.onrender.com/heartSensor");
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

            System.out.println(urlConnection.getResponseCode() + urlConnection.getResponseMessage());

            responseCode = urlConnection.getResponseCode();

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
        // show results
        if (responseCode <= 400) {
            try {
                User.getInstance().setDevicesList(new ArrayList<>());
                JSONArray deviceList = new JSONArray(s);
                for (int i = 0; i < deviceList.length(); i++) {
                    JSONObject obj = deviceList.getJSONObject(i);
                    Device device = new Device();
                    device.getData(obj);
                    System.out.println("Device " + i + " " + device.getName() + " " + device.getId());
                    User.getInstance().getDevicesList().add(device);
                }
                activity.initData();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(activity, "Can not get device list", Toast.LENGTH_LONG).show();
        }
        progressDialog.hide();

    }
}

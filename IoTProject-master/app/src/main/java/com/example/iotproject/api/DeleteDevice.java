package com.example.iotproject.api;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.iotproject.model.Device;
import com.example.iotproject.model.User;
import com.example.iotproject.view.AddDeviceDialog;
import com.example.iotproject.view.DeviceInfoView;
import com.example.iotproject.view.MainView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DeleteDevice extends AsyncTask<String, String, String> {
    DeviceInfoView activity;
    ProgressDialog progressDialog;
    int responseCode;

    public DeleteDevice(DeviceInfoView activity) {
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

            url = new URL("https://iot-health.onrender.com/heartSensor/" + strings[0]);



            // Set property for connection
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("token", User.USER_TOKEN);
            urlConnection.setDoOutput(true);
            //
            InputStream in = urlConnection.getInputStream();

            InputStreamReader isw = new InputStreamReader(in);

            int data = isw.read();

            while (data != -1) {
                result += (char) data;
                data = isw.read();
            }

            responseCode = urlConnection.getResponseCode();
            System.out.println("Received data is" + result);

            urlConnection.disconnect();

        } catch (Exception e) {
            try {
                result = String.valueOf(urlConnection.getResponseCode());
                responseCode = urlConnection.getResponseCode();
                if (result.equals("440"))
                    result = "Thiết bị không tìm thấy";
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            e.printStackTrace();
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("Result is " + s + responseCode);
        // show results
        if (responseCode < 400) {
            progressDialog.hide();
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(activity, MainView.class);
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            progressDialog.hide();
        }
    }
}

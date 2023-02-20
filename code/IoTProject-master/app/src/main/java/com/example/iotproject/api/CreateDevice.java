package com.example.iotproject.api;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.iotproject.model.Device;
import com.example.iotproject.model.User;
import com.example.iotproject.view.AddDeviceDialog;
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

public class CreateDevice extends AsyncTask<String, String, String> {

    AddDeviceDialog activity;
    ProgressDialog progressDialog;
    int responseCode;

    public CreateDevice(AddDeviceDialog activity) {
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

            // Params list
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("Id", strings[0]);
            params.put("name", strings[1]);
            if (strings.length >= 3)
                params.put("info", strings[2]);

            // Convert map to String
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));

            }

            // Convert string to byte
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            // Set property for connection
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            urlConnection.setRequestProperty("token", User.USER_TOKEN);
            urlConnection.setDoOutput(true);
            urlConnection.getOutputStream().write(postDataBytes);
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
                if (result.equals("450"))
                    result = "Id đã tồn tại";
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
            Device device = new Device();
            try {
                device.getData(new JSONObject(s));
                User.getInstance().getDevicesList().add(device);
                progressDialog.hide();
                Intent intent = new Intent(activity, MainView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
            progressDialog.hide();
        }
    }
}

package com.example.iotproject.api;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class RegisterUser extends AsyncTask<String, String, String> {

    Activity activity;
    ProgressDialog progressDialog;
    public RegisterUser(Activity activity) {
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

            url = new URL("https://iot-health.onrender.com/user/register");

            // Params list
            Map<String, Object> params = new LinkedHashMap<>();
            params.put("username", strings[0]);
            params.put("password", strings[1]);
            params.put("name", strings[2]);
            params.put("age", strings[3]);
            params.put("weight", strings[4]);
            params.put("height", strings[5]);

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

            System.out.println("Received data is" + result);

            urlConnection.disconnect();

        } catch (Exception e) {
            try {
                result = urlConnection.getResponseMessage();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }

        return result;
    }

    @Override
    protected void onPostExecute(String s) {
        System.out.println("Result is " + s);
        progressDialog.hide();
        Toast.makeText(activity, s, Toast.LENGTH_LONG).show();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        // show results
    }
}
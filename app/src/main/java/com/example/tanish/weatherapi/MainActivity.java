/*
* Weather Map API APP
*/
package com.example.tanish.weatherapi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Submit = findViewById(R.id.Submit);
        final TextView Temperature = findViewById(R.id.Temperature);


        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = getTemp();
                Temperature.setText(temp);
            }
        });
    }

    EditText City = findViewById(R.id.CityName);
    private String city = City.toString();
    private String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&APPID=ec1a1af995d8ce805dcad33a60e41892";

    public String getTemp() {
        String temp = "";
        try {
            URL tempUrl = new URL(url);
            String json = null;
            try {
                json = setupHTTP_connection(tempUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            temp = parseJSON(json);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    private String setupHTTP_connection(URL url) throws IOException {
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        String JSON_response = "";
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(15000);
            connection.setConnectTimeout(15000);
            connection.connect();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                JSON_response = setupInputStream(inputStream);
            } else {
                Log.d("Network Utils", "Error Message");
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null)
                inputStream.close();
            assert connection != null;
            connection.disconnect();
        }
        return JSON_response;
    }

    private String setupInputStream(InputStream inputStream) {
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        StringBuilder stringBuilder;
        stringBuilder = new StringBuilder();
        String JSONresponse = "";
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
            JSONresponse = bufferedReader.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return JSONresponse;
    }

    private String parseJSON(String json) {
        String temp = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject main = jsonObject.getJSONObject("main");
            temp = main.getString("temp");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return temp;
    }
}

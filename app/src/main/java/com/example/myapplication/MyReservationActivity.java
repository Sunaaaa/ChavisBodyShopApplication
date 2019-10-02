package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.BODYINFO.SaveSharedPreference;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ListView listView;
        TextView textView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_reservation);


        listView = (ListView)findViewById(R.id.reservaionList);
        textView = (TextView)findViewById(R.id.bs_name);
        textView.setText(SaveSharedPreference.getBodyShopId(MyReservationActivity.this));


    }

    private String sendPost(String id, String pw) throws Exception {

        String receivedata;

        URL url = new URL("http://70.12.115.57:9090/TestProject/blist.do");
//        URL url = new URL("http://70.12.115.73:9090/Chavis/Member/view.do");  // 한석햄

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("charset", "utf-8");
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

        Map<String, String> map = new HashMap<String, String>();

        map.put("id", id);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);

        Log.i("msi", "가랏 데이터 : " + json);

        osw.write(json);
        osw.flush();

        Log.i("msi", "222");
        int responseCode = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        receivedata = response.toString();
        in.close();
        Log.i("KAKAOBOOKLog22", receivedata);

        Log.i("오은애", "오은애");


        return receivedata;
    }
}

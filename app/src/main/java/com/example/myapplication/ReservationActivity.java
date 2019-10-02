package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        Button btn = (Button) findViewById(R.id.btn_login);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Thread wThread = new Thread() {      // UI 관련작업 아니면 Thread를 생성해서 처리해야 하는듯... main thread는 ui작업(손님접대느낌) 하느라 바쁨
                        public void run() {
                            try {
                                EditText userId = (EditText) findViewById(R.id.userId);
                                EditText userpw = (EditText) findViewById(R.id.userPw);

                                 String id = userId.getText().toString();
                                 String pw = userpw.getText().toString();
                                sendPost(id, pw);
                                Log.i("msi", id);
                            } catch (Exception e) {
                                Log.i("msi", e.toString());
                            }
                        }
                    };
                    wThread.start();

                    try {
                        wThread.join();
                    } catch (Exception e) {
                        Log.i("msi", "이상이상22");
                    }
                } catch (Exception e) {
                    Log.i("msi", e.toString());
                }

            }


        });
    }

    private String sendPost(String id, String pw) throws Exception {

        String receivedata;
        String sendMsg;

        URL url = new URL("http://70.12.115.57:9090/TestProject/blogin.do");
//        URL url = new URL("http://70.12.115.73:9090/Chavis/Member/view.do");  // 한석햄

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("charset", "utf-8");
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

        Map<String, String> map = new HashMap<String, String>();


        map.put("id", id);
        map.put("pw", pw);

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

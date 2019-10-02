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

public class MyReservationActivity extends AppCompatActivity {

    class ReservationRunnable implements Runnable{

        String bodyshop_id;
        Handler handler;

        public ReservationRunnable(String bodyshop_id, Handler handler) {
            this.bodyshop_id = bodyshop_id;
            this.handler = handler;
        }

        @Override
        public void run() {

            String sendMsg = "";
//
//        // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
            URL url = null;
            try {
                url = new URL("http://70.12.115.57:9090/TestProject/bodyshoplogin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "id=" + bodyshop_id ;

                Log.i("msi", sendMsg);

                osw.write(sendMsg);
                osw.flush();

                String input = "";
                String result = "";
                StringBuffer sb = new StringBuffer();
                // stream을 통해 data 읽어오기
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                while ((input = br.readLine()) != null) {
                    sb.append(input);
                    result += input;
                }
                Log.i("what?",result);
//                ObjectMapper mapper = new ObjectMapper();
//                ArrayList<ReservationDTO> myreservation = mapper.readValue(result, new TypeReference<ArrayList<ReservationDTO>>() {});
//
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("Reserv_List", myreservation);
//                Message message = new Message();
//                message.setData(bundle);
//
//                handler.sendMessage(message);
//
//                if (myreservation.get(0).getBodyshop_id().equals("0")){
//
//                } else {
//                    Log.i("what?","로그인 성공");
//                    ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.MyReservationActivity");
//
//                }
                br.close();
                conn.disconnect();

            } catch (IOException e) {
                Log.i("MY_RESERVATION","서버 데이터 주고 받기 에러");
                e.printStackTrace();
            }

        }
    }

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
}

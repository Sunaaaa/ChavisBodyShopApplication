package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DTO.BodyShopDTO;
import com.example.myapplication.DTO.ReservationListDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReservationStatusActivity extends AppCompatActivity {


    class MyReservationRunnable implements Runnable{
        private String id;
        private Handler handler;

        public MyReservationRunnable(String id, Handler handler) {
            this.id = id;
            this.handler = handler;
        }

        @Override
        public void run() {
            String receivedata = "";
            URL url = null;
            try {
                url = new URL("http://70.12.115.57:9090/TestProject/blist.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                Map<String, String> map = new HashMap<String, String>();

                map.put("bodyshop_no", id);

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
                ArrayList<ReservationListDTO> myObject = mapper.readValue(receivedata, new TypeReference<ArrayList<ReservationListDTO>>() {});
                in.close();
                Log.i("ReservationList__", receivedata);
                Log.i("ReservationList__", myObject.get(1).getMember_mname());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("reservation_list", myObject);

                Message message = new Message();
                message.setData(bundle);
                Log.i("ReservationList__", String.valueOf(myObject.size()));

                handler.sendMessage(message);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_status);

        TextView shop_name = (TextView)findViewById(R.id.shop_name);
        listView = (ListView)findViewById(R.id.reservation_listview);

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String myObject = preferences.getString("myObject", "NO");

        Gson gson = new Gson();
        BodyShopDTO bodyShopDTO = gson.fromJson(myObject, BodyShopDTO.class);

        Log.i("정비소 이름 들어옴", bodyShopDTO.getBodyshop_name());
        shop_name.setText(bodyShopDTO.getBodyshop_name());
        final ReservationAdapter adapter = new ReservationAdapter();

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                ArrayList<ReservationListDTO> result = bundle.getParcelableArrayList("reservation_list");
                for (ReservationListDTO dto : result){
                    Log.i("정비소 이름 들어옴", dto.getMember_mname());
                    adapter.addItem(dto);
                }
                listView.setAdapter(adapter);
            }
        };

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getApplicationContext(), adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                ReservationListDTO reservationListDTO = adapter.getItem(position);
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.SignatureActivity");
            }
        });

        Log.i("msi", "쓰레드 스타트 해봅시다");

        MyReservationRunnable myReservationRunnable = new MyReservationRunnable(bodyShopDTO.getBodyshop_no(), handler);
        Thread thread = new Thread(myReservationRunnable);
        thread.start();
        Log.i("msi", "쓰레드 스타트 함!");

    }
}

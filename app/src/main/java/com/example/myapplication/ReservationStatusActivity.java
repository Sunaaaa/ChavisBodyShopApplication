package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.security.AccessController.getContext;

public class ReservationStatusActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    SharedPreferences preferences;
    String flag = "", repair_t, repair_p, reservation_n;

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
                url = new URL("http://70.12.115.73:9090/Chavis/Bodyshop/blist.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                Map<String, String> map = new HashMap<String, String>();

                map.put("bodyshop_no", id);

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(map);

                Log.i("FIRST", "가랏 데이터 : " + json);

                osw.write(json);
                osw.flush();

                Log.i("FIRST", "데이터 받기 ");
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
                Log.i("ReservationList__FIRST", receivedata);
                Log.i("ReservationList__FIRST", myObject.get(1).getMember_mname());
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("reservation_list", myObject);

                Message message = new Message();
                message.setData(bundle);
                Log.i("ReservationList__FIRST", String.valueOf(myObject.size()));

                handler.sendMessage(message);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    class ChangeReservationRunnable implements Runnable{
        private String bodyshop_no;
        private String reservation_no;
        private String rtime;
        private String rperson;

        public ChangeReservationRunnable(String bodyshop_no, String reservation_no, String rtime, String rperson) {
            this.bodyshop_no = bodyshop_no;
            this.reservation_no = reservation_no;
            this.rtime = rtime;
            this.rperson = rperson;
        }

        @Override
        public void run() {
            String newreceivedata = "";
            URL url = null;
            URL url2 = null;
            try {
                url = new URL("http://70.12.115.73:9090/Chavis/Reservation/finishrepair.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                Map<String, String> map = new HashMap<String, String>();

                map.put("bodyshop_no", bodyshop_no);
                map.put("reservation_no", reservation_no);
                map.put("repaired_time", rtime);
                map.put("repaired_person", rperson);

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(map);

                Log.i("SECOND", "가랏 데이터 : " + json);

                osw.write(json);
                osw.flush();
                Log.i("SECOND", "가랏 데이터 결과 : " + conn.getResponseCode());
//
//                Log.i("msi", "222");
//                int responseCode = conn.getResponseCode();
//                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                String inputLine;
//                StringBuffer response = new StringBuffer();
//
//                while ((inputLine = in.readLine()) != null) {
//                    response.append(inputLine);
//                }
//
//                newreceivedata = response.toString();
//                ArrayList<ReservationListDTO> myObject = mapper.readValue(newreceivedata, new TypeReference<ArrayList<ReservationListDTO>>() {});
//                in.close();
//                Log.i("ReservationList_NEW_", newreceivedata);
//                Log.i("ReservationList_NEW_", myObject.get(1).getMember_mname());
//                Bundle bundle = new Bundle();
//                bundle.putParcelableArrayList("reservation_list", myObject);
//
//                Message message = new Message();
//                message.setData(bundle);
//                Log.i("ReservationList__", String.valueOf(myObject.size()));
//
//                handler.sendMessage(message);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    BodyShopDTO bodyShopDTO;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ListView listView;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_status);

        backPressCloseHandler = new BackPressCloseHandler(this);

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        TextView shop_name = (TextView)findViewById(R.id.shop_name);
        TextView today = (TextView)findViewById(R.id.today);
        today.setText(sdf.format(date));

        listView = (ListView)findViewById(R.id.reservation_listview);

        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String myObject = preferences.getString("myObject", "NO");

        Gson gson = new Gson();
        final BodyShopDTO bodyShopDTO = gson.fromJson(myObject, BodyShopDTO.class);
//        final BodyShopDTO bodyShopDTO = gson.fromJson(myObject, BodyShopDTO.class);

        Log.i("FIRST", bodyShopDTO.getBodyshop_name());
        shop_name.setText(bodyShopDTO.getBodyshop_name());
        final ReservationAdapter adapter = new ReservationAdapter();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                ArrayList<ReservationListDTO> result = bundle.getParcelableArrayList("reservation_list");

                for (ReservationListDTO dto : result){
                    Log.i("FIRST", dto.getMember_mname());
                    adapter.addItem(dto);
                }

                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }
        };

        Log.i("FIRST", "쓰레드 스타트 해봅시다");

        MyReservationRunnable myReservationRunnable = new MyReservationRunnable(bodyShopDTO.getBodyshop_no(), handler);
        Thread thread = new Thread(myReservationRunnable);
        thread.start();
        Log.i("FIRST", "쓰레드 스타트 함!");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), adapter.getItem(position).getMember_mname(), Toast.LENGTH_SHORT).show();
                ReservationListDTO reservationListDTO = adapter.getItem(position);

                Log.i("reservation_item", reservationListDTO.getMember_mname());
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.CarKeyActivity");
                intent.setComponent(componentName);
                intent.putExtra("reservation_info", reservationListDTO);
                startActivityForResult(intent, 5050);
                // startActivity(intent);

            }
        });

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MyReservationRunnable myReservationRunnable = new MyReservationRunnable(bodyShopDTO.getBodyshop_no(), handler);
        Thread thread = new Thread(myReservationRunnable);
        thread.start();
    }
}

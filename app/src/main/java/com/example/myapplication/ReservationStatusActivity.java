package com.example.myapplication;

import androidx.annotation.NonNull;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final ListView listView;

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_status);

        TextView shop_name = (TextView)findViewById(R.id.shop_name);
        TextView today = (TextView)findViewById(R.id.today);
        today.setText(sdf.format(date));

        listView = (ListView)findViewById(R.id.reservation_listview);

        preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        String myObject = preferences.getString("myObject", "NO");

        Gson gson = new Gson();
        final BodyShopDTO bodyShopDTO = gson.fromJson(myObject, BodyShopDTO.class);

        Log.i("FIRST", bodyShopDTO.getBodyshop_name());
        shop_name.setText(bodyShopDTO.getBodyshop_name());
        final ReservationAdapter adapter = new ReservationAdapter();

        final Handler handler = new Handler(){
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
//                ReservationListDTO reservationListDTO = new ReservationListDTO();
                ReservationListDTO reservationListDTO = adapter.getItem(position);

                Log.i("msi", reservationListDTO.getMember_mname());

//                if (reservationListDTO.getRepaired_time().equals("NO")){
                if (reservationListDTO.getRepaired_time() == null){
                    makeDialog(reservationListDTO, bodyShopDTO);

//                    ChangeReservationRunnable changeReservationRunnable = new ChangeReservationRunnable(bodyShopDTO.getBodyshop_no(), reservation_n, repair_t, repair_p, handler);
//                    Thread thread = new Thread(changeReservationRunnable);
//                    thread.start();

                } else {
                    checkDialog(reservationListDTO);
                }

                Log.i("RESERVATION_LIST_FLAG", flag);

//                if (flag.equals("true")){
//                    ChangeReservationRunnable changeReservationRunnable = new ChangeReservationRunnable(bodyShopDTO.getBodyshop_no(), reservationListDTO.getReservation_no(), reservationListDTO.getRepaired_time(), reservationListDTO.getRepaired_person(), handler);
//                    Thread thread = new Thread(changeReservationRunnable);
//                    thread.start();
//                }
            }
        });

    }

    public void makeDialog(final ReservationListDTO reservationListDTO, final BodyShopDTO bDTO) {
        final EditText personname = new EditText(getApplicationContext());
        personname.setSingleLine(true);
        final TextView day = new TextView(getApplicationContext());

        long now2 = System.currentTimeMillis();
        final Date date2 = new Date(now2);
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        final AlertDialog.Builder alert = new AlertDialog.Builder(ReservationStatusActivity.this);
        alert.setTitle(reservationListDTO.getMember_mname() + " 님의 차량 수리 확인");
        Log.i("나와라 예약 넘버여", reservationListDTO.getMember_mname());
        alert.setMessage(sdf.format(date2));
        alert.setView(personname);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final ImageView repair_status = (ImageView)findViewById(R.id.repair_status);
                if (personname.getText().toString().length()!=0){
                    // 채우세요 하는 다이어로드 띄우기
                    flag = "true";
                    repair_t = sdf.format(date2);
                    repair_p = personname.getText().toString();
                    reservation_n = reservationListDTO.getReservation_no();
                    Log.i("SECOND_ChangeR", "FALSE");
                    Log.i("SECOND_ChangeR", "데이터 변경 합니다.");
                    Log.i("RESERVATION_LIST_FLAG", repair_p);
                    Log.i("RESERVATION_LIST_FLAG", repair_t);
                    Log.i("RESERVATION_LIST_FLAG", repair_t);
                    Log.i("RESERVATION_LIST_FLAG", reservationListDTO.getReservation_no());

                    ChangeReservationRunnable changeReservationRunnable = new ChangeReservationRunnable(bDTO.getBodyshop_no(), reservation_n, repair_t, repair_p);
                    Thread thread = new Thread(changeReservationRunnable);
                    thread.start();

                    dialog.dismiss();
                } else {
                    flag = "false";
//                    Log.i("flag", flag);
                    dialog.dismiss();   //닫기
                }
            }
        });
        alert.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();   //닫기
            }
        });
        alert.show();
    }

    public void checkDialog(ReservationListDTO reservationListDTO) {
//        final List<String> listItems = new ArrayList<>();
//        listItems.add(reservationListDTO.getRepaired_time());
//        listItems.add(reservationListDTO.getRepaired_person());
//        final CharSequence[] items = listItems.toArray(new String[ listItems.size()]);

        final TextView car = (TextView)findViewById(R.id.repair_car);
        final TextView time = (TextView)findViewById(R.id.repair_time);
        final TextView name = (TextView)findViewById(R.id.repair_name);



        AlertDialog.Builder alert = new AlertDialog.Builder(ReservationStatusActivity.this);
        alert.setTitle(reservationListDTO.getMember_mname() + " 님의 수리 확인");
//        alert.setItems(items, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
        alert.setView(R.layout.repair_check);
        car.setText(reservationListDTO.getCar_type() + " ( " + reservationListDTO.getCar_id() + " ) ");
        time.setText(reservationListDTO.getRepaired_time());
        name.setText(reservationListDTO.getRepaired_person());

        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.show();
    }
}

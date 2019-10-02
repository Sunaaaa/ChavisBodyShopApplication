package com.example.myapplication;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.BODYINFO.SaveSharedPreference;
import com.example.myapplication.DTO.BodyShopDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    BodyShopDTO dto = null;
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

                                dto = sendPost(userId.getText().toString(), userpw.getText().toString());
                                Log.i("LOGIN", dto.getBodyshop_id());

                                if (dto.getBodyshop_id().equals("NO")){
                                    makeDialog();
                                } else {
                                    Intent intent = new Intent();
                                    ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.MyReservationActivity");
                                    intent.setComponent(componentName);
                                    intent.putExtra("data", dto);
                                    startActivity(intent);
                                    Log.i("LOGIN", dto.getBodyshop_id());
                                    Log.i("msi", "로그인 성공!!");
                                }
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

    public void makeDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("긁적 로그인 실패했는디 다시 로그인 해볼라요?");
        alert.show();
    }

    private BodyShopDTO sendPost(String id, String pw) throws Exception {

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
        Log.i("LOGIN", receivedata);
        BodyShopDTO myObject = mapper.readValue(receivedata, new TypeReference<BodyShopDTO>() {});
        Log.i("LOGIN", myObject.getBodyshop_id());
        Log.i("오은애", "오은애");
//        if (myObject.getBodyshop_id().equals("0")) {
//
//        }else {
//            SaveSharedPreference.setBodyShopNo(LoginActivity.this, myObject.getBodyshop_no());
//            SaveSharedPreference.setBodyShopId(LoginActivity.this, myObject.getBodyshop_id());
//            SaveSharedPreference.setBodyShopPw(LoginActivity.this, myObject.getBodyshop_pw());
//            SaveSharedPreference.setBodyShopName(LoginActivity.this, myObject.getBodyshop_name());
//            SaveSharedPreference.setBodyShopAddress(LoginActivity.this, myObject.getBodyshop_address());
//            SaveSharedPreference.setBodyShopLat(LoginActivity.this, myObject.getBodyshop_lat());
//            SaveSharedPreference.setBodyShopLong(LoginActivity.this, myObject.getBodyshop_long());
//            Log.i("오은애", SaveSharedPreference.getBodyShopName(LoginActivity.this));




        return myObject;
    }
    // 값 불러오기
    private void getPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        pref.getString("hi", "");
    }

    // 값 저장하기
    private void savePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("hi", "인사잘하네");
        editor.commit();
    }

    // 값(Key Data) 삭제하기
    private void removePreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("hi");
        editor.commit();
    }

    // 값(ALL Data) 삭제하기
    private void removeAllPreferences(){
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
    }

}

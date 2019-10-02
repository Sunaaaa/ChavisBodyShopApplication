package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.myapplication.BODYINFO.SaveSharedPreference;
import com.example.myapplication.DTO.BodyShopDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    private Intent intent;
    EditText id_EditText, pw_EditText;

    class LoginRunnable implements Runnable{

        private String id;
        private String pw;

        public LoginRunnable(String id, String pw) {
            this.id = id;
            this.pw = pw;
        }

        @Override
        public void run() {

            String sendMsg = "";
//
//        // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)
            URL url = null;
            try {
                url = new URL("http://70.12.115.57:9090/TestProject/blogin.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "id=" + id + "&pw=" + pw;

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
                ObjectMapper mapper = new ObjectMapper();
                BodyShopDTO bodyShop = mapper.readValue(result, new TypeReference<BodyShopDTO>() {});

                if (bodyShop.getBodyshop_id().equals("NO")){
                    makeDialog();
                    Log.i("what?","긁적 여기 올 곳이 아닌디 우째 왔댜 ");
                } else {
                    Log.i("what?","로그인 성공");
                    ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.MyReservationActivity");
                    SaveSharedPreference.setBodyShopNo(LoginActivity.this, bodyShop.getBodyshop_no());
                    SaveSharedPreference.setBodyShopId(LoginActivity.this, bodyShop.getBodyshop_id());
                    SaveSharedPreference.setBodyShopPw(LoginActivity.this, bodyShop.getBodyshop_pw());
                    SaveSharedPreference.setBodyShopName(LoginActivity.this, bodyShop.getBodyshop_name());
                    SaveSharedPreference.setBodyShopAddress(LoginActivity.this, bodyShop.getBodyshop_address());
                    SaveSharedPreference.setBodyShopLat(LoginActivity.this, bodyShop.getBodyshop_lat());
                    SaveSharedPreference.setBodyShopLong(LoginActivity.this, bodyShop.getBodyshop_long());
//
//                    Log.i("aa", SaveSharedPreference.getBodyShopId(LoginActivity.this));
//                    Log.i("aa", bodyShop.getBody_id());

                    intent.setComponent(componentName);
                    startActivity(intent);
                    finish();
                }
                br.close();
                conn.disconnect();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void makeDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("긁적 다시 로그인 해볼래요?");
        alert.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id_EditText = (EditText)findViewById(R.id.userId);
        pw_EditText = (EditText)findViewById(R.id.userPw);

        Button btn_login = (Button)findViewById(R.id.btn_login);
        Button btn_resist = (Button)findViewById(R.id.btn_resist);


        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);

            }
        };


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginRunnable mLoginRunnable = new LoginRunnable(id_EditText.getText().toString(), pw_EditText.getText().toString());
                Thread t = new Thread(mLoginRunnable);
                t.start();
            }
        });

        btn_resist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent();
                ComponentName cname = new ComponentName("com.example.myapplication", "com.example.myapplication.RegistActivity");
                mIntent.setComponent(cname);
                startActivity(mIntent);
            }
        });
    }
}

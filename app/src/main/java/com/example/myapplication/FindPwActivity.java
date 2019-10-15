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

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FindPwActivity extends AppCompatActivity {

    class FindPwRunnable implements Runnable{

        private String name;
        private String id;
        private Handler handler;

        public FindPwRunnable(String name, String id, Handler handler) {
            this.name = name;
            this.id = id;
            this.handler = handler;
        }

        @Override
        public void run() {
            String receivedata;
            String sendMsg = "";

            try {
                URL url = new URL("http://70.12.115.63:9090/Chavis/Bodyshop/findpw.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                Map<String, String> map = new HashMap<String, String>();

                map.put("name", name);
                map.put("id", id);

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(map);

                Log.i("msi", "가랏 데이터 : " + json);

                osw.write(json);
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
                Log.i("what?", result);

                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                Message message = new Message();
                message.setData(bundle);

                br.close();
                conn.disconnect();


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Button btn_gofpw;
    EditText fpwname, fpwid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pw);

        fpwname = (EditText)findViewById(R.id.fpwname);
        fpwid = (EditText)findViewById(R.id.fpwid);

        btn_gofpw = (Button)findViewById(R.id.btn_gofpw);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String result = bundle.getString("result");
                if (result.equals("NO BODYSHOP")){
                    findSuccessDialog(result);
                } else {
                    findFailDialog(result);
                }
            }
        };

        btn_gofpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FindPwRunnable findpwRunnable = new FindPwRunnable(fpwname.getText().toString(), fpwid.getText().toString() , handler);
                Thread thread = new Thread(findpwRunnable);
                thread.start();

            }
        });

    }

    // 찾기 성공
    public void findSuccessDialog(String result) {
        AlertDialog.Builder alert = new AlertDialog.Builder(FindPwActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.LoginActivity");
                intent.setComponent(componentName);
                startActivity(intent);
                Log.i("msi", "회원가입 성공!!");
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("비밀번호는 " + result + " 입니다." + "\n" + " 로그인 페이지로 이동합니다.");
        alert.show();
    }

    // 찾기 실패
    public void findFailDialog(String result) {
        AlertDialog.Builder alert = new AlertDialog.Builder(FindPwActivity.this);
        alert.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.LoginActivity");
                intent.setComponent(componentName);
                startActivity(intent);
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("없는 정보입니다. 다시 입력하시겠습니까?");
        alert.show();
    }


}

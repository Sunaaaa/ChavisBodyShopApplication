package com.example.myapplication;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.DTO.BodyShopDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    BodyShopDTO dto = null;
    EditText userId, userpw;
    TextView fid, fpw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btn_login = (Button) findViewById(R.id.btn_login);
        Button btn_regist = (Button) findViewById(R.id.btn_regist);
        userId = (EditText) findViewById(R.id.userId);
        userpw = (EditText) findViewById(R.id.userPw);
        fid = (TextView)findViewById(R.id.fid);
        fpw = (TextView)findViewById(R.id.fpw);

        fid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.FindIDActivity");
                i.setComponent(componentName);
                startActivity(i);
            }
        });

        fpw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.FindPwActivity");
                i.setComponent(componentName);
                startActivity(i);
            }
        });

        final InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.layout);

        // editText가 아닌 다른 곳을 터치하면 키보드 숨기기 기능 발동
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(userId.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(userpw.getWindowToken(),0);
            }
        });
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent doRegistIntent = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication","com.example.myapplication.RegistActivity");
                doRegistIntent.setComponent(componentName);
                startActivity(doRegistIntent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("LoginAcitivty", "로그인 버튼 눌렀다. ");
                try {
                    Log.i("LoginAcitivty", "여기서 죽니");
                    Thread wThread = new Thread() {      // UI 관련작업 아니면 Thread를 생성해서 처리해야 하는듯... main thread는 ui작업(손님접대느낌) 하느라 바쁨
                        public void run() {
                            try {
                                dto = sendPost(userId.getText().toString(), userpw.getText().toString());
                                Log.i("LOGIN", dto.getBodyshop_id());

                                if (dto.getBodyshop_id().equals("NO")) {
                                    makeDialog();
                                } else {

                                    // 서비스 실행
                                    Intent i = new Intent();
                                    ComponentName sComponentName = new ComponentName("com.example.myapplication", "com.example.myapplication.BodyShopService");
                                    i.setComponent(sComponentName);
                                    startService(i);

                                    Intent intent = new Intent();
                                    ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.ReservationStatusActivity");
                                    intent.setComponent(componentName);
                                    intent.putExtra("data", dto);
                                    startActivity(intent);
                                    Log.i("LOGIN", dto.getBodyshop_id());
                                    Log.i("msi", "로그인 성공!!");
                                }
                            } catch (Exception e) {
                                Log.i("LoginAcitivty_HERE", e.toString());
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

        Log.i("sendPost", "sendPost 들어왔다 1" );

        String receivedata;
        String sendMsg;

//        URL url = new URL("http://70.12.115.57:9090/TestProject/blogin.do");
        URL url = new URL("http://70.12.115.63:9090/Chavis/Bodyshop/login.do");
//        URL url = new URL("http://70.12.115.73:9090/Chavis/Member/view.do");  // 한석햄

        Log.i("sendPost", "sendPost 들어왔다 2" );

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
//        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("charset", "utf-8");
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

        Map<String, String> map = new HashMap<String, String>();

        map.put("id", id);
        map.put("pw", pw);

        Log.i("sendPost", "sendPost 들어왔다 3" );

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
        BodyShopDTO myObject = mapper.readValue(receivedata, new TypeReference<BodyShopDTO>() {
        });
        Log.i("LOGIN", myObject.getBodyshop_id());

        // 자동 로그인 등록
        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String bodyshopjson = gson.toJson(myObject);
        editor.putString("myObject", bodyshopjson);
        editor.commit();
        Log.i("LOGIN_ADD_SharedPref", "로그인 객체 저장 성공");
        return myObject;
    }

}

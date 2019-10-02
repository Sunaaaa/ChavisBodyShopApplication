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
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.myapplication.BODYINFO.SaveSharedPreference;
import com.example.myapplication.DTO.BodyShopDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RegistActivity extends AppCompatActivity {

    String id = "", pw = "";
    EditText res_id_ET, reg_pw_ET, detailAddress_ET;
    ArrayAdapter<CharSequence> adspin1, adspin2;
    Spinner spin1, spin2;
    String choice_do = "", choice_se="", address = "";

    class RegistRunnable implements Runnable{

        Intent intent = new Intent();

        private String resid;
        private String respw;
        private String resaddress;

        public RegistRunnable(String resid, String respw, String resaddress) {
            this.resid = resid;
            this.respw = respw;
            this.resaddress = resaddress;
        }

        @Override
        public void run() {
            String receivedata;
            String sendMsg = "";
//
//        // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)

            try {
                URL url = new URL("http://70.12.115.57:9090/TestProject/bodyshoplogin");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                sendMsg = "id=" + resid + "&pw=" + respw + "&address=" + resaddress;

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

//                if (bodyShop.getBodyshop_num().equals("0")){
//                    makeDialog();
//                    Log.i("what?","긁적 여기 올 곳이 아닌디 우째 왔댜 ");
//                } else {
//                    ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.LoginActivity");
//                    intent.setComponent(componentName);
//                    startActivity(intent);
//                    finish();
//                }
                br.close();
                conn.disconnect();



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        res_id_ET = (EditText)findViewById(R.id.reg_Id);
        reg_pw_ET = (EditText)findViewById(R.id.reg_pw);
        detailAddress_ET = (EditText)findViewById(R.id.detailAddress);
        spin1 = (Spinner)findViewById(R.id.spinner);
        spin2 = (Spinner)findViewById(R.id.spinner2);

        adspin1 = ArrayAdapter.createFromResource(this, R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adspin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adspin1.getItem(position).equals("서울")){
                    choice_do = "서울 특별시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_seoul, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("인천")){
                    choice_do = "인천 광역시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_incheon, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("경기")){
                    choice_do = "경기도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_gyeonggi, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("강원")){
                    choice_do = "강원도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_kangwon, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("대전")){
                    choice_do = "대전 광역시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_daejeon, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("세종")){
                    choice_do = "세종 특별 자치시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_sejong, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("충남")){
                    choice_do = "충청남도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_chungnam, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("충북")){
                    choice_do = "충청북도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_chungbuk, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("부산")){
                    choice_do = "부산광역시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_busan, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("울산")){
                    choice_do = "울산 광역시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_ulsan, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("경남")){
                    choice_do = "경상남도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_gyeongnam, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("경북")){
                    choice_do = "경상북도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_gyeongbuk, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("대구")){
                    choice_do = "대구 광역시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_daegu, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("광주")){
                    choice_do = "광주 광역시";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_kwangju, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("전남")){
                    choice_do = "전라남도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_jeonnam, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("전북")){
                    choice_do = "전라북도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_jeonbuk, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }else if (adspin1.getItem(position).equals("제주")){
                    choice_do = "제주도";
                    adspin2 = ArrayAdapter.createFromResource(RegistActivity.this, R.array.spinner_do_jeju, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);

                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            choice_se = adspin2.getItem(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button btn_regist = (Button)findViewById(R.id.btn_regist);
        btn_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = res_id_ET.getText().toString();
                pw = reg_pw_ET.getText().toString();
                address = choice_do + " " + choice_se + " " + detailAddress_ET.getText().toString();
                RegistRunnable mRegistRunnable = new RegistRunnable(id, pw, address);
                Thread t = new Thread(mRegistRunnable);
                t.start();

            }
        });
    }
    public void makeDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("긁적 다시 회원가입 해볼래요?");
        alert.show();
    }
}

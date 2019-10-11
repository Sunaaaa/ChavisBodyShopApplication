package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

public class RegistActivity extends AppCompatActivity {

    String name = "", pw = "";
    EditText res_name_ET, reg_pw_ET, detailAddress_ET, reg_pwCk_ET;
    ArrayAdapter<CharSequence> adspin1, adspin2;
    Spinner spin1, spin2;
    String choice_do = "", choice_se = "", address = "";
    ImageView imageView;
    Button doregist;

    class RegistRunnable implements Runnable {

        private String resname;
        private String respw;
        private String resaddress;
        private Handler handler;

        public RegistRunnable(String resname, String respw, String resaddress, Handler handler) {
            this.resname = resname;
            this.respw = respw;
            this.resaddress = resaddress;
            this.handler = handler;
        }

        @Override
        public void run() {
            String receivedata;
            String sendMsg = "";
//
//        // 접속할 서버 주소 (이클립스에서 android.jsp 실행시 웹브라우저 주소)

            try {
                URL url = new URL("http://70.12.115.73:9090/Chavis/Bodyshop/regist.do");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("charset", "utf-8");
                OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

                Map<String, String> map = new HashMap<String, String>();

                map.put("name", resname);
                map.put("pw", respw);
                map.put("address", resaddress);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        res_name_ET = (EditText) findViewById(R.id.reg_name);
        reg_pw_ET = (EditText) findViewById(R.id.reg_pw);
        reg_pwCk_ET = (EditText)findViewById(R.id.reg_pwCk);
        detailAddress_ET = (EditText) findViewById(R.id.detailAddress);
        imageView = (ImageView)findViewById(R.id.ckImg);
        spin1 = (Spinner) findViewById(R.id.spinner);
        spin2 = (Spinner) findViewById(R.id.spinner2);
        doregist = (Button)findViewById(R.id.doregist);

        adspin1 = ArrayAdapter.createFromResource(this, R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adspin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (adspin1.getItem(position).equals("서울")) {
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
                } else if (adspin1.getItem(position).equals("인천")) {
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
                } else if (adspin1.getItem(position).equals("경기")) {
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
                } else if (adspin1.getItem(position).equals("강원")) {
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
                } else if (adspin1.getItem(position).equals("대전")) {
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
                } else if (adspin1.getItem(position).equals("세종")) {
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
                } else if (adspin1.getItem(position).equals("충남")) {
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
                } else if (adspin1.getItem(position).equals("충북")) {
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
                } else if (adspin1.getItem(position).equals("부산")) {
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
                } else if (adspin1.getItem(position).equals("울산")) {
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
                } else if (adspin1.getItem(position).equals("경남")) {
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
                } else if (adspin1.getItem(position).equals("경북")) {
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
                } else if (adspin1.getItem(position).equals("대구")) {
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
                } else if (adspin1.getItem(position).equals("광주")) {
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
                } else if (adspin1.getItem(position).equals("전남")) {
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
                } else if (adspin1.getItem(position).equals("전북")) {
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
                } else if (adspin1.getItem(position).equals("제주")) {
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
        reg_pw_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (reg_pwCk_ET.getText().length()!=0){
                    if (reg_pw_ET.getText().toString().equals(reg_pwCk_ET.getText().toString())){
                        imageView.setImageResource(R.drawable.ck);
                    } else {
                        imageView.setImageResource(R.drawable.nck);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reg_pwCk_ET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (reg_pw_ET.getText().toString().equals(reg_pwCk_ET.getText().toString())){
                    imageView.setImageResource(R.drawable.ck);
                } else {
                    imageView.setImageResource(R.drawable.nck);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.reglayout);

        // editText가 아닌 다른 곳을 터치하면 키보드 숨기기 기능 발동
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(res_name_ET.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(reg_pw_ET.getWindowToken(),0);
                imm.hideSoftInputFromWindow(reg_pwCk_ET.getWindowToken(), 0);
                imm.hideSoftInputFromWindow(detailAddress_ET.getWindowToken(),0);
            }
        });

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String result = bundle.getString("result");
                if (result.equals("SUCCUESS")) {
                    registSuccessDialog(result);

                } else {
                    makeDialog();
                }

            }
        };
        doregist.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                name = res_name_ET.getText().toString();
                pw = reg_pw_ET.getText().toString();
                address = choice_do + "/" + choice_se + "/" + detailAddress_ET.getText().toString();
                Log.i("asdasda", address);
                Toast.makeText(getApplicationContext(), address, Toast.LENGTH_SHORT).show();

//                if (name.length()==0 || pw.length()==0 || choice_se.length()==0 || choice_do.length()==0 || !pw.equals(reg_pwCk_ET.getText().toString())){
//                    fillDataDialog();
//
//                } else {
//                    RegistRunnable mRegistRunnable = new RegistRunnable(name, pw, address, handler);
//                    Thread t = new Thread(mRegistRunnable);
//                    t.start();
//                }
            }
        });
    }

    // 회원 가입 실패
    public void fillDataDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("회원가입 정보 다시 확인하세요.");
        alert.show();
    }

    // 회원 가입 실패
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

    // 회원 가입 성공
    public void registSuccessDialog(String result) {
        AlertDialog.Builder alert = new AlertDialog.Builder(RegistActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.LoginActivity");
                startActivity(intent);
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("아이디 " + result + " 로 회원가입이 완료되었습니다." + "\n" + " 로그인 페이지로 이동합니다.");

        alert.show();
    }


    private String sendPost(String name, String pw, String address) throws Exception {

        String receiveData;

        URL url = new URL("http://70.12.115.52:9090/Bodyshop/login.do");
//        URL url = new URL("http://70.12.115.73:9090/Chavis/Member/view.do");  // 한석햄

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("charset", "utf-8");
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());

        Map<String, String> map = new HashMap<String, String>();


        map.put("name", name);
        map.put("pw", pw);
        map.put("address", address);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(map);

        Log.i("REGIST__", "가랏 회원가입 데이터 : " + json);

        osw.write(json);
        osw.flush();


        Log.i("REGIST__", "222");
        int responseCode = conn.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        receiveData = response.toString();
        in.close();
        Log.i("REGIST__", receiveData);
        return receiveData;
    }

}

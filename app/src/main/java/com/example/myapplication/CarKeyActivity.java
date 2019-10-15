package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.DTO.ReservationListDTO;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CarKeyActivity extends AppCompatActivity {

    BodyShopService bodyShopService;
    Boolean isService = false;
    ReservationListDTO reservationListDTO;
    Button btn_getKey, btn_repair_finish, btn;
    String repair_list = "", repaired_info;
    EditText editText;
    TextView carkey_name, carkey_type, carkey_num, pname;

    final String[] items = new String[]{"타이어", "와이퍼", "냉각수", "엔진오일"};
    final List<String> selectedItems = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_key);

        Intent reserv_info = getIntent();
        reservationListDTO = reserv_info.getParcelableExtra("reservation_info");

        ServiceConnection conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                BodyShopService.MyBinder mb = (BodyShopService.MyBinder) iBinder;
                bodyShopService = mb.getService();
                isService = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isService = false;
            }
        };

        Intent intent2 = new Intent(CarKeyActivity.this, BodyShopService.class);
        bindService(intent2, conn, Context.BIND_ABOVE_CLIENT);

        final InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.carkeylayout);

        editText = (EditText) findViewById(R.id.repaired_personname);
        carkey_name = (TextView) findViewById(R.id.carkey_name);
        carkey_type = (TextView) findViewById(R.id.carkey_type);
        carkey_num = (TextView) findViewById(R.id.carkey_num);
        pname = (TextView)findViewById(R.id.pname);

        carkey_name.setText(reservationListDTO.getMember_mname());
        carkey_type.setText(reservationListDTO.getCar_type());
        carkey_num.setText(reservationListDTO.getCar_id());
        editText = (EditText) findViewById(R.id.repaired_personname);

        btn_getKey = (Button) findViewById(R.id.btn_getKey);
        btn_repair_finish = (Button) findViewById(R.id.btn_repair_finish);
        // editText가 아닌 다른 곳을 터치하면 키보드 숨기기 기능 발동
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        });


        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.ReservationStatusActivity");
                i.setComponent(componentName);
                startActivity(i);
            }
        });
//        if (reservationListDTO.getRepaired_person().length() > 0 ) {
        if (reservationListDTO.getRepaired_person() != null) {
            editText.setText(reservationListDTO.getRepaired_person() + " 이/가 정비를 완료했습니다. ");
            editText.setEnabled(false);
            btn_getKey.setEnabled(false);
            btn_getKey.setBackgroundResource(R.drawable.disablekey);
            btn_repair_finish.setVisibility(View.GONE);
            pname.setVisibility(View.GONE);


        } else {
            btn_repair_finish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("수리 목록 체크 다이얼로그", "폴킴쨩");
                    Log.i("수리 목록 체크 다이얼로그", "폴킴");
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CarKeyActivity.this);
                    dialog.setTitle("수리 목록")
//                        .setMessage("정비사 이름을 입력하세요.")
//                        .setView(et)
                            .setMultiChoiceItems(
                                    items,
                                    new boolean[]{false, false, false, false},
                                    new DialogInterface.OnMultiChoiceClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                            if (isChecked) {
                                                selectedItems.add(items[which]);
                                            } else {
                                                selectedItems.remove(items[which]);
                                            }
                                        }
                                    })
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    long now = System.currentTimeMillis();
                                    Date date = new Date(now);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                    if (selectedItems.size() == 0) {
//                                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_LONG).show();
                                        makeDialog();
                                    } else {
                                        for (String ckitem : selectedItems) {

                                            switch (ckitem) {
                                                case "타이어":
                                                    repair_list += ("Tire" + "#");
                                                    break;
                                                case "와이퍼":
                                                    repair_list += ("Wiper" + "#");
                                                    break;
                                                case "냉각수":
                                                    repair_list += ("Cooler" + "#");
                                                    break;
                                                case "엔진오일":
                                                    repair_list += ("EngineOil" + "#");
                                                    break;
                                            }
                                        }
                                        Log.i("수리 목록", repair_list);

                                        selectedItems.clear();
                                        repair_list = repair_list.substring(0, repair_list.length() - 1);
                                        String repaired_time = sdf.format(date);
//                                    String repaired_person = editText.getText().toString();
                                        repaired_info = reservationListDTO.getReservation_no() + "/" + repaired_time + "/" + editText.getText().toString();
                                        Log.i("수리 목록 (맨 뒤에 '#' 빼기) ", repair_list);
                                        bodyShopService.clientToServer("RepairFinish", repair_list + "/" + repaired_info);
                                    }

                                }
                            }).create().show();

                }
            });

            btn_getKey.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("service에게 보낼 것 ", reservationListDTO.getMember_no());
                    String msg = reservationListDTO.getMember_no() + "#" + reservationListDTO.getCar_id();
                    bodyShopService.clientToServer("CarOpen", msg);
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String result = intent.getStringExtra("repairedResult");
        Log.i("repairedResult", result);
        if (result.equals("Success")) {
            Intent i = new Intent();
            ComponentName componentName = new ComponentName("com.example.myapplication", "com.example.myapplication.ReservationStatusActivity");
            i.setComponent(componentName);
            startActivity(i);
        } else {
            makeDialogDoAgain();
        }

    }

    public void makeDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(CarKeyActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("수리 목록을 체크하세요...");
        alert.show();
    }

    public void makeDialogName() {
        AlertDialog.Builder alert = new AlertDialog.Builder(CarKeyActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("정비사 명을 입력해주세요...");
        alert.show();
    }

    public void makeDialogDoAgain() {
        AlertDialog.Builder alert = new AlertDialog.Builder(CarKeyActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();     //닫기
            }
        });
        alert.setMessage("정비목록 및 정비사명이 제대로 입력됬는지 확인하세용");
        alert.show();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}

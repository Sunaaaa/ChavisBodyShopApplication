package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class OpeningActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        ImageView mImageView = (ImageView)findViewById(R.id.loading_img);
        Glide.with(this).load(R.raw.logo1).into(mImageView);

        Handler mHandler = new Handler();
        mHandler.postDelayed(new SplashHandler(), 3000);
        Log.i("SplashAcitivty", "SplashAcitivty");
    }

    private class SplashHandler implements Runnable{

        @Override
        public void run() {
            startActivity(new Intent(getApplicationContext(), ReservationStatusActivity.class));
            OpeningActivity.this.finish();

            SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);
            String data = preferences.getString("myObject", "NO");

            // 로그인 데이터 없음  -  call LoginActivity
            if(data.equals("NO")) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                OpeningActivity.this.finish();
            } else {
                // 서비스 실행
                Intent i = new Intent();
                ComponentName sComponentName = new ComponentName("com.example.myapplication", "com.example.myapplication.BodyShopService");
                i.setComponent(sComponentName);
                startService(i);

                // 로그인 데이터 있음 - Call ReservationStatusActivity
                startActivity(new Intent(getApplicationContext(), ReservationStatusActivity.class));
                OpeningActivity.this.finish();

            }
        }
    }
    @Override
    public void onBackPressed() {
        //초반 플래시 화면에서 넘어갈때 뒤로가기 버튼 못누르게 함
    }
}

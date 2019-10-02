package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.myapplication.BODYINFO.SaveSharedPreference;

public class FirstAuthActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_auth);

        if(SaveSharedPreference.getBodyShopId(FirstAuthActivity.this).length() == 0) {
            // call Login Activity
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
            Log.i("FirstAuthActivity_LOG","로그인 페이지로 가즈아  ");
            startActivity(intent);
            this.finish();
        } else {
            // Call Next Activity
            intent = new Intent(FirstAuthActivity.this, MyReservationActivity.class);
            intent.putExtra("STD_NUM", SaveSharedPreference.getBodyShopId(this).toString());
            Log.i("FirstAuthActivity_LOG","자동로그인 되어 있음 ");
            startActivity(intent);
            this.finish();
        }
    }
}

package com.example.capstoneproject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //액션바 없애는 코드 2줄
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        //버튼, 이미지 변수생성
        Button btnSub1 = findViewById(R.id.btnSub1);
        Button btnWeb = findViewById(R.id.btnWeb);
        Button btnDiary = findViewById(R.id.btnDiary);


        btnSub1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSub1 = new Intent(getApplicationContext(), Sub1Activity.class);
                startActivity(intentSub1);
            }
        });

        btnDiary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentDiary = new Intent(getApplicationContext(), DiaryActivity.class);
                startActivity(intentDiary);
            }
        });

        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBogun=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.mohw.go.kr/react/index.jsp"));
                startActivity(intentBogun);
            }
        });


    }
}
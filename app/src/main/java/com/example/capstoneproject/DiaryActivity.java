package com.example.capstoneproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DiaryActivity extends AppCompatActivity {

    private ArrayList<MainData> arrayList;
    private MainAdapter mainAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SharedPreferences preferences;
    private int INDEX;
    private View dialogViewDiary;
    private EditText dlgEdtDiary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        //타이틀 설정
        getSupportActionBar().setTitle("흡연일지");

        //흡연일지 데이터 저장
        preferences = getSharedPreferences("SmokeDiary", MODE_PRIVATE);


        recyclerView = (RecyclerView) findViewById(R.id.rvDiary);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        mainAdapter = new MainAdapter(arrayList);
        recyclerView.setAdapter(mainAdapter);

        //일지 갯수 체크용, 리스트가 몇 개 있는지 불러오는 코드, 아무값도 없으면 0
        //글구 여기서 인덱스값 초기화
        INDEX = preferences.getInt("INDEX",0);
        //Editor를 preferences에 쓰겠다고 연결
        SharedPreferences.Editor editor = preferences.edit();

        for (int i = 0; i < INDEX; i++) {
            //여기 들어가는 내용으로 리사이클러뷰가 만들어짐, 일단 기존 리스트값을 불러올 필요가 있다.
            MainData mainData = new MainData(
                    preferences.getString("CONTENT_"+i,"복원은 아직 구현전이네요.. 애석하게 생각합니다"),
                    preferences.getString("DATE_"+i,"삭제된 데이터입니다")
            );
            arrayList.add(mainData);
            mainAdapter.notifyDataSetChanged();
        }


        Button btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(getTime(), "잘 안보일까봐 한글로 적어두자: "+ getTime());

                dialogViewDiary = (View) View.inflate(DiaryActivity.this,R.layout.dialog_add_diary,null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(DiaryActivity.this);
                dlg.setTitle("흡연일지 추가");
                dlg.setIcon(R.drawable.diary_icon);
                dlg.setView(dialogViewDiary);
                //확인 버튼 누르는 순간 onClick함수 실행
                dlg.setPositiveButton(
                        "확인",
                        //놀랍게도 이거 함수 안에 들어가는 인자임...
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dlgEdtDiary = (EditText) dialogViewDiary.findViewById(R.id.dlgEdtDiary);

                                //putString(KEY,VALUE)
                                editor.putString("DATE_"+Integer.toString(INDEX),getTime());
                                //dlgEdtDiary에서 텍스트 가져와서 추가
                                editor.putString("CONTENT_"+Integer.toString(INDEX),dlgEdtDiary.getText().toString());
                                //항상 commit & apply 를 해주어야 저장이 된다.
                                editor.commit();

                                //여기 들어가는 내용으로 리사이클러뷰가 만들어짐
                                MainData mainData = new MainData(
                                        preferences.getString("CONTENT_"+INDEX,"복원은 아직 구현전이네요.. 애석하게 생각합니다"),
                                        preferences.getString("DATE_"+INDEX,"삭제된 데이터입니다")
                                );
                                //추가버튼 한 번 눌러서 리스트 추가할 때마다 인덱스 1씩 증가, getInt는 위쪽에서 이미 진행됨
                                INDEX = INDEX+1;
                                editor.putInt("INDEX",INDEX);
                                editor.commit();

                                arrayList.add(mainData);
                                mainAdapter.notifyDataSetChanged();
                                Log.d("이게 보이면 여기가 읽히고 있는것222", "이게 보이면 여기가 읽히고 있는것222");
                            }
                        }
                );
                dlg.setNegativeButton("취소",null);
                dlg.show();
                Log.d("이게 보이면 여기가 읽히고 있는것", "이게 보이면 여기가 읽히고 있는것");
            }
        });
    }

    public String getTime(){
        //현재시간 가져오기
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String getTime = dateFormat.format(date);
        //리턴타입은 String
        return getTime;
    }

}
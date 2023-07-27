package com.example.capstoneproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Sub1Activity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private GoogleMap mMap;

    RequestQueue queue;
    TextView textOri, textParse;

    ArrayList<String> nm = new ArrayList<>();
    ArrayList<Double> la = new ArrayList<>();
    ArrayList<Double> lo = new ArrayList<>();
    ArrayList<String> nmNsmk = new ArrayList<>();
    ArrayList<Double> laNsmk = new ArrayList<>();
    ArrayList<Double> loNsmk = new ArrayList<>();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub1);

        getSupportActionBar().setTitle("흡연부스 지도");

        Button btnRequest = findViewById(R.id.btnRequest);



        //초기화(null이면 값을 초기화)
        if(queue == null) {
            queue = Volley.newRequestQueue(this);
        }

        //흡연부스 Json요청하는 구문
        String url = "http://apis.data.go.kr/5690000/sjSmokingAreaLocation/sj_00001180?serviceKey=CwriQJZKSP6y2LQaHXDLSHPmEXZx5l05UssRhMEGYtwmLuihSXIxCOgf4k846%2FlnMV6sj6lcx29IS%2F0k6bbNpA%3D%3D&pageIndex=1&pageUnit=20&dataTy=json&searchCondition=nm&searchKeyword=%EC%84%B8%EC%A2%85";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject body = response.getJSONObject("body");
                    JSONArray arrItems = body.getJSONArray("items");
                    for (int i = 0; i < arrItems.length(); i++) {
                        JSONObject obj = arrItems.getJSONObject(i);
                        nm.add(obj.getString("nm"));
                        la.add(obj.getDouble("la"));
                        lo.add(obj.getDouble("lo"));
                    }
                    Log.d("의도한 부분 체크용","이게 1번째로 실행되기를 기대함");


                    //금연구역 Json요청하는 구문, 웹통신이 비동기라서 어쩔 수 없이 Response구문 안에 또 웹통신 구문을 넣음
                    String urlNotSmoke = "https://www.seogu.go.kr/seoguAPI/3660000/getPrhsmkZone?pageNo=1&numOfRows=10";
                    JsonObjectRequest jsonObjectRequestNotSmoke = new JsonObjectRequest(Request.Method.GET,
                            urlNotSmoke, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.d("리스폰스를 받아오는지 체크", response.toString());
                                JSONObject responseJSONObject = response.getJSONObject("response");
                                JSONObject body = responseJSONObject.getJSONObject("body");
                                JSONArray itemsArray = body.getJSONArray("items");
                                for (int i = 0; i < itemsArray.length(); i++) {
//                                    Log.d("금연구역 체크용", itemsArray.get(i).toString());
                                    JSONObject eachItems = itemsArray.getJSONObject(i);
                                    nmNsmk.add(eachItems.getString("prhsmk_zone_nm"));
                                    laNsmk.add(eachItems.getDouble("la"));
                                    loNsmk.add(eachItems.getDouble("lo"));
                                    Log.d("파싱 체크용", nmNsmk.get(i)+" 위도"+laNsmk.get(i)+" 경도"+loNsmk.get(i));
                                }
                                Log.d("의도한 부분 체크용","이게 2번째로 실행되기를 기대함");
                                //지도관련, 비동기 같아서 그냥 네트워크 통신 끝나고 지도 표시하게 만듦
                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);
                                mapFragment.getMapAsync(Sub1Activity.this);
                            }
                            //2차 웹통신 에러 처리 구문
                            catch(JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //오류 발생 시 실행
                        }
                    });
                    //웹통신 큐 한번 더 추가
                    queue.add(jsonObjectRequestNotSmoke);
                }
                //1차 웹통신 에러처리 구문
                catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //오류 발생 시 실행
//                textOri.setText("에러: " + error.toString());
            }
        });

        //지도 및 1차 웹통신 실행 코드
        queue.add(jsonObjectRequest);


        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentMain = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intentMain);
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.pin);
        BitmapDrawable bitmapDrawableDontSmoke = (BitmapDrawable)getResources().getDrawable(R.drawable.do_not_smoke_pin);
        Bitmap bitmap = bitmapdraw.getBitmap();
        Bitmap bitmapDontSomke = bitmapDrawableDontSmoke.getBitmap();
        Bitmap pin = Bitmap.createScaledBitmap(bitmap, 110, 110, false);
        Bitmap doNotSmokePin = Bitmap.createScaledBitmap(bitmapDontSomke,110,110,false);

        mMap.setOnMyLocationButtonClickListener(this);
        Log.d("뭐가 먼저 실행되는지 알아보자", "체크0체크0체크0체크0체크0체크0체크0체크0체크0체크0체크0체크0체크0");
        enableMyLocation();

        //흡연부스 마커찍는 반복문
        for(int i=0; i< nm.size(); i++){
            // 1. 마커 옵션 설정 (만드는 과정)
            MarkerOptions makerOptions = new MarkerOptions();
            makerOptions // LatLng에 대한 어레이를 만들어서 이용할 수도 있다.
                    .position(new LatLng(la.get(i), lo.get(i)))
                    .title(nm.get(i)); // 타이틀.
            makerOptions.icon(BitmapDescriptorFactory.fromBitmap(pin));
            makerOptions.snippet("흡연부스");
            // 2. 마커 생성 (마커를 나타냄)
            mMap.addMarker(makerOptions);
        }

        //금연구역 마커찍는 반복문
        for (int i = 0; i < nmNsmk.size(); i++) {
            MarkerOptions markerOptionsNsmk = new MarkerOptions();
            markerOptionsNsmk
                    .position(new LatLng(laNsmk.get(i),loNsmk.get(i)))
                    .title(nmNsmk.get(i));
            markerOptionsNsmk.icon(BitmapDescriptorFactory.fromBitmap(doNotSmokePin));
            markerOptionsNsmk.snippet("금연구역");
            mMap.addMarker(markerOptionsNsmk);

            CircleOptions circleOptions = new CircleOptions()
                    .center(new LatLng(laNsmk.get(i), loNsmk.get(i)))
                    .radius(200) // In meters
                    .strokeWidth(3)
                    //argb값은  각255가 최대 0으로 갈수로 투명
                    .strokeColor(Color.argb(200, 204, 69, 56))
                    .fillColor(Color.argb(100, 204, 69, 56));
            mMap.addCircle(circleOptions);
        }

        LatLng nowPlace = new LatLng(la.get(1), lo.get(1));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nowPlace, 11));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                    Log.d("뭐가 먼저 실행되는지 알아보자", "체크1체크1체크1체크1체크1체크1체크1체크1체크1체크1체크1체크1체크1체크1체크1체크1");
                    enableMyLocation();
                } else {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다
                }
                return;
        }
    }

    /**
     * Enables the My Location layer if the fine location permission has been granted.
     */
    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        // 1. 사용 권한이 부여되었는지 확인하고, 부여된 경우 내 위치 계층 사용
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("뭐가 먼저 실행되는지 알아보자", "체크2체크2체크2체크2체크2체크2체크2체크2체크2체크2체크2체크2체크2");
            mMap.setMyLocationEnabled(true);
            return;
        }
        // 2. 그렇지 않으면 사용자에게 위치 권한을 요청합니다.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "현재 위치를 표시합니다", Toast.LENGTH_SHORT)
                .show();
        return false;
    }
}
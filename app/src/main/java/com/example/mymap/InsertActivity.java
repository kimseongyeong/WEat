package com.example.mymap;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertActivity extends AppCompatActivity {

    ActionBar ab;
    private Spinner spinnerCategory;
    EditText editTitle;
    EditText editContent;

    private GpsTracker gpsTracker;
    private DatabaseReference bDatabase;

    SharedPreferences mPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        init();
    }


    public void init(){

        ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setCustomView(getLayoutInflater().inflate(R.layout.action_bar,null));
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mPref = getSharedPreferences("NicName", Context.MODE_PRIVATE);

        editTitle = findViewById(R.id.edit_title);
        editContent = findViewById(R.id.edit_content);
        spinnerCategory = (Spinner)findViewById(R.id.spinner);

        bDatabase = FirebaseDatabase.getInstance().getReference("Board");
        gpsTracker = new GpsTracker(InsertActivity.this);

    }

    public void onClick(View v){

        switch (v.getId()){

            case R.id.btn_back:
                onBackPressed();
                //Intent intent = new Intent(this,BoardActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                //startActivity(intent);
                break;

            case R.id.btn_enter:

                if(isEditFilled()){

                    String user = mPref.getString("NicName","익명");
                    String title = editTitle.getText().toString();
                    String content = editContent.getText().toString();
                    String category = spinnerCategory.getSelectedItem().toString();
                    double latitude = gpsTracker.getLatitude();
                    double longitude = gpsTracker.getLongitude();
                    boolean complete = false;

                    AddBoard(title,user,content,category,latitude,longitude,complete);
                }

                break;
        }
    }

    private void AddBoard(String title, String user, String content, String category, double latitude, double longitude, boolean complete){
        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date_now = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yy-MM-dd_HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date_now);

        String date = formatDate;

        Board board = new Board(title, user, date, content, category, latitude, longitude, complete);
        bDatabase.child(board.getDate()).setValue(board);

        Intent intent = new Intent(this,PostActivity.class);
        intent.putExtra("REG_DATE", board.getDate().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        startActivity(intent);
    }

    public boolean isEditFilled(){

        if(editTitle.getText().toString().equals("")){
            Toast.makeText(this,"제목을 입력해주세요.",Toast.LENGTH_SHORT).show();
            editTitle.requestFocus();
            return false;
        }
        if(editContent.getText().toString().equals("")){
            Toast.makeText(this,"본문을 입력해주세요.",Toast.LENGTH_SHORT).show();
            editContent.requestFocus();
            return false;
        }

        return true;
    }

    public void onBottomClick(View view) {

    }

    //Board버튼 클릭 시 BoardAct로 이동
    public void onCallBtnBoard(View v)
    {
        Intent intent = new Intent(this, BoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
    public void onCallBtnHome(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}

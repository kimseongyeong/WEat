package com.example.mymap;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    
    private Spinner sp_category;
    EditText et_title;
    EditText et_contents;
    Button bt_back;
    Button bt_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp_category = (Spinner)findViewById(R.id.sp_category);

        sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        et_title = findViewById(R.id.et_title);
        et_contents = findViewById(R.id.et_contents);
        bt_back = findViewById(R.id.bt_back);
        bt_enter = findViewById(R.id.bt_enter);
    }

    public void onCall(View v)
    {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    //enter버튼 클릭 시 타이틀과 컨텐츠 PActivity로 전달
    public void onCallBtnEnter(View v)
    {
        String title = et_title.getText().toString();
        String contents = et_contents.getText().toString();

        String category = sp_category.getSelectedItem().toString();


        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("category", category);
        intent.putExtra("title", title);
        intent.putExtra("contents", contents);

        intent.putExtra("type", 2);
        startActivity(intent);

    }


}
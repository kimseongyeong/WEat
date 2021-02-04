package com.example.mymap;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PostActivity extends AppCompatActivity {

    TextView top, head, body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        init();
    }

    public void init() {
        top = findViewById(R.id.tv_category);
        head = findViewById(R.id.tv_title);
        body = findViewById(R.id.tv_contents);

        Intent intent = getIntent();

        int calltype = intent.getIntExtra("type", 0);

        if (calltype == 2) {
            String category = intent.getStringExtra("category");
            String title = intent.getStringExtra("title");
            String contents = intent.getStringExtra("contents");

            top = findViewById(R.id.tv_category);
            head = findViewById(R.id.tv_title);
            body = findViewById(R.id.tv_contents);

            top.setText(("카테고리: " +category));
            head.setText(("제목: " +title));
            body.setText((contents));
        }

    }
}
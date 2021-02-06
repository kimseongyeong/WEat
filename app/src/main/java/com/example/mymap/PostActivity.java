package com.example.mymap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostActivity extends AppCompatActivity {

    TextView title, date, content, user;
    EditText editText;
    Button btnReply;
    ImageView categoryImg;
    ActionBar ab;
    SharedPreferences mPref = null;

    String boardDate;

    private CommentAdapter commentAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private Board board;
    private DatabaseReference bDatabase;

    private ArrayList<Comment> commentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        init();
    }

    public void init() {

        ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setCustomView(getLayoutInflater().inflate(R.layout.action_bar,null));
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mPref = getSharedPreferences("NicName", Context.MODE_PRIVATE);

        Intent intent = getIntent();
        boardDate = intent.getStringExtra("REG_DATE");

        title = findViewById(R.id.text_post_title);
        date = findViewById(R.id.text_post_date);
        content = findViewById(R.id.text_post_content);
        editText = findViewById(R.id.edit_reply);
        categoryImg = findViewById(R.id.img_category);
        btnReply = findViewById(R.id.btn_reply);
        user = findViewById(R.id.text_post_id);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView_reply);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        commentAdapter = new CommentAdapter(commentList,this);
        recyclerView.setAdapter(commentAdapter);

        commentList = new ArrayList<>();
        bDatabase = FirebaseDatabase.getInstance().getReference("Board/"+boardDate);

        commentAdapter = new CommentAdapter(commentList,this);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 1));

        bDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {  //데이터가 변경 되면 호출
                commentList.clear();

                board = snapshot.getValue(Board.class);

                //commentList = new ArrayList<>();
                for (DataSnapshot dataSnapshot_comment : snapshot.child("comList").getChildren()){
                    commentList.add(dataSnapshot_comment.getValue(Comment.class));
                }

                board.setCommentList(commentList);

                title.setText(board.getTitle());
                date.setText(board.getDate());
                content.setText(board.getContent());
                user.setText(board.getUser());
                categoryImg.setImageResource(getCategoryImg(board.getCategory()));

                if(board.isComplete()){
                    editText.setClickable(false);
                    editText.setFocusable(false);
                    btnReply.setEnabled(false);
                }

                commentAdapter.notifyDataSetChanged();

            }
        @Override
        public void onCancelled(@NonNull DatabaseError error) {
//
        }
    });
    }

    public void onClick(View view) {


        if(editText.getText().toString().equals("")){
            editText.requestFocus();
            Toast.makeText(this,"댓글에 내용을 적어주세요",Toast.LENGTH_SHORT).show();
            return;
        }

        AddComment(boardDate,editText.getText().toString(),
                mPref.getString("NicName","익명"));

        editText.setText("");
    }

    private void AddComment(String board_time, String content, String user){

        long now = System.currentTimeMillis();
        // 현재시간을 date 변수에 저장한다.
        Date date_now = new Date(now);
        // 시간을 나타냇 포맷을 정한다 ( yyyy/MM/dd 같은 형태로 변형 가능 )
        SimpleDateFormat sdfNow = new SimpleDateFormat("yy-MM-dd_HH:mm:ss");
        // nowDate 변수에 값을 저장한다.
        String formatDate = sdfNow.format(date_now);
        String date = formatDate;
        Comment comment = new Comment(content, date, user);

        bDatabase.child("comList").child(comment.getDate()).setValue(comment);
    }

    public int getCategoryImg(String category){

        switch (category){

            case "분식":
                return R.drawable.dduck;
            case "피자":
                return R.drawable.pizza;
            case "중식":
                return R.drawable.black_noddle;
            case "일식":
                return R.drawable.sushi;
            case "한식":
                return R.drawable.bossam;
            case "치킨":
                return R.drawable.chicken;
        }
        return -1;
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
    public void onCallBtnNew(View v)
    {
        Intent intent = new Intent(this, InsertActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
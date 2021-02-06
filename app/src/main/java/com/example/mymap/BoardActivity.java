package com.example.mymap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class BoardActivity extends AppCompatActivity {

    private ActionBar ab;
    //private ArrayList<BoardData> boardList;
    private BoardAdapter boardAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeRefreshLayout;

    private ArrayList<Board> boardList;
    private DatabaseReference bDatabase;

    SharedPreferences mPref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        init();
    }

    public void init(){

        ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(false);
        ab.setCustomView(getLayoutInflater().inflate(R.layout.action_bar,null));
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        mPref = getSharedPreferences("NicName", Context.MODE_PRIVATE);

        // 리사이클러뷰 설정

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //boardList = setTempContent();

        boardList = new ArrayList<>();
        bDatabase = FirebaseDatabase.getInstance().getReference("Board");

        boardAdapter = new BoardAdapter(boardList,this);
        recyclerView.setAdapter(boardAdapter);

        bDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {  //데이터가 변경 되면 호출
                boardList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Board board = dataSnapshot.getValue(Board.class);

                    boardList.add(board);
                }
                //Update(text);
                Collections.reverse(boardList);
                boardAdapter.notifyDataSetChanged();
            }
            //
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//
            }
        });


        // 새로고침 기능
        swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(Color.rgb(255,76,0));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(){

                boardAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        boardAdapter.setOnItemLongClickListener(new BoardAdapter.OnItemLongClickListener(){

            @Override
            public void onItemLongClick(View v, int pos) {

                final Board tmpBoard = boardAdapter.getBoard(pos);

                if(tmpBoard.isComplete()){
                    Toast.makeText(BoardActivity.this,"이미 마감된 모집입니다.",Toast.LENGTH_SHORT).show();
                    return;
                }

                if (tmpBoard.getUser().equals(mPref.getString("NicName", "익명"))) {
                    new AlertDialog.Builder(BoardActivity.this)
                            .setMessage("모집 인원을 모두 모으셨습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    updateBoard(tmpBoard.getDate());
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(BoardActivity.this,"계정이 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void updateBoard(String date){

        bDatabase.child(date).child("complete").setValue(true);
    }

    public void onBottomClick(View view) {

    }


    public void onCallBtnHome(View v) {
        Intent intent = new Intent(this, MapsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void onCallBtnNew(View v)
    {
        Intent intent = new Intent(this, InsertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }
}

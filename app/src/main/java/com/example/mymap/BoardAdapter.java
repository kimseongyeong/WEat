package com.example.mymap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder>{

    private OnItemLongClickListener mLongListener = null;
    private List<Board> boardList;
    private Context context;


    public BoardAdapter(List<Board> boardList, Context context) {
        this.boardList = boardList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final Board boardData = boardList.get(position);

        holder.boardImg.setImageResource(getCategoryImg(boardData.getCategory()));
        holder.boardDate.setText(boardData.getDate());
        holder.boardTitle.setText(boardData.getTitle());
        holder.boardContent.setText(boardData.getContent());
        holder.boardId.setText(boardData.getUser());

        if(boardData.isComplete())
            holder.soldOutLayout.setVisibility(View.VISIBLE);
        else
            holder.soldOutLayout.setVisibility(View.GONE);

        holder.itemView.setTag(position);

    }

    // 삭제 원할시 호출
    public void removeItem(int position){

        try{

            boardList.remove(position);

            // 새로고침 의미
            notifyItemRemoved(position);

        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        if(boardList != null)
            return boardList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected ImageView boardImg;
        protected TextView boardDate;
        protected TextView boardTitle;
        protected TextView boardContent;
        protected TextView boardId;
        protected LinearLayout soldOutLayout;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            this.boardImg = (ImageView)itemView.findViewById(R.id.board_img);

            this.boardDate = (TextView)itemView.findViewById(R.id.board_date);
            this.boardTitle = (TextView)itemView.findViewById(R.id.board_title);
            this.boardContent = (TextView)itemView.findViewById(R.id.board_content);
            this.boardId = (TextView)itemView.findViewById(R.id.board_id);
            this.soldOutLayout = (LinearLayout)itemView.findViewById(R.id.soldout_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(boardList.get(getAdapterPosition()).isComplete()){
                        Toast.makeText(context,"이미 마감된 모집입니다.",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Intent intent = new Intent(context,PostActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    intent.putExtra("REG_DATE", boardDate.getText().toString());

                    context.startActivity(intent);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        mLongListener.onItemLongClick(v, pos);
                    }
                    return true;
                }
            });
        }
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

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int pos);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mLongListener){
        this.mLongListener = mLongListener;
    }

    public Board getBoard(int position){
        return boardList.get(position);
    }

}

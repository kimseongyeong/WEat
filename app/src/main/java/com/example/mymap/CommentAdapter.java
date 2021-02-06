package com.example.mymap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{
    Context context;
    private List<Comment> commentList;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.board_reply_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Comment commentData = commentList.get(position);

        holder.commentDate.setText(commentData.getDate());
        holder.commentUser.setText(commentData.getUser());
        holder.commentContent.setText(commentData.getContent());

        Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_to_right);
        holder.itemView.setAnimation(animation);
        holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 클릭시 다음 화면으로 넘어가는 것 구현
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                //롱클릭 필요시 구현
                return true;
            }
        });
    }

    // 삭제 원할시 호출
    public void removeItem(int position){

        try{

            commentList.remove(position);

            // 새로고침 의미
            notifyItemRemoved(position);

        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        if(commentList != null)
            return commentList.size();
        else
            return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected TextView commentDate;
        protected TextView commentUser;
        protected TextView commentContent;

        public ViewHolder(@NonNull View itemView){
            super(itemView);


            this.commentDate = (TextView)itemView.findViewById(R.id.text_reply_date);
            this.commentUser = (TextView)itemView.findViewById(R.id.text_reply_user);
            this.commentContent = (TextView)itemView.findViewById(R.id.text_reply_content);
        }
    }
}

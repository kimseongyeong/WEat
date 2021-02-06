package com.example.mymap;

import java.util.Date;

public class Comment {
    private String content;  //댓글 내용
    private String date;     //댓글 작성한 시간
    private String user;     //댓글을 작성한 사람의 이름

    public Comment() {      //파이어 베이스에서 정보를 받아올때 필요
    }

    public Comment(String content, String date, String user) {
        this.content = content;
        this.date = date;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "content='" + content + '\'' +
                ", date=" + date +
                ", user='" + user + '\'' +
                '}' + "\n";
    }
}

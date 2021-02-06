package com.example.mymap;

import java.util.ArrayList;


public class Board {
        private String title;                       //게시물 제목
        private String user;                        //게시글을 작성한 사람의 이름
        private String date;                        //게시글쓴 시간
        private String content;                     //내용
        private String category;                    //카테고리
        private double latitude;                    //위도
        private double longitude;                   //경도
        private boolean complete;                   //사람을 다 구했는지
        private ArrayList<Comment> commentList;     //댓글 리스트

        public Board(){ //파이어 베이스에서 정보를 받아올때 필요

        }

        public Board(String title, String user, String date, String content, String category, double latitude, double longitude, boolean complete) {
            this.title = title;
            this.user = user;
            this.date = date;
            this.content = content;
            this.category = category;
            this.latitude = latitude;
            this.longitude = longitude;
            this.complete = complete;
        }

        public ArrayList<Comment> getCommentList() {
            return commentList;
        }

        public void setCommentList(ArrayList<Comment> commentList) {
            this.commentList = commentList;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public boolean isComplete() {
            return complete;
        }

        public void setComplete(boolean complete) {
            this.complete = complete;
        }

        @Override
        public String toString() {
            return "Board{" +
                    "title='" + title + '\'' +
                    ", user='" + user + '\'' +
                    ", date=" + date +
                    ", content='" + content + '\'' +
                    ", category='" + category + '\'' +
                    ", latitude=" + latitude +
                    ", longitude=" + longitude +
                    ", complete=" + complete +
                    ", commentList=" + commentList +
                    '}';
        }
    }


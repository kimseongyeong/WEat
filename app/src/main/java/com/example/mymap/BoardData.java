package com.example.mymap;

public class BoardData {

    private int boardImg;
    private String boardDate;
    private String boardTitle;
    private String boardContent;
    private String boardId;
    private boolean isSoldOut;


    public BoardData(int boardImg, String boardDate, String boardTitle, String boardContent, String boardId, boolean isSoldOut) {
        this.boardImg = boardImg;
        this.boardDate = boardDate;
        this.boardTitle = boardTitle;
        this.boardContent = boardContent;
        this.boardId = boardId;
        this.isSoldOut = isSoldOut;
    }

    public int getBoardImg() {
        return boardImg;
    }

    public String getBoardDate() {
        return boardDate;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    public String getBoardContent() {
        return boardContent;
    }

    public String getBoardId() {
        return boardId;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setBoardImg(int boardImg) {
        this.boardImg = boardImg;
    }

    public void setBoardDate(String boardDate) {
        this.boardDate = boardDate;
    }

    public void setBoardTitle(String boardTitle) {
        this.boardTitle = boardTitle;
    }

    public void setBoardContent(String boardContent) {
        this.boardContent = boardContent;
    }

    public void setBoardId(String boardId) {
        this.boardId = boardId;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }
}

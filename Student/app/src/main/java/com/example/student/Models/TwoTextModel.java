package com.example.student.Models;


public class TwoTextModel {
    
    private String title;
    private String content;
    public TwoTextModel(String title, String content) {
        
        this.title = title;
        this.content = content;
    }
    
    public String gettitle() {
        return title;
    }

    public void settitle(String title) {
        this.title = title;
    }

    public String getcontent() {
        return content;
    }

    public void setcontent(String content) {
        this.content = content;
    }
}
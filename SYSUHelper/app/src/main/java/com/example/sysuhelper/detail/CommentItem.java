package com.example.sysuhelper.detail;

public class CommentItem {
    private String name;
    private String content;
    private String id;

    public CommentItem(String n, String c){
        name = n;
        content = c;
    }

    public CommentItem(String i, String n, String c){
        name = n;
        content = c;
        id = i;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

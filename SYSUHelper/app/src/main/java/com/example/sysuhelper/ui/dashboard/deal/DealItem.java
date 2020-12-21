package com.example.sysuhelper.ui.dashboard.deal;

public class DealItem {
    private String ID;
    private String CreatedAt;
    private String name;
    private String type;
    private String content;
    private String time;
    private String addr;

    private String contact;
    private String deadline;
    private int mode;

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public String getType(){
        return type;
    }

    public String getAddr() { return addr; }


    public DealItem(int mode, String name, String type, String content, String time, String addr) {
        this.name = name;
        this.type = type;
        this.content = content;
        this.time = time;
        this.addr = addr;
    }

    public DealItem(int mode, String name, String type, String content, String time) {
        this.name = name;
        this.type = type;
        this.content = content;
        this.time = time;
        this.addr = "";
    }

    public  DealItem(int mode){
        this.mode = mode;
    }

    public  DealItem(){

    }




}
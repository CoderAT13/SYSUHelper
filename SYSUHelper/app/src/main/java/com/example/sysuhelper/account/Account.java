package com.example.sysuhelper.account;

public class Account {
    private String username;
    private String password;

    public Account(String name, String pwd){
        username = name;
        password = pwd;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

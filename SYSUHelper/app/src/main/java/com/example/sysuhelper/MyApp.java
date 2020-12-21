package com.example.sysuhelper;

import android.app.Application;

public class MyApp extends Application {


    private String username;
    private String token;



    @Override
    public void onCreate(){
        super.onCreate();
//        username = "coderat";
//        token = "coderathTHctcuA";
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

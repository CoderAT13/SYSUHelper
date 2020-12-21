package com.example.sysuhelper.tools;

public class TokenMsg {

    private String token;
    private String status;

    TokenMsg(String tk, String st){
        token = tk;
        status = st;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}

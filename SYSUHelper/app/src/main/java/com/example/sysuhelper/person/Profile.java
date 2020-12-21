package com.example.sysuhelper.person;

public class Profile {
    private String username;

    private String name;
    private String sid;
    private String school;
    private String major;

    public Profile(String n, String s, String sc, String m) {
        name = n;
        sid = s;
        school = sc;
        major = m;
        username = "";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String toJsonString(){
        return "{\"name\":" + "\"" + name + "\"," +
                "{\"sid\":" + "\"" + sid + "\"," +
                "{\"school\":" + "\"" + school + "\"," +
                "{\"major\":" + "\"" + major + "\"}" ;
    }
}

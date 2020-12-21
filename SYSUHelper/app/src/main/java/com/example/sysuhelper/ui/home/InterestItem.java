package com.example.sysuhelper.ui.home;

import com.example.sysuhelper.ui.dashboard.deal.DealItem;

public class InterestItem extends DealItem {

    private String in_sit;
    private String not_sit;

    public InterestItem(String name, String type, String content, String time, String in_sit, String not_sit) {
        super();
        setName(name);
        setType(type);
        setContent(content);
        setTime(time);
        this.in_sit = in_sit;
        this.not_sit = not_sit;
    }

    public String getIn_sit() {
        return in_sit;
    }

    public void setIn_sit(String in_sit) {
        this.in_sit = in_sit;
    }

    public String getNot_sit() {
        return not_sit;
    }

    public void setNot_sit(String not_sit) {
        this.not_sit = not_sit;
    }



}

package com.example.sysuhelper.person;

import android.os.Handler;
import android.os.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.sysuhelper.MyApp;
import com.example.sysuhelper.tools.HttpHelper;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StepViewModel extends ViewModel {

    private MutableLiveData<List<DealItem>> insList;

    private int mode;

    private CardHandler myHandler;
    private String token;

    public StepViewModel() {

        insList = new MutableLiveData<>();
        myHandler = new CardHandler();
        //ssetMode(0);

    }

    public void setToken(String token) {
        this.token = token;
    }

    public LiveData<List<DealItem>> getList() {
        return insList;
    }

    private void getMyItem() {
        List<DealItem> iList = new ArrayList<>();
        insList.setValue(iList);
        final HttpHelper net = new HttpHelper(myHandler, token);
        new Thread(){
            @Override
            public void run(){
                net.getMyCard("my");
            }
        }.start();

    }

    private void getOtherItem() {
        List<DealItem> iList = new ArrayList<>();
        insList.setValue(iList);
        final HttpHelper net = new HttpHelper(myHandler, token);
        new Thread(){
            @Override
            public void run(){
                net.getMyCard("other");
            }
        }.start();

    }

    public void setMode(int i){
        mode = i;
        if(i == 0){
            getMyItem();
        }else{
            getOtherItem();
        }
    }

    private class CardHandler extends Handler {
        @Override

        public void handleMessage(Message msg){
            super.handleMessage(msg);
            List<DealItem> iList = (List<DealItem>)msg.obj;
            Date date = new Date(System.currentTimeMillis());
            long now = date.getTime();


            for(int i = 0; i < iList.size(); i++){
                DealItem item = iList.get(i);
                if(item.getDeadline().length() > 0 &&
                        now - Long.parseLong(item.getDeadline()) > 0){
                    iList.remove(i);
                    i--;
                }
            }
            insList.setValue(iList);
        }
    }

}
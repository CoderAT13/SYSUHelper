package com.example.sysuhelper.ui.dashboard;

import android.os.Handler;
import android.os.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.sysuhelper.tools.HttpHelper;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<List<DealItem>> insList;

    private int mode;
    private String type;
    private CardHandler myHandler;

    public DashboardViewModel() {

        insList = new MutableLiveData<>();
        myHandler = new CardHandler();
        setMode(0);

    }

    public LiveData<List<DealItem>> getList() {
        return insList;
    }

    private void getDealItem() {
        List<DealItem> iList = new ArrayList<>();
        insList.setValue(iList);
        final HttpHelper net = new HttpHelper(myHandler);
        new Thread(){
            @Override
            public void run(){
                net.getDealCard(1,"All", 10);
            }
        }.start();
    }

    private void getLostItem() {
        List<DealItem> iList = new ArrayList<>();
        insList.setValue(iList);
        final HttpHelper net = new HttpHelper(myHandler);
        new Thread(){
            @Override
            public void run(){
                net.getDealCard(2,"All", 10);
            }
        }.start();

    }

    public void setMode(int i){
        mode = i;
        if(i == 0){
            getDealItem();
        }else{
            getLostItem();
        }
    }

    public void setType(String t){
        type = t;
        final HttpHelper net = new HttpHelper(myHandler);
        new Thread(){
            @Override
            public void run(){
                net.getDealCard(mode + 1,type, 10);
            }
        }.start();
    }

    private class CardHandler extends Handler {
        @Override

        public void handleMessage(Message msg){
            super.handleMessage(msg);
            List<DealItem> iList = (List<DealItem>)msg.obj;

            insList.setValue(iList);
        }
    }

}
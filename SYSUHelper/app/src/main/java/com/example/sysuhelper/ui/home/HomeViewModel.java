package com.example.sysuhelper.ui.home;

import android.os.Handler;
import android.os.Message;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.sysuhelper.MyApp;
import com.example.sysuhelper.R;
import com.example.sysuhelper.tools.HttpHelper;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    private MutableLiveData<List<DealItem>> insList;

    private CardHandler myHandler;

    private String type;


    public HomeViewModel() {
        mText = new MutableLiveData<>();
        insList = new MutableLiveData<>();
        myHandler = new CardHandler();
        type = "All";
        initDealItem();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<DealItem>> getList() {
        return insList;
    }

    public void initDealItem() {
        List<DealItem> iList = new ArrayList<>();
        insList.setValue(iList);
        final HttpHelper net = new HttpHelper(myHandler);
        new Thread(){
            @Override
            public void run(){
                net.getDealCard(0,type, 10);
            }
        }.start();

    }

    public void setType(String t){
        type = t;
        initDealItem();
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
                if(now - Long.parseLong(item.getDeadline()) > 0){
                    iList.remove(i);
                    i--;
                }
            }
            insList.setValue(iList);
        }
    }


}
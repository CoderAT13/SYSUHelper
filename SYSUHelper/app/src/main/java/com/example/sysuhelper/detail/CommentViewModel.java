package com.example.sysuhelper.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.example.sysuhelper.tools.HttpHelper;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import android.os.Message;

public class CommentViewModel extends ViewModel {

    private MutableLiveData<List<CommentItem>> insList;
    private CommentHandler myHandler;
    private String id;

    public CommentViewModel() {
        insList = new MutableLiveData<>();
        myHandler = new CommentHandler();
        //initItem();

    }

    public LiveData<List<CommentItem>> getList() {
        return insList;
    }

    public void initItem() {
        List<CommentItem> iList = new ArrayList<>();
        insList.setValue(iList);
        final HttpHelper net = new HttpHelper(myHandler);
        new Thread(){
            @Override
            public void run(){
                net.getComment(id);
            }
        }.start();
    }

    public void setId(String id) {
        this.id = id;
    }

    private class CommentHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            List<CommentItem> iList = (List<CommentItem>)msg.obj;
            insList.setValue(iList);
        }
    }

}
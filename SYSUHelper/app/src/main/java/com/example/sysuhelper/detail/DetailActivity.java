package com.example.sysuhelper.detail;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysuhelper.MyApp;
import com.example.sysuhelper.R;
import com.example.sysuhelper.tools.HttpHelper;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;
import com.google.gson.Gson;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private DealItem item;
    private TextView nameText, typeText, contentText, contactText;
    private Button getButton, sendButton, deleteButton, leaveButton;
    private EditText commentText;

    private CommentViewModel commentViewModel;
    private List<CommentItem> iList;
    private CommentAdapter iAdapter;
    private RecyclerView recyclerView;
    private MyHandler myHandler;
    private JoinHandler joinHandler;
    private CommentHandler commentHandler;
    private boolean showContact;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        myHandler = new MyHandler();
        joinHandler = new JoinHandler();
        commentHandler = new CommentHandler();
        showContact = false;
        initView();
        setView();


    }

    private void initView(){
        String itemJson = getIntent().getStringExtra("DealItem");
        final MyApp myApp = (MyApp) getApplication();

        item = new Gson().fromJson(itemJson, DealItem.class);

        nameText = findViewById(R.id.name_text);
        typeText = findViewById(R.id.type_text);
        contentText = findViewById(R.id.content_text);
        contactText = findViewById(R.id.contact_text);
        getButton = findViewById(R.id.get_button);
        sendButton = findViewById(R.id.send_button);
        deleteButton = findViewById(R.id.delete_btn);
        leaveButton = findViewById(R.id.leave_button);
        commentText = findViewById(R.id.comment_text);

        final HttpHelper net = new HttpHelper(joinHandler, myApp.getToken());
        new Thread(){
            @Override
            public void run(){
                net.getJoin(item.getID());
            }
        }.start();


        findViewById(R.id.leave_button).setVisibility(View.GONE);


        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpHelper net = new HttpHelper(joinHandler, myApp.getToken());
                new Thread(){
                    @Override
                    public void run(){
                        net.joinCard(item.getID());
                    }
                }.start();
            }
        });

        leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final HttpHelper net = new HttpHelper(joinHandler, myApp.getToken());
                new Thread(){
                    @Override
                    public void run(){
                        net.leaveCard(item.getID());
                    }
                }.start();
            }
        });


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String comment = commentText.getText().toString();
                InputMethodManager manager = ((InputMethodManager)getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE));
                if(manager != null){
                    manager.hideSoftInputFromWindow(commentText.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
                }
                if(comment.length() == 0) {
                    Toast.makeText(DetailActivity.this, "请输入评论", Toast.LENGTH_SHORT);
                }else{
                    final HttpHelper net = new HttpHelper(commentHandler, myApp.getToken());
                    new Thread(){
                        @Override
                        public void run(){
                            net.addComment(item.getID(), comment);
                        }
                    }.start();
                }

            }
        });


        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HttpHelper net = new HttpHelper(myHandler);
                new Thread(){
                    @Override
                    public void run(){
                        net.deleteCard(item.getID());
                    }
                }.start();
            }
        });


        if (!myApp.getUsername().equals(item.getName())){
            findViewById(R.id.delete_btn).setVisibility(View.GONE);
        }else{
            showContact = true;
            findViewById(R.id.get_button).setVisibility(View.GONE);
            contactText.setText(item.getContact());
            leaveButton.setVisibility(View.GONE);
        }

        initRecyclerView();

    }

    private void setView(){
        nameText.setText(item.getName());
        contentText.setText(item.getContent());
        typeText.setText(item.getType());
    }

    private void initRecyclerView(){
        commentViewModel = ViewModelProviders.of(this).get(CommentViewModel.class);
        commentViewModel.setId(item.getID());
        commentViewModel.initItem();
        recyclerView = findViewById(R.id.recycler_view);
        iAdapter = new CommentAdapter(iList);
        recyclerView.setAdapter(iAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DetailActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        commentViewModel.getList().observe(this, new Observer<List<CommentItem>>() {
            @Override
            public void onChanged(List<CommentItem> s) {
                iAdapter = new CommentAdapter(s);
                recyclerView.setAdapter(iAdapter);
            }
        });
    }

    private void setContactStatus(){
        if(!showContact){
            getButton.setVisibility(View.GONE);
            leaveButton.setVisibility(View.VISIBLE);
            contactText.setText(item.getContact());

            showContact = true;
        }else{
            getButton.setVisibility(View.VISIBLE);
            leaveButton.setVisibility(View.GONE);
            contactText.setText("******");
            showContact = false;
        }
    }

    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            finish();
        }
    }

    private class JoinHandler extends Handler{
        @Override
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            if (msg.what == 1){
                setContactStatus();
            }
            //Toast.makeText(DetailActivity.this,"操作成功",Toast.LENGTH_SHORT).show();
        }
    }

    private class CommentHandler extends Handler{
        @Override

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            commentText.setText("");
            commentViewModel.initItem();
        }
    }

}

package com.example.sysuhelper.person;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.sysuhelper.MyApp;
import com.example.sysuhelper.R;
import com.example.sysuhelper.tools.HttpHelper;


public class InformationActivity extends AppCompatActivity {

    private EditText nameText, sidText, schoolText, majorText;
    private MyHandler handler;
    private UpHandler upHandler;
    private String token;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        initView();
        new Thread(){
            @Override
            public void run(){
                HttpHelper net = new HttpHelper(handler);
                net.getProfile(token);
            }
        }.start();
        Button button = findViewById(R.id.save_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run(){
                        HttpHelper net = new HttpHelper(upHandler);
                        net.updateProfile(token, getProfile());
                    }
                }.start();
            }
        });

    }

    private void initView(){
        nameText = findViewById(R.id.input_name);
        sidText = findViewById(R.id.input_sid);
        schoolText = findViewById(R.id.input_school);
        majorText = findViewById(R.id.input_major);
        MyApp myApp = (MyApp)getApplication();
        token = myApp.getToken();
        handler = new MyHandler();
        upHandler = new UpHandler();
    }

    private void setView(Profile profile){
        nameText.setText(profile.getName());
        sidText.setText(profile.getSid());
        schoolText.setText(profile.getSchool());
        majorText.setText(profile.getMajor());
    }

    private Profile getProfile(){
        return new Profile(nameText.getText().toString(), sidText.getText().toString(), schoolText.getText().toString(), majorText.getText().toString());
    }

    private class MyHandler extends Handler{
        @Override

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 1){
                setView((Profile)msg.obj);
            }

        }
    }

    private class UpHandler extends Handler{
        @Override

        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what == 1){
                Toast.makeText(nameText.getContext(), "保存成功", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(nameText.getContext(), "保存失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

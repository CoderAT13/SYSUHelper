package com.example.sysuhelper.account;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.sysuhelper.MainActivity;
import com.example.sysuhelper.MyApp;
import com.example.sysuhelper.R;
import com.example.sysuhelper.tools.HttpHelper;
import com.example.sysuhelper.tools.TokenMsg;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private EditText nameText, pwdText;
    private Handler handler;
    private ImageView login_img;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button b_register = findViewById(R.id.register_button);
        Button b_login = findViewById(R.id.login_button);
        initView();
        b_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(LoginActivity.this,R.anim.anim_out);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                login_img.setAnimation(animationSet);
                login_img.startAnimation(animationSet);

            }
        });

        b_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = nameText.getText().toString(),
                        pwd = pwdText.getText().toString();
                if (!checkUsername(username)){
                    Toast.makeText(view.getContext(), "用户名字符：a-z,A-Z,0-9，长度6-12", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pwd.length() < 6 || pwd.length() > 12){
                    Toast.makeText(view.getContext(), "密码长度为：6-12", Toast.LENGTH_SHORT).show();
                    return;
                }
                new Thread(){
                    @Override
                    public void run(){
                        HttpHelper net = new HttpHelper(handler);
                        net.login(username,pwd);
                    }
                }.start();

            }
        });
    }

    private void initView(){
        nameText = findViewById(R.id.username_view);
        pwdText = findViewById(R.id.pwd_view);
        handler = new LoginActivity.MyHandler();

        login_img = findViewById(R.id.ic_logo);
        AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(this,R.anim.anim_in);
        login_img.setAnimation(animationSet);
        login_img.startAnimation(animationSet);
    }


    private boolean checkUsername(String username){
        String regex = "([a-zA-Z0-9]{6,12})";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(username);
        return m.matches();
    }

    private class MyHandler extends Handler {
        @Override

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TokenMsg e = (TokenMsg) msg.obj;
            MyApp myApp = (MyApp)getApplication();

            if(e.getStatus().equals("SUCCESS")){
                myApp.setToken(e.getToken());
                myApp.setUsername(nameText.getText().toString());
                AnimationSet animationSet = (AnimationSet) AnimationUtils.loadAnimation(LoginActivity.this,R.anim.anim_out);
                animationSet.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                login_img.setAnimation(animationSet);
                login_img.startAnimation(animationSet);

            }
            else {
                Toast.makeText(pwdText.getContext(), "登录失败", Toast.LENGTH_SHORT).show();
            }

        }
    }
}

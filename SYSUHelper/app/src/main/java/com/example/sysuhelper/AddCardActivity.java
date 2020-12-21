package com.example.sysuhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.sysuhelper.tools.HttpHelper;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddCardActivity extends AppCompatActivity {

    private int mode;
    private int INTEREST_MODE = 0, DEAL_MODE = 1, LOST_MODE = 2;
    private Button confirm_btn;
    private EditText typeText, contentText, placeText, timeText, contactText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);
        Intent intent = getIntent();

        setMode(intent.getIntExtra("mode", 0));
        initView();
    }

    private void initView(){
        if (mode == INTEREST_MODE) {
            findViewById(R.id.place_text).setVisibility(View.GONE);
            findViewById(R.id.input_place).setVisibility(View.GONE);

        }
        else if (mode == DEAL_MODE){
            findViewById(R.id.last_time_text1).setVisibility(View.GONE);
            findViewById(R.id.last_time_text2).setVisibility(View.GONE);
            findViewById(R.id.input_time).setVisibility(View.GONE);
            findViewById(R.id.place_text).setVisibility(View.GONE);
            findViewById(R.id.input_place).setVisibility(View.GONE);
        }
        else if (mode == LOST_MODE){
            findViewById(R.id.last_time_text1).setVisibility(View.GONE);
            findViewById(R.id.last_time_text2).setVisibility(View.GONE);
            findViewById(R.id.input_time).setVisibility(View.GONE);
        }
        contentText = findViewById(R.id.input_content);
        contactText = findViewById(R.id.input_contact);
        typeText = findViewById(R.id.input_type);
        placeText = findViewById(R.id.input_place);
        timeText = findViewById(R.id.input_time);


        confirm_btn = findViewById(R.id.button);
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String type = typeText.getText().toString();
                String content = contentText.getText().toString();
                String contact = contactText.getText().toString();

                if (content.length() == 0){
                    Toast.makeText(contactText.getContext(), "请输入内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (type.length() == 0){
                    Toast.makeText(contactText.getContext(), "请选择类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (contact.length() == 0){
                    Toast.makeText(contactText.getContext(), "请输入联系方式", Toast.LENGTH_SHORT).show();
                    return;
                }

                MyApp myApp = (MyApp)getApplication();

                final DealItem item = new DealItem(mode);
                item.setName(myApp.getUsername());

                item.setContent(content);
                item.setType(type);
                item.setContact(contact);


                if(mode == 0){
                    Date date = new Date(System.currentTimeMillis());
                    long now = date.getTime();
                    if(timeText.getText().toString().length() == 0){
                        Toast.makeText(contactText.getContext(), "请输入持续时间", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    long interval = Integer.parseInt(timeText.getText().toString());
                    now += interval * 60 * 1000;

                    item.setDeadline(String.valueOf(now));
                }

                if (mode == 2){
                    String place = placeText.getText().toString();
                    if(place.length() == 0){
                        Toast.makeText(contactText.getContext(), "请输入地点", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    item.setAddr(place);
                }
                final HttpHelper httpHelper = new HttpHelper(new Handler());
                new Thread(){
                    @Override
                    public void run(){
                        httpHelper.createDealCard(item);
                    }
                }.start();
                finish();

            }
        });


    }

    private void setMode(int i){
        mode = i;
        Log.d("Debug", "" + i);
        TextView title = findViewById(R.id.title_text);
        typeText = findViewById(R.id.input_type);
        final PopupMenu popupMenu = new PopupMenu(AddCardActivity.this, typeText);
        typeText.setFocusable(false);
        typeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                typeText.setText(item.getTitle());
                return true;
            }
        });

        if (mode == INTEREST_MODE){
            title.setText("创建兴趣卡片");
            getMenuInflater().inflate(R.menu.interest_type_menu, popupMenu.getMenu());
        }
        else if (mode == DEAL_MODE){
            title.setText("创建二手交易卡片");
            getMenuInflater().inflate(R.menu.deal_type_menu, popupMenu.getMenu());
        }
        else if (mode == LOST_MODE){
            title.setText("创建失物招领卡片");
            getMenuInflater().inflate(R.menu.lost_type_menu, popupMenu.getMenu());
        }
    }


}

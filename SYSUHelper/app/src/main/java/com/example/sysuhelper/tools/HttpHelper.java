package com.example.sysuhelper.tools;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.sysuhelper.MyApp;
import com.example.sysuhelper.account.Account;


import com.example.sysuhelper.detail.CommentItem;
import com.example.sysuhelper.person.Profile;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;
import com.google.gson.Gson;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HttpHelper {
    private Handler handler;
    private String token;

    @SuppressLint("HandlerLeak")
    public HttpHelper(Handler h){
        handler = h;
    }

    public HttpHelper(Handler h, String t){
        handler = h;
        token = t;
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void login(String username, String password){
        try{
            URL url = new URL("http://10.0.2.2:8080/login" );
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("POST");
            coon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            DataOutputStream wr = new DataOutputStream(coon.getOutputStream());
            Gson gson = new Gson();
            String jsonString = gson.toJson(new Account(username,password));
            wr.write(jsonString.getBytes());
            wr.flush();
            wr.close();

            coon.setReadTimeout(6000);
            coon.connect();
            //获取响应码
            if(coon.getResponseCode() == 200){
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024*512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1){
                    baos.write(b,0,len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);
                JSONObject obj = new JSONObject(msg);

                String status = obj.getString("status");

                //参数1：满足json对象格式的字符串
                String data = msg;
                TokenMsg t_msg = gson.fromJson(data, TokenMsg.class);

                Message message = handler.obtainMessage();
                message.obj = t_msg;
                handler.sendMessage(message);

            }else{
                Message message = handler.obtainMessage();
                message.obj = new TokenMsg("","failed");
                handler.sendMessage(message);
            }
            coon.disconnect();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void register(String username, String password){
        try{
            URL url = new URL("http://10.0.2.2:8080/register");
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("POST");
            coon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            DataOutputStream wr = new DataOutputStream(coon.getOutputStream());
            Gson gson = new Gson();
            String jsonString = gson.toJson(new Account(username,password));
            wr.write(jsonString.getBytes());
            wr.flush();
            wr.close();
            coon.setReadTimeout(6000);
            Log.d("debug","In HTTP");
            coon.connect();
            //获取响应码
            if(coon.getResponseCode() == 200){
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024*512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1){
                    baos.write(b,0,len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);
                JSONObject obj = new JSONObject(msg);

                String status = obj.getString("status");

                //参数1：满足json对象格式的字符串
                String data = msg;
                TokenMsg t_msg = gson.fromJson(data, TokenMsg.class);

                Message message = handler.obtainMessage();
                message.obj = t_msg;
                handler.sendMessage(message);

            }else{
                Message message = handler.obtainMessage();
                message.obj = new TokenMsg("","failed");
                handler.sendMessage(message);
            }

            coon.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getProfile(String token){
        try{
            URL url = new URL("http://10.0.2.2:8080/getprofile?token="+token );
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            //获取响应码
            if(coon.getResponseCode() == 200){
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024*512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1){
                    baos.write(b,0,len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);
                JSONObject obj = new JSONObject(msg);

                String status = obj.getString("status");
                String data = obj.getString("profile");
                if (status.equals("SUCCESS") && data.length()!=0){
                    Gson gson = new Gson();
                    Profile profile = gson.fromJson(data, Profile.class);
                    Message message = handler.obtainMessage();
                    message.obj = profile;
                    message.what = 1;
                    handler.sendMessage(message);
                }
                else{
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    handler.sendMessage(message);
                }


            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateProfile(String token, Profile profile){
        try{

            URL url = new URL("http://10.0.2.2:8080/updateprofile?token="+token);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("POST");
            coon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            DataOutputStream wr = new DataOutputStream(coon.getOutputStream());
            Gson gson = new Gson();
            String jsonString = gson.toJson(profile);
            wr.write(jsonString.getBytes());
            wr.flush();
            wr.close();
            coon.setReadTimeout(6000);
            coon.connect();
            //获取响应码
            if(coon.getResponseCode() == 200){
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024*512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1){
                    baos.write(b,0,len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);
                JSONObject obj = new JSONObject(msg);

                String status = obj.getString("status");

                if (status.equals("SUCCESS")){
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);
                }
                else{
                    Message message = handler.obtainMessage();
                    message.what = 0;
                    handler.sendMessage(message);
                }


            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // 创建帖子
    public void createDealCard(DealItem card) {
        /*
        * DealItem
        * */
        try{
            URL url = new URL("http://10.0.2.2:8080/createcard" );
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("POST");
            coon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            DataOutputStream wr = new DataOutputStream(coon.getOutputStream());
            Gson gson = new Gson();
            String jsonString = gson.toJson(card);
            Log.d("Debug", jsonString);
            wr.write(jsonString.getBytes()); wr.flush(); wr.close();

            coon.setReadTimeout(6000);
            coon.connect();

            if(coon.getResponseCode() == 200){
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024*512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1){
                    baos.write(b,0,len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);
                JSONObject obj = new JSONObject(msg);

                String status = obj.getString("status");
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // 获取DealCard，分 mode
    public void getDealCard(int mode, String type, int num) {
        /*
         * { mode , "", type, num }
         * { status, cards: []DealItem}
         * */
        try{
            URL url = new URL("http://10.0.2.2:8080/getdealcard?mode="+mode+"&type="+type+"&num="+num );
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            if(coon.getResponseCode() == 200) {
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();


                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(msg).getAsJsonObject();
                JsonArray cardJsonList = obj.getAsJsonArray("cards");

                List<DealItem> cardList = new ArrayList<>();
                for(JsonElement card : cardJsonList){
                    DealItem item = gson.fromJson(card, DealItem.class);
                    cardList.add(item);
                }

                Message message = handler.obtainMessage();
                message.obj = cardList;
                handler.sendMessage(message);
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getMyCard(String mode){
        /*
         *
         * { status, cards: []DealItem}
         * */
        try{
            URL url = new URL("http://10.0.2.2:8080/getmycard?mode="+mode+"&token="+token );
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            if(coon.getResponseCode() == 200) {
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();
                Log.d("TAG", msg);


                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(msg).getAsJsonObject();
                JsonArray cardJsonList = obj.getAsJsonArray("cards");

                List<DealItem> cardList = new ArrayList<>();
                for(JsonElement card : cardJsonList){
                    DealItem item = gson.fromJson(card, DealItem.class);
                    cardList.add(item);
                }

                Message message = handler.obtainMessage();
                message.obj = cardList;
                handler.sendMessage(message);
            }

        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteCard(String id) {
        try {
            URL url = new URL("http://10.0.2.2:8080/deletecard?id=" + id);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            if (coon.getResponseCode() == 200) {
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();

                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void joinCard(String id) {

        try {
            URL url = new URL("http://10.0.2.2:8080/joincard?id=" + id + "&token="+token );
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            if (coon.getResponseCode() == 200) {
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();

                Message message = handler.obtainMessage();
                message.what = 1;
                handler.sendMessage(message);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void leaveCard(String id) {
        try {
            URL url = new URL("http://10.0.2.2:8080/leavecard?id=" + id + "&token=" + token);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            if (coon.getResponseCode() == 200) {
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();

                Message message = handler.obtainMessage();
                message.what = 1;
                handler.sendMessage(message);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getJoin(String id) {
        try {
            URL url = new URL("http://10.0.2.2:8080/getjoin?id=" + id + "&token=" + token);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            if (coon.getResponseCode() == 200) {
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);
                JSONObject obj = new JSONObject(msg);
                Message message = handler.obtainMessage();
                message.what = obj.getBoolean("status") ? 1 : 0;

                handler.sendMessage(message);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void addComment(String id, String comment) {
        try{
            URL url = new URL("http://10.0.2.2:8080/addcomment" );
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("POST");
            coon.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            DataOutputStream wr = new DataOutputStream(coon.getOutputStream());
            Gson gson = new Gson();
            String jsonString = gson.toJson(new CommentItem(id, token, comment));
            Log.d("Debug", jsonString);
            wr.write(jsonString.getBytes()); wr.flush(); wr.close();
            coon.setReadTimeout(6000);
            coon.connect();

            if(coon.getResponseCode() == 200){
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024*512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1){
                    baos.write(b,0,len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);
                Message message = handler.obtainMessage();
                handler.sendMessage(message);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getComment(String id){
        try {
            URL url = new URL("http://10.0.2.2:8080/getcomment?id=" + id);
            HttpURLConnection coon = (HttpURLConnection) url.openConnection();
            coon.setRequestMethod("GET");
            coon.setReadTimeout(6000);
            coon.connect();
            if (coon.getResponseCode() == 200) {
                InputStream in = coon.getInputStream();
                byte[] b = new byte[1024 * 512];
                int len = 0;
                //建立缓存流，保存所读取的字节数组
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                while ((len = in.read(b)) > -1) {
                    baos.write(b, 0, len);
                }
                String msg = baos.toString();
                Log.e("TAG",msg);

                Gson gson = new Gson();
                JsonParser parser = new JsonParser();
                JsonObject obj = parser.parse(msg).getAsJsonObject();
                JsonArray cardJsonList = obj.getAsJsonArray("comments");

                List<CommentItem> commentList = new ArrayList<>();
                for(JsonElement comment : cardJsonList){
                    CommentItem item = gson.fromJson(comment, CommentItem.class);
                    commentList.add(item);
                }

                Message message = handler.obtainMessage();
                message.obj = commentList;
                handler.sendMessage(message);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

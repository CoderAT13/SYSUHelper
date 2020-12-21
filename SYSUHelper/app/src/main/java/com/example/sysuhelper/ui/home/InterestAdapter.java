package com.example.sysuhelper.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysuhelper.R;
import com.example.sysuhelper.detail.DetailActivity;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;
import com.google.gson.Gson;

import java.util.Date;
import java.util.List;

public class InterestAdapter extends RecyclerView.Adapter<InterestAdapter.ViewHolder>{

    private List<DealItem> mFruitList;

    private Context iContext;

    private CountDownTimer countDownTimer;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;

        TextView iName;
        TextView iType;
        TextView iContent;
        TextView iDate;
        TextView iTime;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            iName = view.findViewById(R.id.name_text);
            iContent = view.findViewById(R.id.content_text);
            iType = view.findViewById(R.id.type_text);
            iDate = view.findViewById(R.id.in_sit_text);

            iTime = view.findViewById(R.id.time_text);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_model, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                DealItem fruit = mFruitList.get(position);
                Intent intent = new Intent(iContext, DetailActivity.class);
                String itemJson = new Gson().toJson(fruit);
                intent.putExtra("DealItem", itemJson);
                iContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final DealItem fruit = mFruitList.get(position);
        holder.iName.setText(fruit.getName());
        holder.iType.setText(fruit.getType());
        holder.iDate.setText(fruit.getCreatedAt().substring(0,10));
        holder.iContent.setText(fruit.getContent());

        long deadline = Long.parseLong(fruit.getDeadline());

        Date date = new Date(System.currentTimeMillis());
        long now = date.getTime();
        if( deadline - now > 0){
            holder.iTime.setText("");
            countDownTimer = new CountDownTimer(deadline-now,1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    holder.iTime.setText(formatTime(millisUntilFinished));
                }

                @Override
                public void onFinish() {
                    holder.iTime.setText("已结束");
                }
            }.start();
        }else{
            holder.iTime.setText("已结束");
        }

    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    public InterestAdapter(Context c, List<DealItem> fruitList) {
        iContext = c ;
        mFruitList = fruitList;
    }

    private String formatTime(long millisecond){
        int minute;
        int second;
        int hour;
        second = (int)(millisecond/1000) % 60;
        minute = (int)((millisecond/1000)/60) % 60;
        hour = (int)(millisecond/1000)/60/60;
        return (hour > 0 ? hour + ":" : "") +
                (minute >= 10 ? minute :"0" + minute) + ":" +
                (second >= 10 ? second : "0" + second);
    }


}

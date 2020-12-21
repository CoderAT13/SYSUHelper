package com.example.sysuhelper.ui.dashboard.deal;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysuhelper.detail.DetailActivity;
import com.example.sysuhelper.R;
import com.google.gson.Gson;


import java.util.List;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.ViewHolder>{

    private List<DealItem> mFruitList;
    Activity iContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;

        TextView iName;
        TextView iType;
        TextView iContent;
        TextView iInSit;
        TextView iNotSit;
        TextView iTime;

        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            iName = view.findViewById(R.id.name_text);
            iContent = view.findViewById(R.id.content_text);
            iType = view.findViewById(R.id.type_text);
            iInSit = view.findViewById(R.id.in_sit_text);
            iNotSit = view.findViewById(R.id.not_sit_text);
            iTime = view.findViewById(R.id.time_text);
        }
    }

    @NonNull
    @Override
    public DealAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_model, parent, false);
        final DealAdapter.ViewHolder holder = new DealAdapter.ViewHolder(view);
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                DealItem fruit = mFruitList.get(position);
                Intent intent = new Intent(iContext, DetailActivity.class);
                Gson gson = new Gson();
                intent.putExtra("DealItem",gson.toJson(fruit));
                iContext.startActivityForResult(intent, 3);

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DealAdapter.ViewHolder holder, int position) {
        DealItem fruit = mFruitList.get(position);
        holder.iName.setText(fruit.getName());
        holder.iType.setText(fruit.getType());
        holder.iContent.setText(fruit.getContent());
        holder.iTime.setText(fruit.getCreatedAt().substring(0,10));
        holder.iInSit.setText(fruit.getAddr());
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    public DealAdapter(Activity context, List<DealItem> fruitList) {
        iContext = context;
        mFruitList = fruitList;
    }


}

package com.example.sysuhelper.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysuhelper.R;


import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder>{

    private List<CommentItem> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View fruitView;

        TextView iName;
        TextView iContent;


        public ViewHolder(View view) {
            super(view);
            fruitView = view;
            iName = view.findViewById(R.id.name_text);
            iContent = view.findViewById(R.id.comment_text);
        }
    }

    @NonNull
    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_model, parent, false);
        final CommentAdapter.ViewHolder holder = new CommentAdapter.ViewHolder(view);
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                //CommentItem fruit = mFruitList.get(position);
                //Toast.makeText(view.getContext(), "你点击了View"+ fruit.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.ViewHolder holder, int position) {
        CommentItem fruit = mFruitList.get(position);
        holder.iName.setText(fruit.getName());
        holder.iContent.setText(fruit.getContent());


    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }

    public CommentAdapter(List<CommentItem> fruitList) {
        mFruitList = fruitList;
    }


}

package com.example.sysuhelper.ui.notifications;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProviders;




import com.example.sysuhelper.R;
import com.example.sysuhelper.account.LoginActivity;
import com.example.sysuhelper.person.AboutActivity;
import com.example.sysuhelper.person.InformationActivity;
import com.example.sysuhelper.person.StepActivity;
import com.example.sysuhelper.ui.home.InterestItem;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        Button b_about = root.findViewById(R.id.about_view);
        Button b_step = root.findViewById(R.id.step_view);
        Button b_information = root.findViewById(R.id.person_view);

        b_about.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), AboutActivity.class));
            }
        });
        b_step.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), StepActivity.class));
            }
        });
        b_information.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InformationActivity.class));
            }
        });






        return root;
    }





}
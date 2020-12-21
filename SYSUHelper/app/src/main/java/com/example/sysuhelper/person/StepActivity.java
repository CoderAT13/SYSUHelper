package com.example.sysuhelper.person;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysuhelper.MyApp;
import com.example.sysuhelper.R;
import com.example.sysuhelper.ui.dashboard.DashboardViewModel;
import com.example.sysuhelper.ui.dashboard.deal.DealAdapter;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class StepActivity extends AppCompatActivity {

    private StepViewModel stepViewModel;
    private RecyclerView recyclerView;

    private List<DealItem> iList;
    private DealAdapter iAdapter;
    private int mode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_step);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        stepViewModel =
                ViewModelProviders.of(this).get(StepViewModel.class);
        MyApp myApp = (MyApp)getApplication();
        stepViewModel.setToken(myApp.getToken());
        stepViewModel.setMode(0);
        mode = 0;
        recyclerView = findViewById(R.id.recycler_view);
        iAdapter = new DealAdapter(StepActivity.this, iList);
        recyclerView.setAdapter(iAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(StepActivity.this, LinearLayoutManager.VERTICAL, false));

        stepViewModel.getList().observe(this, new Observer<List<DealItem>>() {
            @Override
            public void onChanged(@Nullable List<DealItem> s) {
                iAdapter = new DealAdapter(StepActivity.this, s);
                recyclerView.setAdapter(iAdapter);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mode = (int)tab.getPosition();
                stepViewModel.setMode((int)tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }

    @Override
    public void onResume(){
        super.onResume();
        stepViewModel.setMode(mode);
    }
}

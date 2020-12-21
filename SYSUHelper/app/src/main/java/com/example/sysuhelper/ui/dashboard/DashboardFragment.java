package com.example.sysuhelper.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.sysuhelper.AddCardActivity;
import com.example.sysuhelper.R;
import com.example.sysuhelper.ui.dashboard.deal.DealAdapter;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;


import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RecyclerView recyclerView;

    private List<DealItem> iList;
    private DealAdapter iAdapter;
    private int mode;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        TabLayout tabLayout = (TabLayout)root.findViewById(R.id.tab_layout);
        recyclerView = root.findViewById(R.id.recycler_view);
        iAdapter = new DealAdapter(getActivity(), iList);
        recyclerView.setAdapter(iAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        mode = 1;
        View fabAdd = root.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                intent.putExtra("mode", mode);
                startActivityForResult(intent,3);
            }
        });


        dashboardViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<DealItem>>() {
            @Override
            public void onChanged(@Nullable List<DealItem> s) {
                iAdapter = new DealAdapter(getActivity(), s);
                recyclerView.setAdapter(iAdapter);
            }
        });

        View fabScreening = root.findViewById(R.id.fab_screening);
        final PopupMenu popupMenu1 = new PopupMenu(getActivity(), fabScreening);
        popupMenu1.getMenuInflater().inflate(R.menu.deal_type_menu, popupMenu1.getMenu());
        popupMenu1.getMenu().add("All");

        final PopupMenu popupMenu2 = new PopupMenu(getActivity(), fabScreening);
        popupMenu2.getMenuInflater().inflate(R.menu.lost_type_menu, popupMenu2.getMenu());
        popupMenu2.getMenu().add("All");

        PopupMenu.OnMenuItemClickListener listener = new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                dashboardViewModel.setType(item.getTitle().toString());

                return true;
            }
        };

        popupMenu1.setOnMenuItemClickListener(listener);
        popupMenu2.setOnMenuItemClickListener(listener);

        fabScreening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mode == 1){
                    popupMenu1.show();
                }
                else if(mode == 2){
                    popupMenu2.show();
                }
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //选中某个tab
                mode = (int)tab.getPosition() + 1;
                dashboardViewModel.setMode((int)tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //当tab从选择到未选择
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //已经选中tab后的重复点击tab
            }
        });

        return root;

    }

    @Override
    public void onResume() {

        super.onResume();
        dashboardViewModel.setType("All");
    }

}
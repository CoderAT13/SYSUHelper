package com.example.sysuhelper.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sysuhelper.AddCardActivity;
import com.example.sysuhelper.R;
import com.example.sysuhelper.ui.dashboard.deal.DealItem;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private RecyclerView recyclerView;
    private List<DealItem> iList;
    private InterestAdapter iAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.recycler_view);
        iAdapter = new InterestAdapter(getActivity(),iList);
        recyclerView.setAdapter(iAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        View fabAdd = root.findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddCardActivity.class);
                intent.putExtra("mode",0);
                startActivityForResult(intent,3);
            }
        });
        View fabScreening = root.findViewById(R.id.fab_screening);
        final PopupMenu popupMenu = new PopupMenu(getActivity(), fabScreening);
        popupMenu.getMenuInflater().inflate(R.menu.interest_type_menu, popupMenu.getMenu());
        popupMenu.getMenu().add("All");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                homeViewModel.setType(item.getTitle().toString());
                return true;
            }
        });
        fabScreening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMenu.show();
            }
        });


        homeViewModel.getList().observe(getViewLifecycleOwner(), new Observer<List<DealItem>>() {
            @Override
            public void onChanged(@Nullable List<DealItem> s) {
                iAdapter = new InterestAdapter(getActivity(),s);
                recyclerView.setAdapter(iAdapter);

            }
        });
        return root;
    }


    @Override
    public void onResume() {

        super.onResume();
        homeViewModel.initDealItem();
    }


}
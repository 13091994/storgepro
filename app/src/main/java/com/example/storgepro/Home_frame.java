package com.example.storgepro;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Home_frame extends Fragment {
private ImageButton add_btn,search_btn,edit_btn,delete_btn;
TextView username;
    DB_Sqlite DB;
    public Home_frame() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home_frame, container, false);
        add_btn = v.findViewById(R.id.add_pro_id);
        search_btn = v.findViewById(R.id.search_pro_id);
        edit_btn = v.findViewById(R.id.edit_pro_id);
        delete_btn = v.findViewById(R.id.delete_pro_id);
        username = v.findViewById(R.id.username);
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setChecked(true);
        DB =  new DB_Sqlite(getActivity());
        username.setText(DB.get_username().replace('_', ' '));
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new New_Product_frame());

            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new Search_Product_frame());
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new Edit_Product_frame());
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFragment(new Delete_Product_frame());
            }
        });
        return v;
    }
    public void setFragment(Fragment f){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.frame2,f);
        ft.addToBackStack(null);
        ft.commit();
    }




}

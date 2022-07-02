package com.example.tabtest2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    FragmentA fragmentA;
    FragmentB fragmentB;
    FragmentC fragmentC;

    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Tab1");
        tabs = findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("Tab1"));
        tabs.addTab(tabs.newTab().setText("Tab2"));
        tabs.addTab(tabs.newTab().setText("Tab3"));

        fragmentA = new FragmentA();
        fragmentB = new FragmentB();
        fragmentC = new FragmentC();

        getSupportFragmentManager().beginTransaction().replace(R.id.containers, fragmentA).commit();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                position = tab.getPosition();

                Fragment selected = null;

                if(position==0){
                    selected = fragmentA;
                    getSupportActionBar().setTitle("Contacts");
                }else if(position==1){
                    selected = fragmentB;
                    getSupportActionBar().setTitle("Album");
                }else if(position==2){
                    selected = fragmentC;
                    getSupportActionBar().setTitle("Tab3");
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.containers, selected).commit();
            }
        });
    }
}
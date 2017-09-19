package com.example.weight;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.example.weight.drawer.DrawerLayout;

public class DrawerActivity extends Activity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        drawerLayout = (DrawerLayout) findViewById(R.id.view_drawer);
        drawerLayout.showFootView(true);
        drawerLayout.showHeadView(true);
        LinearLayout headView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_drawer_head, null);
        LinearLayout contentView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_drawer_content, null);
        LinearLayout footView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.view_drawer_foot, null);
        drawerLayout.addHeadView(headView);
        drawerLayout.addContentView(contentView);
        drawerLayout.addFootView(footView);
    }
}

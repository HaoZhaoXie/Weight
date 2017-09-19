package com.example.weight.drawer.impl;

import android.view.View;

public interface DrawerImpl {
    void addHeadView(View headView);

    void addFootView(View footView);

    void addContentView(View contentView);

    void showHeadView(boolean show);

    void showFootView(boolean show);
}

package com.example.weight.drawer;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.weight.R;
import com.example.weight.drawer.control.DrawerControl;
import com.example.weight.drawer.impl.DrawerImpl;

public class DrawerLayout extends LinearLayout implements DrawerImpl, DrawerControl{

    private OnHeadViewShowListener onHeadViewShowListener;
    private OnFootViewShowListener onFootViewShowListener;
    private LinearLayout mHeadView;
    private LinearLayout mContentView;
    private LinearLayout mFootView;
    private CustomScrollView mControlView;

    public DrawerLayout(Context context) {
        this(context, null);
    }

    public DrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHeadView = (LinearLayout) findViewById(R.id.ll_head);
        mContentView = (LinearLayout) findViewById(R.id.ll_content);
        mFootView = (LinearLayout) findViewById(R.id.ll_foot);
        mControlView = (CustomScrollView) findViewById(R.id.sv_control);
        mControlView.setSmartScrollChangedListener(iSmartScrollChangedListener);
    }

    interface OnHeadViewShowListener{
        void onHeadViewShow(boolean isShow);
    }

    interface OnFootViewShowListener{
        void onFootViewShow(boolean isShow);
    }

    public void setOnHeadViewShowListener(OnHeadViewShowListener onHeadViewShowListener){
        this.onHeadViewShowListener = onHeadViewShowListener;
    }

    public void setOnFootViewShowListener(OnFootViewShowListener onFootViewShowListener){
        this.onFootViewShowListener = onFootViewShowListener;
    }

    @Override
    public void addHeadView(View headView){
        mHeadView.removeAllViews();
        mHeadView.addView(headView);
    }

    @Override
    public void addFootView(View footView){
        mFootView.removeAllViews();
        mFootView.addView(footView);
    }

    @Override
    public void addContentView(View contentView){
        mContentView.removeAllViews();
        mContentView.addView(contentView);
    }

    @Override
    public void showHeadView(boolean show) {
        mHeadView.setVisibility(show ? VISIBLE : GONE);
    }

    @Override
    public void showFootView(boolean show) {
        mFootView.setVisibility(show ? VISIBLE : GONE);
    }

    private CustomScrollView.ISmartScrollChangedListener iSmartScrollChangedListener = new CustomScrollView.ISmartScrollChangedListener() {
        @Override
        public void onScrolledToBottom(float startY, float moveY) {
            Log.e("customScroll", "is scroll to bottom " + "startY : " + startY + " moveY : " + moveY);
            mHeadView.setVisibility(GONE);
        }

        @Override
        public void onScrolledToTop(float startY, float moveY) {
            Log.e("customScroll", "is scroll to top" + "startY : " + startY + " moveY : " + moveY);
            mHeadView.setVisibility(VISIBLE);
        }

        @Override
        public void onScroll(float startY, float moveY) {

        }
    };
}

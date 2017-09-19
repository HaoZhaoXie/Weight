package com.example.weight.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
    private boolean isScrolledToTop = true;
    private boolean isScrolledToBottom = false;

    private ScrollViewListener scrollViewListener = null;
    private ISmartScrollChangedListener mSmartScrollChangedListener = null;

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public interface ScrollViewListener {
        void onScrollChanged(CustomScrollView scrollView, int x, int y, int oldX, int oldY);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        this.scrollViewListener = scrollViewListener;
    }

    public interface ISmartScrollChangedListener {
        void onScrolledToBottom();

        void onScrolledToTop();
    }

    public void setSmartScrollChangedListener(ISmartScrollChangedListener mSmartScrollChangedListener) {
        this.mSmartScrollChangedListener = mSmartScrollChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!canScroll() && ev.getAction() == MotionEvent.ACTION_MOVE) {
            if (scrollViewListener != null) {
                scrollViewListener.onScrollChanged(this, 0, 0, 0, 0);
            }
        }
        return super.onTouchEvent(ev);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        notifyScrollChangedListeners();
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
// API 9及之后走onOverScrolled方法监听,如兼容API 9之前的版本恢复此段代码
//        if (android.os.Build.VERSION.SDK_INT < 9) {
//            if (getScrollY() == 0) {
//                isScrolledToTop = true;
//                isScrolledToBottom = false;
//            } else if (getScrollY() + getHeight() - getPaddingTop()-getPaddingBottom() == getChildAt(0).getHeight()) {
//                isScrolledToBottom = true;
//                isScrolledToTop = false;
//            } else {
//                isScrolledToTop = false;
//                isScrolledToBottom = false;
//            }
//            notifyScrollChangedListeners();
//        }
    }

    private void notifyScrollChangedListeners() {
        if (isScrolledToTop) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToTop();
            }
        } else if (isScrolledToBottom) {
            if (mSmartScrollChangedListener != null) {
                mSmartScrollChangedListener.onScrolledToBottom();
            }
        }
    }

    public boolean isScrolledToTop() {
        return isScrolledToTop;
    }

    public boolean isScrolledToBottom() {
        return isScrolledToBottom;
    }

    /**
     * @return Returns true this ScrollView can be scrolled
     */
    private boolean canScroll() {
        View child = getChildAt(0);
        if (child != null) {
            int childHeight = child.getHeight();
            return getHeight() < childHeight + getPaddingTop() + getPaddingBottom();
        }
        return false;
    }
}

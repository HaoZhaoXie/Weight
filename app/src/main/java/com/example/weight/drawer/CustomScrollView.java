package com.example.weight.drawer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {
    private static final int JUMP_SENSITIVITY = 240;
    private boolean isScrolledToTop = true;
    private boolean isScrolledToBottom = false;
    private ScrollViewListener scrollViewListener = null;
    private ISmartScrollChangedListener mSmartScrollChangedListener = null;
    private float oldEventY = 0;
    private float moveEventY = 0;

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
        void onScrolledToBottom(float startY, float moveY);

        void onScrolledToTop(float startY, float moveY);

        void onScroll(float startY, float moveY);
    }

    public void setSmartScrollChangedListener(ISmartScrollChangedListener mSmartScrollChangedListener) {
        this.mSmartScrollChangedListener = mSmartScrollChangedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_MOVE: {
                if (!canScroll()) {
                    if (ev.getY() - oldEventY > JUMP_SENSITIVITY) {
                        isScrolledToTop = true;
                        isScrolledToBottom = false;
                    } else if (ev.getY() - oldEventY < -JUMP_SENSITIVITY) {
                        isScrolledToTop = false;
                        isScrolledToBottom = true;
                    } else {
                        isScrolledToTop = false;
                        isScrolledToBottom = false;
                    }
                } else {
                    if (getScrollY() == 0) {
                        isScrolledToTop = true;
                        isScrolledToBottom = false;
                    } else if (getScrollY() + getHeight() - getPaddingTop() - getPaddingBottom() == getChildAt(0).getHeight()) {
                        isScrolledToBottom = true;
                        isScrolledToTop = false;
                    } else {
                        isScrolledToTop = false;
                        isScrolledToBottom = false;
                    }
                    notifyScrollChangedListeners(oldEventY, ev.getY());
                }
                moveEventY = ev.getY();
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                resetEventY(ev.getY());
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                resetEventY(0);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                resetEventY(0);
                break;
        }
        return super.onTouchEvent(ev);
    }

    private void resetEventY(float eventY) {
        oldEventY = eventY;
    }

//    @Override
//    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
//        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
//        if (canScroll()) {
//            if (scrollY == 0) {
//                isScrolledToTop = clampedY;
//                isScrolledToBottom = false;
//            } else {
//                isScrolledToTop = false;
//                isScrolledToBottom = clampedY;
//            }
//        }
//        notifyScrollChangedListeners(oldEventY, moveEventY);
//    }

    @Override
    protected void onScrollChanged(int x, int y, int oldX, int oldY) {
        super.onScrollChanged(x, y, oldX, oldY);
        if (scrollViewListener != null) {
            scrollViewListener.onScrollChanged(this, x, y, oldX, oldY);
        }
    }

    private void notifyScrollChangedListeners(float startY, float moveY) {
        if (isScrolledToTop) {
            notifyScrollToTop(startY, moveY);
        } else if (isScrolledToBottom) {
            notifyScrollToBottom(startY, moveY);
        } else {
            notifyScroll(startY, moveY);
        }
    }

    private void notifyScrollToTop(float startY, float moveY) {
        if (mSmartScrollChangedListener != null) {
            mSmartScrollChangedListener.onScrolledToTop(startY, moveY);
        }
    }

    private void notifyScrollToBottom(float startY, float moveY) {
        if (mSmartScrollChangedListener != null) {
            mSmartScrollChangedListener.onScrolledToBottom(startY, moveY);
        }
    }

    private void notifyScroll(float startY, float moveY) {
        if (mSmartScrollChangedListener != null && startY != 0) {
            mSmartScrollChangedListener.onScroll(startY, moveY);
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

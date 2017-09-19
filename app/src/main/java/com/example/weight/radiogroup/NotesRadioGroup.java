package com.example.weight.radiogroup;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

/**
 * Created by xiejie on 16/3/8.
 */
public class NotesRadioGroup extends LinearLayout
{
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    /**
     * {@inheritDoc}
     */
    public NotesRadioGroup(Context context)
    {
        super(context);
        init();
    }

    /**
     * {@inheritDoc}
     */
    public NotesRadioGroup(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        // retrieve selected radio button as requested by the user in the
        // XML layout file
        mCheckedId = View.NO_ID;
        setOrientation(getOrientation());
        init();
    }

    private void init()
    {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener)
    {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate()
    {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        if ( mCheckedId != -1 )
        {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params)
    {
        addCompoundButton(child);
        super.addView(child, index, params);
    }

    private void addCompoundButton(final View child)
    {
        if ( child instanceof CompoundButton )
        {
            final CompoundButton button = (CompoundButton) child;
//            button.setOnTouchListener(new OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    ((CompoundButton) child).setChecked(true);
//                    checkCompoundButton(NotesRadioGroup.this,(CompoundButton) child);
//                    setCheckedId(child.getId());
//                    if(mOnCheckedChangeListener != null){
//                        mOnCheckedChangeListener.onCheckedChanged(NotesRadioGroup.this,child.getId());
//                    }
//                    return true;
//                }
//            });
            button.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
        }
        else if ( child instanceof ViewGroup )
        {
            int childCount = ((ViewGroup) child).getChildCount();
            for ( int i = 0; i < childCount; i++ )
            {
                addCompoundButton(((ViewGroup) child).getChildAt(i));
            }

        }
    }

    private void checkCompoundButton(ViewGroup parent, CompoundButton compoundButton)
    {
        for ( int i = 0; i < parent.getChildCount(); i++ )
        {
            View child = parent.getChildAt(i);
            if ( child instanceof CompoundButton )
            {
                if ( child.getId() == compoundButton.getId() )
                {
                    //do nothing
                }
                else
                {
                    ((CompoundButton) child).setChecked(false);
                }
            }
            else if ( child instanceof ViewGroup )
            {
                for ( int j = 0; j < ((ViewGroup) child).getChildCount(); j++ )
                {
                    checkCompoundButton((ViewGroup) child, compoundButton);
                }
            }
        }
    }

    /**
     * <p>Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id the unique id of the radio button to select in this group
     *
     * @see #getCheckedButtonId()
     * @see #clearCheck()
     */
    public void check(int id)
    {
        // don't even bother
        if ( id != -1 && (id == mCheckedId) )
        {
            return;
        }

        if ( mCheckedId != -1 )
        {
            setCheckedStateForView(mCheckedId, false);
        }

        if ( id != -1 )
        {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(int id)
    {
        if ( mCheckedId != id )
        {
            mCheckedId = id;
            if ( mOnCheckedChangeListener != null )
            {
                mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
            }
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked)
    {
        View checkedView = findViewById(viewId);
        if ( checkedView != null && checkedView instanceof CompoundButton )
        {
            ((CompoundButton) checkedView).setChecked(checked);
        }
    }

    /**
     * <p>Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected radio button in this group
     *
     * @see #check(int)
     * @see #clearCheck()
     */

    public int getCheckedButtonId()
    {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and {@link #getCheckedButtonId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedButtonId()
     */
    public void clearCheck()
    {
        check(-1);
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener)
    {
        mOnCheckedChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p)
    {
        return p instanceof LayoutParams;
    }

    @Override
    protected LinearLayout.LayoutParams generateDefaultLayoutParams()
    {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName()
    {
        return NotesRadioGroup.class.getName();
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener
    {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(NotesRadioGroup group, int checkedId);
    }

    /**
     * <p>This set of layout parameters defaults the width and the height of
     * the children to {@link #WRAP_CONTENT} when they are not specified in the
     * XML file. Otherwise, this class ussed the value read from the XML file.</p>
     */
    public static class LayoutParams extends LinearLayout.LayoutParams
    {
        /**
         * {@inheritDoc}
         */
        public LayoutParams(Context c, AttributeSet attrs)
        {
            super(c, attrs);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h)
        {
            super(w, h);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(int w, int h, float initWeight)
        {
            super(w, h, initWeight);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(ViewGroup.LayoutParams p)
        {
            super(p);
        }

        /**
         * {@inheritDoc}
         */
        public LayoutParams(MarginLayoutParams source)
        {
            super(source);
        }

        /**
         * <p>Fixes the child's width to
         * {@link ViewGroup.LayoutParams#WRAP_CONTENT} and the child's
         * height to  {@link ViewGroup.LayoutParams#WRAP_CONTENT}
         * when not specified in the XML file.</p>
         *
         * @param a the styled attributes set
         * @param widthAttr the width attribute to fetch
         * @param heightAttr the height attribute to fetch
         */
        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr)
        {

            if ( a.hasValue(widthAttr) )
            {
                width = a.getLayoutDimension(widthAttr, "layout_width");
            }
            else
            {
                width = WRAP_CONTENT;
            }

            if ( a.hasValue(heightAttr) )
            {
                height = a.getLayoutDimension(heightAttr, "layout_height");
            }
            else
            {
                height = WRAP_CONTENT;
            }
        }
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener
    {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
        {
            // prevents from infinite recursion
            if ( mProtectFromCheckedChange )
            {
                return;
            }
            mProtectFromCheckedChange = true;
            if ( mCheckedId != -1 )
            {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener
    {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        public void onChildViewAdded(View parent, View child)
        {
            if ( parent == NotesRadioGroup.this )
            {
                onChildChanged(child);
            }

            if ( mOnHierarchyChangeListener != null )
            {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child)
        {
            if ( parent == NotesRadioGroup.this && child instanceof CompoundButton )
            {
                ((CompoundButton) child).setOnCheckedChangeListener(null);
            }

            if ( mOnHierarchyChangeListener != null )
            {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }

        private void onChildChanged(View child)
        {
            if ( child instanceof CompoundButton )
            {
                int id = child.getId();
                // generates an id if it's missing
                if ( id == View.NO_ID && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 )
                {
                    id = View.generateViewId();
                    child.setId(id);
                }
                ((CompoundButton) child).setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener);
            }
            else if ( child instanceof ViewGroup )
            {
                int viewCount = ((ViewGroup) child).getChildCount();
                for ( int id = 0; id < viewCount; id++ )
                {
                    onChildChanged(((ViewGroup) child).getChildAt(id));
                }
            }
        }
    }
}
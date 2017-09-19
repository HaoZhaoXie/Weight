package com.example.weight.actionbar;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.weight.R;

import java.lang.reflect.Field;

public class ActionBarLayout extends RelativeLayout
{
    public ActionBarLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Class< ? > c;
        Object obj;
        Field field;
        int x;
        int statusBar = getResources().getDimensionPixelSize(R.dimen.status_bar_height);
        try
        {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBar = getResources().getDimensionPixelSize(x);
        }
        catch ( Exception e1 )
        {
            e1.printStackTrace();
        }
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
        {
            setPadding(0, statusBar, 0, 0);
        }
    }
}

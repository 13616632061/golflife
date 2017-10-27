package com.glorystudent.golflife.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * TODO 无上拉刷新的ScrollView
 * Created by Gavin.J on 2017/10/26.
 */

public class PullableNoUpScrollView extends ScrollView implements Pullable
{

    public PullableNoUpScrollView(Context context)
    {
        super(context);
    }

    public PullableNoUpScrollView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PullableNoUpScrollView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown()
    {
        if (getScrollY() == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean canPullUp()
    {
        return false;
    }

}

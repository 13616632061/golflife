package com.glorystudent.golflife.customView;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * TODO 无上拉刷新的ExpandableListView
 * Created by Gavin.J on 2017/11/7.
 */

public class PullableNoUpExpandableListView extends ExpandableListView implements
        Pullable
{

    public PullableNoUpExpandableListView(Context context)
    {
        super(context);
    }

    public PullableNoUpExpandableListView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public PullableNoUpExpandableListView(Context context, AttributeSet attrs,
                                          int defStyle)
    {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean canPullDown()
    {
        if (getCount() == 0)
        {
            // 没有item的时候也可以下拉刷新
            return true;
        } else if (getFirstVisiblePosition() == 0
                && getChildAt(0).getTop() >= 0)
        {
            // 滑到顶部了
            return true;
        } else
            return false;
    }

    @Override
    public boolean canPullUp()
    {
        return false;
    }

}

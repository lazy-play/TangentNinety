package com.pudding.tangentninety.weight;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Error on 2017/7/23 0023.
 */

public class DrawerRecyclerView extends RecyclerView{
    public DrawerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction()==MotionEvent.ACTION_DOWN&&ev.getX()<=getWidth()*0.03f){
            stopScroll();
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }
}

package com.pudding.tangentninety.weight;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.pudding.tangentninety.module.event.RecyclerBottomPudding;

import org.greenrobot.eventbus.EventBus;


/**
 * Created by Error on 2017/6/26 0026.
 */

public class InsetFrameLayout extends FrameLayout {
    public static int recyclerBottomPudding=0;
    public InsetFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if(insets.getSystemWindowInsetTop()!=0){
                    setPadding(0,insets.getSystemWindowInsetTop(),0,0);
                    if(insets.getSystemWindowInsetBottom()!=0){
                        recyclerBottomPudding=insets.getSystemWindowInsetBottom();
                        EventBus.getDefault().post(new RecyclerBottomPudding());
                    }
                }
                return insets;
            }
        });
    }
}

package com.pudding.tangentninety.weight;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.OnApplyWindowInsetsListener;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Error on 2017/6/21 0021.
 */

public class InsetAppBarLayout extends AppBarLayout {
    public InsetAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if(insets.getSystemWindowInsetTop()!=0){
                    setPadding(0,insets.getSystemWindowInsetTop(),0,0);
                }
                return insets;
            }
        });
    }
}

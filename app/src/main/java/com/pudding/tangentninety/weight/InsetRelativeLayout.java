package com.pudding.tangentninety.weight;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.pudding.tangentninety.R;

/**
 * Created by Error on 2017/6/26 0026.
 */

public class InsetRelativeLayout extends RelativeLayout {
    private View cover;
    public InsetRelativeLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                if(insets.getSystemWindowInsetTop()!=0){
                    if(cover==null){
                    cover=new View(getContext());
                        cover.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,insets.getSystemWindowInsetTop()));
                        TypedValue mainColor = new TypedValue();
                        getContext().getTheme().resolveAttribute(R.attr.colorPrimary, mainColor, true);
                        cover.setBackgroundResource(mainColor.resourceId);
                        addView(cover);
                    }else{
                        cover.getLayoutParams().height=insets.getSystemWindowInsetTop();
                    }
                    }
                return insets;
            }
        });
    }

    public View getCover() {
        return cover;
    }
}

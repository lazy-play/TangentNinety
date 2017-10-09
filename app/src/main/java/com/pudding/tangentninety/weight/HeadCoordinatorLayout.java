package com.pudding.tangentninety.weight;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.WindowInsetsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.pudding.tangentninety.R;
import com.pudding.tangentninety.app.App;

/**
 * Created by Error on 2017/7/6 0006.
 */

public class HeadCoordinatorLayout extends CoordinatorLayout {
    private View cover;
    int offsetHeight;
    private int buttomHeight;
    AppBarLayout.OnOffsetChangedListener listener;
    CollapsingToolbarLayout collapsingToolbarLayout;
    public HeadCoordinatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        ViewCompat.setOnApplyWindowInsetsListener(this, new android.support.v4.view.OnApplyWindowInsetsListener() {
            @Override
            public WindowInsetsCompat onApplyWindowInsets(View v, WindowInsetsCompat insets) {
                AppBarLayout appBarLayout= (AppBarLayout) findViewById(R.id.appbar);
                collapsingToolbarLayout= (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
                collapsingToolbarLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        offsetHeight= collapsingToolbarLayout.getScrimVisibleHeightTrigger()/2-collapsingToolbarLayout.getHeight();
                    }
                });
                if(listener==null){
                    listener=new AppBarLayout.OnOffsetChangedListener() {
                        @Override
                        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                            if (verticalOffset <offsetHeight) {
                                animateScrim(true);
                            }else{
                                animateScrim(false);
                            }
                        }
                    };
                appBarLayout.addOnOffsetChangedListener(listener);

                    cover=new View(getContext());
                    cover.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,insets.getSystemWindowInsetTop()));
                    TypedValue mainColor = new TypedValue();
                    getContext().getTheme().resolveAttribute(R.attr.colorPrimary, mainColor, true);
                    cover.setBackgroundResource(mainColor.resourceId);
                    ((ViewGroup)getParent()).addView(cover);
                }else{
                    cover.getLayoutParams().height=insets.getSystemWindowInsetTop();
                }
                buttomHeight= (int) (insets.getSystemWindowInsetBottom());
                return insets;
            }
        });
    }

    public int getButtomHeight() {
        return buttomHeight;
    }

    ObjectAnimator Animator;
    private void animateScrim(boolean showAlpha) {
        if(showAlpha){
            if (Animator == null) {
                Animator = ObjectAnimator.ofFloat(cover, "alpha",0,1);
                Animator.setDuration(collapsingToolbarLayout.getScrimAnimationDuration());
                Animator.start();
            }
        }else {
            if(Animator!=null&&Animator.isRunning()){
            Animator.cancel();
            }
            Animator=null;
            cover.setAlpha(0);
        }
    }
}
